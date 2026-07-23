package jojoaky.substance.content.consumable.framework;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VanillaDurabilityStrategy implements DurabilityStrategy {
    @Override
    public Item.Properties configureProperties(Item.Properties properties) {
        return properties;
    }

    @Override
    public void onConsumeTick(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (!level.isClientSide) {
            stack.hurtAndBreak(1, entity, p -> item.stopConsuming(stack, level, entity, useDuration));
        }
    }

    @Override
    public void onStopConsuming(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (entity instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(item));
        }
    }
}
