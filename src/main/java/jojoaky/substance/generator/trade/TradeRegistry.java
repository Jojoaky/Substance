package jojoaky.substance.generator.trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class TradeRegistry {
    public static List<VillagerTradeDef> VILLAGER_TRADES = new ArrayList<>();
    public static List<WanderingTraderTradeDef> WANDERING_TRADER_TRADES = new ArrayList<>();
    public static List<PiglinBarterDef> PIGLIN_BARTERS = new ArrayList<>();

    public static void register(Object... trades) {
        Arrays.stream(trades).forEach(TradeRegistry::registerSingle);
    }

    public static void register(Stream<?> trades) {
        trades.forEach(TradeRegistry::registerSingle);
    }

    public static void register(Collection<?> trades) {
        trades.forEach(TradeRegistry::registerSingle);
    }

    private static void registerSingle(Object trade) {
        if (trade instanceof VillagerTradeDef v) VILLAGER_TRADES.add(v);
        else if (trade instanceof WanderingTraderTradeDef w) WANDERING_TRADER_TRADES.add(w);
        else if (trade instanceof PiglinBarterDef p) PIGLIN_BARTERS.add(p);
    }
}