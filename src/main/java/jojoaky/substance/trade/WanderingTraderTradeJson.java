package jojoaky.substance.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record WanderingTraderTradeJson(
        int pool,
        ItemStack costA,
        Optional<ItemStack> costB,
        ItemStack result,
        int maxUses,
        int xp,
        float priceMultiplier
) {
    public static final Codec<WanderingTraderTradeJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("pool").forGetter(WanderingTraderTradeJson::pool),
            ItemStack.CODEC.fieldOf("costA").forGetter(WanderingTraderTradeJson::costA),
            ItemStack.CODEC.optionalFieldOf("costB").forGetter(WanderingTraderTradeJson::costB),
            ItemStack.CODEC.fieldOf("result").forGetter(WanderingTraderTradeJson::result),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(WanderingTraderTradeJson::maxUses),
            Codec.INT.optionalFieldOf("xp", 2).forGetter(WanderingTraderTradeJson::xp),
            Codec.FLOAT.optionalFieldOf("price_multiplier", 0.05f).forGetter(WanderingTraderTradeJson::priceMultiplier)
    ).apply(instance, WanderingTraderTradeJson::new));
}
