package jojoaky.substance.content.consumable.framework;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface DurabilityStrategy {
    default Item.Properties configureProperties(Item.Properties properties) {
        return properties;
    }

    default void onConsumeTick(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    default void onStopConsuming(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    default Boolean isBarVisible(ItemStack stack) {
        return null;
    }

    default Integer getBarWidth(ItemStack stack) {
        return null;
    }

    default Integer getBarColor(ItemStack stack) {
        return null;
    }

    default boolean allowNbtUpdateAnimation(
            Player player,
            InteractionHand hand,
            ItemStack oldStack,
            ItemStack newStack
    ) {
        return false;
    }
}
