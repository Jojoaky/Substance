package jojoaky.substance.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import jojoaky.substance.content.consumable.PowderConsumableItem;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

/** Shared, tick-based motion for powder consumption in both camera perspectives. */
public final class SniffAnimation {
    private static final float TRANSITION_TICKS = 5.0F;
    private static final float SNIFF_INTERVAL_TICKS = 10.0F;

    private SniffAnimation() {
    }

    public static boolean isSniffing(LivingEntity entity) {
        return entity.isUsingItem() && entity.getUseItemRemainingTicks() > 0
                && entity.getUseItem().getItem() instanceof PowderConsumableItem;
    }

    public static void applyFirstPerson(PoseStack poseStack, LivingEntity entity, float partialTick) {
        float elapsed = ItemUseAnimation.elapsedTicks(entity, partialTick);
        float blend = ItemUseAnimation.poseBlend(elapsed, entity.getUseItem().getUseDuration(), TRANSITION_TICKS);
        float sniff = sniffPulse(elapsed) * blend;
        int side = ItemUseAnimation.usedArm(entity) == HumanoidArm.RIGHT ? 1 : -1;

        // Applied after vanilla's held-item offset, inside its push/pop pair.
        poseStack.translate(side * -0.38F * blend, 0.32F * blend + 0.025F * sniff,
                0.12F * blend + 0.025F * sniff);
        poseStack.mulPose(Axis.XP.rotationDegrees(-25.0F * blend - 3.0F * sniff));
        poseStack.mulPose(Axis.YP.rotationDegrees(side * 20.0F * blend));
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * -15.0F * blend));
    }

    public static void applyThirdPerson(
            ModelPart arm, ModelPart head, LivingEntity entity, float partialTick
    ) {
        float elapsed = ItemUseAnimation.elapsedTicks(entity, partialTick);
        float blend = ItemUseAnimation.poseBlend(elapsed, entity.getUseItem().getUseDuration(), TRANSITION_TICKS);
        float sniff = sniffPulse(elapsed);
        int side = ItemUseAnimation.usedArm(entity) == HumanoidArm.RIGHT ? 1 : -1;

        float pitch = Mth.clamp(head.xRot, -0.7F, 0.7F) - 1.78F - 0.06F * sniff;
        arm.xRot = Mth.lerp(blend, arm.xRot, pitch);
        arm.yRot = Mth.lerp(blend, arm.yRot, head.yRot - side * 0.35F);
        arm.zRot = Mth.lerp(blend, arm.zRot, 0.0F);
    }

    private static float sniffPulse(float elapsed) {
        float phase = Math.max(0.0F, elapsed - TRANSITION_TICKS) / SNIFF_INTERVAL_TICKS;
        float inhale = Math.max(0.0F, Mth.sin(phase * Mth.TWO_PI));
        return inhale * inhale;
    }
}
