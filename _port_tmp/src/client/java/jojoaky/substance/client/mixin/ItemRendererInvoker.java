package jojoaky.substance.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface ItemRendererInvoker {
    @Invoker("renderModelLists")
    void invokeRenderModelLists(
            BakedModel model,
            ItemStack stack,
            int light,
            int overlay,
            PoseStack pose,
            VertexConsumer consumer
    );
}