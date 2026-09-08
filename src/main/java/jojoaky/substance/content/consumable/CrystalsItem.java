package jojoaky.substance.content.consumable;

import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalsItem extends PowderConsumableItem {
    // The default full three-second use adds one minute of Surge and half a minute of Warp.
    private static final int SURGE_DURATION_MULTIPLIER = 20;
    private static final int WARP_DURATION_MULTIPLIER = 10;

    public enum Type {
        WHITE(1.0f, 1, 0.0f),
        WHITE_CHILI(0.9f, 2, 1.0f),
        BLUE(1.1f, 4, 0.0f);

        private final float effectDurationScale;
        private final int maximumEffectAmplifier;
        private final float consumptionDamage;

        Type(float effectDurationScale, int maximumEffectLevel, float consumptionDamage) {
            this.effectDurationScale = effectDurationScale;
            this.maximumEffectAmplifier = maximumEffectLevel - 1;
            this.consumptionDamage = consumptionDamage;
        }
    }

    private final Type type;

    public CrystalsItem(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }

    @Override
    protected void onFinishConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onFinishConsuming(stack, level, entity, useDuration);

        int surgeDuration = Math.round(useDuration * SURGE_DURATION_MULTIPLIER * type.effectDurationScale);
        int warpDuration = Math.round(useDuration * WARP_DURATION_MULTIPLIER * type.effectDurationScale);

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.SURGE,
                surgeDuration, 900, type.maximumEffectAmplifier);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.WARP,
                warpDuration, 600, type.maximumEffectAmplifier);

        if (!level.isClientSide && type.consumptionDamage > 0.0f) {
            entity.hurt(level.damageSources().magic(), type.consumptionDamage);
        }
    }
}
