package jojoaky.substance.mixin;

import jojoaky.substance.content.crops.CuttableCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.core.dispenser.ShearsDispenseItemBehavior.class)
public abstract class ShearsDispenseItemBehaviorMixin extends OptionalDispenseItemBehavior {

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private void substance$cutCrop(
            BlockSource source,
            ItemStack shears,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        ServerLevel level = source.getLevel();
        BlockPos targetPos = source.getPos().relative(
                source.getBlockState().getValue(DispenserBlock.FACING)
        );
        BlockState targetState = level.getBlockState(targetPos);

        if (!(targetState.getBlock() instanceof CuttableCrop crop) || !crop.isMature(targetState)) {
            return;
        }

        crop.cut(targetState, level, targetPos, shears)
                .forEach(drop -> Block.popResource(level, targetPos, drop));
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, targetPos);
        setSuccess(true);

        if (shears.hurt(1, level.random, null)) {
            shears.setCount(0);
        }

        cir.setReturnValue(shears);
    }
}
