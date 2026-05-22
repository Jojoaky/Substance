package jojoaky.substance.datagen.recipe_generator.create_processing_types;

import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipe_generator.ShapedRecipeDef;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Consumer;

public class MechanicalCraftingGen extends MechanicalCraftingRecipeGen {
    public MechanicalCraftingGen(FabricDataOutput output) {
        super(output, "substance");
    }

    private final List<GeneratedRecipe> recipes = RecipeGeneratorRegistry.SHAPED_RECIPES.stream()
            .filter(ShapedRecipeDef::isCreateMechanicalCrafting)
            .map(this::buildRecipe)
            .toList();

    private GeneratedRecipe buildRecipe(ShapedRecipeDef def) {
        ItemStack outputStack = def.getSingleOutputAsItem();

        return create(outputStack::getItem).returns(outputStack.getCount())
                .withSuffix("_mechanical")
                .recipe(b -> {
                    applyKeys(b, def);
                    applyPattern(b, def);
                    if (!def.isMechanicalMirrorAllowed()) b.disallowMirrored();
                    def.getConditions().forEach(b::withCondition);
                    return b;
                });
    }

    void applyKeys(MechanicalCraftingRecipeBuilder builder, ShapedRecipeDef def) {
        def.getKey().forEach((character, ingredient) -> {
                    switch (ingredient) {
                        case ShapedRecipeDef.ShapeIngredient.OfItem item
                                -> builder.key(character, Ingredient.of(item.item()));

                        case ShapedRecipeDef.ShapeIngredient.OfTag tag
                                -> builder.key(character, Ingredient.of(tag.tag()));
                    }

                }
        );
    }

    void applyPattern(MechanicalCraftingRecipeBuilder builder, ShapedRecipeDef def) {
        for (var line : def.getPattern()) {
            builder.patternLine(line);
        }
    }
}