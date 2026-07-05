package jojoaky.substance.register;

import jojoaky.substance.trades.ModSecretTrades;
import jojoaky.substance.trades.ModTrades;

public class ModRegisterTrades {
    public static void initialize() {
        ModTrades.register();
        ModSecretTrades.register();
    }
}
