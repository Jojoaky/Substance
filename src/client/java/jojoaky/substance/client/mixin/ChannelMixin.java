package jojoaky.substance.client.mixin;

import com.mojang.blaze3d.audio.Channel;
import jojoaky.substance.client.audio.AudioManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Channel.class)
public abstract class ChannelMixin {
    @Shadow @Final private int source;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void substance$onInit(int source, CallbackInfo ci) {
        AudioManager.onSourceCreated(this.source);
    }

    // We should also handle release, but Channel usually exists for the lifetime of the engine's pool
}
