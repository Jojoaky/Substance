package jojoaky.substance.mixin;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import jojoaky.substance.content.crops.CuttableCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = HarvesterMovementBehaviour.class)
public class HarvesterMovementBehaviourMixin {

    @Inject(method = "isValidCrop", at = @At("HEAD"), cancellable = true)
    private void onIsValidCrop(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof CuttableCrop) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isValidOther", at = @At("HEAD"), cancellable = true)
    private void onIsValidOther(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof CuttableCrop cuttableCrop) {
            cir.setReturnValue(cuttableCrop.isMature(state));
        }
    }

    @Inject(
            method = "visitNewPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/BlockHelper;destroyBlockAs" +
                            "(Lnet/minecraft/world/level/Level;" +
                            "Lnet/minecraft/core/BlockPos;" +
                            "Lnet/minecraft/world/entity/player/Player;" +
                            "Lnet/minecraft/world/item/ItemStack;" +
                            "F" +
                            "Ljava/util/function/Consumer;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void onDestroyBlockAs(
            MovementContext context,
            BlockPos pos,
            CallbackInfo ci
    ) {
        Level world = context.world;

        BlockState stateVisited = world.getBlockState(pos);

        if (stateVisited.getBlock() instanceof CuttableCrop cuttableCrop) {
            if (world instanceof ServerLevel serverLevel) {
                List<ItemStack> drops = cuttableCrop.cut(
                        stateVisited,
                        serverLevel,
                        pos,
                        Items.SHEARS.getDefaultInstance()
                );
                drops.forEach(item -> substance$dropItem(context, item));
            }

            ci.cancel();
        }
    }

    @Unique
    private void substance$dropItem(MovementContext context, ItemStack stack) {
        ((MovementBehaviour) (Object) this).dropItem(context, stack);
    }
}
