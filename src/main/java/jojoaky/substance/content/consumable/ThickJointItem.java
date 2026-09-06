package jojoaky.substance.content.consumable;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThickJointItem extends JointItem {
    public ThickJointItem(Properties properties) {
        super(properties, stack -> Config.gameplay().thickHerbalRollDurability());
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int i) {
        super.onUseTick(level, entity, stack, i);

        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.RELAXATION, 160, 0);
        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.WARP, 160, 0);
    }

    @Override
    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onStopConsuming(stack, level, entity, useDuration);
        if (useDuration > 100) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 4 * 20));

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.RELAXATION, useDuration * 12, 800, 3);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.WARP, useDuration * 12, 600, 4);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.HAZE, useDuration * 8, 600, 1);
    }
}
