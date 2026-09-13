package jojoaky.substance.content.tray;

import jojoaky.substance.register.ModTrays;
import jojoaky.substance.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FillingTrayDispenserBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        ModTrays.TrayEntry tray = ModTrays.getForFlask(stack.getItem());
        if (tray == null) return super.execute(source, stack);

        BlockPos targetPos = DispenserHelper.getTargetPos(source);
        BlockState state = source.getLevel().getBlockState(targetPos);
        boolean filled = state.getBlock() instanceof EmptyTrayBlock emptyTray
                ? emptyTray.tryFill(state, source.getLevel(), targetPos, stack)
                : state.getBlock() instanceof TrayBlock filledTray
                && filledTray.tryFill(state, source.getLevel(), targetPos, stack);

        if (!filled) return super.execute(source, stack);

        return DispenserHelper.createFilledResult(source, stack, new ItemStack(tray.emptyFlask()));
    }
}
