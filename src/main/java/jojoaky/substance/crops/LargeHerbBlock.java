package jojoaky.substance.crops;

import jojoaky.substance.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LargeHerbBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final int MAX_AGE = 3;
    public static final int AGE_AFTER_HARVEST = 1;

    public LargeHerbBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder); // registers HALF
        builder.add(AGE);
    }

    protected void setAge(Level level, BlockPos pos, BlockState state, int newAge) {
        DoubleBlockHalf half = state.getValue(HALF);
        level.setBlock(pos, state.setValue(AGE, newAge), 2);

        if (half == DoubleBlockHalf.LOWER) {
            BlockState upper = level.getBlockState(pos.above());
            if (upper.is(this)) {
                level.setBlock(pos.above(), upper.setValue(AGE, newAge), 2);
            }
        } else {
            BlockState lower = level.getBlockState(pos.below());
            if (lower.is(this)) {
                level.setBlock(pos.below(), lower.setValue(AGE, newAge), 2);
            }
        }
    }

    public ItemStack cut(BlockState state, Level level, BlockPos pos) {
        int age = state.getValue(AGE);

        if (age < MAX_AGE) return ItemStack.EMPTY;

        setAge(level, pos, state, AGE_AFTER_HARVEST);

        float pitch = 0.9F + level.random.nextFloat() * 0.2F;
        level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, pitch);

        int count = 1 + level.random.nextInt(3);
        return new ItemStack(ModItems.HERB_BUD, count);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        ItemStack stack = player.getItemInHand(hand);

        if (age < MAX_AGE || !stack.is(Items.SHEARS)) {
            return InteractionResult.PASS;
        }

        ItemStack result = cut(state, level, pos);

        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        popResource(level, pos, result);

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos,
                                         BlockState state, boolean isClient) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random,
                                     BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random,
                                BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE && (random.nextInt(10) == 0)) {
            setAge(level, pos, state, age+1);
        }
    }

    // Advance age randomly via random tick
    @Override
    public void randomTick(BlockState state, ServerLevel level,
                           BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);

        if (age < MAX_AGE && random.nextInt(10) == 0) {
            setAge(level, pos, state, age+1);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }
}