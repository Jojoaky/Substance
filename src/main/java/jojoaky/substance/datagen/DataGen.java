package jojoaky.substance.datagen;

import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.recipes.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelProvider::new);

        MiscRecipes.initialize();
        ChemicalFluidRecipes.initialize();
        ChemicalPowderRecipes.initialize();
        CrystalRecipes.initialize();
        HerbRollRecipes.initialize();
        PlantRecipes.initialize();
        TobaccoRecipes.initialize();

        RecipeGeneratorRegistry.generate(pack);
    }
}