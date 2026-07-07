package jojoaky.substance.data.datapatch.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record VillagerTradeJson(
        boolean disabled,
        VillagerProfession profession,
        int level,
        ItemStack costA,
        Optional<ItemStack> costB,
        ItemStack result,
        int maxUses,
        int xp,
        float priceMultiplier
) {
    public static final Codec<VillagerTradeJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("disabled", false).forGetter(VillagerTradeJson::disabled),
            BuiltInRegistries.VILLAGER_PROFESSION.byNameCodec().fieldOf("profession").forGetter(VillagerTradeJson::profession),
            Codec.INT.fieldOf("level").forGetter(VillagerTradeJson::level),
            ItemStack.CODEC.fieldOf("costA").forGetter(VillagerTradeJson::costA),
            ItemStack.CODEC.optionalFieldOf("costB").forGetter(VillagerTradeJson::costB),
            ItemStack.CODEC.fieldOf("result").forGetter(VillagerTradeJson::result),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(VillagerTradeJson::maxUses),
            Codec.INT.optionalFieldOf("xp", 2).forGetter(VillagerTradeJson::xp),
            Codec.FLOAT.optionalFieldOf("price_multiplier", 0.05f).forGetter(VillagerTradeJson::priceMultiplier)
    ).apply(instance, VillagerTradeJson::new));
}