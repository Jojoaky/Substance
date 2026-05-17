package jojoaky.substance.mixin;

import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import jojoaky.substance.crops.LargeHerbBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HarvesterMovementBehaviour.class)
public class HarvesterMovementBehaviourMixin {

    @Inject(method = "isValidCrop", at = @At("HEAD"), cancellable = true)
    private void onIsValidCrop(Level world, BlockPos pos, BlockState state,
                               CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof LargeHerbBlock) {
            boolean isLower = state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER;
            boolean isMature = state.getValue(LargeHerbBlock.AGE) == LargeHerbBlock.MAX_AGE;
            cir.setReturnValue(isLower && isMature);
        }
    }

    @Inject(method = "cutCrop", at = @At("HEAD"), cancellable = true)
    private void onCutCrop(Level world, BlockPos pos, BlockState state,
                           CallbackInfoReturnable<BlockState> cir) {
        if (state.getBlock() instanceof LargeHerbBlock) {
            BlockPos upperPos = pos.above();

            BlockState upperState = world.getBlockState(upperPos);

            BlockState newLowerState = state.setValue(LargeHerbBlock.AGE, LargeHerbBlock.AGE_AFTER_HARVEST);

            if (upperState.isAir()) {
                BlockState newUpperState = newLowerState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
                world.setBlock(upperPos, newUpperState, 2);
            }

            /*
            if (upperState.getBlock() instanceof LargeHerbBlock) {
                BlockState newUpperState = upperState.setValue(LargeHerbBlock.AGE, LargeHerbBlock.AGE_AFTER_HARVEST);
                world.setBlock(upperPos, newUpperState, 2);
            }
             */

            cir.setReturnValue(newLowerState);
        }
    }
}