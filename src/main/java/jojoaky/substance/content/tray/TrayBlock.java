package jojoaky.substance.content.tray;

import jojoaky.substance.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

public class TrayBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);
    public static final BooleanProperty DRIED = BooleanProperty.create("dried");
    private static final int DRY_DELAY_TICKS = 30;

    private final Item filledFlask;
    private final Item emptyFlask;
    private final ResourceLocation lootTable;

    public TrayBlock(Properties properties, Item filledFlask, Item emptyFlask, ResourceLocation lootTable) {
        super(properties);
        this.filledFlask = filledFlask;
        this.emptyFlask = emptyFlask;
        this.lootTable = lootTable;
        registerDefaultState(stateDefinition.any()
                .setValue(LEVEL, 3)
                .setValue(DRIED, false)
                .setValue(EmptyTrayBlock.FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        int trayLevel = state.getValue(LEVEL);

        if (state.getValue(DRIED) && heldStack.getItem() instanceof PickaxeItem) {
            if (!level.isClientSide) {
                LootTable table = ((ServerLevel) level).getServer().getLootData().getLootTable(lootTable);
                LootParams params = new LootParams.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, heldStack)
                        .withOptionalParameter(LootContextParams.BLOCK_STATE, state)
                        .create(LootContextParamSets.BLOCK);

                for (ItemStack drop : table.getRandomItems(params)) {
                    popResource(level, pos, drop);
                }
                heldStack.hurtAndBreak(1, player, ignored -> {});
                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.GLASS_BREAK,
                        net.minecraft.sounds.SoundSource.BLOCKS, 0.7F, 1.1F);
                level.setBlock(pos, ModBlocks.TRAY.defaultBlockState()
                        .setValue(EmptyTrayBlock.FACING, state.getValue(EmptyTrayBlock.FACING)), Block.UPDATE_ALL);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (heldStack.is(filledFlask) && trayLevel < 3) {
            if (!level.isClientSide) {
                replaceFlask(player, hand, emptyFlask);
                BlockState filled = state.setValue(LEVEL, trayLevel + 1).setValue(DRIED, false);
                level.setBlock(pos, filled, Block.UPDATE_ALL);
                if (trayLevel + 1 == 3) level.scheduleTick(pos, this, DRY_DELAY_TICKS);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (heldStack.is(emptyFlask) && !state.getValue(DRIED)) {
            if (!level.isClientSide) {
                replaceFlask(player, hand, filledFlask);
                BlockState replacement = trayLevel == 1
                        ? ModBlocks.TRAY.defaultBlockState().setValue(EmptyTrayBlock.FACING, state.getValue(EmptyTrayBlock.FACING))
                        : state.setValue(LEVEL, trayLevel - 1).setValue(DRIED, false);
                level.setBlock(pos, replacement, Block.UPDATE_ALL);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    static void replaceFlask(Player player, InteractionHand hand, Item result) {
        if (player.getAbilities().instabuild) return;

        ItemStack stack = player.getItemInHand(hand);
        ItemStack resultStack = new ItemStack(result);
        if (stack.getCount() == 1) {
            player.setItemInHand(hand, resultStack);
        } else {
            stack.shrink(1);
            player.getInventory().placeItemBackInInventory(resultStack);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(EmptyTrayBlock.FACING).getAxis() == net.minecraft.core.Direction.Axis.Z
                ? EmptyTrayBlock.SHAPE
                : EmptyTrayBlock.ROTATED_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(EmptyTrayBlock.FACING, EmptyTrayBlock.getPlacementFacing(context));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && state.getValue(LEVEL) == 3 && !state.getValue(DRIED)) {
            level.scheduleTick(pos, this, DRY_DELAY_TICKS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, DRIED, EmptyTrayBlock.FACING);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (state.getValue(LEVEL) == 3 && !state.getValue(DRIED)) {
            level.setBlock(pos, state.setValue(DRIED, true), Block.UPDATE_ALL);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BOTTLE_FILL,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.45F, 0.8F);
        }
    }
}