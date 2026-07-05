package jojoaky.substance.datagen.generator.trade.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TradeLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final ResourceLocation PIGLIN_BARTERING_ID = new ResourceLocation("minecraft", "gameplay/piglin_bartering");
    private static final String PIGLIN_BARTER_PATH = "trades/piglin";

    public TradeLoader() {
        super(new Gson(), "trades");
        registerLootTableEvent();
    }

    private void registerLootTableEvent() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, lootId, tableBuilder, source) -> {
            if (source.isBuiltin() && PIGLIN_BARTERING_ID.equals(lootId)) {
                long startTime = System.currentTimeMillis();
                int barterCount = 0;

                List<PiglinBarterJson> piglinBarters = loadPiglinBarters(resourceManager);
                for (PiglinBarterJson trade : piglinBarters) {
                    tableBuilder.modifyPools(poolBuilder -> poolBuilder.add(
                            LootItem.lootTableItem(trade.item())
                                    .setWeight(trade.weight())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(trade.minCount(), trade.maxCount())))
                    ));
                    barterCount++;
                }

                long endTime = System.currentTimeMillis();
                Substance.LOGGER.info("Loaded {} piglin barters in {}ms",
                        barterCount, endTime - startTime);
            }
        });
    }

    private List<PiglinBarterJson> loadPiglinBarters(ResourceManager resourceManager) {
        List<PiglinBarterJson> result = new ArrayList<>();
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                PIGLIN_BARTER_PATH, location -> location.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            try (Reader reader = entry.getValue().openAsReader()) {
                JsonElement json = JsonParser.parseReader(reader);
                PiglinBarterJson.CODEC.parse(JsonOps.INSTANCE, json)
                        .resultOrPartial(errorMessage ->
                                Substance.LOGGER.error("Failed to parse piglin barter {}: {}", id, errorMessage))
                        .ifPresent(result::add);
            } catch (IOException e) {
                Substance.LOGGER.error("Failed to read piglin barter {}", id, e);
            }
        }

        return result;
    }

    @Override
    public @NotNull ResourceLocation getFabricId() {
        return new ResourceLocation(Substance.MOD_ID, "trade_loader");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
        long startTime = System.currentTimeMillis();
        int villagerTrades = 0;
        int wanderingTraderTrades = 0;

        for (Map.Entry<ResourceLocation, JsonElement> entry : prepared.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();

            if (id.getPath().startsWith("villager/")) {
                loadVillagerTrade(id, json);
                villagerTrades++;
            } else if (id.getPath().startsWith("wandering_trader/")) {
                loadWanderingTraderTrade(id, json);
                wanderingTraderTrades++;
            }
        }

        long endTime = System.currentTimeMillis();
        Substance.LOGGER.info("Loaded {} villager trades in {}ms",
                villagerTrades + wanderingTraderTrades, endTime - startTime);
    }

    private void loadVillagerTrade(ResourceLocation id, JsonElement json) {
        VillagerTradeJson.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(errorMessage -> {
                    Substance.LOGGER.error("Failed to parse villager trade {}: {}", id, errorMessage);
                })
                .ifPresent(trade -> {
                    TradeOfferHelper.registerVillagerOffers(trade.profession(), trade.level(), factories -> {
                        factories.add((Entity entity, RandomSource random) -> new MerchantOffer(
                                trade.costA(), trade.costB().orElse(ItemStack.EMPTY), trade.result(), trade.maxUses(), trade.xp(), trade.priceMultiplier()
                        ));
                    });
                });
    }

    private void loadWanderingTraderTrade(ResourceLocation id, JsonElement json) {
        WanderingTraderTradeJson.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(errorMessage -> {
                    Substance.LOGGER.error("Failed to parse wandering trader trade {}: {}", id, errorMessage);
                })
                .ifPresent(trade -> {
                    TradeOfferHelper.registerWanderingTraderOffers(trade.pool(), factories -> {
                        factories.add((Entity entity, RandomSource random) -> new MerchantOffer(
                                trade.costA(), trade.costB().orElse(ItemStack.EMPTY), trade.result(), trade.maxUses(), trade.xp(), trade.priceMultiplier()
                        ));
                    });
                });
    }
}