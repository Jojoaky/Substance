package jojoaky.substance.mixin;

import jojoaky.substance.content.pipe.PipeItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
            )
    )
    private <T extends LivingEntity> void onPipeItemBreak(
            int amount,
            T entity,
            Consumer<T> onBroken,
            CallbackInfo ci
    ) {
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.getItem() instanceof PipeItem) {
            PipeItem.dropContents(stack, entity.level(), entity.position());
        }
    }
}