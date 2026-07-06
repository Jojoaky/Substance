package jojoaky.substance.datagen.generator;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import jojoaky.substance.datagen.generator.trade.MerchantTradeDef;
import jojoaky.substance.datagen.generator.trade.TradeRegistry;
import jojoaky.substance.trade.PiglinBarterJson;
import jojoaky.substance.trade.VillagerTradeJson;
import jojoaky.substance.trade.WanderingTraderTradeJson;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TradeDatagenProvider implements DataProvider {
    protected final FabricDataOutput output;
    private static final String MOD_ID = "substance";

    public TradeDatagenProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        TradeRegistry.VILLAGER_TRADES.forEach(trade -> {
            VillagerTradeJson json = new VillagerTradeJson(
                    trade.getProfession(),
                    trade.getLevel(),
                    trade.getCostA(),
                    trade.getCostB().isEmpty() ? Optional.empty() : Optional.of(trade.getCostB()),
                    trade.getResult(),
                    trade.getMaxUses(),
                    trade.getVillagerXp(),
                    trade.getPriceMultiplier()
            );
            futures.add(save(dc, VillagerTradeJson.CODEC.encodeStart(JsonOps.INSTANCE, json).getOrThrow(false, s -> {}),
                    getPath("villager/" + MerchantTradeDef.toPathSafe(trade.getName()))));
        });

        TradeRegistry.WANDERING_TRADER_TRADES.forEach(trade -> {
            WanderingTraderTradeJson json = new WanderingTraderTradeJson(
                    trade.getPool(),
                    trade.getCostA(),
                    trade.getCostB().isEmpty() ? Optional.empty() : Optional.of(trade.getCostB()),
                    trade.getResult(),
                    trade.getMaxUses(),
                    trade.getVillagerXp(),
                    trade.getPriceMultiplier()
            );
            futures.add(save(dc, WanderingTraderTradeJson.CODEC.encodeStart(JsonOps.INSTANCE, json).getOrThrow(false, s -> {}),
                    getPath("wandering_trader/" + MerchantTradeDef.toPathSafe(trade.getName()))));
        });

        TradeRegistry.PIGLIN_BARTERS.forEach(trade -> {
            PiglinBarterJson json = new PiglinBarterJson(
                    trade.getItem().asItem(),
                    trade.getWeight(),
                    trade.getMinCount(),
                    trade.getMaxCount()
            );
            futures.add(save(dc, PiglinBarterJson.CODEC.encodeStart(JsonOps.INSTANCE, json).getOrThrow(false, s -> {}),
                    getPath("piglin/" + MerchantTradeDef.toPathSafe(trade.getName()))));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> save(CachedOutput cache, JsonElement json, Path path) {
        return DataProvider.saveStable(cache, json, path);
    }

    private Path getPath(String name) {
        return output.createPathProvider(PackOutput.Target.DATA_PACK, "trades").json(new ResourceLocation(MOD_ID, name));
    }

    @Override
    public @NotNull String getName() {
        return "Substance Trades";
    }
}
