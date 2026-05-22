package jojoaky.substance.datagen.recipe;

import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.register.ModItems;

import static jojoaky.substance.datagen.recipe.RecipeConstants.BASE_FLUID;

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
