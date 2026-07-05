package jojoaky.substance.trades.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record PiglinBarterJson(
        Item item,
        int weight,
        float minCount,
        float maxCount
) {
    public static final Codec<PiglinBarterJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(PiglinBarterJson::item),
            Codec.INT.optionalFieldOf("weight", 10).forGetter(PiglinBarterJson::weight),
            Codec.FLOAT.optionalFieldOf("min_count", 1.0f).forGetter(PiglinBarterJson::minCount),
            Codec.FLOAT.optionalFieldOf("max_count", 1.0f).forGetter(PiglinBarterJson::maxCount)
    ).apply(instance, PiglinBarterJson::new));
}
