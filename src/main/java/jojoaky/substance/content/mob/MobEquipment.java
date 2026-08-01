package jojoaky.substance.content.mob;

import net.minecraft.resources.ResourceLocation;
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
    public boolean matches(EntityType<?> entityType) {
        return this.target.test(entityType);
    }
}
