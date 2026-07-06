package jojoaky.substance.client.audio;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import java.util.HashSet;
import java.util.Set;

public class AudioManager {
    private static int lowPassFilter = -1;
    private static int reverbEffect = -1;
    private static int reverbSlot = -1;
    private static boolean initialized = false;
    private static final Set<Integer> activeSources = new HashSet<>();

    public static void init() {
        if (!Config.get().enableAudioEffects) return;
        if (initialized) return;

        try {
            AL.getCapabilities();
        } catch (IllegalStateException e) {
            return;
        }

        lowPassFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(lowPassFilter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);

        reverbEffect = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(reverbEffect, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_REVERB);
        // Default reverb settings, can be tuned
        EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_DENSITY, 1.0f);
        EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_DIFFUSION, 1.0f);
        EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_GAIN, 0.32f);
        EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_GAINHF, 0.89f);
        EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_DECAY_TIME, 1.49f);
        EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_DECAY_HFRATIO, 0.83f);

        reverbSlot = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(reverbSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverbEffect);

        initialized = true;
    }

    public static float getEffectIntensity(MobEffectInstance effect) {
        if (effect == null) return 0.0f;
        float intensity;
        if (effect.getDuration() > 200) intensity = 1.0f;
        else intensity = (float)effect.getDuration() / 200.0f;
        return intensity;
    }

    private static final float OVERDRIVE_MAX = 2.0f;

    public static void tick(boolean pause) {
        if (!Config.get().enableAudioEffects) return;
        if (!initialized) init();

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || pause) return;

        float baseIntensity = Math.max(
                getEffectIntensity(player.getEffect(ModEffects.HAZE)),
                getEffectIntensity(player.getEffect(ModEffects.WARP))
        );

        float configMultiplier = Config.get().audioEffectStrength; // 0..2

        // Ramp stays 0..1 - controls *when* the effect kicks in, untouched by multiplier
        // Overdrive extends 0..2 - controls *how strong* it gets once engaged
        float overdrive = Mth.clamp(baseIntensity * configMultiplier, 0.0f, OVERDRIVE_MAX);

        updateFilters(overdrive);
    }

    private static void updateFilters(float overdrive) {
        if (lowPassFilter == -1) return;

        float stage1 = Mth.clamp(overdrive, 0.0f, 1.0f);
        float gainHF = 1.0f - (stage1 * 0.8f); // 1.0 -> 0.2

        float stage2 = Mth.clamp(overdrive - 1.0f, 0.0f, 1.0f);
        float gain = 1.0f - (stage2 * 0.5f);

        EXTEfx.alFilterf(lowPassFilter, EXTEfx.AL_LOWPASS_GAINHF, gainHF);
        EXTEfx.alFilterf(lowPassFilter, EXTEfx.AL_LOWPASS_GAIN, gain);

        if (reverbEffect != -1 && reverbSlot != -1) {
            float reverbGain = 0.32f + (stage2 * 0.4f);      // wetter mix past 1x
            float decayTime = 1.49f + (stage2 * 1.5f);        // longer tail past 1x
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_GAIN, reverbGain);
            EXTEfx.alEffectf(reverbEffect, EXTEfx.AL_REVERB_DECAY_TIME, decayTime);
            EXTEfx.alAuxiliaryEffectSloti(reverbSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverbEffect);
        }

        synchronized (activeSources) {
            for (Integer source : activeSources) {
                if (AL10.alIsSource(source)) {
                    AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, lowPassFilter);
                    if (reverbSlot != -1) {
                        AL11.alSource3i(source, EXTEfx.AL_AUXILIARY_SEND_FILTER, reverbSlot, 0, EXTEfx.AL_FILTER_NULL);
                    }
                }
            }
        }
    }

    public static void onSourceCreated(int sourceId) {
        if (!Config.get().enableAudioEffects) return;

        synchronized (activeSources) {
            activeSources.add(sourceId);
        }
        if (initialized) {
            try {
                if (lowPassFilter != -1 && AL10.alIsSource(sourceId)) {
                    AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, lowPassFilter);
                }
                if (reverbSlot != -1 && AL10.alIsSource(sourceId)) {
                    AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, reverbSlot, 0, EXTEfx.AL_FILTER_NULL);
                }
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public static void onSourceReleased(int sourceId) {
        if (!Config.get().enableAudioEffects) return;

        synchronized (activeSources) {
            activeSources.remove(sourceId);
        }
    }
}