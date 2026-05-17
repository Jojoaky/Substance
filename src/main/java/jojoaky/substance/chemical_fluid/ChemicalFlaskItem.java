package jojoaky.substance.chemical_fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;

public class ChemicalFlaskItem extends Item {
    public final int fluidColor;
    public final FlowingFluid fluid;

    public ChemicalFlaskItem(FlowingFluid fluid, Properties settings, int fluidColor) {
        super(settings);
        this.fluid = fluid;
        this.fluidColor = fluidColor;
    }

    public int getColor(int tintIndex) {
        return tintIndex == 1 ? fluidColor: -1;
    }
}