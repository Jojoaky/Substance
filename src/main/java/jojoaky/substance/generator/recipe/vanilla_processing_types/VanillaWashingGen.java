package jojoaky.substance.generator.recipe.vanilla_processing_types;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static jojoaky.substance.generator.recipe.ShapelessRecipeDef.Operation.CUSTOM_VANILLA_WASHING;

public class VanillaWashingGen extends FabricRecipeProvider {

    public VanillaWashingGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(def -> def.hasOperation(CUSTOM_VANILLA_WASHING))
                .forEach(def -> buildRecipe(def, writer));
    }

    private void buildRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer) {
        ItemStack outputStack = def.getSingleOutputAsItem();
        RecipeResult output = new RecipeResult(outputStack.getItem(), outputStack.getCount());

        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, output.item(), output.count())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        def.getItemInputs().forEach(stack ->
                builder.requires(stack.getItem(), stack.getCount())
        );

        def.getTagInputs().forEach(tag -> {
            for (int i = 0; i < tag.count(); i++)
                builder.requires(tag.tag());
        });

        def.getFluidInputsAsFlasks().forEach(flask ->
                builder.requires(flask.getItem(), flask.getCount())
        );

        builder.requires(ModFlasks.WATER_FLASK);

        Consumer<FinishedRecipe> finalWriter = writer;

        for (var con : def.getConditionsFor(CUSTOM_VANILLA_WASHING)) {
            finalWriter = withConditions(finalWriter, con);
        }

        builder.save(finalWriter, def.getRecipeName(CUSTOM_VANILLA_WASHING));
    }

    private record RecipeResult(ItemLike item, int count) {}
}