package jojoaky.substance.client.shaders;

import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;

public class HazeShader extends MobEffectShader {
    public HazeShader() {
        super(ModEffects.HAZE, Substance.resource("shaders/post/haze.json"));
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        super.onRender(accessor, partialTicks, time, tick);
    }
}