package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public abstract class TripVisual {
    private final TripVisualType type;
    private final Vec3 position;
    private final int lifetime;
    private int age;
    private boolean expired;

    protected TripVisual(TripVisualType type, Vec3 position, int lifetime) {
        this.type = type;
        this.position = position;
        this.lifetime = lifetime;
    }

    public final TripVisualType type() {
        return type;
    }

    protected final Vec3 position() {
        return position;
    }

    protected final int age() {
        return age;
    }

    protected final void expire() {
        expired = true;
    }

    public final void tick(Minecraft minecraft, ClientLevel level, Config config) {
        age++;
        tickVisual(minecraft, level, config);
    }

    protected void tickVisual(Minecraft minecraft, ClientLevel level, Config config) {
    }

    public boolean shouldRemove(Minecraft minecraft, ClientLevel level, Config config) {
        return expired || age >= lifetime
                || position.distanceToSqr(minecraft.player.position()) > 160 * 160;
    }

    public final void render(
            WorldRenderContext context,
            Minecraft minecraft,
            ClientLevel level,
            Config config
    ) {
        float renderedAge = age + context.tickDelta();
        float fade = Mth.clamp(Math.min(renderedAge / 20, (lifetime - renderedAge) / 30), 0, 1);
        float alpha = Mth.clamp(
                fade * opacity(minecraft, config)
                        * Math.min(1, config.visualEffectStrength)
                        * Mth.clamp(type.strength(config), 0.0f, 2.0f)
                        * 0.85f,
                0.0f,
                1.0f
        );
        if (alpha <= 0) {
            return;
        }

        var pose = context.matrixStack();
        pose.pushPose();
        try {
            Vec3 relativePosition = position.subtract(context.camera().getPosition());
            pose.translate(relativePosition.x, relativePosition.y, relativePosition.z);
            renderVisual(context, minecraft, level, renderedAge, alpha);
        } finally {
            pose.popPose();
        }
    }

    protected float opacity(Minecraft minecraft, Config config) {
        return 1.0f;
    }

    protected abstract void renderVisual(
            WorldRenderContext context,
            Minecraft minecraft,
            ClientLevel level,
            float age,
            float alpha
    );
}
