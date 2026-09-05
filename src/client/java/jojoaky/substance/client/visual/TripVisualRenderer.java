package jojoaky.substance.client.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;

final class TripVisualRenderer {
    private static LivingEntity renderingEntity;

    private TripVisualRenderer() {
    }

    static boolean isRendering(LivingEntity entity) {
        return renderingEntity == entity;
    }

    static void renderEntity(
            WorldRenderContext context,
            Minecraft minecraft,
            PoseStack pose,
            LivingEntity entity,
            float alpha
    ) {
        renderingEntity = entity;
        try {
            MultiBufferSource faded = type -> new AlphaConsumer(context.consumers().getBuffer(type), alpha);
            minecraft.getEntityRenderDispatcher().getRenderer(entity).render(
                    entity,
                    entity.getYRot(),
                    context.tickDelta(),
                    pose,
                    faded,
                    minecraft.getEntityRenderDispatcher().getPackedLightCoords(entity, context.tickDelta())
            );
        } finally {
            renderingEntity = null;
        }
    }

    static MultiBufferSource alphaBuffers(WorldRenderContext context, float alpha) {
        return type -> new AlphaConsumer(context.consumers().getBuffer(type), alpha);
    }

    private record AlphaConsumer(VertexConsumer delegate, float alpha) implements VertexConsumer {
        public VertexConsumer vertex(double x, double y, double z) { delegate.vertex(x, y, z); return this; }
        public VertexConsumer color(int r, int g, int b, int a) { delegate.color(r, g, b, (int) (a * alpha)); return this; }
        public VertexConsumer uv(float u, float v) { delegate.uv(u, v); return this; }
        public VertexConsumer overlayCoords(int u, int v) { delegate.overlayCoords(u, v); return this; }
        public VertexConsumer uv2(int u, int v) { delegate.uv2(u, v); return this; }
        public VertexConsumer normal(float x, float y, float z) { delegate.normal(x, y, z); return this; }
        public void endVertex() { delegate.endVertex(); }
        public void defaultColor(int r, int g, int b, int a) { delegate.defaultColor(r, g, b, (int) (a * alpha)); }
        public void unsetDefaultColor() { delegate.unsetDefaultColor(); }
    }
}
