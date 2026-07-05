package jojoaky.substance.register;

import jojoaky.substance.datagen.generator.trade.json.TradeLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ModRegisterTrades {
    public static void initialize() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new TradeLoader());
    }
}
