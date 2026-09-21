package jojoaky.substance.content.flask;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class FilledFlaskItem extends FlaskItem {
    public static @Nullable FilledFlaskItem getFlaskForFluid(Fluid fluid) {
        ModFlasks.FlaskEntry entry = ModFlasks.getEntry(fluid);
        return entry == null ? null : entry.flask().get();
    }

    public final boolean useCustomModel;
    public final int fluidColor;
    public final FlowingFluid fluid;

    public FilledFlaskItem(FlowingFluid fluid, Properties settings, int fluidColor, boolean useCustomModel) {
        super(settings);
        this.fluid = fluid;
        this.fluidColor = fluidColor;
        this.useCustomModel = useCustomModel;
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? fluidColor : -1;
    }
}
