package jojoaky.substance.client.shaders;

import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;

public class WarpShader extends MobEffectShader {
    private static final float DISTORTION_INTENSITY = 0.005f;
    private static final float CHROMA_INTENSITY = 0.003f;

    public WarpShader() {
        super(ModEffects.WARP, Substance.resource("shaders/post/warp.json"));
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        assert Minecraft.getInstance().level != null;
        float t = (Minecraft.getInstance().level.getGameTime() + partialTicks);

        float intensity = getIntensity();

        setGlobalUniformf("ShaderGameTime", t / 20.0f);
        setGlobalUniformf("Intensity", intensity);
    }
}