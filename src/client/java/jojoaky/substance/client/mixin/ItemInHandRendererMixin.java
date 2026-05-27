package jojoaky.substance.client.mixin;

// https://github.com/TeamGalena/Nirvana/blob/main/1.21.x/common/src/main/java/galena/nirvana/mixins/ItemInHandRendererMixin.java

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import jojoaky.substance.register.ModTags;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @WrapOperation(
            method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;")
    )
    public UseAnim hideFirstPersonSmokableAnimation(ItemStack instance, Operation<UseAnim> original) {
        if(instance.is(ModTags.SMOKABLE_ITEM)) {
            return UseAnim.BOW;
        }
        return original.call(instance);
    }
}