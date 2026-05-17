package jojoaky.substance.client;

import jojoaky.substance.Substance;
import jojoaky.substance.client.itemmodel.SmokableItemModel;
import jojoaky.substance.client.shader.PostShaderManager;
import jojoaky.substance.client.shaders.*;
import jojoaky.substance.consumable.SmokableItem;
import jojoaky.substance.register.ModBlocks;
import jojoaky.substance.register.ModFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;

import static jojoaky.substance.register.ModFluids.ALL_FLUIDS;

public class SubstanceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_HERB, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EPHEDRA_CROP, RenderType.cutout());

		BuiltInRegistries.ITEM.stream()
				.filter(item -> item instanceof SmokableItem)
				.forEach(SmokableItemModel::registerModel);

		registerFluids();

		PostShaderManager.add(new DreadShader());
		PostShaderManager.add(new HallucinationShader());
		PostShaderManager.add(new HazeShader());
		PostShaderManager.add(new KeenShader());
		PostShaderManager.add(new RelaxationShader());
		PostShaderManager.add(new StaggerShader());
		PostShaderManager.add(new SurgeShader());
		PostShaderManager.add(new WarpShader());

		PostShaderManager.init();
	}

	private void registerFluids() {
		for (ModFluids.ChemicalFluidSet fluid : ALL_FLUIDS) {
			FluidRenderHandlerRegistry.INSTANCE.register(
					fluid.still(),
					fluid.flowing(),
					new SimpleFluidRenderHandler(
							Substance.resource("block/chemical_still"),
							Substance.resource("block/chemical_flow"),
							Substance.resource("block/chemical_overlay"),
							fluid.tint()
					)
			);

			BlockRenderLayerMap.INSTANCE.putFluids(
					RenderType.translucent(),
					fluid.still(),
					fluid.flowing()
			);
		}

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (tintIndex != 1) return -1;

			for (ModFluids.ChemicalFluidSet fluid : ALL_FLUIDS) {
				if (stack.is(fluid.bucket())) {
					return fluid.tint();
				}
			}

			return -1;
		}, ALL_FLUIDS.stream()
				.map(ModFluids.ChemicalFluidSet::bucket)
				.toArray(net.minecraft.world.item.Item[]::new));
	}
}