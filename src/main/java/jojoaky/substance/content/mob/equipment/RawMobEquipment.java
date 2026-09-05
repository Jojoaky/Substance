package jojoaky.substance.content.mob.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jojoaky.substance.Substance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public record RawMobEquipment(
        List<String> entities,
        Map<EquipmentSlot, ResourceLocation> equipment
) {

    private static final Codec<EquipmentSlot> SLOT_CODEC = Codec.STRING.comapFlatMap(
            name -> {
                try {
                    return DataResult.success(EquipmentSlot.byName(name));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Unknown equipment slot: " + name);
                }
            },
            EquipmentSlot::getName
    );

    public static final Codec<RawMobEquipment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.listOf().fieldOf("entities").forGetter(RawMobEquipment::entities),
                    Codec.unboundedMap(SLOT_CODEC, ResourceLocation.CODEC)
                            .fieldOf("equipment")
                            .forGetter(RawMobEquipment::equipment)
            ).apply(instance, RawMobEquipment::new)
    );

    public List<MobEquipment> toMobEquipment(ResourceLocation id) {
        List<MobEquipment> definitions = new ArrayList<>();

        for (String rawEntity : entities) {
            if (rawEntity.startsWith("#")) {
                ResourceLocation tagId = ResourceLocation.tryParse(rawEntity.substring(1));
                if (tagId != null) {
                    TagKey<EntityType<?>> tagKey = TagKey.create(Registries.ENTITY_TYPE, tagId);
                    definitions.add(MobEquipment.forTag(id, tagKey, equipment));
                } else {
                    Substance.LOGGER.warn("Invalid entity tag ID '{}' in equipment definition {}", rawEntity, id);
                }
            } else {
                ResourceLocation entityId = ResourceLocation.tryParse(rawEntity);
                if (entityId != null && BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
                    EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entityId);
                    definitions.add(MobEquipment.forEntity(id, entityType, equipment));
                } else {
                    Substance.LOGGER.warn("Invalid entity ID '{}' in equipment definition {}", rawEntity, id);
                }
            }
        }

        if (definitions.isEmpty()) {
            Substance.LOGGER.error("Skipping mob equipment definition {}: no valid entities or tags found", id);
        }

        return definitions;
    }
}