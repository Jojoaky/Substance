package jojoaky.substance.content.gas_bottle;

import jojoaky.substance.register.ModItems;
import jojoaky.substance.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilledGasBottleItem extends Item {

    private static final int STREAM_PARTICLE_COUNT = 24;
    private static final double STREAM_LENGTH = 4.5;
    private static final double STREAM_RADIUS = 0.6;
    private static final double PUSH_STRENGTH = 0.4;

    public FilledGasBottleItem(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        level.playSound(
                player,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.FIRE_EXTINGUISH,
                SoundSource.PLAYERS,
                0.2F, 1.2F
        );
        player.awardStat(Stats.ITEM_USED.get(this));

        if (level instanceof ServerLevel serverLevel) {
            Vec3 origin = player.getEyePosition();
            Vec3 direction = player.getLookAngle();
            spawnGasStream(serverLevel, origin, direction);
            pushEntitiesInStream(serverLevel, origin, direction, player);
        }

        ItemStack emptyBottle = ItemUtils.createFilledResult(
                heldStack,
                player,
                new ItemStack(ModItems.GAS_BOTTLE.get()),
                false
        );
        return InteractionResultHolder.sidedSuccess(emptyBottle, level.isClientSide());
    }

    private static void spawnGasStream(ServerLevel level, Vec3 origin, Vec3 direction) {
        Vec3 normalizedDir = direction.normalize();
        Vec3 right = normalizedDir.cross(new Vec3(0, 1, 0));
        if (right.lengthSqr() < 1.0E-4) {
            right = new Vec3(1, 0, 0);
        }
        right = right.normalize();
        Vec3 up = right.cross(normalizedDir).normalize();

        for (int i = 0; i < STREAM_PARTICLE_COUNT; i++) {
            double t = level.random.nextDouble();
            double dist = t * STREAM_LENGTH;
            double spread = (0.15 + t) * STREAM_RADIUS;

            double angle = level.random.nextDouble() * Math.PI * 2;
            double rx = Math.cos(angle) * spread;
            double ry = Math.sin(angle) * spread;

            Vec3 pos = origin
                    .add(normalizedDir.scale(dist))
                    .add(right.scale(rx))
                    .add(up.scale(ry));

            level.sendParticles(
                    ParticleTypes.CLOUD,
                    pos.x, pos.y, pos.z,
                    1,
                    normalizedDir.x * 0.04, normalizedDir.y * 0.04, normalizedDir.z * 0.04,
                    0.02
            );
        }
    }

    private static void pushEntitiesInStream(ServerLevel level, Vec3 origin, Vec3 direction, Entity source) {
        Vec3 normalizedDir = direction.normalize();
        Vec3 end = origin.add(normalizedDir.scale(STREAM_LENGTH));

        AABB streamBounds = new AABB(origin, end).inflate(STREAM_RADIUS);
        List<Entity> entities = level.getEntities(
                source,
                streamBounds,
                entity -> entity != source && !entity.isSpectator()
        );

        for (Entity entity : entities) {
            AABB hitBox = entity.getBoundingBox().inflate(STREAM_RADIUS);
            Vec3 hitPosition;

            if (hitBox.contains(origin)) {
                hitPosition = origin;
            } else {
                var hit = hitBox.clip(origin, end);

                if (hit.isEmpty()) {
                    continue;
                }

                hitPosition = hit.get();
            }

            double distance = hitPosition.subtract(origin).dot(normalizedDir);
            double falloff = 1.0 - distance / STREAM_LENGTH;

            Vec3 push = normalizedDir.scale(PUSH_STRENGTH * falloff);

            entity.push(
                    push.x,
                    push.y * 0.3,
                    push.z
            );

            entity.hasImpulse = true;
            entity.hurtMarked = true;

        }
    }

    public static class DispenserBehavior extends DefaultDispenseItemBehavior {

        @Override
        protected @NotNull ItemStack execute(
                @NotNull BlockSource source,
                @NotNull ItemStack stack
        ) {
            BlockPos targetPos = DispenserHelper.getTargetPos(source);

            source.level().playSound(
                    null,
                    targetPos,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.NEUTRAL,
                    0.3F, 1.2F
            );

            Vec3 origin = DispenserHelper.getFacePos(source);
            Vec3 direction = DispenserHelper.getDirection(source);

            ServerLevel serverLevel = source.level();
            spawnGasStream(serverLevel, origin, direction);
            pushEntitiesInStream(serverLevel, origin, direction, null);

            return DispenserHelper.createFilledResult(
                    source,
                    stack,
                    new ItemStack(ModItems.GAS_BOTTLE.get())
            );
        }
    }
}
