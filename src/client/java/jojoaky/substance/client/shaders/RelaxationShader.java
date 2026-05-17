package jojoaky.substance.client.shaders;

import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import jojoaky.substance.client.shader.MobEffectShader;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class RelaxationShader extends MobEffectShader {
    public RelaxationShader() {
        super(ModEffects.RELAXATION, Substance.resource("shaders/post/relaxation.json"));
    }

    @Override
    protected void onRender(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        assert Minecraft.getInstance().level != null;
        float t = (Minecraft.getInstance().level.getGameTime() + partialTicks);

        setGlobalUniformf("ShaderGameTime", t / 20.0f);
        setGlobalUniformf("Intensity", getIntensity());
        setUniformf("minecraft:blur", "Radius", Math.round(Mth.clamp(getIntensity() * 2, 0, 50)));
    }
}