package jojoaky.substance.content.mob.equipment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.function.Predicate;

public record MobEquipment(
        ResourceLocation id,
        int priority,
        Predicate<EntityType<?>> target,
        Map<EquipmentSlot, ResourceLocation> equipment
) {
    public static final int ENTITY_PRIORITY = 200;
    public static final int TAG_PRIORITY = 100;

    public MobEquipment {
        equipment = Map.copyOf(equipment);
    }

    public static MobEquipment forEntity(ResourceLocation id, EntityType<?> entityType, Map<EquipmentSlot, ResourceLocation> equipment) {
        return forEntity(id, entityType, ENTITY_PRIORITY, equipment);
    }

    public static MobEquipment forEntity(ResourceLocation id, EntityType<?> entityType, int priority, Map<EquipmentSlot, ResourceLocation> equipment) {
        return new MobEquipment(id, priority, type -> type == entityType, equipment);
    }

    public static MobEquipment forTag(ResourceLocation id, TagKey<EntityType<?>> tagKey, Map<EquipmentSlot, ResourceLocation> equipment) {
        return forTag(id, tagKey, TAG_PRIORITY, equipment);
    }

    public static MobEquipment forTag(ResourceLocation id, TagKey<EntityType<?>> tagKey, int priority, Map<EquipmentSlot, ResourceLocation> equipment) {
        return new MobEquipment(id, priority, type -> type.is(tagKey), equipment);
    }

    public boolean matches(EntityType<?> entityType) {
        return this.target.test(entityType);
    }
}
