package jojoaky.substance.client.mixin;

import jojoaky.substance.client.visual.TripVisuals;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class TripVisualRendererMixin<T extends LivingEntity> extends EntityRenderer<T> {
    protected TripVisualRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void substance$useTranslucentTripVisual(
            T entity,
            boolean bodyVisible,
            boolean translucent,
            boolean glowing,
            CallbackInfoReturnable<RenderType> cir
    ) {
        if (TripVisuals.isRendering(entity)) {
            cir.setReturnValue(RenderType.entityTranslucent(getTextureLocation(entity)));
        }
    }
}
