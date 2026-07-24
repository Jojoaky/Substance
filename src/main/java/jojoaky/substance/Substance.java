package jojoaky.substance;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.pipe.PipeMenu;
import jojoaky.substance.content.pipe.PipeRegistry;
import jojoaky.substance.content.pipe.PipeSmokableItem;
import jojoaky.substance.datagen.entries.Loot;
import jojoaky.substance.datagen.entries.SecretTrades;
import jojoaky.substance.datagen.entries.Trades;
import jojoaky.substance.register.*;
import jojoaky.substance.util.StackingEffect;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
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

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> WelcomeHandler.onPlayerJoin(handler.getPlayer(), server));

		ModCreativeTab.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModFlasks.initialize();
		ModFluids.initialize();
		ModEffects.initialize();
		ModTags.initialize();


		Trades.initialize();
		SecretTrades.initialize();
		Loot.initialize();
		ModRegisterDatapatch.initialize();


		PipeRegistry.initialize();

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

		PipeRegistry.register(new PipeSmokableItem(
				Items.RED_MUSHROOM,
				(context) -> {
					boolean horrorTrip = context.level().random.nextFloat() < Config.get().horrorTripChance;

					if (horrorTrip) {
						SubstanceEffectHelper.applyEffectBase(
								context.entity(),
								ModEffects.DREAD,
								context.consumeDuration() * 10,
								0
						);
					} else {
						SubstanceEffectHelper.applyStackingEffect(
								context.entity(),
								context.consumeDuration(),
								new StackingEffect(ModEffects.HALLUCINATION, 8, 1000, 1)
						);
					}
				}
		));
	}

	// TODO:
	//  Fix smoke animation (left hand)
	//  hallucination effect
	//  Placeable Trays
}