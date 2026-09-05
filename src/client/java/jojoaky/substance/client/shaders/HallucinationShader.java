package jojoaky.substance.client.shaders;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;
import net.minecraft.util.Mth;

public class HallucinationShader extends MobEffectShader {
    public HallucinationShader() {
        super(ModEffects.HALLUCINATION, Substance.resource("shaders/post/hallucination.json"));
    }

    @Override
    public float getIntensity() {
        return super.getIntensity() * Mth.clamp(Config.get().hallucinationVisualStrength, 0.0f, 2.0f);
    }

    @Override
    protected boolean shouldRender(GameRendererAccessor accessor) {
        return Config.get().enableHallucinationVisuals && super.shouldRender(accessor);
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        super.onRender(accessor, partialTicks, time, tick);
    }
}
