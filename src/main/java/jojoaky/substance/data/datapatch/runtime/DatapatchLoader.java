package jojoaky.substance.data.datapatch.runtime;

import jojoaky.substance.Substance;
import jojoaky.substance.data.generator.datapatch.DatapatchRegistry;
import jojoaky.substance.data.generator.datapatch.def.LootEntryDef;
import jojoaky.substance.data.generator.datapatch.def.VillagerTradeDef;
import jojoaky.substance.data.generator.datapatch.def.WanderingTraderTradeDef;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.*;


// Currently uses hardcoded items from DatapackRegistry, might upgrade to data-driven approach in the future
public class DatapatchLoader {
    public static final Map<ResourceLocation, List<LootEntryDef>> LOOT_PATCHES = new HashMap<>();
    public static final Map<VillagerProfession, Map<Integer, List<VillagerTradeDef>>> VILLAGER_TRADES = new HashMap<>();
    public static final Map<Integer, List<WanderingTraderTradeDef>> WANDERING_TRADER_TRADES = new HashMap<>();

    private static boolean lootTableEventRegistered = false;

    public static void init() {
        LOOT_PATCHES.clear();
        VILLAGER_TRADES.clear();
        WANDERING_TRADER_TRADES.clear();

        DatapatchRegistry.LOOT_TABLE_PATCHES.forEach(entry -> LOOT_PATCHES.computeIfAbsent(entry.getTargetTable(), k -> new ArrayList<>()).add(entry));
        DatapatchRegistry.VILLAGER_TRADES.forEach(trade -> VILLAGER_TRADES.computeIfAbsent(trade.getProfession(), p -> new HashMap<>()).computeIfAbsent(trade.getLevel(), l -> new ArrayList<>()).add(trade));
        DatapatchRegistry.WANDERING_TRADER_TRADES.forEach(trade -> WANDERING_TRADER_TRADES.computeIfAbsent(trade.getPool(), p -> new ArrayList<>()).add(trade));

        registerLootTableEvent();

        // Register villager trades via Fabric's TradeOfferHelper
        VILLAGER_TRADES.forEach((profession, levelMap) -> levelMap.forEach((level, trades) -> {
            TradeOfferHelper.registerVillagerOffers(profession, level, factories -> {
                for (var def : trades) {
                    factories.add((entity, random) -> {
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
                    });
                }
            });
        }));

        Substance.LOGGER.info("Loaded hardcoded datapatches: {} loot entries, {} villager trades, {} wandering trades",
                DatapatchRegistry.LOOT_TABLE_PATCHES.size(), DatapatchRegistry.VILLAGER_TRADES.size(), DatapatchRegistry.WANDERING_TRADER_TRADES.size());
    }

    private static void registerLootTableEvent() {
        if (lootTableEventRegistered) return;
        lootTableEventRegistered = true;

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
    }
}