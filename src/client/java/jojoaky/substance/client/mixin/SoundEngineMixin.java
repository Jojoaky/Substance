package jojoaky.substance.client.mixin;

import jojoaky.substance.client.audio.AudioManager;
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
        AudioManager.tick(pause);
    }
}
