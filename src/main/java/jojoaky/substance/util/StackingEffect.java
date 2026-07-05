package jojoaky.substance.util;

import net.minecraft.world.effect.MobEffect;

public record StackingEffect(MobEffect effect, int durationMultiplier, int durationPerAmplifier, int maxAmplifier) {
}
