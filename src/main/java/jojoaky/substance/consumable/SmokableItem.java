package jojoaky.substance.consumable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SmokableItem extends ConsumableItem {
    public SmokableItem(Properties properties) {
        super(properties, 64, 40);
    }

    @Override
    @NotNull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    protected void onStartConsuming(ItemStack stack, Level level, LivingEntity entity) {}

    protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        spawnSmokeParticles(level, entity, false);

        if (level.random.nextFloat() < 0.15f) {
            level.playSound(
                    null,
                    entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.FIRE_AMBIENT,
                    SoundSource.PLAYERS,
                    0.18f,
                    1.4f + level.random.nextFloat() * 0.4f
            );
        }
    }

    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        spawnSmokeParticles(level, entity, true);

        level.playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.CANDLE_EXTINGUISH,
                SoundSource.PLAYERS,
                0.6f,
                0.8f + level.random.nextFloat() * 0.4f
        );
    }

    private void spawnSmokeParticles(Level level, LivingEntity entity, boolean puff) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 eyePos = entity.getEyePosition();
            Vec3 lookVec = entity.getLookAngle();

            Vec3 spawnPos = eyePos.add(lookVec.scale(0.6D));

            if (puff) {
                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        spawnPos.x, spawnPos.y, spawnPos.z,
                        serverLevel.random.nextInt(5, 12),
                        0.05, 0.05, 0.05,
                        0.01
                );
            } else {
                if (serverLevel.random.nextFloat() < 0.2) {
                    serverLevel.sendParticles(
                            ParticleTypes.LARGE_SMOKE,
                            spawnPos.x, spawnPos.y, spawnPos.z,
                            0,
                            lookVec.x * 0.1,
                            lookVec.y * 0.1 + 0.03,
                            lookVec.z * 0.1,
                            1.0
                    );
                }

                if (serverLevel.random.nextFloat() < 0.05) {
                    serverLevel.sendParticles(
                            ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            spawnPos.x, spawnPos.y, spawnPos.z,
                            0,
                            lookVec.x * 0.04,
                            lookVec.y * 0.04 + 0.03,
                            lookVec.z * 0.04,
                            1.0
                    );
                }
            }
        }
    }
}