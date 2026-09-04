package jojoaky.substance.client.animation;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

/** Common timing and handedness for consumable item poses. */
public final class ItemUseAnimation {
    private ItemUseAnimation() {
    }

    public static HumanoidArm usedArm(LivingEntity entity) {
        return entity.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? entity.getMainArm() : entity.getMainArm().getOpposite();
    }

    static float elapsedTicks(LivingEntity entity, float partialTick) {
        // Match vanilla use animations' interpolation between the previous and current tick.
        return Math.max(0.0F, entity.getUseItem().getUseDuration()
                - entity.getUseItemRemainingTicks() - 1.0F + Mth.clamp(partialTick, 0.0F, 1.0F));
    }

    static float poseBlend(float elapsed, int duration, float transitionTicks) {
        // Short configured uses still have time to lift and lower the item.
        float transition = Math.min(transitionTicks, Math.max(1.0F, duration * 0.5F));
        float progress = Mth.clamp(Math.min(elapsed, duration - elapsed) / transition, 0.0F, 1.0F);
        return progress * progress * (3.0F - 2.0F * progress);
    }
}
