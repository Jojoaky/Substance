package jojoaky.substance.client.shaders;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;
import net.minecraft.util.Mth;

public class DreadShader extends MobEffectShader {
    public DreadShader() {
        super(ModEffects.DREAD, Substance.resource("shaders/post/dread.json"));
    }

    @Override
    public float getIntensity() {
        return super.getIntensity() * Mth.clamp(Config.get().dreadVisualStrength, 0.0f, 2.0f);
    }

    @Override
    protected boolean shouldRender(GameRendererAccessor accessor) {
        return Config.get().enableDreadVisuals && super.shouldRender(accessor);
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        super.onRender(accessor, partialTicks, time, tick);
    }
}
