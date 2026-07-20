package jojoaky.substance.data.datapatch.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

// TODO: Support different types than only item
public record LootPatchJson(
        boolean disabled,
        ResourceLocation targetTable,
        Item item,
        int weight,
        float minCount,
        float maxCount
) {
    public static final Codec<LootPatchJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("disabled", false).forGetter(LootPatchJson::disabled),
            ResourceLocation.CODEC.fieldOf("target_table").forGetter(LootPatchJson::targetTable),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(LootPatchJson::item),
            Codec.INT.optionalFieldOf("weight", 10).forGetter(LootPatchJson::weight),
            Codec.FLOAT.optionalFieldOf("min_count", 1.0f).forGetter(LootPatchJson::minCount),
            Codec.FLOAT.optionalFieldOf("max_count", 1.0f).forGetter(LootPatchJson::maxCount)
    ).apply(instance, LootPatchJson::new));
}