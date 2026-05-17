package jojoaky.substance.chemical_fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.FlowingFluid;

public class ChemicalBucket extends BucketItem {
    private final int fluidColor;

    public ChemicalBucket(FlowingFluid fluid, Properties settings, int fluidColor) {
        super(fluid, settings);
        this.fluidColor = fluidColor;
    }

    public int getColor(int tintIndex) {
        return tintIndex == 1 ? fluidColor: -1;
    }
}