package jojoaky.substance.data.generator.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RecipeGeneratorRegistry {
    public static List<ShapelessRecipeDef> SHAPELESS_RECIPES = new ArrayList<>();
    public static List<ShapedRecipeDef> SHAPED_RECIPES = new ArrayList<>();

    public static void accept(RecipeDef recipe) {
        if (recipe instanceof ShapelessRecipeDef r) {
            SHAPELESS_RECIPES.add(r);
        } else if (recipe instanceof ShapedRecipeDef r) {
            SHAPED_RECIPES.add(r);
        }
    }

    public static void accept(RecipeDef... recipes) {
        Arrays.stream(recipes).forEach(RecipeGeneratorRegistry::accept);
    }

    public static void accept(Collection<? extends RecipeDef> recipes) {
        recipes.forEach(RecipeGeneratorRegistry::accept);
    }

    public static void generate(FabricDataGenerator.Pack pack) {
        pack.addProvider(ModRecipeProvider::registerAll);
    }
}