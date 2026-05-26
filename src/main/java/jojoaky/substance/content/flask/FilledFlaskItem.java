package jojoaky.substance.content.flask;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;

public class FilledFlaskItem extends FlaskItem {
    private static final Map<Fluid, FilledFlaskItem> map = new HashMap<>();

    public static FilledFlaskItem getFlaskForFluid(Fluid fluid) {
        return map.get(fluid);
    }

    public final boolean useCustomModel;
    public final int fluidColor;
    public final FlowingFluid fluid;

    public FilledFlaskItem(FlowingFluid fluid, Properties settings, int fluidColor, boolean useCustomModel) {
        super(settings);
        this.fluid = fluid;
        this.fluidColor = fluidColor;
        this.useCustomModel = useCustomModel;
        map.put(fluid, this);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? fluidColor: -1;
    }
}