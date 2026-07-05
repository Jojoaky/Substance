package jojoaky.substance.content.consumable;

import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalsItem extends PowderConsumableItem {
    public CrystalsItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onConsumeTick(stack, level, entity, useDuration);

        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.WARP, 100, 0);
    }

    @Override
    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onStopConsuming(stack, level, entity, useDuration);

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.SURGE, useDuration, 900, 3);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.WARP, useDuration, 600, 2);
    }
}
