package jojoaky.substance.datagen.recipes.mixing;

import jojoaky.substance.register.ModFluids;
import net.minecraft.world.item.ItemStack;

public sealed interface MixingRecipeOutput permits MixingRecipeOutput.Fluid, MixingRecipeOutput.Item {

    record Fluid(ModFluids.ChemicalFluidSet fluid, long amount)
            implements MixingRecipeOutput {}

    record Item(ItemStack stack)
            implements MixingRecipeOutput {}
}