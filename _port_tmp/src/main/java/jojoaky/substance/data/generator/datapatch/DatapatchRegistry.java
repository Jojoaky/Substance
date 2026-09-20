package jojoaky.substance.data.generator.datapatch;

import jojoaky.substance.data.generator.datapatch.def.LootEntryDef;
import jojoaky.substance.data.generator.datapatch.def.VillagerTradeDef;
import jojoaky.substance.data.generator.datapatch.def.WanderingTraderTradeDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DatapatchRegistry {
    public static List<VillagerTradeDef> VILLAGER_TRADES = new ArrayList<>();
    public static List<WanderingTraderTradeDef> WANDERING_TRADER_TRADES = new ArrayList<>();
    public static List<LootEntryDef> LOOT_TABLE_PATCHES = new ArrayList<>();

    public static void accept(Object... trades) {
        Arrays.stream(trades).forEach(DatapatchRegistry::acceptSingle);
    }

    public static void accept(Stream<?> trades) {
        trades.forEach(DatapatchRegistry::acceptSingle);
    }

    public static void accept(Collection<?> trades) {
        trades.forEach(DatapatchRegistry::acceptSingle);
    }

    private static void acceptSingle(Object trade) {
        if (trade instanceof VillagerTradeDef v) VILLAGER_TRADES.add(v);
        else if (trade instanceof WanderingTraderTradeDef w) WANDERING_TRADER_TRADES.add(w);
        else if (trade instanceof LootEntryDef p) LOOT_TABLE_PATCHES.add(p);
    }
}