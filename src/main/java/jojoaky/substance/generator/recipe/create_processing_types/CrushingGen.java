package jojoaky.substance.generator.recipe.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

import static jojoaky.substance.generator.recipe.ShapelessRecipeDef.Operation.CREATE_CRUSHING;

public class CrushingGen extends CrushingRecipeGen {
    public CrushingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(def -> def.hasOperation(CREATE_CRUSHING))
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getRecipeName(CREATE_CRUSHING), b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            def.getConditionsFor(CREATE_CRUSHING).forEach(b::withCondition);
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

        def.getFluidInputsAsFlasks().forEach(flask -> {
            for (int i = 0; i < flask.getCount(); i++)
                b.require(flask.getItem());
        });
    }

    private void applyOutputs(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, ShapelessRecipeDef def) {
        def.getItemOutputs().forEach(item -> {
            b.output(item.getItem(), item.getCount());
        });

        def.getChancedItemOutputs().forEach(item -> {
            b.output(item.chance(), item.stack().getItem(), item.stack().getCount());
        });

        def.getFluidOutputsAsFlasks().forEach(flask -> {
            b.output(flask.getItem(), flask.getCount());
        });
    }
}