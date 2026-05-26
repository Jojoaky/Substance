package jojoaky.substance.datagen.recipe_generator.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

import static jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef.Operation.CREATE_MIXING;

public class MixingGen extends MixingRecipeGen {
    public MixingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(def -> def.hasOperation(CREATE_MIXING))
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getRecipeName(CREATE_MIXING), b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.requiresHeat(def.getMixingHeat());
            b.whenModLoaded(Create.ID);
            def.getConditionsFor(CREATE_MIXING).forEach(b::withCondition);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
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

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
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