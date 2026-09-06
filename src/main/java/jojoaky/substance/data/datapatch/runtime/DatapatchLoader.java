package jojoaky.substance.data.datapatch.runtime;

import jojoaky.substance.Substance;
import jojoaky.substance.data.generator.datapatch.DatapatchRegistry;
import jojoaky.substance.data.generator.datapatch.def.LootEntryDef;
import jojoaky.substance.data.generator.datapatch.def.MerchantTradeDef;
import jojoaky.substance.data.generator.datapatch.def.VillagerTradeDef;
import jojoaky.substance.data.generator.datapatch.def.WanderingTraderTradeDef;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.*;


// Currently uses hardcoded items from DatapackRegistry, might upgrade to data-driven approach in the future
public class DatapatchLoader {
    public static volatile Map<ResourceLocation, List<LootEntryDef>> LOOT_PATCHES = Map.of();
    public static volatile Map<VillagerProfession, Map<Integer, List<VillagerTradeDef>>> VILLAGER_TRADES = Map.of();
    public static volatile Map<Integer, List<WanderingTraderTradeDef>> WANDERING_TRADER_TRADES = Map.of();

    private static boolean callbacksRegistered = false;

    public static void init() {
        Map<ResourceLocation, List<LootEntryDef>> lootPatches = new HashMap<>();
        Map<VillagerProfession, Map<Integer, List<VillagerTradeDef>>> villagerTrades = new HashMap<>();
        Map<Integer, List<WanderingTraderTradeDef>> wanderingTraderTrades = new HashMap<>();

        DatapatchRegistry.LOOT_TABLE_PATCHES.forEach(entry -> lootPatches.computeIfAbsent(entry.getTargetTable(), k -> new ArrayList<>()).add(entry));
        DatapatchRegistry.VILLAGER_TRADES.forEach(trade -> villagerTrades.computeIfAbsent(trade.getProfession(), p -> new HashMap<>()).computeIfAbsent(trade.getLevel(), l -> new ArrayList<>()).add(trade));
        DatapatchRegistry.WANDERING_TRADER_TRADES.forEach(trade -> wanderingTraderTrades.computeIfAbsent(trade.getPool(), p -> new ArrayList<>()).add(trade));

        LOOT_PATCHES = immutableLists(lootPatches);
        VILLAGER_TRADES = immutableNestedLists(villagerTrades);
        WANDERING_TRADER_TRADES = immutableLists(wanderingTraderTrades);

        registerCallbacks();

        Substance.LOGGER.info("Loaded hardcoded datapatches: {} loot entries, {} villager trades, {} wandering trades",
                DatapatchRegistry.LOOT_TABLE_PATCHES.size(), DatapatchRegistry.VILLAGER_TRADES.size(), DatapatchRegistry.WANDERING_TRADER_TRADES.size());
    }

    private static void registerCallbacks() {
        if (callbacksRegistered) return;
        callbacksRegistered = true;

        LootTableEvents.MODIFY.register((resourceManager, lootManager, lootId, tableBuilder, source) -> {
            for (LootEntryDef entry : LOOT_PATCHES.getOrDefault(lootId, Collections.emptyList())) {
                try {
                    tableBuilder.modifyPools(poolBuilder -> poolBuilder.add(
                            LootItem.lootTableItem(entry.getItem().asItem())
                                    .setWeight(entry.getWeight())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(entry.getMinCount(), entry.getMaxCount())))
                    ));
                } catch (Exception e) {
                    Substance.LOGGER.error("Failed to apply hardcoded loot entry {} to {}: {}", entry.getName(), lootId, e.getMessage());
                }
            }
        });

        VILLAGER_TRADES.forEach((profession, levelMap) -> levelMap.forEach((level, ignored) -> {
            TradeOfferHelper.registerVillagerOffers(profession, level, factories -> {
                for (VillagerTradeDef def : VILLAGER_TRADES
                        .getOrDefault(profession, Collections.emptyMap())
                        .getOrDefault(level, Collections.emptyList())) {
                    factories.add((entity, random) -> createOffer(def));
                }
            });
        }));

        WANDERING_TRADER_TRADES.forEach((pool, ignored) ->
                TradeOfferHelper.registerWanderingTraderOffers(pool, factories -> {
                    for (WanderingTraderTradeDef def : WANDERING_TRADER_TRADES
                            .getOrDefault(pool, Collections.emptyList())) {
                        factories.add((entity, random) -> createOffer(def));
                    }
                })
        );
    }

    private static MerchantOffer createOffer(MerchantTradeDef<?> def) {
        try {
            if (!def.getCostB().isEmpty()) {
                return new MerchantOffer(def.getCostA(), def.getCostB(), def.getResult(), def.getMaxUses(), def.getVillagerXp(), def.getPriceMultiplier());
            } else {
                return new MerchantOffer(def.getCostA(), def.getResult(), def.getMaxUses(), def.getVillagerXp(), def.getPriceMultiplier());
            }
        } catch (Exception e) {
            Substance.LOGGER.error("Failed to create merchant offer for trade {}: {}", def.getName(), e.getMessage());
            return null;
        }
    }

    private static <K, V> Map<K, List<V>> immutableLists(Map<K, List<V>> source) {
        Map<K, List<V>> snapshot = new HashMap<>();
        source.forEach((key, values) -> snapshot.put(key, List.copyOf(values)));
        return Map.copyOf(snapshot);
    }

    private static <K1, K2, V> Map<K1, Map<K2, List<V>>> immutableNestedLists(Map<K1, Map<K2, List<V>>> source) {
        Map<K1, Map<K2, List<V>>> snapshot = new HashMap<>();
        source.forEach((key, values) -> snapshot.put(key, immutableLists(values)));
        return Map.copyOf(snapshot);
    }
}
