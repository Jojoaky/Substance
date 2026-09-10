package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
        EntityType<?>[] entityTypes = config.dreadDistantEntityTypeCache();
        if (entityTypes.length == 0) {
            return;
        }
        EntityType<?> entityType = entityTypes[random.nextInt(entityTypes.length)];
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

        int highestY = Math.min(level.getMaxBuildHeight() - 2, column.getY() + 2);
        int lowestY = Math.max(level.getMinBuildHeight() + 1, column.getY() - 16);
        for (int y = highestY; y >= lowestY; y--) {
            BlockPos ground = new BlockPos(column.getX(), y, column.getZ());
            BlockPos floor = ground.below();
            if (level.getFluidState(floor).isEmpty()
                    && level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)
                    && level.getBlockState(ground).isAir()
                    && level.getBlockState(ground.above()).isAir()) {
                return ground;
            }
        }
        return null;
    }
}
