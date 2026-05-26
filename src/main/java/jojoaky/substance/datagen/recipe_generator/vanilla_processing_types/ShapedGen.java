package jojoaky.substance.datagen.recipe_generator.vanilla_processing_types;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.datagen.recipe_generator.ShapedRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static jojoaky.substance.datagen.recipe_generator.ShapedRecipeDef.Operation.VANILLA_SHAPED;

public class ShapedGen extends FabricRecipeProvider {

    public ShapedGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        RecipeGeneratorRegistry.SHAPED_RECIPES.stream()
                .filter(def -> def.hasOperation(VANILLA_SHAPED))
                .forEach(def -> buildRecipe(def, writer));
    }

    private void buildRecipe(ShapedRecipeDef def, Consumer<FinishedRecipe> writer) {
        ItemStack outputStack = def.getSingleOutputAsItem();
        RecipeResult output = new RecipeResult(outputStack.getItem(), outputStack.getCount());

        ShapedRecipeBuilder builder = ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, output.item(), output.count())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        def.getKey().forEach((character, ingredient) -> {
                switch (ingredient) {
                    case ShapedRecipeDef.ShapeIngredient.OfItem item
                            -> builder.define(character, item.item());

                    case ShapedRecipeDef.ShapeIngredient.OfTag tag
                            -> builder.define(character, tag.tag());
                }

            }
        );

        for (var row : def.getPattern()) {
            builder.pattern(row);
        }

        Consumer<FinishedRecipe> finalWriter = writer;

        if (def.isDisableVanillaIfCreate()) {
            finalWriter = withConditions(writer,
                    DefaultResourceConditions.not(
                            DefaultResourceConditions.anyModLoaded("create")
                    )
            );
        }

        for (var con : def.getConditionsFor(VANILLA_SHAPED)) {
            finalWriter = withConditions(finalWriter, con);
        }

        builder.save(finalWriter, def.getRecipeName(VANILLA_SHAPED));
    }

    private record RecipeResult(ItemLike item, int count) {}
}