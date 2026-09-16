package jojoaky.substance.content.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface CuttableCrop {

    boolean isMature(BlockState state);

    List<ItemStack> cut(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool);
}
