package jojoaky.substance.content.consumable;

import jojoaky.substance.Config;
import jojoaky.substance.content.consumable.framework.ConsumableItem;
import jojoaky.substance.content.consumable.framework.PowderComponent;
import jojoaky.substance.content.consumable.framework.SingleUseDurabilityStrategy;
import net.minecraft.world.item.UseAnim;

public class PowderConsumableItem extends ConsumableItem {
    public PowderConsumableItem(Properties properties) {
        super(
                properties,
                new SingleUseDurabilityStrategy(),
                () -> Math.round(Config.gameplay().maxSniffDuration() * 20.0f),
                () -> Math.round(Config.gameplay().sniffCooldown() * 20.0f),
                UseAnim.NONE,
                new PowderComponent()
        );
    }
}
