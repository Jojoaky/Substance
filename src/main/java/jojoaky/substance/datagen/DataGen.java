package jojoaky.substance.datagen;

import jojoaky.substance.datagen.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipes.*;
import jojoaky.substance.datagen.generator.TradeDatagenProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelProvider::new);
        pack.addProvider(TradeDatagenProvider::new);

        MiscRecipes.initialize();
        ChemicalFluidRecipes.initialize();
        ChemicalPowderRecipes.initialize();
        CrystalRecipes.initialize();
        HerbRollRecipes.initialize();
        PlantRecipes.initialize();
        TobaccoRecipes.initialize();

        Trades.register();
        SecretTrades.register();

        RecipeGeneratorRegistry.generate(pack);
    }
}