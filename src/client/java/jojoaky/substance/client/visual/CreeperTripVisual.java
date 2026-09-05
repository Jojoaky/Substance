package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class CreeperTripVisual extends EntityTripVisual {
    public CreeperTripVisual(TripVisualType type, LivingEntity entity, Vec3 position, int lifetime) {
        super(type, entity, position, lifetime, false);
    }

    @Override
    protected void tickVisual(Minecraft minecraft, ClientLevel level, Config config) {
        super.tickVisual(minecraft, level, config);
        Vec3 direction = minecraft.player.getEyePosition().subtract(position());
        if (age() > 15
                && minecraft.player.getLookAngle().dot(direction.normalize().scale(-1)) > 0.88
                && isVisible(minecraft, level, position().add(0, 1, 0))) {
            // This is a local particle only: it sends no explosion packet and changes no terrain.
            level.addParticle(ParticleTypes.EXPLOSION, position().x, position().y + 0.8,
                    position().z, 0, 0, 0);
            expire();
        }
    }

    private boolean isVisible(Minecraft minecraft, ClientLevel level, Vec3 target) {
        return level.clip(new ClipContext(
                minecraft.player.getEyePosition(),
                target,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                minecraft.player
        )).getType() == HitResult.Type.MISS;
    }
}
