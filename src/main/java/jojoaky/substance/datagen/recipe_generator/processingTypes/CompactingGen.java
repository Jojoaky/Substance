package jojoaky.substance.datagen.recipe_generator.processingTypes;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.RecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

public class CompactingGen extends CompactingRecipeGen {
    public CompactingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.ALL_RECIPES.stream()
                .filter(RecipeDef::isCreateCompacting)
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(RecipeDef def) {
        create(def.getName() + "_compacting", b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.requiresHeat(def.getCompactingHeat());
            b.whenModLoaded(Create.ID);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, RecipeDef def) {
        def.getItemInputs().forEach(itemStack -> {
            for (int i = 0; i < itemStack.getCount(); i++)
                b.require(itemStack.getItem());
        });

        def.getTagInputs().forEach(tag -> {
            for (int i = 0; i < tag.count(); i++)
                b.require(tag.tag());
        });

        def.getFluidInputs().forEach(fluid -> {
            b.require(fluid.fluid(), fluid.amount());
        });
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, RecipeDef def) {
        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });

        def.getChancedItemOutputs().forEach(item -> {
            b.output(item.chance(), item.stack().getItem(), item.stack().getCount());
        });

        def.getFluidOutputs().forEach(fluid -> {
            b.output(fluid.fluid(), fluid.amount());
        });
    }
}