package jojoaky.substance.data.datapatch.runtime;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import jojoaky.substance.Substance;
import jojoaky.substance.data.datapatch.json.LootPatchJson;
import jojoaky.substance.data.datapatch.json.VillagerTradeJson;
import jojoaky.substance.data.datapatch.json.WanderingTraderTradeJson;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DatapatchLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

    // Registries to prevent duplicate TradeOfferHelper injections on /reload
    private static final Set<ResourceLocation> REGISTERED_VILLAGER = new HashSet<>();
    private static final Set<ResourceLocation> REGISTERED_WANDERING = new HashSet<>();

    // Real-time state maps (cleared and repopulated on /reload)
    private static final Map<ResourceLocation, VillagerTradeJson> VILLAGER_TRADES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, WanderingTraderTradeJson> WANDERING_TRADES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, List<LootPatchJson>> LOOT_PATCHES = new ConcurrentHashMap<>();

    public DatapatchLoader() {
        super(new Gson(), "datapatch");
        registerLootTableEvent();
    }

    private void registerLootTableEvent() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, lootId, tableBuilder, source) -> {
            List<LootPatchJson> patches = LOOT_PATCHES.get(lootId);
            if (patches != null && !patches.isEmpty()) {
                for (LootPatchJson patch : patches) {
                    if (patch.disabled()) continue;

                    tableBuilder.modifyPools(poolBuilder -> poolBuilder.add(
                            LootItem.lootTableItem(patch.item())
                                    .setWeight(patch.weight())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(patch.minCount(), patch.maxCount())))
                    ));
                }
            }
        });
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
        // Clear maps for the incoming /reload
        VILLAGER_TRADES.clear();
        WANDERING_TRADES.clear();
        LOOT_PATCHES.clear();

        int parsedTrades = 0;
        int parsedLoot = 0;

        for (Map.Entry<ResourceLocation, JsonElement> entry : prepared.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();
            String path = id.getPath();

            if (path.startsWith("villager/")) {
                loadVillagerTrade(id, json);
                parsedTrades++;
            } else if (path.startsWith("wandering_trader/")) {
                loadWanderingTrade(id, json);
                parsedTrades++;
            } else if (path.startsWith("loot/")) {
                loadLootPatch(id, json);
                parsedLoot++;
            }
        }

        Substance.LOGGER.info("Datapatches reloaded! Trades: {}, Loot Patches: {}", parsedTrades, parsedLoot);
    }

    private void loadVillagerTrade(ResourceLocation id, JsonElement json) {
        VillagerTradeJson.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(err -> Substance.LOGGER.error("Failed to parse villager trade {}: {}", id, err))
                .ifPresent(trade -> {
                    VILLAGER_TRADES.put(id, trade);

                    // Only inject into Fabric API if this ID hasn't been injected before
                    if (REGISTERED_VILLAGER.add(id)) {
                        TradeOfferHelper.registerVillagerOffers(trade.profession(), trade.level(), factories -> {
                            factories.add((entity, random) -> {
                                VillagerTradeJson current = VILLAGER_TRADES.get(id);
                                // If trade was removed via datapack or marked disabled, return null (safely ignored by Vanilla)
                                if (current == null || current.disabled()) return null;
                                return new MerchantOffer(current.costA(), current.costB().orElse(ItemStack.EMPTY), current.result(), current.maxUses(), current.xp(), current.priceMultiplier());
                            });
                        });
                    }
                });
    }

    private void loadWanderingTrade(ResourceLocation id, JsonElement json) {
        WanderingTraderTradeJson.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(err -> Substance.LOGGER.error("Failed to parse wandering trade {}: {}", id, err))
                .ifPresent(trade -> {
                    WANDERING_TRADES.put(id, trade);

                    if (REGISTERED_WANDERING.add(id)) {
                        TradeOfferHelper.registerWanderingTraderOffers(trade.pool(), factories -> {
                            factories.add((entity, random) -> {
                                WanderingTraderTradeJson current = WANDERING_TRADES.get(id);
                                if (current == null || current.disabled()) return null;
                                return new MerchantOffer(current.costA(), current.costB().orElse(ItemStack.EMPTY), current.result(), current.maxUses(), current.xp(), current.priceMultiplier());
                            });
                        });
                    }
                });
    }

    private void loadLootPatch(ResourceLocation id, JsonElement json) {
        LootPatchJson.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(err -> Substance.LOGGER.error("Failed to parse loot patch {}: {}", id, err))
                .ifPresent(patch -> LOOT_PATCHES.computeIfAbsent(patch.targetTable(), k -> new ArrayList<>()).add(patch));
    }

    @Override
    public @NotNull ResourceLocation getFabricId() {
        return new ResourceLocation(Substance.MOD_ID, "datapatch_loader");
    }
}