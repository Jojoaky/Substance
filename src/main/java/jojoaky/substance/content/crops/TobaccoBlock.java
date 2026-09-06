package jojoaky.substance.content.crops;

import jojoaky.substance.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TobaccoBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final int MAX_AGE = 3;
    public static final int AGE_AFTER_HARVEST = 0;

    private static final ResourceLocation CUT_LOOT_TABLE =
            new ResourceLocation("substance", "gameplay/tobacco_cut");

    public TobaccoBlock(Properties properties) {
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

    public List<ItemStack> cut(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool) {
        int age = state.getValue(AGE);

        if (age < MAX_AGE) return List.of();

        setAge(level, pos, state, AGE_AFTER_HARVEST);

        float pitch = 0.9F + level.random.nextFloat() * 0.2F;
        level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, pitch);

        LootTable lootTable = level.getServer().getLootData().getLootTable(CUT_LOOT_TABLE);

        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool)
                .withOptionalParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.BLOCK);

        return lootTable.getRandomItems(params);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        ItemStack stack = player.getItemInHand(hand);

        if (age < MAX_AGE || !stack.is(Items.SHEARS)) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            List<ItemStack> drops = cut(state, serverLevel, pos, stack);

            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

            for (ItemStack drop : drops) {
                popResource(level, pos, drop);
            }
        }

        return InteractionResult.SUCCESS;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
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
        if (age < MAX_AGE && (random.nextInt(7) == 0)) {
            setAge(level, pos, state, age+1);
        }
    }

    // Advance age randomly via random tick
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level,
                           BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);

        if (age < MAX_AGE && random.nextInt(7) == 0) {
            setAge(level, pos, state, age+1);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }
}