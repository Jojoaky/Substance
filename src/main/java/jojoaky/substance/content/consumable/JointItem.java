package jojoaky.substance.content.consumable;

import jojoaky.substance.register.ModEffects;
import net.minecraft.util.Mth;
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
        if (useDuration > 80 / 8) entity.addEffect(new MobEffectInstance(ModEffects.RELAXATION, 80));
        if (useDuration > 80 / 8) entity.addEffect(new MobEffectInstance(ModEffects.WARP, 80));
    }

    @Override
    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onStopConsuming(stack, level, entity, useDuration);
        if (useDuration > 100) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 4 * 20));

        var effect = entity.getEffect(ModEffects.RELAXATION);
        int durationBefore = effect != null ? effect.getDuration() : 0;
        int amplifierBefore = effect != null ? effect.getAmplifier() : -1;

        int newDuration = Math.max(durationBefore, Math.round(durationBefore * 0.75f + useDuration * 8.f));
        int newAmplifier = Mth.clamp(amplifierBefore <= 1 ? 0 : amplifierBefore + 1, 0, 3);

        entity.addEffect(new MobEffectInstance(ModEffects.RELAXATION, newDuration, newAmplifier));
        entity.addEffect(new MobEffectInstance(ModEffects.WARP, newDuration, newAmplifier));
    }
}
