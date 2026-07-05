package jojoaky.substance.recipes;

import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.register.ModItems;

public class PlantRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("ephedra_seeds")
                        .vanillaShapeless()
                        .require(ModItems.EPHEDRA_BUNDLE)
                        .output(ModItems.EPHEDRA_SEEDS)
                        .build(),

                ShapelessRecipeDef.named("herb_seeds")
                        .vanillaShapeless()
                        .require(ModItems.HERB_BUD)
                        .output(ModItems.HERB_SEEDS)
                        .build()
        );
    }
}
