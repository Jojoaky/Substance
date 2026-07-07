package jojoaky.substance.data.generator.datapatch;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import jojoaky.substance.Substance;
import jojoaky.substance.data.datapatch.json.LootPatchJson;
import jojoaky.substance.data.datapatch.json.VillagerTradeJson;
import jojoaky.substance.data.datapatch.json.WanderingTraderTradeJson;
import jojoaky.substance.data.generator.datapatch.def.MerchantTradeDef;
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

public class DatapatchDatagenProvider implements DataProvider {
    protected final FabricDataOutput output;
    private static final String MOD_ID = Substance.MOD_ID;

    public DatapatchDatagenProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        DatapatchRegistry.VILLAGER_TRADES.forEach(trade -> {
            VillagerTradeJson json = new VillagerTradeJson(
                    false,
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

        DatapatchRegistry.WANDERING_TRADER_TRADES.forEach(trade -> {
            WanderingTraderTradeJson json = new WanderingTraderTradeJson(
                    false,
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

        DatapatchRegistry.LOOT_TABLE_PATCHES.forEach(entry -> {
            LootPatchJson json = new LootPatchJson(
                    false,
                    entry.getTargetTable(),
                    entry.getItem().asItem(),
                    entry.getWeight(),
                    entry.getMinCount(),
                    entry.getMaxCount()
            );
            futures.add(save(dc, LootPatchJson.CODEC.encodeStart(JsonOps.INSTANCE, json).getOrThrow(false, s -> {}),
                    getPath("loot/" + MerchantTradeDef.toPathSafe(entry.getName()))));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> save(CachedOutput cache, JsonElement json, Path path) {
        return DataProvider.saveStable(cache, json, path);
    }

    private Path getPath(String name) {
        return output.createPathProvider(PackOutput.Target.DATA_PACK, "datapatch").json(new ResourceLocation(MOD_ID, name));    }

    @Override
    public @NotNull String getName() {
        return "Substance Trades";
    }
}
