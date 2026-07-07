package jojoaky.substance.data.generator.recipe.vanilla_processing_types;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.data.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.data.generator.recipe.RecipeGeneratorRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static jojoaky.substance.data.generator.recipe.ShapelessRecipeDef.Operation.*;

public class ShapelessGen extends FabricRecipeProvider {

    public ShapelessGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.forEach(def -> {
            if (def.hasOperation(VANILLA_SHAPELESS)) {
                buildRecipe(def, writer, VANILLA_SHAPELESS, HeatCondition.NONE);
            }

            if (def.hasOperation(CUSTOM_VANILLA_MIXING)) {
                buildRecipe(def, writer, CUSTOM_VANILLA_MIXING, def.getMixingHeat());
            }

            if (def.hasOperation(CUSTOM_VANILLA_COMPACTING)) {
                buildRecipe(def, writer, CUSTOM_VANILLA_COMPACTING, def.getCompactingHeat());
            }
        });
    }

    private void buildRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer, ShapelessRecipeDef.Operation op, HeatCondition heating) {
        ItemStack outputStack = def.getSingleOutputAsItem();
        RecipeResult output = new RecipeResult(outputStack.getItem(), outputStack.getCount());

        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, output.item(), output.count())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        def.getItemInputs().forEach(stack ->
                builder.requires(stack.getItem(), stack.getCount())
        );

        def.getTagInputs().forEach(tag -> {
            for (int i = 0; i < tag.count(); i++)
                builder.requires(tag.tag());
        });

        def.getFluidInputsAsFlasks().forEach(flask ->
                builder.requires(flask.getItem(), flask.getCount())
        );

        switch (heating) {
            case HEATED -> builder.requires(ModFlasks.LAVA_FLASK);
            case SUPERHEATED -> builder.requires(Items.LAVA_BUCKET);
        }

        Consumer<FinishedRecipe> finalWriter = writer;

        for (var con : def.getConditionsFor(op)) {
            finalWriter = withConditions(finalWriter, con);
        }

        builder.save(finalWriter, def.getRecipeName(op));
    }

    private record RecipeResult(ItemLike item, int count) {}
}