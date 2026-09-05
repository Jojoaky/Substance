package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class DistantEntityTripVisual extends EntityTripVisual {
    public DistantEntityTripVisual(TripVisualType type, LivingEntity entity, Vec3 position, int lifetime) {
        super(type, entity, position, lifetime, false);
    }

    @Override
    public boolean shouldRemove(Minecraft minecraft, ClientLevel level, Config config) {
        return super.shouldRemove(minecraft, level, config)
                || position().distanceToSqr(minecraft.player.position()) < Math.pow(fadeDistance(config), 2);
    }

    @Override
    protected float opacity(Minecraft minecraft, Config config) {
        return Mth.clamp(
                (float) (position().distanceTo(minecraft.player.position()) - fadeDistance(config)) / 40,
                0,
                1
        );
    }

    private float fadeDistance(Config config) {
        return Mth.clamp(config.dreadAnimalFadeDistance, 4.0f, 64.0f);
    }
}
