package jojoaky.substance.client.shaders;

import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;

public class KeenShader extends MobEffectShader {
    public KeenShader() {
        super(ModEffects.KEEN, Substance.resource("shaders/post/keen.json"));
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        super.onRender(accessor, partialTicks, time, tick);
    }
}