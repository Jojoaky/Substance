package jojoaky.substance.content.consumable;

import jojoaky.substance.Config;
import jojoaky.substance.content.consumable.framework.ConsumableItem;
import jojoaky.substance.content.consumable.framework.ConsumptionDurabilityStrategy;
import jojoaky.substance.content.consumable.framework.SmokeComponent;
import jojoaky.substance.register.ModEffects;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class JointItem extends ConsumableItem {
    public JointItem(Properties properties) {
        this(properties, stack -> Config.get().herbalRollDurability);
    }

    protected JointItem(Properties properties, java.util.function.ToIntFunction<ItemStack> durabilityProvider) {
        super(
                properties,
                new ConsumptionDurabilityStrategy(durabilityProvider),
                () -> Math.round(Config.get().maxSmokeDuration * 20.0f),
                () -> Math.round(Config.get().smokeCooldown * 20.0f),
                UseAnim.SPYGLASS,
                new SmokeComponent()
        );
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

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.RELAXATION, useDuration * 10, 800, 2);
        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.WARP, useDuration * 10, 600, 3);
    }
}
