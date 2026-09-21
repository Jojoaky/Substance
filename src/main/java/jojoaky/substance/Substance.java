package jojoaky.substance;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import jojoaky.substance.datagen.ModDataGenerators;
import jojoaky.substance.register.ModBlocks;
import jojoaky.substance.register.ModCreativeTab;
import jojoaky.substance.register.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Substance.MOD_ID)
public final class Substance {
    public static final String MOD_ID = "substance";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new Gson();

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public Substance(IEventBus modEventBus, ModContainer modContainer) {
        Config.register(modContainer, modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        modEventBus.addListener(ModDataGenerators::gatherData);
    }
}
