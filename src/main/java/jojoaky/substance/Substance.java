package jojoaky.substance;

import com.google.gson.Gson;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.mob.equipment.MobEquipmentRegistry;
import jojoaky.substance.content.pipe.PipeMenu;
import jojoaky.substance.content.pipe.PipeRegistry;
import jojoaky.substance.content.pipe.PipeSmokableItem;
import jojoaky.substance.config.ConfigSync;
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
	public static final Gson GSON = new Gson();

	public static ResourceLocation resource(String string) {
		return new ResourceLocation(MOD_ID, string);
	}

	@Override
	public void onInitialize() {
		Config.HANDLER.load();
		ConfigSync.initializeServer();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> WelcomeHandler.onPlayerJoin(handler.getPlayer(), server));

		ModCreativeTab.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModFlasks.initialize();
		ModFluids.initialize();
		ModTrays.initialize();
		ModEffects.initialize();
		ModSounds.initialize();
		ModTags.initialize();

		Trades.initialize();
		SecretTrades.initialize();
		Loot.initialize();
		ModRegisterDatapatch.initialize();

		MobEquipmentRegistry.initialize();

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
				ModItems.WHITE_CRYSTALS,
				new StackingEffect(ModEffects.SURGE, 8, 1000, 1)
		));

		PipeRegistry.register(PipeSmokableItem.effectGiving(
				ModItems.BLUE_CRYSTALS,
				new StackingEffect(ModEffects.SURGE, 8, 700, 3)
		));

		PipeRegistry.register(new PipeSmokableItem(
				Items.RED_MUSHROOM,
				(context) -> {
					boolean horrorTrip = context.level().random.nextFloat() < Config.gameplay().horrorTripChance();

					if (horrorTrip) {
						SubstanceEffectHelper.applyEffectBase(
								context.entity(),
								ModEffects.DREAD,
								context.consumeDuration() * 5,
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
}

// TODO: (before release)
//  - Check crafting recipes and ensure all content is obtainable.
//  - Check mod compatibility with other mods.
//  - Update README and pages with features, quick tutorial and screenshots
//  - Create detailed documentation / wiki for all content (markdown or github pages)
//  - Create a tutorial / summary.
//  - Add more translations.
//	- Check config
//  - Make relaxation effect only stop when hitting living entity
// 	(future):
//  - Addiction
//  - Update to work with 1.21.1 neoforge and latest fabric & (neo/)forge
