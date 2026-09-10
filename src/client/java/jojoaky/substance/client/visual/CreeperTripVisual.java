package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class CreeperTripVisual extends EntityTripVisual {
    private boolean fusing = false;
    private int fuseTime = 0;
    private static final int MAX_FUSE_TIME = 30; // 1.5 seconds fuse timer

    public CreeperTripVisual(TripVisualType type, LivingEntity entity, Vec3 position, int lifetime) {
        super(type, entity, position, lifetime, false);
    }

    @Override
    protected void tickVisual(Minecraft minecraft, ClientLevel level, Config config) {
        super.tickVisual(minecraft, level, config);
        if (minecraft.player == null) return;

        Vec3 direction = minecraft.player.getEyePosition().subtract(position());
        boolean targetAcquired = age() > 15
                && minecraft.player.getLookAngle().dot(direction.normalize().scale(-1)) > 0.88
                && isVisible(minecraft, level, position().add(0, 1, 0));

        // Start fuse sequence when player looks at the creeper
        if (!fusing && targetAcquired) {
            fusing = true;
            level.playLocalSound(
                    position().x, position().y, position().z,
                    SoundEvents.CREEPER_PRIMED,
                    SoundSource.HOSTILE,
                    1.0f, 0.5f, false
            );
        }

        // Advance fuse timer and update visual/audio effects
        if (fusing) {
            fuseTime++;

            // Triggers vanilla white flashing and expansion rendering on the entity
            if (entity() instanceof Creeper creeper) {
                creeper.setSwellDir(1);
                creeper.tick();
            }

            if (fuseTime >= MAX_FUSE_TIME) {
                // Play explosion sound
                level.playLocalSound(
                        position().x, position().y + 0.8, position().z,
                        SoundEvents.GENERIC_EXPLODE,
                        SoundSource.HOSTILE,
                        1.0f, 1.0f, false
                );

                // Spawn visual explosion effect
                level.addParticle(
                        ParticleTypes.EXPLOSION_EMITTER,
                        position().x, position().y + 0.8, position().z,
                        0, 0, 0
                );

                expire();
            }
        }
    }

    private boolean isVisible(Minecraft minecraft, ClientLevel level, Vec3 target) {
        if (minecraft.player == null) return false;
        return level.clip(new ClipContext(
                minecraft.player.getEyePosition(),
                target,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                minecraft.player
        )).getType() == HitResult.Type.MISS;
    }
}