package jojoaky.substance.datagen.generator.recipe.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.EmptyingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.datagen.generator.recipe.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

import static jojoaky.substance.datagen.generator.recipe.ShapelessRecipeDef.Operation.CREATE_EMPTYING;

public class EmptyingGen extends EmptyingRecipeGen {
    public EmptyingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(def -> def.hasOperation(CREATE_EMPTYING))
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getRecipeName(CREATE_EMPTYING), b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            def.getConditionsFor(CREATE_EMPTYING).forEach(b::withCondition);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (!def.getFluidInputs().isEmpty()) {
            throw new IllegalArgumentException("Emptying Recipe can't have fluid ingredients! in " + def.getBaseName());
        }

        int totalItemInputs = def.getTagInputs().size() + def.getItemInputs().size();
        if (totalItemInputs != 1) {
            throw new IllegalArgumentException("Emptying Recipe must have exactly one ingredient! " + def.getBaseName() + " has " + totalItemInputs);
        }

        def.getItemInputs().forEach(itemStack -> b.require(itemStack.getItem()));
        def.getTagInputs().forEach(tag -> b.require(tag.tag()));
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (def.getFluidOutputs().size() != 1) {
            throw new IllegalArgumentException("Emptying Recipe must have exactly one fluid output! " + def.getBaseName() + " has " + def.getFluidOutputs().size());
        }

        def.getFluidOutputs().forEach(fluid -> {
            b.output(fluid.fluid(), fluid.amount());
        });

        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });
    }
}