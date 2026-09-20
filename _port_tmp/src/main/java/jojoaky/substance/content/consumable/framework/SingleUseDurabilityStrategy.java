package jojoaky.substance.content.consumable.framework;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SingleUseDurabilityStrategy implements DurabilityStrategy {
    @Override
    public void onFinishConsuming(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (level.isClientSide) return;

        if (entity instanceof Player player && player.isCreative()) return;

        stack.shrink(1);

        if (entity instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(item));
        }
    }
}
