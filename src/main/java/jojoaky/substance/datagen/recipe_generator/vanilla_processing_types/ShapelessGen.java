package jojoaky.substance.datagen.recipe_generator.vanilla_processing_types;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class ShapelessGen extends FabricRecipeProvider {

    public ShapelessGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(ShapelessRecipeDef::isVanillaShapeless)
                .forEach(def -> buildRecipe(def, writer, HeatCondition.NONE, def.getName()));

        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(ShapelessRecipeDef::isCustomVanillaMixing)
                .forEach(def -> buildRecipe(def, writer, def.getMixingHeat(), def.getName() + "_v_mixing"));

        RecipeGeneratorRegistry.SHAPELESS_RECIPES.stream()
                .filter(ShapelessRecipeDef::isCustomVanillaCompacting)
                .forEach(def -> buildRecipe(def, writer, def.getCompactingHeat(), def.getName() + "v_compacting"));
    }

    private void buildRecipe(ShapelessRecipeDef def, Consumer<FinishedRecipe> writer, HeatCondition heating, String name) {
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

        if (def.isDisableVanillaIfCreate()) {
            finalWriter = withConditions(writer,
                    DefaultResourceConditions.not(
                            DefaultResourceConditions.anyModLoaded("create")
                    )
            );
        }

        for (var con : def.getConditions()) {
            finalWriter = withConditions(finalWriter, con);
        }

        builder.save(finalWriter, name);
    }

    private record RecipeResult(ItemLike item, int count) {}
}