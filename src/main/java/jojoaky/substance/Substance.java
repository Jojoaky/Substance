package jojoaky.substance;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.pipe.PipeMenu;
import jojoaky.substance.content.pipe.PipeRegistry;
import jojoaky.substance.content.pipe.PipeSmokableItem;
import jojoaky.substance.register.*;
import jojoaky.substance.util.StackingEffect;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Substance implements ModInitializer {
	public static final String MOD_ID = "substance";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MenuType<PipeMenu> PIPE_MENU = net.minecraft.core.Registry.register(
			BuiltInRegistries.MENU,
			resource("pipe_menu"),
			new ExtendedScreenHandlerType<>(PipeMenu::new)
);

	public static ResourceLocation resource(String string) {
		return new ResourceLocation(MOD_ID, string);
	}

	@Override
	public void onInitialize() {
		Config.HANDLER.load();

		PipeRegistry.initialize();

		ModCreativeTab.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModFlasks.initialize();
		ModFluids.initialize();
		ModEffects.initialize();
		ModTags.initialize();
		ModRegisterTrades.initialize();

		PipeRegistry.register(PipeSmokableItem.effectGiving(
				ModItems.DRIED_HERB_BUD,
				new StackingEffect(ModEffects.RELAXATION, 8, 1000, 4)
		));

		PipeRegistry.register(PipeSmokableItem.effectGiving(
				ModItems.DRIED_TOBACCO_LEAF,
				new StackingEffect(ModEffects.KEEN, 6, 550, 3)
		));

		PipeRegistry.register(PipeSmokableItem.effectGiving(
				ModFluids.WHITE_CRYSTAL_OIL_FLASK,
				new StackingEffect(ModEffects.SURGE, 8, 1000, 1)
		));

		PipeRegistry.register(PipeSmokableItem.effectGiving(
				ModFluids.BLUE_CRYSTAL_OIL_FLASK,
				new StackingEffect(ModEffects.SURGE, 8, 700, 3)
		));

		PipeRegistry.register(PipeSmokableItem.effectGiving(
				Items.RED_MUSHROOM,
				new StackingEffect(ModEffects.HALLUCINATION, 8, 1000, 1)
		));
	}
}