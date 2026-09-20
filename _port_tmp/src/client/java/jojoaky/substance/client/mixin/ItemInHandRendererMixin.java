package jojoaky.substance.client.mixin;

// https://github.com/TeamGalena/Nirvana/blob/main/1.21.x/common/src/main/java/galena/nirvana/mixins/ItemInHandRendererMixin.java

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import jojoaky.substance.client.animation.SmokeAnimation;
import jojoaky.substance.client.animation.SniffAnimation;
import jojoaky.substance.content.consumable.PowderConsumableItem;
import jojoaky.substance.register.ModTags;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(
            method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    )
    private void animateConsumption(
            AbstractClientPlayer player, float partialTick, float pitch, InteractionHand hand,
            float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack,
            MultiBufferSource buffers, int light, CallbackInfo ci
    ) {
        if (!player.isUsingItem() || player.getUseItemRemainingTicks() <= 0
                || player.getUsedItemHand() != hand) {
            return;
        }

        if (stack.is(ModTags.SMOKABLE_ITEM) && player.getUseItem().is(ModTags.SMOKABLE_ITEM)) {
            SmokeAnimation.applyFirstPerson(poseStack, player, partialTick);
        } else if (SniffAnimation.isSniffing(player) && stack.getItem() instanceof PowderConsumableItem) {
            SniffAnimation.applyFirstPerson(poseStack, player, partialTick);
        }
    }

    @WrapOperation(
            method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;")
    )
    private UseAnim selectFirstPersonUseAnimation(ItemStack instance, Operation<UseAnim> original) {
        if (instance.is(ModTags.SMOKABLE_ITEM)) {
            // Keep the normal held-item offset; animateConsumption supplies the smoking pose.
            return UseAnim.NONE;
        }
        return original.call(instance);
    }
}
