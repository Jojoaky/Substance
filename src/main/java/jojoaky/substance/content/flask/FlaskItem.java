package jojoaky.substance.content.flask;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class FlaskItem extends Item {

    public FlaskItem(Properties properties) {
        super(properties);
    }

    public static final int CAPACITY = FluidType.BUCKET_VOLUME / 100;
}
