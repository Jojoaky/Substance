package jojoaky.substance.generator.trade;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class TradeRegistry {
    public interface TradeDef {
        void register();
    }

    public static void register(TradeDef... trades) {
        Arrays.stream(trades).forEach(TradeDef::register);
    }

    public static void register(Stream<? extends TradeDef> trades) {
        trades.forEach(TradeDef::register);
    }

    public static void register(Collection<? extends TradeDef> trades) {
        trades.forEach(TradeDef::register);
    }
}