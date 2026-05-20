package jojoaky.substance.datagen;

import jojoaky.substance.datagen.recipes.mixing.MixingCraftingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ChemicalModelProvider::new);

        pack.addProvider(SubstanceRecipeProvider::registerAll);
    }
}