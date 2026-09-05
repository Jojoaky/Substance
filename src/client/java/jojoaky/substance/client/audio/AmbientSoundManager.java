package jojoaky.substance.client.audio;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import jojoaky.substance.register.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;

/** Plays occasional, non-positional ambience while the local player is under a trip effect. */
public final class AmbientSoundManager {
    private static final float BASE_VOLUME = 0.45F;

    private static final RandomSource RANDOM = RandomSource.create();
    private static final EffectSoundState HALLUCINATION = new EffectSoundState(
            ModEffects.HALLUCINATION,
            ModSounds.HALLUCINATION_AMBIENT
    );
    private static final EffectSoundState DREAD = new EffectSoundState(
            ModEffects.DREAD,
            ModSounds.DREAD_AMBIENT
    );

    private AmbientSoundManager() {
    }

    public static void tick(boolean paused) {
        if (paused) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (!Config.get().enableAmbientSounds || player == null || minecraft.level == null) {
            stopAll(minecraft.getSoundManager());
            return;
        }

        float intervalSeconds = Config.get().ambientSoundInterval;
        if (intervalSeconds <= 0.0F) {
            stopAll(minecraft.getSoundManager());
            return;
        }

        SoundManager soundManager = minecraft.getSoundManager();
        tickEffect(player, soundManager, HALLUCINATION, intervalSeconds);
        tickEffect(player, soundManager, DREAD, intervalSeconds);
    }

    public static void reset() {
        HALLUCINATION.reset();
        DREAD.reset();
    }

    private static void tickEffect(LocalPlayer player, SoundManager soundManager, EffectSoundState state, float intervalSeconds) {
        if (!player.hasEffect(state.effect)) {
            state.stop(soundManager);
            return;
        }

        if (state.activeSound != null) {
            if (soundManager.isActive(state.activeSound)) {
                return;
            }
            state.activeSound = null;
            state.delayTicks = nextInterval(intervalSeconds);
        }

        if (state.delayTicks < 0) {
            float initialSeconds = Math.min(10.0F, Math.max(2.0F, intervalSeconds * 0.33F));
            state.delayTicks = nextInterval(initialSeconds);
        }

        if (state.delayTicks-- > 0) {
            return;
        }

        float pitch = 0.96F + RANDOM.nextFloat() * 0.08F;
        state.activeSound = SimpleSoundInstance.forLocalAmbience(state.sound, BASE_VOLUME, pitch);
        soundManager.play(state.activeSound);
    }

    private static int nextInterval(float seconds) {
        int average = Math.max(1, Math.round(Mth.clamp(seconds, 0.5F, 300.0F) * 20));
        int variance = Math.max(1, average * 2 / 5);
        return average - variance + RANDOM.nextInt(variance * 2 + 1);
    }

    private static void stopAll(SoundManager soundManager) {
        HALLUCINATION.stop(soundManager);
        DREAD.stop(soundManager);
    }

    private static final class EffectSoundState {
        private final MobEffect effect;
        private final SoundEvent sound;
        private SimpleSoundInstance activeSound;
        private int delayTicks = -1;

        private EffectSoundState(MobEffect effect, SoundEvent sound) {
            this.effect = effect;
            this.sound = sound;
        }

        private void stop(SoundManager soundManager) {
            if (activeSound != null) {
                soundManager.stop(activeSound);
            }
            reset();
        }

        private void reset() {
            activeSound = null;
            delayTicks = -1;
        }
    }
}
