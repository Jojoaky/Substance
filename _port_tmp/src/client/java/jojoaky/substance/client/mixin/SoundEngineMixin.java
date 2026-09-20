package jojoaky.substance.client.mixin;

import jojoaky.substance.client.audio.AmbientSoundManager;
import jojoaky.substance.client.audio.AudioProcessingManager;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Shadow private boolean loaded;

    @Inject(method = "tick", at = @At("HEAD"))
    private void substance$tick(boolean pause, CallbackInfo ci) {
        if (!this.loaded) return;
        AudioProcessingManager.tick(pause);
        AmbientSoundManager.tick(pause);
    }

    @Inject(method = "destroy", at = @At("HEAD"))
    private void substance$onDestroy(CallbackInfo ci) {
        AudioProcessingManager.reset();
        AmbientSoundManager.reset();
    }
}
