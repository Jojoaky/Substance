package jojoaky.substance.datagen.recipe_generator;

import jojoaky.substance.datagen.recipe_generator.processingTypes.ShapelessGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RecipeGeneratorRegistry {
    public static List<RecipeDef> ALL_RECIPES = new ArrayList<>();

    public static void accept(RecipeDef recipe) {
        ALL_RECIPES.add(recipe);
    }

    public static void accept(RecipeDef... recipes) {
        ALL_RECIPES.addAll(Arrays.asList(recipes));
    }

    public static void accept(Collection<RecipeDef> recipes) {
        ALL_RECIPES.addAll(recipes);
    }

    public static void generate(FabricDataGenerator.Pack pack) {
        pack.addProvider(ShapelessGen::new);
        pack.addProvider(CreateRecipeProvider::registerAll);
    }
}
