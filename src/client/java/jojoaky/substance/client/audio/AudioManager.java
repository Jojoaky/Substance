package jojoaky.substance.client.audio;

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
        if (initialized) return;

        try {
            // Check if AL capabilities are set, otherwise we might crash
            AL.getCapabilities();
        } catch (IllegalStateException e) {
            // AL capabilities not set yet
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

    public static void tick(boolean pause) {
        if (!initialized) init();

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || pause) return;

        float intensity = 0;
        // We'll use Haze or Relaxation as a base for distant audio
        MobEffectInstance effect = player.getEffect(ModEffects.HAZE);
        if (effect == null) effect = player.getEffect(ModEffects.RELAXATION);

        if (effect != null) {
            intensity = Mth.clamp((float)effect.getDuration() / 20.0f, 0.0f, 1.0f);
            if (effect.getDuration() > 200) intensity = 1.0f;
            else intensity = (float)effect.getDuration() / 200.0f;
        }

        updateFilters(intensity);
    }

    private static void updateFilters(float intensity) {
        if (lowPassFilter == -1) return;

        // GainHF: 1.0 is no filter, lower is more muffled
        float gainHF = 1.0f - (intensity * 0.8f); 
        EXTEfx.alFilterf(lowPassFilter, EXTEfx.AL_LOWPASS_GAINHF, gainHF);

        // Update all active sources
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
                // Should not happen if initialized is true and we're on the right thread, 
                // but better safe than crashing.
            }
        }
    }

    public static void onSourceReleased(int sourceId) {
        synchronized (activeSources) {
            activeSources.remove(sourceId);
        }
    }
}
