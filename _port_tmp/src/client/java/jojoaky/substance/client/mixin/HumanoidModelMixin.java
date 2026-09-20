package jojoaky.substance.client.mixin;

import jojoaky.substance.client.animation.ItemUseAnimation;
import jojoaky.substance.client.animation.SniffAnimation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void animateSniffing(
            LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, CallbackInfo ci
    ) {
        if (SniffAnimation.isSniffing(entity)) {
            ModelPart arm = ItemUseAnimation.usedArm(entity) == HumanoidArm.RIGHT ? rightArm : leftArm;
            // PlayerModel copies these arm poses to the sleeves after this method returns.
            SniffAnimation.applyThirdPerson(arm, head, entity, ageInTicks - entity.tickCount);
        }
    }
}
