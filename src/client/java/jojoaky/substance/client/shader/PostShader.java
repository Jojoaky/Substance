package jojoaky.substance.client.shader;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.mixin.PostChainAccessor;
import jojoaky.substance.client.mixin.PostPassAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.List;

import static jojoaky.substance.client.shader.PostShaderManager.lazyLoadShaders;

public class PostShader {

    private boolean errorLoading = false;

    public PostShader(ResourceLocation resource) {
        this.resource = resource;
    }

    public void init() {
        if (!lazyLoadShaders) load(Minecraft.getInstance());
    }

    private final ResourceLocation resource;
    private PostChain postChain = null;

    private void load(Minecraft minecraft) {
        close();
        Substance.LOGGER.info("Loading shader {}", resource);
        try {
            postChain = new PostChain(
                    minecraft.getTextureManager(),
                    minecraft.getResourceManager(),
                    minecraft.getMainRenderTarget(),
                    resource
            );
            postChain.resize(minecraft.getMainRenderTarget().width, minecraft.getMainRenderTarget().height);
            errorLoading = false;
        } catch (IOException | JsonSyntaxException ex) {
            Substance.LOGGER.error("Unable to load shader '{}': invalid post chain JSON or missing shader resource", resource, ex);
            errorLoading = true;
            postChain = null;
        }
    }

    private void close() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }
    }

    public boolean isLoaded() {
        return postChain != null;
    }

    public boolean hasError() {
        return errorLoading;
    }

    public void setGlobalUniformf(String name, float value) {
        if (postChain == null) return;

        List<PostPass> passes = ((PostChainAccessor) postChain).substance$getPasses();

        for (PostPass pass : passes) {
            EffectInstance effect = ((PostPassAccessor) pass).substance$getEffect();
            effect.safeGetUniform(name).set(value);
        }
    }

    public void setUniformf(String passName, String name, float value) {
        if (postChain == null) return;

        List<PostPass> passes = ((PostChainAccessor) postChain).substance$getPasses();

        for (PostPass pass : passes) {
            if (!pass.getName().equals(passName)) continue;
            EffectInstance effect = ((PostPassAccessor) pass).substance$getEffect();
            effect.safeGetUniform(name).set(value);
        }
    }

    protected boolean shouldRender(GameRendererAccessor accessor) { return true; }
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {}
    protected void update(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {}

    public void render(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        update(accessor, partialTicks, time, tick);

        if (shouldRender(accessor)) {
            if (!isLoaded() && !hasError()) {
                load(accessor.substance$getMinecraft());
            }

            if (postChain == null) return;

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);

            RenderSystem.resetTextureMatrix();

            onRender(accessor, partialTicks, time, tick);

            postChain.process(partialTicks);

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        } else {
            if (lazyLoadShaders) close();
        }
    }

    public void resize(GameRendererAccessor accessor, int width, int height) {
        if (postChain != null) postChain.resize(width, height);
    }
}