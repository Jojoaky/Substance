package jojoaky.substance.mixin;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.BlockHelper;
import jojoaky.substance.content.crops.LargeHerbBlock;
import jojoaky.substance.content.crops.TobaccoBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = HarvesterMovementBehaviour.class)
public class HarvesterMovementBehaviourMixin implements MovementBehaviour {

    @Inject(method = "isValidCrop", at = @At("HEAD"), cancellable = true)
    private void onIsValidCrop(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof LargeHerbBlock) {
            cir.setReturnValue(false);
        }

        if (state.getBlock() instanceof TobaccoBlock) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isValidOther", at = @At("HEAD"), cancellable = true)
    private void onIsValidOther(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof LargeHerbBlock) {
            boolean isMature = state.getValue(LargeHerbBlock.AGE) == LargeHerbBlock.MAX_AGE;
            cir.setReturnValue(isMature);
        }

        if (state.getBlock() instanceof TobaccoBlock) {
            boolean isMature = state.getValue(TobaccoBlock.AGE) == TobaccoBlock.MAX_AGE;
            cir.setReturnValue(isMature);
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
                            "Ljava/util/function/Consumer;)V"
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

        if (stateVisited.getBlock() instanceof LargeHerbBlock largeHerbBlock) {
            if (world instanceof ServerLevel serverLevel) {
                List<ItemStack> result = largeHerbBlock.cut(stateVisited, serverLevel, pos, Items.SHEARS.getDefaultInstance());
                result.forEach(item -> dropItem(context, item));
            }
            ci.cancel();
        }

        if (stateVisited.getBlock() instanceof TobaccoBlock tobaccoBlock) {
            if (world instanceof ServerLevel serverLevel) {
                List<ItemStack> result = tobaccoBlock.cut(stateVisited, serverLevel, pos, Items.SHEARS.getDefaultInstance());
                result.forEach(item -> dropItem(context, item));
            }

            ci.cancel();
        }
    }
}