package jojoaky.substance.content.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChiliCropBlock extends BushBlock implements BonemealableBlock {

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public static final int AGE_AFTER_HARVEST = 1;

    private static final ResourceLocation HARVEST_LOOT_TABLE =
            new ResourceLocation("substance", "gameplay/chili_cut");

    private static final VoxelShape SHAPE =
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);

    public ChiliCropBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(AGE, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(AGE);
    }

    @Override
    protected boolean mayPlaceOn(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos
    ) {
        return state.is(BlockTags.DIRT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random
    ) {
        int age = state.getValue(AGE);

        if (age >= MAX_AGE) {
            return;
        }

        if (level.getRawBrightness(pos.above(), 0) < 9) {
            return;
        }

        if (random.nextInt(5) != 0) {
            return;
        }

        level.setBlock(
                pos,
                state.setValue(AGE, age + 1),
                Block.UPDATE_CLIENTS
        );
    }

    public List<ItemStack> harvest(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            ItemStack tool
    ) {
        if (state.getValue(AGE) < MAX_AGE) {
            return List.of();
        }

        level.setBlock(
                pos,
                state.setValue(AGE, AGE_AFTER_HARVEST),
                Block.UPDATE_CLIENTS
        );

        float pitch = 0.8F + level.random.nextFloat() * 0.4F;

        level.playSound(
                null,
                pos,
                SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                SoundSource.BLOCKS,
                1.0F,
                pitch
        );

        LootTable lootTable = level.getServer()
                .getLootData()
                .getLootTable(HARVEST_LOOT_TABLE);

        LootParams params = new LootParams.Builder(level)
                .withParameter(
                        LootContextParams.ORIGIN,
                        Vec3.atCenterOf(pos)
                )
                .withParameter(
                        LootContextParams.TOOL,
                        tool
                )
                .withOptionalParameter(
                        LootContextParams.BLOCK_STATE,
                        state
                )
                .create(LootContextParamSets.BLOCK);

        return lootTable.getRandomItems(params);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit
    ) {
        if (state.getValue(AGE) < MAX_AGE) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            List<ItemStack> drops = harvest(
                    state,
                    serverLevel,
                    pos,
                    player.getItemInHand(hand)
            );

            for (ItemStack drop : drops) {
                popResource(level, pos, drop);
            }

            level.gameEvent(
                    player,
                    GameEvent.BLOCK_CHANGE,
                    pos
            );
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isValidBonemealTarget(
            @NotNull LevelReader level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            boolean isClient
    ) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(
            @NotNull Level level,
            @NotNull RandomSource random,
            @NotNull BlockPos pos,
            @NotNull BlockState state
    ) {
        return true;
    }

    @Override
    public void performBonemeal(
            @NotNull ServerLevel level,
            @NotNull RandomSource random,
            @NotNull BlockPos pos,
            @NotNull BlockState state
    ) {
        int age = state.getValue(AGE);

        level.setBlock(
                pos,
                state.setValue(AGE, Math.min(MAX_AGE, age + 1)),
                Block.UPDATE_CLIENTS
        );
    }
}