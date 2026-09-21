package jojoaky.substance;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Substance.MOD_ID)
public final class Substance {
    public static final String MOD_ID = "substance";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Substance(IEventBus modEventBus, ModContainer modContainer) {
        Config.register(modContainer, modEventBus);
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
