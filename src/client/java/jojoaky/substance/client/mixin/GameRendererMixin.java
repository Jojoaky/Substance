package jojoaky.substance.client.mixin;

import jojoaky.substance.client.shader.PostShaderManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Map<String, ShaderInstance> shaders;

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V")
    )
    private void renderShaders(float partialTicks, long l, boolean bl, CallbackInfo ci) {
        var accessor = (GameRendererAccessor) this;

        PostShaderManager.renderShaders(accessor, partialTicks, l, bl);
    }

    @Inject(
            method = "resize",
            at = @At("HEAD")
    )
    private void resizeShaders(int width, int height, CallbackInfo ci) {
        PostShaderManager.resizeShaders((GameRendererAccessor) this, width, height);
    }

}