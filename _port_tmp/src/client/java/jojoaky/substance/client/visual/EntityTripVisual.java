package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class EntityTripVisual extends TripVisual {
    private final LivingEntity entity;
    private final boolean floating;

    public EntityTripVisual(
            TripVisualType type,
            LivingEntity entity,
            Vec3 position,
            int lifetime,
            boolean floating
    ) {
        super(type, position, lifetime);
        this.entity = entity;
        this.floating = floating;
    }

    protected final LivingEntity entity() {
        return entity;
    }

    @Override
    protected void tickVisual(Minecraft minecraft, ClientLevel level, Config config) {
        if (minecraft.player == null) return;
        Vec3 direction = minecraft.player.getEyePosition().subtract(position());
        float yaw = (float) (Math.atan2(-direction.x, direction.z) * Mth.RAD_TO_DEG);
        entity.setYRot(yaw);
        entity.yRotO = yaw;
        entity.yHeadRot = entity.yHeadRotO = yaw;
        entity.yBodyRot = entity.yBodyRotO = yaw;
    }

    @Override
    protected void renderVisual(
            WorldRenderContext context,
            Minecraft minecraft,
            ClientLevel level,
            float age,
            float alpha
    ) {
        if (floating) {
            context.matrixStack().translate(0, Math.sin(age * 0.035) * 0.8, 0);
        }
        TripVisualRenderer.renderEntity(context, minecraft, context.matrixStack(), entity, alpha);
    }
}
