package jojoaky.substance.config;

import net.minecraft.world.entity.LivingEntity;

/** An active effect whose cached state must be rebuilt after a gameplay config change. */
public interface ConfigDependentEffect {
    void refreshAfterConfigChange(LivingEntity entity, int amplifier);
}
