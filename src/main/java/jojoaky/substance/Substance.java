package jojoaky.substance;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.register.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Substance implements ModInitializer {
	public static final String MOD_ID = "substance";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation resource(String string) {
		return new ResourceLocation(MOD_ID, string);
	}

	@Override
	public void onInitialize() {
		Config.HANDLER.load();

		ModCreativeTab.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModFlasks.initialize();
		ModFluids.initialize();
		ModEffects.initialize();
		ModTags.initialize();
	}
}