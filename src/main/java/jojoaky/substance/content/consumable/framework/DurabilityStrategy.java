package jojoaky.substance.content.consumable.framework;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DurabilityStrategy {
    default Item.Properties configureProperties(Item.Properties properties) {
        return properties;
    }

    default void onConsumeTick(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    default void onStopConsuming(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
    }

    default void onFinishConsuming(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
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

    default int getMaxDamage(Item item, ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
    }

    default void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
    }

}
