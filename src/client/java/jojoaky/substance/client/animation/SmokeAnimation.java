package jojoaky.substance.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public final class SmokeAnimation {
    private static final float TRANSITION_TICKS = 6.0F;
    private static final float BREATH_INTERVAL_TICKS = 30.0F;

    private SmokeAnimation() {
    }

    public static void applyFirstPerson(PoseStack poseStack, LivingEntity entity, float partialTick) {
        float elapsed = ItemUseAnimation.elapsedTicks(entity, partialTick);
        float blend = ItemUseAnimation.poseBlend(elapsed, entity.getUseItem().getUseDuration(), TRANSITION_TICKS);
        float phase = Math.max(0.0F, elapsed - TRANSITION_TICKS) / BREATH_INTERVAL_TICKS;
        float breath = (0.5F - 0.5F * Mth.cos(phase * Mth.TWO_PI)) * blend;
        int side = ItemUseAnimation.usedArm(entity) == HumanoidArm.RIGHT ? 1 : -1;

        // Lift toward the mouth, with a small, slow draw once the item is raised.
        poseStack.translate(side * -0.42F * blend, 0.27F * blend + 0.012F * breath,
                0.18F * blend + 0.02F * breath);
        poseStack.mulPose(Axis.XP.rotationDegrees(-15.0F * blend - 2.0F * breath));
        poseStack.mulPose(Axis.YP.rotationDegrees(side * 25.0F * blend));
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * -25.0F * blend));
    }
}
