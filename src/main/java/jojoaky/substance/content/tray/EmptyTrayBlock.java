package jojoaky.substance.content.tray;

import jojoaky.substance.register.ModTrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmptyTrayBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 3, 15, 1, 13),
            box(1, 1, 3, 15, 2, 4),
            box(1, 1, 12, 15, 2, 13),
            box(1, 1, 4, 2, 2, 12),
            box(14, 1, 4, 15, 2, 12)
    );
    public static final VoxelShape ROTATED_SHAPE = Shapes.or(
            box(3, 0, 1, 13, 1, 15),
            box(3, 1, 1, 4, 2, 15),
            box(12, 1, 1, 13, 2, 15),
            box(4, 1, 1, 12, 2, 2),
            box(4, 1, 14, 12, 2, 15)
    );

    public EmptyTrayBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack heldStack,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit
    ) {
        ModTrays.TrayEntry tray = ModTrays.getForFlask(heldStack.getItem());
        if (tray == null || !tryFill(state, level, pos, heldStack)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide) {
            TrayBlock.replaceFlask(player, hand, tray.emptyFlask());
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    public boolean tryFill(BlockState state, Level level, BlockPos pos, ItemStack stack) {
        ModTrays.TrayEntry tray = ModTrays.getForFlask(stack.getItem());
        if (tray == null) return false;

        if (!level.isClientSide) {
            level.setBlock(pos, tray.block().defaultBlockState()
                    .setValue(TrayBlock.LEVEL, 1)
                    .setValue(FACING, state.getValue(FACING)), Block.UPDATE_ALL);
        }
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.Z ? SHAPE : ROTATED_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, getPlacementFacing(context));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState,
                                           @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public static Direction getPlacementFacing(BlockPlaceContext context) {
        return context.getHorizontalDirection().getClockWise();
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        return List.of(new ItemStack(asItem()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
