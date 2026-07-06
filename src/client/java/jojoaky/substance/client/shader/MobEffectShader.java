package jojoaky.substance.client.shader;

import jojoaky.substance.Config;
import jojoaky.substance.client.mixin.GameRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class MobEffectShader extends PostShader {
    private static final float FADE_IN_DURATION_TICKS = 120.0f;
    private static final float FADE_OUT_DURATION_TICKS = 80.0f;

    public MobEffectShader(MobEffect effect, ResourceLocation resource) {
        super(resource);
        this.mobEffect = effect;
    }

    final MobEffect mobEffect;
    private float timeBefore = 0;
    private float intensity = 0;

    public float getIntensity() {
        return intensity * Config.get().visualEffectStrength;
    }

    @Override
    protected void update(GameRendererAccessor accessor, float partialTicks, long time, boolean tick) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        MobEffectInstance effect = player.getEffect(this.mobEffect);

        if (effect == null) {
            intensity = 0;
            timeBefore = 0;
            return;
        }

        float timeNow = (float)player.tickCount + partialTicks;
        if (timeBefore == 0) {
            timeBefore = timeNow;
        }
        float dt = Mth.clamp(timeNow - timeBefore, 0.0001f, 1f);

        boolean fadingOut = effect.getDuration() >= 0 && effect.getDuration() < FADE_OUT_DURATION_TICKS;

        int amp = Byte.toUnsignedInt((byte)effect.getAmplifier());

        float multiplier = (float) Math.pow(amp + 1.f, 0.6f);

        if (!fadingOut) {
            float fadeSpeed = dt / FADE_IN_DURATION_TICKS;
            intensity += fadeSpeed * multiplier;
        } else {
            float fadeSpeed = dt / FADE_OUT_DURATION_TICKS;
            intensity -= fadeSpeed * multiplier;
        }

        intensity = Mth.clamp(intensity, 0, multiplier);

        timeBefore = timeNow;
    }

    @Override
    protected boolean shouldRender(GameRendererAccessor accessor) {
        Player player = Minecraft.getInstance().player;
        return player != null && player.hasEffect(mobEffect);
    }
}
