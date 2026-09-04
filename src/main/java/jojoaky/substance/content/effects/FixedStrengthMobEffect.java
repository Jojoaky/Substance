package jojoaky.substance.content.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A mob effect whose attribute modifiers do not scale with its amplifier.
 */
public class FixedStrengthMobEffect extends VisualMobEffect {
    public FixedStrengthMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount();
    }
}
