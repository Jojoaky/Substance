package jojoaky.substance.content.consumable;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

abstract public class ConsumableItem extends Item {

    public final int USE_DURATION;
    public final int COOLDOWN;

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    public ConsumableItem(Properties properties, int useDuration, int cooldown) {
        super(properties);
        this.USE_DURATION = useDuration;
        this.COOLDOWN = cooldown;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int i) {
        int useDuration = getUseDuration(stack) - i;

        if (useDuration == 0) onStartConsuming(stack, level, entity);

        if (!level.isClientSide && entity instanceof Player player) {
            stack.hurtAndBreak(1, player, p -> {
                stopConsuming(stack, level, entity, useDuration);
            });
        }

        onConsumeTick(stack, level, entity, useDuration);
    }

    @Override
    @NotNull
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        stopConsuming(stack, level, entity, getUseDuration(stack));
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int i) {
        int useDuration = getUseDuration(stack) - i;
        stopConsuming(stack, level, entity, useDuration);
    }

    public void stopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {

        if (!level.isClientSide) {
            if (entity instanceof Player) {
                ((Player) entity).getCooldowns().addCooldown(this, COOLDOWN);
            }
        }

        if (entity instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        onStopConsuming(stack, level, entity, useDuration);
    }

    abstract protected void onStartConsuming(ItemStack stack, Level level, LivingEntity entity);
    abstract protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration);
    abstract protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration);

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }
}
