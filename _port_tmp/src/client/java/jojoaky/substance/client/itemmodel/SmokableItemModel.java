package jojoaky.substance.client.itemmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import jojoaky.substance.Substance;
import jojoaky.substance.client.mixin.ItemRendererInvoker;
import jojoaky.substance.register.ModItems;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

// Inspired by https://github.com/TeamGalena/Nirvana/blob/main/1.21.x/common/src/main/java/galena/nirvana/client/CustomItemModel.java

import static net.minecraft.world.item.ItemDisplayContext.*;

public class SmokableItemModel {

    private final ResourceLocation flatModel;
    private final ResourceLocation equippedModel;
    private final Collection<ItemDisplayContext> supportedContexts;

    public SmokableItemModel(String name, Collection<ItemDisplayContext> contexts) {
        this.supportedContexts = contexts;

        var base = new ResourceLocation(Substance.MOD_ID, name).withPrefix("item/");
        this.flatModel = base.withSuffix("_flat");
        this.equippedModel = base.withSuffix("_equipped");
    }

    public Collection<? extends ResourceLocation> getModelLocations() {
        return List.of(flatModel, equippedModel);
    }

    private boolean usesEquippedModel(ItemDisplayContext context) {
        return supportedContexts.contains(context);
    }

    public void render(
            ItemStack stack,
            ItemDisplayContext context,
            PoseStack poseStack,
            MultiBufferSource buffers,
            RenderFunction renderFunction
    ) {
        poseStack.pushPose();

        boolean useEquipped = usesEquippedModel(context);
        ResourceLocation modelLocation = useEquipped ? equippedModel : flatModel;

        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        var model = itemRenderer.getItemModelShaper()
                .getModelManager()
                .getModel(modelLocation);

        if (model == null) {
            poseStack.popPose();
            return;
        }

        poseStack.translate(0.5, 0.5, 0.5);
        model.getTransforms().getTransform(context).apply(false, poseStack);
        poseStack.translate(-0.5, -0.5, -0.5);

        var renderType = useEquipped
                ? ItemBlockRenderTypes.getRenderType(stack, false)
                : RenderType.cutout();

        VertexConsumer vertexConsumer =
                ItemRenderer.getFoilBufferDirect(buffers, renderType, true, stack.hasFoil());

        renderFunction.render(itemRenderer, model, vertexConsumer);

        poseStack.popPose();
    }

    @FunctionalInterface
    public interface RenderFunction {
        void render(ItemRenderer renderer, BakedModel model, VertexConsumer vertexConsumer);
    }

    public static class Renderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

        private final SmokableItemModel model;

        public Renderer(SmokableItemModel model) {
            this.model = model;
        }

        @Override
        public void render(
                ItemStack stack,
                ItemDisplayContext context,
                PoseStack poseStack,
                MultiBufferSource buffers,
                int light,
                int overlay
        ) {
            model.render(stack, context, poseStack, buffers,
                    (renderer, bakedModel, vertexConsumer) ->
                            ((ItemRendererInvoker) renderer)
                                    .invokeRenderModelLists(bakedModel, stack, light, overlay, poseStack, vertexConsumer)
            );
        }
    }


    public static SmokableItemModel registerModel(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        SmokableItemModel model = new SmokableItemModel(name, List.of(THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND, HEAD));

        BuiltinItemRendererRegistry.INSTANCE.register(item, new Renderer(model));

        ModelLoadingPlugin.register(plugin ->
                plugin.addModels(model.getModelLocations())
        );

        Substance.LOGGER.info("Registered smokable model for {}", item);

        return model;
    }

    public static void register() {
    }
}