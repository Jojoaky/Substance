package jojoaky.substance.content.consumable.framework;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ConsumableComponent {
    default void onStartConsuming(ItemStack stack, Level level, LivingEntity entity) {
    }

    default void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    default void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    default boolean hasCustomRenderModel() {
        return false;
    }
}
