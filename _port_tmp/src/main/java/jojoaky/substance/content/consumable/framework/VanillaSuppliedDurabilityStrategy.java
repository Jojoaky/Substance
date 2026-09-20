package jojoaky.substance.content.consumable.framework;

import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.ToIntFunction;

public class VanillaSuppliedDurabilityStrategy implements DurabilityStrategy {
    private final ToIntFunction<ItemStack> maxDurabilityProvider;

    public VanillaSuppliedDurabilityStrategy(ToIntFunction<ItemStack> maxDurabilityProvider) {
        this.maxDurabilityProvider = maxDurabilityProvider;
    }

    @Override
    public Item.Properties configureProperties(Item.Properties properties) {
        return properties.durability(1);
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

    @Override
    public int getMaxDamage(Item item, ItemStack stack) {
        return getMaxDurability(stack);
    }

    @Override
    public Integer getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) stack.getDamageValue() * 13.0F / getMaxDurability(stack));
    }

    @Override
    public Integer getBarColor(ItemStack stack) {
        float remaining = Math.max(0.0F, (float) (getMaxDurability(stack) - stack.getDamageValue()) / getMaxDurability(stack));
        return Mth.hsvToRgb(remaining / 3.0F, 1.0F, 1.0F);
    }

    private int getMaxDurability(ItemStack stack) {
        return Math.max(1, maxDurabilityProvider.applyAsInt(stack));
    }
}
