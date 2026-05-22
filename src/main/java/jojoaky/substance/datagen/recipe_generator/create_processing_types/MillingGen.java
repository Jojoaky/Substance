package jojoaky.substance.datagen.recipe_generator.create_processing_types;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.MillingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.minecraft.data.PackOutput;

public class MillingGen extends MillingRecipeGen {
    public MillingGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(ShapelessRecipeDef::isCreateMilling)
                .forEach(this::buildRecipe);
    }

    private void buildRecipe(ShapelessRecipeDef def) {
        create(def.getName() + "_milling", b -> {
            applyIngredients(b, def);
            applyOutputs(b, def);
            b.whenModLoaded(Create.ID);
            def.getConditions().forEach(b::withCondition);
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