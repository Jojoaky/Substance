package jojoaky.substance.content.consumable;

import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JointItem extends SmokableItem {
    public JointItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onConsumeTick(stack, level, entity, useDuration);

        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.RELAXATION, 100, 0);
        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.WARP, 100, 0);
    }

    @Override
    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onStopConsuming(stack, level, entity, useDuration);
        if (useDuration > 100) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 4 * 20));

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.RELAXATION, useDuration * 10, 800, 3);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.WARP, useDuration * 10, 600, 4);
    }
}
