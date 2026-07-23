package jojoaky.substance.content.consumable.framework;

import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.ToIntFunction;

public class ConsumptionDurabilityStrategy implements DurabilityStrategy {
    private static final String DAMAGE_KEY = "consumption";
    private final ToIntFunction<ItemStack> maxDurabilityProvider;

    public ConsumptionDurabilityStrategy(ToIntFunction<ItemStack> maxDurabilityProvider) {
        this.maxDurabilityProvider = maxDurabilityProvider;
    }

    @Override
    public Item.Properties configureProperties(Item.Properties properties) {
        return properties.stacksTo(1);
    }

    @Override
    public void onConsumeTick(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        int nextDamage = getCustomDamage(stack) + 1;
        if (nextDamage >= getConsumeDurability(stack)) {
            item.stopConsuming(stack, level, entity, useDuration);
            stack.shrink(1);
        } else {
            setCustomDamage(stack, nextDamage);
        }
    }

    @Override
    public void onStopConsuming(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (entity instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(item));
        }
    }

    @Override
    public Boolean isBarVisible(ItemStack stack) {
        return getCustomDamage(stack) > 0;
    }

    @Override
    public Integer getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) getCustomDamage(stack) * 13.0F / (float) getConsumeDurability(stack));
    }

    @Override
    public Integer getBarColor(ItemStack stack) {
        float hue = Math.max(0.0F, (float) (getConsumeDurability(stack) - getCustomDamage(stack)) / (float) getConsumeDurability(stack));
        return Mth.hsvToRgb(hue / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void appendAdvancedTooltip(ItemStack stack, List<Component> tooltip) {
        if (getCustomDamage(stack) > 0) {
            tooltip.add(Component.translatable(
                    "item.substance.consumption",
                    getConsumeDurability(stack) - getCustomDamage(stack),
                    getConsumeDurability(stack)
            ));
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    private int getConsumeDurability(ItemStack stack) {
        return maxDurabilityProvider.applyAsInt(stack);
    }

    private int getCustomDamage(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getInt(DAMAGE_KEY) : 0;
    }

    private void setCustomDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt(DAMAGE_KEY, Mth.clamp(damage, 0, getConsumeDurability(stack)));
    }
}
