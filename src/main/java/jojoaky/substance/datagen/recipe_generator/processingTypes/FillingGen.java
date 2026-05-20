package jojoaky.substance.datagen.recipe_generator.processingTypes;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.RecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

public class FillingGen extends FillingRecipeGen {
    public FillingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.ALL_RECIPES.stream()
                .filter(RecipeDef::isCreateCrushing)
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(RecipeDef def) {
        create(def.getName() + "_filling", b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, RecipeDef def) {
        if (def.getFluidInputs().size() != 1)
            throw new IllegalArgumentException("Filling Recipe must have exactly one fluid input! " + def.getName() + " has " + def.getFluidInputs().size());

        if (!def.getTagInputs().isEmpty() || !def.getItemInputs().isEmpty())
            throw new IllegalArgumentException("Filling Recipe can only have fluid ingredients! in " + def.getName());

        def.getFluidInputs().forEach(fluid -> {
            b.require(fluid.fluid(), fluid.amount());
        });
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, RecipeDef def) {
        if (!def.getFluidOutputs().isEmpty())
            throw new IllegalArgumentException("Filling Recipe can't have fluid outputs! in " + def.getName());

        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });

        def.getChancedItemOutputs().forEach(item -> {
            b.output(item.chance(), item.stack().getItem(), item.stack().getCount());
        });
    }
}