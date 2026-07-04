package jojoaky.substance.client.shaders;

import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;

public class HazeShader extends MobEffectShader {
    public HazeShader() {
        super(ModEffects.HAZE, Substance.resource("shaders/post/haze.json"));
    }

    private float timeBefore = 0.0f;

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        assert Minecraft.getInstance().level != null;

        float t = (Minecraft.getInstance().level.getGameTime() + partialTicks) / 20.f;
        float dt = (t - timeBefore);

        float intensity = getIntensity();

        setGlobalUniformf("ShaderGameTime", t);
        setGlobalUniformf("FrameTime", dt);
        setGlobalUniformf("Intensity", intensity);

        timeBefore = t;
    }
}