package jojoaky.substance.register;

import jojoaky.substance.data.datapatch.runtime.DatapatchLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ModRegisterDatapatch {
    public static void initialize() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new DatapatchLoader());
    }
}
