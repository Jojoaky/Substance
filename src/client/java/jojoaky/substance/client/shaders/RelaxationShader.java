package jojoaky.substance.client.shaders;

import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;
import net.minecraft.util.Mth;

public class RelaxationShader extends MobEffectShader {
    public RelaxationShader() {
        super(ModEffects.RELAXATION, Substance.resource("shaders/post/relaxation.json"));
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        super.onRender(accessor, partialTicks, time, tick);
        setUniformf("minecraft:blur", "Radius", Math.round(Mth.clamp(getIntensity(), 0, 4)));
    }
}