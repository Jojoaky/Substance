package jojoaky.substance.content.tray;

import jojoaky.substance.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ShatteringTrayDispenserBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        BlockPos targetPos = DispenserHelper.getTargetPos(source);
        BlockState state = source.level().getBlockState(targetPos);
        boolean shattered = state.getBlock() instanceof TrayBlock tray
                && tray.tryShatter(state, source.level(), targetPos, stack);
        setSuccess(shattered);

        if (shattered) {
            stack.hurtAndBreak(1, source.level(), null, ignored -> {});
        }
        return stack;
    }
}
