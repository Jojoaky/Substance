package jojoaky.substance.datagen.recipe_generator.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

public class FillingGen extends FillingRecipeGen {
    public FillingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(ShapelessRecipeDef::isCreateFilling)
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getName() + "_filling", b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            def.getConditions().forEach(b::withCondition);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (def.getFluidInputs().size() != 1) {
            throw new IllegalArgumentException("Filling Recipe must have exactly one fluid input! " + def.getName() + " has " + def.getFluidInputs().size());
        }

        int totalItemInputs = def.getTagInputs().size() + def.getItemInputs().size();
        if (totalItemInputs != 1) {
            throw new IllegalArgumentException("Filling Recipe must have exactly one item container ingredient! " + def.getName() + " has " + totalItemInputs);
        }

        def.getFluidInputs().forEach(fluid -> b.require(fluid.fluid(), fluid.amount()));

        def.getItemInputs().forEach(itemStack -> b.require(itemStack.getItem()));
        def.getTagInputs().forEach(tag -> b.require(tag.tag()));
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        if (!def.getFluidOutputs().isEmpty()) {
            throw new IllegalArgumentException("Filling Recipe can't have fluid outputs! in " + def.getName());
        }

        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });

        def.getChancedItemOutputs().forEach(item -> {
            b.output(item.chance(), item.stack().getItem(), item.stack().getCount());
        });
    }
}