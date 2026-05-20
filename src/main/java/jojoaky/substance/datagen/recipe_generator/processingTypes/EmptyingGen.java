package jojoaky.substance.datagen.recipe_generator.processingTypes;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.EmptyingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.RecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

public class EmptyingGen extends EmptyingRecipeGen {
    public EmptyingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.ALL_RECIPES.stream()
                .filter(RecipeDef::isCreateCrushing)
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(RecipeDef def) {
        create(def.getName() + "_emptying", b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, RecipeDef def) {
        if (!def.getFluidInputs().isEmpty())
            throw new IllegalArgumentException("Emptying Recipe can't have fluid ingredients! in " + def.getName());

        if (def.getTagInputs().size() + def.getItemInputs().size() != 1)
            throw new IllegalArgumentException("Emptying Recipe must have exactly one ingredient! " + def.getName() + " has " + def.getTagInputs().size() + def.getItemInputs().size());

        def.getItemInputs().forEach(itemStack -> {
            for (int i = 0; i < itemStack.getCount(); i++)
                b.require(itemStack.getItem());
        });

        def.getTagInputs().forEach(tag -> {
            for (int i = 0; i < tag.count(); i++)
                b.require(tag.tag());
        });
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, RecipeDef def) {
        if (def.getFluidOutputs().size() != 1)
            throw new IllegalArgumentException("Emptying Recipe must have exactly one fluid output! " + def.getName() + " has " + def.getFluidOutputs().size());

        if (!def.getItemInputs().isEmpty())
            throw new IllegalArgumentException("Emptying Recipe can only have fluid ingredients! in " + def.getName());

        def.getFluidOutputs().forEach(fluid -> {
            b.output(fluid.fluid(), fluid.amount());
        });
    }
}