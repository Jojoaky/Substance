package jojoaky.substance.content.consumable;

import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalsItem extends PowderConsumableItem {
    // A full three-second use adds one minute of Surge and half a minute of Warp.
    private static final int SURGE_DURATION_MULTIPLIER = 20;
    private static final int WARP_DURATION_MULTIPLIER = 10;

    public CrystalsItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void onFinishConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onFinishConsuming(stack, level, entity, useDuration);

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.SURGE,
                useDuration * SURGE_DURATION_MULTIPLIER, 900, 3);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.WARP,
                useDuration * WARP_DURATION_MULTIPLIER, 600, 2);
    }
}
