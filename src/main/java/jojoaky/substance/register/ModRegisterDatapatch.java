package jojoaky.substance.register;

import jojoaky.substance.data.datapatch.runtime.DatapatchLoader;

public class ModRegisterDatapatch {
    public static void initialize() {
        // Use hardcoded registry instead of datapack-driven reloads
        DatapatchLoader.init();
    }
}
