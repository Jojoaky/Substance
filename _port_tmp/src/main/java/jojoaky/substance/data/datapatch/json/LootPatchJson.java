package jojoaky.substance.data.datapatch.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jojoaky.substance.data.generator.datapatch.def.LootEntryDef;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Locale;

// TODO: Support different types than only item
public record LootPatchJson(
        boolean disabled,
        ResourceLocation targetTable,
        Item item,
        LootEntryDef.Placement placement,
        float chance,
        int poolIndex,
        int weight,
        float minCount,
        float maxCount
) {
    private static final Codec<LootEntryDef.Placement> PLACEMENT_CODEC = Codec.STRING.comapFlatMap(
            value -> {
                try {
                    return DataResult.success(LootEntryDef.Placement.valueOf(value.toUpperCase(Locale.ROOT)));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Unknown loot patch placement: " + value);
                }
            },
            value -> value.name().toLowerCase(Locale.ROOT)
    );

    public static final Codec<LootPatchJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("disabled", false).forGetter(LootPatchJson::disabled),
            ResourceLocation.CODEC.fieldOf("target_table").forGetter(LootPatchJson::targetTable),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(LootPatchJson::item),
            PLACEMENT_CODEC.optionalFieldOf("placement", LootEntryDef.Placement.EXISTING_POOL).forGetter(LootPatchJson::placement),
            Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(LootPatchJson::chance),
            Codec.INT.optionalFieldOf("pool_index", 0).forGetter(LootPatchJson::poolIndex),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(LootPatchJson::weight),
            Codec.FLOAT.optionalFieldOf("min_count", 1.0f).forGetter(LootPatchJson::minCount),
            Codec.FLOAT.optionalFieldOf("max_count", 1.0f).forGetter(LootPatchJson::maxCount)
    ).apply(instance, LootPatchJson::new));
}
