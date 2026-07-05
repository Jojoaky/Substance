package jojoaky.substance.generator.recipe.create_processing_types;

import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.generator.recipe.ShapedRecipeDef;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static jojoaky.substance.generator.recipe.ShapedRecipeDef.Operation.CREATE_MECHANICAL_CRAFTING;

public class MechanicalCraftingGen extends MechanicalCraftingRecipeGen {
    public MechanicalCraftingGen(FabricDataOutput output) {
        super(output, "substance");
    }

    private final List<GeneratedRecipe> recipes = RecipeGeneratorRegistry.SHAPED_RECIPES.stream()
            .filter(def -> def.hasOperation(CREATE_MECHANICAL_CRAFTING))
            .map(this::buildRecipe)
            .toList();

    protected NamedRecipeBuilder create(String name, Supplier<ItemLike> result) {
        return new NamedRecipeBuilder(name, result);
    }

    protected class NamedRecipeBuilder {
        private String name;
        private Supplier<ItemLike> result;
        private int amount;

        public NamedRecipeBuilder(String name, Supplier<ItemLike> result) {
            this.name = name;
            this.result = result;
            this.amount = 1;
        }

        public NamedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        public GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return register(consumer -> {
                MechanicalCraftingRecipeBuilder b =
                        builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));

                ResourceLocation location = asResource("mechanical_crafting/" + name);
                b.build(consumer, location);
            });
        }
    }

    private GeneratedRecipe buildRecipe(ShapedRecipeDef def) {
        ItemStack outputStack = def.getSingleOutputAsItem();

        return create(
                def.getRecipeName(CREATE_MECHANICAL_CRAFTING),
                outputStack::getItem)
                .returns(outputStack.getCount())
                .recipe(b -> {
                    applyKeys(b, def);
                    applyPattern(b, def);
                    if (!def.isMechanicalMirrorAllowed()) b.disallowMirrored();
                    def.getConditionsFor(CREATE_MECHANICAL_CRAFTING).forEach(b::withCondition);
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