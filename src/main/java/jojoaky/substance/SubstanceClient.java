package jojoaky.substance;

import jojoaky.substance.content.flask.FilledFlaskItem;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.register.ModBlocks;
import jojoaky.substance.register.ModFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/** Registers client-only extensions for Substance. */
@Mod(value = Substance.MOD_ID, dist = Dist.CLIENT)
public final class SubstanceClient {
    public SubstanceClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(SubstanceClient::clientSetup);
        modEventBus.addListener(SubstanceClient::registerItemColors);
        modEventBus.addListener(SubstanceClient::registerClientExtensions);
    }

    @SuppressWarnings("deprecation")
    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LARGE_HERB.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EPHEDRA_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHILI_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOBACCO.get(), RenderType.cutout());
            for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
                ItemBlockRenderTypes.setRenderLayer(fluid.still().get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(fluid.flowing().get(), RenderType.translucent());
            }
        });
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (ModFlasks.FlaskEntry entry : ModFlasks.ALL_FLASK_ENTRIES) {
            if (!entry.useCustomModel()) {
                event.register(
                        (stack, tintIndex) ->
                                ((FilledFlaskItem) stack.getItem()).getColor(tintIndex),
                        entry.flask().get()
                );
            }
        }

        for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
            event.register(
                    (stack, tintIndex) -> fluid.bucket().get().getColor(tintIndex),
                    fluid.bucket().get()
            );
        }
    }

    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        ResourceLocation stillTexture = ResourceLocation.withDefaultNamespace("block/water_still");
        ResourceLocation flowingTexture = ResourceLocation.withDefaultNamespace("block/water_flow");

        for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return stillTexture;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return flowingTexture;
                }

                @Override
                public int getTintColor() {
                    return fluid.tint();
                }
            }, fluid.fluidType().get());
        }
    }
}
