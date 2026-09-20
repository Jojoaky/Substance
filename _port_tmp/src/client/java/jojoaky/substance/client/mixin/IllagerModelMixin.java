package jojoaky.substance.client.mixin;

import jojoaky.substance.register.ModTags;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerModel.class)
public abstract class IllagerModelMixin<T extends AbstractIllager> extends HierarchicalModel<T> {

    @Final
    @Shadow private ModelPart rightArm;
    @Final
    @Shadow private ModelPart leftArm;
    @Final
    @Shadow private ModelPart arms;

    @Inject(
            method = "setupAnim(Lnet/minecraft/world/entity/monster/AbstractIllager;FFFFF)V",
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
            this.arms.visible = false;
            this.rightArm.visible = true;
            this.leftArm.visible = true;

            InteractionHand usedHand = entity.getUsedItemHand();
            HumanoidArm armSide = (usedHand == InteractionHand.MAIN_HAND)
                    ? entity.getMainArm()
                    : entity.getMainArm().getOpposite();

            boolean isRightArm = armSide == HumanoidArm.RIGHT;
            ModelPart activeArm = isRightArm ? this.rightArm : this.leftArm;

            activeArm.xRot = -1.8F;
            activeArm.yRot = isRightArm ? -0.3F : 0.3F;
            activeArm.zRot = 0.0F;
        }
    }
}