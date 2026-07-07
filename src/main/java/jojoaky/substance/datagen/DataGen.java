package jojoaky.substance.datagen;

import jojoaky.substance.data.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipes.*;
import jojoaky.substance.data.generator.datapatch.DatapatchDatagenProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelProvider::new);
        pack.addProvider(DatapatchDatagenProvider::new);

        MiscRecipes.initialize();
        ChemicalFluidRecipes.initialize();
        ChemicalPowderRecipes.initialize();
        CrystalRecipes.initialize();
        HerbRollRecipes.initialize();
        PlantRecipes.initialize();
        TobaccoRecipes.initialize();

        Trades.initialize();
        SecretTrades.initialize();
        Loot.initialize();

        RecipeGeneratorRegistry.generate(pack);
    }
}