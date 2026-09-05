package jojoaky.substance.content.consumable.framework;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
        return properties;
    }

    @Override
    public void onConsumeTick(ConsumableItem item, ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (!(entity instanceof Player player) || player.isCreative()) return;

        if (isUnbreakable(stack)) return;

        int nextDamage = getCustomDamage(stack) + 1;
        if (nextDamage >= getConsumeDurability(stack)) {
            item.stopConsuming(stack, level, entity, useDuration);
            stack.shrink(1);
            if (!stack.isEmpty()) {
                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    tag.remove(DAMAGE_KEY);
                    if (tag.isEmpty()) {
                        stack.setTag(null);
                    }
                }
            }
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
        // Hide durability bar if item is unbreakable
        if (isUnbreakable(stack)) return false;
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (isAdvanced.isAdvanced()) {
            if (getCustomDamage(stack) > 0) {
                tooltipComponents.add(Component.translatable(
                        "item.substance.consumption",
                        getConsumeDurability(stack) - getCustomDamage(stack),
                        getConsumeDurability(stack)
                ));
            }
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    private boolean isUnbreakable(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean("Unbreakable");
    }

    private int getConsumeDurability(ItemStack stack) {
        return maxDurabilityProvider.applyAsInt(stack);
    }

    private int getCustomDamage(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt(DAMAGE_KEY) : 0;
    }

    private void setCustomDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt(DAMAGE_KEY, Mth.clamp(damage, 0, getConsumeDurability(stack)));
    }
}
