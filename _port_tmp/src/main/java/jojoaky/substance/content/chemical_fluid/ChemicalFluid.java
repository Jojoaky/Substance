package jojoaky.substance.content.chemical_fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ChemicalFluid extends FlowingFluid {

    private final float thickness;
    // Suppliers to avoid forward-reference issues at class-load time
    private final Supplier<FlowingFluid> still;
    private final Supplier<FlowingFluid> flowing;
    private final Supplier<Block> block;
    private final Supplier<Item> bucket;

    protected ChemicalFluid(
            float thickness,
            Supplier<FlowingFluid> still,
            Supplier<FlowingFluid> flowing,
            Supplier<Block> block,
            Supplier<Item> bucket
    ) {
        this.thickness = Math.max(1, thickness);
        this.still = still;
        this.flowing = flowing;
        this.block = block;
        this.bucket = bucket;
    }

    @Override
    public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !(Boolean) state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playLocalSound(
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                        SoundSource.AMBIENT,
                        random.nextFloat() * 0.25F + 0.75F,
                        random.nextFloat() + 0.5F,
                        false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(
                    ParticleTypes.UNDERWATER,
                    pos.getX() + random.nextDouble(),
                    pos.getY() + random.nextDouble(),
                    pos.getZ() + random.nextDouble(),
                    0.0, 0.0, 0.0);
        }
    }

    @Nullable
    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    @Override
    protected boolean canConvertToSource(Level world) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader world) {
        return Math.max(1, Math.round(5 - thickness));
    }

    @Override
    public int getDropOff(LevelReader world) {
        return Math.round(thickness);
    }

    @Override
    public int getTickDelay(LevelReader world) {
        return Math.round(5 * thickness);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !isSame(fluid);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public @NotNull Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return flowing.get();
    }

    @Override
    public @NotNull Fluid getSource() {
        return still.get();
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == still.get() || fluid == flowing.get();
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState state) {
        return block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public @NotNull Item getBucket() {
        return bucket.get();
    }

    // -------------------------------------------------------------------------

    public static class Flowing extends ChemicalFluid {
        public Flowing(float thickness, Supplier<FlowingFluid> still, Supplier<FlowingFluid> flowing,
                       Supplier<Block> block, Supplier<Item> bucket) {
            super(thickness, still, flowing, block, bucket);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends ChemicalFluid {
        public Source(float thickness, Supplier<FlowingFluid> still, Supplier<FlowingFluid> flowing,
                      Supplier<Block> block, Supplier<Item> bucket) {
            super(thickness, still, flowing, block, bucket);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}