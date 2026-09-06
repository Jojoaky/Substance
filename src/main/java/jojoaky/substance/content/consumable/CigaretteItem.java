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

public class CigaretteItem extends ConsumableItem {
    public CigaretteItem(Properties properties) {
        super(
                properties,
                new ConsumptionDurabilityStrategy(stack -> Config.gameplay().cigaretteDurability()),
                () -> Math.round(Config.gameplay().maxSmokeDuration() * 20.0f),
                () -> Math.round(Config.gameplay().smokeCooldown() * 20.0f),
                UseAnim.SPYGLASS,
                new SmokeComponent()
        );
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int i) {
        super.onUseTick(level, entity, stack, i);
        int useDuration = getUseDuration(stack) - i;

        SubstanceEffectHelper.applyEffectBase(entity, ModEffects.KEEN, 160, 0);
    }

    @Override
    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.onStopConsuming(stack, level, entity, useDuration);
        if (useDuration > 100) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 4 * 20));

        SubstanceEffectHelper.applyStackingEffect(entity, ModEffects.KEEN, useDuration * 6, 550, 2);
        SubstanceEffectHelper.applyEffectBase(entity, MobEffects.DIG_SPEED, 400, 1);

    }
}
