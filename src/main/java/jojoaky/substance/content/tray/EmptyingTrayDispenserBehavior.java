package jojoaky.substance.content.tray;

import jojoaky.substance.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EmptyingTrayDispenserBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        BlockPos targetPos = DispenserHelper.getTargetPos(source);
        BlockState state = source.level().getBlockState(targetPos);

        if (!(state.getBlock() instanceof TrayBlock tray)
                || !tray.tryEmpty(state, source.level(), targetPos, stack)) {
            return super.execute(source, stack);
        }

        return DispenserHelper.createFilledResult(source, stack, new ItemStack(tray.getFilledFlask()));
    }
}
