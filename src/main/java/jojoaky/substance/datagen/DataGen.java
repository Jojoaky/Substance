package jojoaky.substance.datagen;

import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.recipes.*;
import jojoaky.substance.generator.trade.TradeDatagenProvider;
import jojoaky.substance.trades.ModSecretTrades;
import jojoaky.substance.trades.ModTrades;
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

        ModTrades.register();
        ModSecretTrades.register();

        RecipeGeneratorRegistry.generate(pack);
    }
}