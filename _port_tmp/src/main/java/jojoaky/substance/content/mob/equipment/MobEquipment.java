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
        int specificity,
        boolean override,
        Predicate<EntityType<?>> target,
        Map<EquipmentSlot, ResourceLocation> equipment
) {
    public static final int ENTITY_SPECIFICITY = 1;
    public static final int TAG_SPECIFICITY = 0;

    public MobEquipment {
        equipment = Map.copyOf(equipment);
    }

    public static MobEquipment forEntity(ResourceLocation id, EntityType<?> entityType, Map<EquipmentSlot, ResourceLocation> equipment) {
        return forEntity(id, entityType, 0, false, equipment);
    }

    public static MobEquipment forEntity(
            ResourceLocation id,
            EntityType<?> entityType,
            int priority,
            Map<EquipmentSlot, ResourceLocation> equipment
    ) {
        return forEntity(id, entityType, priority, false, equipment);
    }

    public static MobEquipment forEntity(
            ResourceLocation id,
            EntityType<?> entityType,
            int priority,
            boolean override,
            Map<EquipmentSlot, ResourceLocation> equipment
    ) {
        return new MobEquipment(id, priority, ENTITY_SPECIFICITY, override, type -> type == entityType, equipment);
    }

    public static MobEquipment forTag(ResourceLocation id, TagKey<EntityType<?>> tagKey, Map<EquipmentSlot, ResourceLocation> equipment) {
        return forTag(id, tagKey, 0, false, equipment);
    }

    public static MobEquipment forTag(
            ResourceLocation id,
            TagKey<EntityType<?>> tagKey,
            int priority,
            Map<EquipmentSlot, ResourceLocation> equipment
    ) {
        return forTag(id, tagKey, priority, false, equipment);
    }

    public static MobEquipment forTag(
            ResourceLocation id,
            TagKey<EntityType<?>> tagKey,
            int priority,
            boolean override,
            Map<EquipmentSlot, ResourceLocation> equipment
    ) {
        return new MobEquipment(id, priority, TAG_SPECIFICITY, override, type -> type.is(tagKey), equipment);
    }

    public boolean matches(EntityType<?> entityType) {
        return this.target.test(entityType);
    }

    public boolean shouldSkipSlot(boolean assignedByDefinition, boolean occupied) {
        return assignedByDefinition || (occupied && !override);
    }
}
