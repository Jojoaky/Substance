package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public final class DreadTripVisualType extends TripVisualType {
    public DreadTripVisualType() {
        super(ModEffects.DREAD, 100);
    }

    @Override
    protected boolean isEnabled(Config config) {
        return config.enableDreadVisuals;
    }

    @Override
    public int maxInstances(Config config) {
        return config.dreadMaxApparitions;
    }

    @Override
    public float intervalSeconds(Config config) {
        return config.dreadApparitionInterval;
    }

    @Override
    public float strength(Config config) {
        return config.dreadVisualStrength;
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

        if (random.nextFloat() < config.dreadCreeperChance) {
            Vec3 behind = minecraft.player.getLookAngle().multiply(1, 0, 1).normalize().scale(-5);
            BlockPos ground = groundAt(level, minecraft.player.position().add(behind));
            if (ground != null && Math.abs(ground.getY() - minecraft.player.getY()) < 4) {
                Vec3 position = Vec3.atBottomCenterOf(ground);
                LivingEntity creeper = createEntity(level, EntityType.CREEPER, position);
                if (creeper != null) {
                    sink.accept(new CreeperTripVisual(this, creeper, position, 180));
                }
            }
            return;
        }

        double angle = random.nextDouble() * Math.PI * 2;
        float distance = config.dreadAnimalDistance;
        Vec3 center = minecraft.player.position().add(
                Math.sin(angle) * distance,
                0,
                Math.cos(angle) * distance
        );
        EntityType<?>[] animals = {EntityType.COW, EntityType.PIG, EntityType.CHICKEN};
        EntityType<?> entityType = animals[random.nextInt(animals.length)];
        int count = 1 + random.nextInt(3);
        for (int i = 0; i < count; i++) {
            BlockPos ground = groundAt(level, center.add(i * 2.5, 0, random.nextDouble() * 3));
            if (ground == null) {
                continue;
            }
            Vec3 position = Vec3.atBottomCenterOf(ground);
            LivingEntity animal = createEntity(level, entityType, position);
            if (animal != null) {
                sink.accept(new DistantEntityTripVisual(this, animal, position, 600));
            }
        }
    }

    private BlockPos groundAt(ClientLevel level, Vec3 position) {
        BlockPos column = BlockPos.containing(position);
        if (!level.hasChunk(SectionPos.blockToSectionCoord(column.getX()), SectionPos.blockToSectionCoord(column.getZ()))) {
            return null;
        }

        // NO_LEAVES is not synchronized to clients and starts empty in client chunks.
        BlockPos ground = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, column);
        if (ground.getY() <= level.getMinBuildHeight()
                || !level.getFluidState(ground.below()).isEmpty()
                || !level.getBlockState(ground).isAir()
                || !level.getBlockState(ground.above()).isAir()) {
            return null;
        }
        return ground;
    }
}
