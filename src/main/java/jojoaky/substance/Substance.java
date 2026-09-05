package jojoaky.substance;

import com.google.gson.Gson;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.mob.equipment.MobEquipmentRegistry;
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
	public static final Gson GSON = new Gson();

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
		ModTrays.initialize();
		ModEffects.initialize();
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
					boolean horrorTrip = context.level().random.nextFloat() < Config.get().horrorTripChance;

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
//  2. Balance content and default config.
//  3. Check crafting recipes and ensure all content is obtainable.
//  4. Check mod compatibility with other mods.
//  5. Add translations.
//  6. Add the bubble pipe lit texture.
//  7. Update README and pages with features and screenshots.
//  8. Create a README tutorial.
// 	(future):
//  - Addiction
//  - Update to work with 1.21.1 neoforge and latest fabric & (neo/)forge
//  - Sound effects for effects


/*
	Hallucination:
	- Subtle visual artifacts (floaters, distortions)
	- Random blocks slightly shifting out of grid
	- something unexpected, like villager floating in the sky
	- Fitting sound effects & chimes
	Dread (dark version of hallucination / horror trip):
	- Farm animals in the distance (eg. 100 blocks away, somtimes multiple in groups) that stare at the player and fade when approaching
	- Fake creepers that spawn beind the player, fake explotion when the player turns around
	- Random dark noises and visuals
*/

/*
	Gameplay effects:
	- Purely visual: Haze, Warp, Hallucination, Dread
	- Relaxation: Won't be targeted by mobs, Attacking: stops effect, gives darkness and lightning strike
	- Surge: Increased movement speed, player can fly without elytra and has constant but slow boost with elytra
	- Keen: Significantly increase mining speed
	- (Stagger: Delayed inputs)
*/
