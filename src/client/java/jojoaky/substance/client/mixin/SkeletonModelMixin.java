package jojoaky.substance.client.mixin;

import jojoaky.substance.register.ModTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonModel.class)
public abstract class SkeletonModelMixin<T extends Mob> extends HumanoidModel<T> {

    public SkeletonModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(
            method = "setupAnim(Lnet/minecraft/world/entity/Mob;FFFFF)V",
            at = @At("TAIL")
    )
    private void applyCustomItemAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (entity.isUsingItem() && entity.getUseItem().is(ModTags.SMOKABLE_ITEM)) {
            InteractionHand usedHand = entity.getUsedItemHand();
            HumanoidArm armSide = (usedHand == InteractionHand.MAIN_HAND)
                    ? entity.getMainArm()
                    : entity.getMainArm().getOpposite();

            boolean isRightArm = armSide == HumanoidArm.RIGHT;
            ModelPart arm = isRightArm ? this.rightArm : this.leftArm;

            arm.xRot = -1.8F;
            arm.yRot = isRightArm ? -0.3F : 0.3F;
            arm.zRot = 0.0F;
        }
    }
}