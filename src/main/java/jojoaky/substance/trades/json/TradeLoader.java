package jojoaky.substance.trades.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TradeLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final ResourceLocation PIGLIN_BARTERING_ID = new ResourceLocation("minecraft", "gameplay/piglin_bartering");

    public TradeLoader() {
        super(new Gson(), "trades");
    }

    @Override
    public @NotNull ResourceLocation getFabricId() {
        return new ResourceLocation(Substance.MOD_ID, "trade_loader");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
        prepared.forEach((id, json) -> {
            if (id.getPath().startsWith("villager/")) {
                loadVillagerTrade(id, json);
            } else if (id.getPath().startsWith("wandering_trader/")) {
                loadWanderingTraderTrade(id, json);
            } else if (id.getPath().startsWith("piglin/")) {
                loadPiglinBarter(id, json);
            }
        });
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

    private void loadPiglinBarter(ResourceLocation id, JsonElement json) {
        PiglinBarterJson.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(errorMessage -> {
                    Substance.LOGGER.error("Failed to parse piglin barter {}: {}", id, errorMessage);
                })
                .ifPresent(trade -> {
                    LootTableEvents.MODIFY.register((resourceManager, lootManager, lootId, tableBuilder, source) -> {
                        if (source.isBuiltin() && PIGLIN_BARTERING_ID.equals(lootId)) {
                            tableBuilder.modifyPools(poolBuilder -> poolBuilder.add(
                                    LootItem.lootTableItem(trade.item())
                                            .setWeight(trade.weight())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(trade.minCount(), trade.maxCount())))
                            ));
                        }
                    });
                });
    }
}
