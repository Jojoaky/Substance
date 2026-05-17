package jojoaky.substance.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    @Accessor("minecraft")
    Minecraft substance$getMinecraft();

    @Accessor("postEffect")
    @Nullable
    PostChain substance$getPostEffect();
}