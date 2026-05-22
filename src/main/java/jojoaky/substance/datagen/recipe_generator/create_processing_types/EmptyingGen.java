package jojoaky.substance.datagen.recipe_generator.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.EmptyingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

public class EmptyingGen extends EmptyingRecipeGen {
    public EmptyingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(ShapelessRecipeDef::isCreateEmptying) // FIXED: Was isCreateCrushing
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getName() + "_emptying", b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            def.getConditions().forEach(b::withCondition);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (!def.getFluidInputs().isEmpty()) {
            throw new IllegalArgumentException("Emptying Recipe can't have fluid ingredients! in " + def.getName());
        }

        int totalItemInputs = def.getTagInputs().size() + def.getItemInputs().size();
        if (totalItemInputs != 1) {
            throw new IllegalArgumentException("Emptying Recipe must have exactly one ingredient! " + def.getName() + " has " + totalItemInputs);
        }

        def.getItemInputs().forEach(itemStack -> b.require(itemStack.getItem()));
        def.getTagInputs().forEach(tag -> b.require(tag.tag()));
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (def.getFluidOutputs().size() != 1) {
            throw new IllegalArgumentException("Emptying Recipe must have exactly one fluid output! " + def.getName() + " has " + def.getFluidOutputs().size());
        }

        def.getFluidOutputs().forEach(fluid -> {
            b.output(fluid.fluid(), fluid.amount());
        });

        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });
    }
}