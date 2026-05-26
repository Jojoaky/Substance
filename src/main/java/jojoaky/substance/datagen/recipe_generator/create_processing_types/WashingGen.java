package jojoaky.substance.datagen.recipe_generator.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

import static jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef.Operation.CREATE_WASHING;

public class WashingGen extends WashingRecipeGen {
    public WashingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(def -> def.hasOperation(CREATE_WASHING))
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getRecipeName(CREATE_WASHING), b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            def.getConditionsFor(CREATE_WASHING).forEach(b::withCondition);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (!def.getFluidInputs().isEmpty()) {
            throw new IllegalArgumentException("Splashing Recipe can't have fluid ingredients! in " + def.getBaseName());
        }

        int totalItemInputs = def.getTagInputs().size() + def.getItemInputs().size();
        if (totalItemInputs != 1) {
            throw new IllegalArgumentException("Splashing Recipe must have exactly one ingredient! " + def.getBaseName() + " has " + totalItemInputs);
        }

        def.getItemInputs().forEach(itemStack -> b.require(itemStack.getItem()));
        def.getTagInputs().forEach(tag -> b.require(tag.tag()));
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });

        def.getFluidOutputsAsFlasks().forEach(flaskStack -> {
            b.output(flaskStack.getItem(), flaskStack.getCount());
        });

        def.getChancedItemOutputs().forEach(item -> {
            b.output(item.chance(), item.stack().getItem(), item.stack().getCount());
        });
    }
}