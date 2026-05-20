package jojoaky.substance.content.flask;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.world.item.Item;

public class FlaskItem extends Item {
    public FlaskItem(Properties properties) {
        super(properties);
    }

    public static final long CAPACITY = 10 * FluidConstants.BLOCK / 1000;
}
