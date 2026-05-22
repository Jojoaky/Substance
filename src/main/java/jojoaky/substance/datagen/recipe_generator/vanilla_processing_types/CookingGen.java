package jojoaky.substance.datagen.recipe_generator.vanilla_processing_types;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class CookingGen extends FabricRecipeProvider {

    public CookingGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES
                .forEach(def -> {
                    if (def.isSmelting()) buildSmeltingRecipe(def, writer);
                    if (def.isBlasting()) buildBlastingRecipe(def, writer);
                    if (def.isSmoking()) buildSmokingRecipe(def, writer);
                    if (def.isVanillaCampfireCooking()) buildCampfireRecipe(def, writer);
                });
    }

    private void buildSmeltingRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer) {
        RecipeResult result = getRecipeResult(def);
        Ingredient input = def.getSingleInputAsIngredient();

        SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder
                .smelting(input, RecipeCategory.MISC, result.item, def.getSmeltXP(), def.getSmeltDuration())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        Consumer<FinishedRecipe> finalWriter = writer;
        for (var con : def.getConditions()) {
            finalWriter = withConditions(finalWriter, con);
        }
        builder.save(finalWriter, def.getName() + "_smelting");
    }

    private void buildBlastingRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer) {
        RecipeResult result = getRecipeResult(def);
        Ingredient input = def.getSingleInputAsIngredient();

        SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder
                .blasting(input, RecipeCategory.MISC, result.item, def.getBlastXP(), def.getBlastDuration())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        Consumer<FinishedRecipe> finalWriter = writer;
        for (var con : def.getConditions()) {
            finalWriter = withConditions(finalWriter, con);
        }
        builder.save(finalWriter, def.getName() + "_blasting");
    }

    private void buildSmokingRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer) {
        RecipeResult result = getRecipeResult(def);
        Ingredient input = def.getSingleInputAsIngredient();

        SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder
                .smoking(input, RecipeCategory.FOOD, result.item, def.getSmokeXP(), def.getSmokeDuration())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        Consumer<FinishedRecipe> finalWriter = writer;
        for (var con : def.getConditions()) {
            finalWriter = withConditions(finalWriter, con);
        }
        builder.save(finalWriter, def.getName() + "_smoking");
    }

    private void buildCampfireRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer) {
        RecipeResult result = getRecipeResult(def);
        Ingredient input = def.getSingleInputAsIngredient();

        SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder
                .campfireCooking(input, RecipeCategory.FOOD, result.item, def.getCampfireXP(), def.getCampfireDuration())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        Consumer<FinishedRecipe> finalWriter = writer;
        for (var con : def.getConditions()) {
            finalWriter = withConditions(finalWriter, con);
        }
        builder.save(finalWriter, def.getName() + "_campfire_cooking");
    }

    private RecipeResult getRecipeResult(ShapelessRecipeDef def) {
        ItemStack outputStack = def.getSingleOutputAsItem();
        return new RecipeResult(outputStack.getItem(), outputStack.getCount());
    }

    private record RecipeResult(ItemLike item, int count) {}
}