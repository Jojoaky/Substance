package jojoaky.substance.util;

import jojoaky.substance.content.effects.VisualMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SubstanceEffectHelper {

    public static void applyStackingEffect(LivingEntity entity, int baseDuration, StackingEffect effect) {
        applyStackingEffect(
                entity,
                effect.effect(),
                baseDuration * effect.durationMultiplier(),
                effect.durationPerAmplifier(),
                effect.maxAmplifier()
        );
    }

    public static void applyStackingEffect(LivingEntity entity, MobEffect effect, int duration, int durationPerAmplifier, int maxAmplifier) {
        if (entity.level().isClientSide) return;

        var effectInstance = entity.getEffect(effect);
        int currentDuration = effectInstance!=null ? effectInstance.getDuration() : 0;
        int currentAmplifier = effectInstance!=null ? effectInstance.getAmplifier() : 0;

        int newDuration = currentDuration + duration;
        int newAmplifier = Math.min(newDuration / durationPerAmplifier, maxAmplifier);

        if (newAmplifier < currentAmplifier) return;

        int maximumDuration = Math.max(duration, maxAmplifier * durationPerAmplifier);
        newDuration = Math.min(newDuration, maximumDuration);

        entity.addEffect(new MobEffectInstance(effect, newDuration, newAmplifier));
    }

    public static void applyEffectBase(LivingEntity entity, MobEffect effect, int duration, int amplifier) {
        if (entity.level().isClientSide) return;

        entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }
}
