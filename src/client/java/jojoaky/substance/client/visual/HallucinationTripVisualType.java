package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public final class HallucinationTripVisualType extends TripVisualType {
    public HallucinationTripVisualType() {
        super(ModEffects.HALLUCINATION, 60);
    }

    @Override
    protected boolean isEnabled(Config config) {
        return config.enableHallucinationVisuals;
    }

    @Override
    public int maxInstances(Config config) {
        return config.hallucinationMaxApparitions;
    }

    @Override
    public float intervalSeconds(Config config) {
        return config.hallucinationApparitionInterval;
    }

    @Override
    public float strength(Config config) {
        return config.hallucinationVisualStrength;
    }

    @Override
    public void spawn(
            Minecraft minecraft,
            ClientLevel level,
            Config config,
            RandomSource random,
            Consumer<TripVisual> sink
    ) {
        if (minecraft.player == null) {
            return;
        }

        if (random.nextFloat() < config.hallucinationVillagerChance) {
            Vec3 position = minecraft.player.position().add(minecraft.player.getLookAngle().scale(22))
                    .add(random.nextInt(13) - 6, 10 + random.nextInt(8), random.nextInt(13) - 6);
            LivingEntity villager = createEntity(level, EntityType.VILLAGER, position);
            if (villager != null) {
                sink.accept(new EntityTripVisual(this, villager, position, 240, true));
            }
            return;
        }

        // Render a displaced image of nearby terrain without changing the real block.
        for (int i = 0; i < 16; i++) {
            BlockPos position = minecraft.player.blockPosition().offset(
                    random.nextInt(15) - 7,
                    random.nextInt(7) - 3,
                    random.nextInt(15) - 7
            );
            if (!level.hasChunkAt(position)) {
                continue;
            }
            BlockState state = level.getBlockState(position);
            if (state.getRenderShape() != RenderShape.MODEL
                    || state.hasBlockEntity()
                    || !state.isSolidRender(level, position)
                    || !level.getBlockState(position.above()).isAir()) {
                continue;
            }

            sink.accept(new BlockTripVisual(this, state, Vec3.atLowerCornerOf(position), 100));
            return;
        }
    }

}
