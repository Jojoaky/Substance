package jojoaky.substance.client.mixin;

import com.mojang.blaze3d.audio.Channel;
import jojoaky.substance.Config;
import jojoaky.substance.client.audio.AudioProcessingManager;
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
        AudioProcessingManager.onSourceCreated(this.source);
    }

    @Inject(method = "destroy", at = @At("HEAD"))
    private void substance$onClose(CallbackInfo ci) {
        AudioProcessingManager.onSourceReleased(this.source);
    }
}
