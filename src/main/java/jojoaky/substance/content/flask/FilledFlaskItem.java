package jojoaky.substance.content.flask;

import net.minecraft.world.level.material.FlowingFluid;

public class FilledFlaskItem extends FlaskItem {
    public final int fluidColor;
    public final FlowingFluid fluid;

    public FilledFlaskItem(FlowingFluid fluid, Properties settings, int fluidColor) {
        super(settings);
        this.fluid = fluid;
        this.fluidColor = fluidColor;
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? fluidColor: -1;
    }
}