package jojoaky.substance.flask;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;

public class FlaskItem extends Item {
    public final int fluidColor;
    public final FlowingFluid fluid;

    public FlaskItem(FlowingFluid fluid, Properties settings, int fluidColor) {
        super(settings);
        this.fluid = fluid;
        this.fluidColor = fluidColor;
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? fluidColor: -1;
    }
}