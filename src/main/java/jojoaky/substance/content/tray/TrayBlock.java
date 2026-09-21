package jojoaky.substance.content.tray;

import jojoaky.substance.register.ModTrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.material.PushReaction;
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
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TrayBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);
    public static final BooleanProperty DRIED = BooleanProperty.create("dried");
    public static final int DRY_DELAY_TICKS = 30;

    private final Supplier<? extends Item> filledFlask;
    private final Supplier<? extends Item> emptyFlask;
    private final ResourceKey<LootTable> lootTable;

    public TrayBlock(
            Properties properties,
            Supplier<? extends Item> filledFlask,
            Supplier<? extends Item> emptyFlask,
            ResourceLocation lootTable
    ) {
        super(properties.pushReaction(PushReaction.DESTROY));
        this.filledFlask = filledFlask;
        this.emptyFlask = emptyFlask;
        this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, lootTable);
        registerDefaultState(stateDefinition.any()
                .setValue(LEVEL, 3)
                .setValue(DRIED, false)
                .setValue(EmptyTrayBlock.FACING, Direction.NORTH));
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

        if (tryShatter(state, level, pos, heldStack)) {
            if (level instanceof ServerLevel serverLevel) {
                heldStack.hurtAndBreak(1, serverLevel, player, ignored -> {});
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (tryFill(state, level, pos, heldStack)) {
            if (!level.isClientSide) {
                replaceFlask(player, hand, emptyFlask.get());
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (tryEmpty(state, level, pos, heldStack)) {
            if (!level.isClientSide) {
                replaceFlask(player, hand, filledFlask.get());
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public boolean tryFill(BlockState state, Level level, BlockPos pos, ItemStack stack) {
        int trayLevel = state.getValue(LEVEL);
        if (!stack.is(filledFlask.get()) || trayLevel >= 3) return false;

        if (!level.isClientSide) {
            BlockState filled = state.setValue(LEVEL, trayLevel + 1).setValue(DRIED, false);
            level.setBlock(pos, filled, Block.UPDATE_ALL);
            if (trayLevel + 1 == 3) level.scheduleTick(pos, this, DRY_DELAY_TICKS);
        }
        return true;
    }

    public boolean tryEmpty(BlockState state, Level level, BlockPos pos, ItemStack stack) {
        if (!stack.is(emptyFlask.get()) || state.getValue(DRIED)) return false;

        if (!level.isClientSide) {
            int trayLevel = state.getValue(LEVEL);
            BlockState replacement = trayLevel == 1
                    ? ModTrays.EMPTY_TRAY.get().defaultBlockState().setValue(EmptyTrayBlock.FACING, state.getValue(EmptyTrayBlock.FACING))
                    : state.setValue(LEVEL, trayLevel - 1).setValue(DRIED, false);
            level.setBlock(pos, replacement, Block.UPDATE_ALL);
        }
        return true;
    }

    public boolean tryShatter(BlockState state, Level level, BlockPos pos, ItemStack stack) {
        if (!state.getValue(DRIED) || !(stack.getItem() instanceof PickaxeItem)) return false;

        if (level instanceof ServerLevel serverLevel) {
            LootTable table = serverLevel.getServer().reloadableRegistries().getLootTable(lootTable);
            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, stack)
                    .withOptionalParameter(LootContextParams.BLOCK_STATE, state)
                    .create(LootContextParamSets.BLOCK);

            for (ItemStack drop : table.getRandomItems(params)) {
                popResource(level, pos, drop);
            }
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.GLASS_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.7F, 1.1F);
            level.setBlock(pos, ModTrays.EMPTY_TRAY.get().defaultBlockState()
                    .setValue(EmptyTrayBlock.FACING, state.getValue(EmptyTrayBlock.FACING)), Block.UPDATE_ALL);
        }
        return true;
    }

    public Item getFilledFlask() {
        return filledFlask.get();
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
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(EmptyTrayBlock.FACING).getAxis() == Direction.Axis.Z
                ? EmptyTrayBlock.SHAPE
                : EmptyTrayBlock.ROTATED_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState().setValue(EmptyTrayBlock.FACING, EmptyTrayBlock.getPlacementFacing(context));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
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

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
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
    public void tick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull net.minecraft.util.RandomSource random) {
        if (state.getValue(LEVEL) == 3 && !state.getValue(DRIED)) {
            level.setBlock(pos, state.setValue(DRIED, true), Block.UPDATE_ALL);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BOTTLE_FILL,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.45F, 0.8F);
        }
    }
}
