package jojoaky.substance.datagen.recipes.mixing;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.FlaskItem;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.register.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import java.util.function.Consumer;

public class MixingCraftingRecipeGen extends FabricRecipeProvider {

    public MixingCraftingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        MixingRecipeSet.AUTO.forEach(def -> buildRecipe(def, writer));
        MixingRecipeSet.VANILLA_ONLY.forEach(def -> buildRecipe(def, writer));
    }

    private void buildRecipe(MixingRecipeDef def, Consumer<FinishedRecipe> writer) {
        RecipeResult output = parseRecipeOutput(def.output());

        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, output.item(), output.count())
                .unlockedBy(getHasName(ModFlasks.EMPTY_FLASK), has(ModFlasks.EMPTY_FLASK));

        for (MixingRecipeIngredient ing : def.ingredients()) {
            switch (ing) {
                case MixingRecipeIngredient.Item item -> builder.requires(item.item(), item.count());
                case MixingRecipeIngredient.Tag tag -> builder.requires(Ingredient.of(tag.tag()), tag.count());
                case MixingRecipeIngredient.Fluid fluid -> {
                    int flaskCount = (int) (fluid.amount() / FlaskItem.CAPACITY);
                    builder.requires(fluid.fluid().flask(), flaskCount);
                }
            }
        }

        switch (def.requiredHeat()) {
            case HEATED -> builder.requires(ModFlasks.LAVA_FLASK);
            case SUPERHEATED -> builder.requires(Items.LAVA_BUCKET);
        }

        Consumer<FinishedRecipe> finalWriter = writer;

        if (def.replaceCraftingWhenCreate()) {
            finalWriter = withConditions(writer,
                    DefaultResourceConditions.not(
                            DefaultResourceConditions.allModsLoaded("create")
                    )
            );
        }

        builder.save(finalWriter);
    }

    private RecipeResult parseRecipeOutput(MixingRecipeOutput output) {
        return switch (output) {
            case MixingRecipeOutput.Item itemOutput ->
                    new RecipeResult(itemOutput.stack().getItem(), itemOutput.stack().getCount());
            case MixingRecipeOutput.Fluid fluidOutput -> {
                int count = (int) (fluidOutput.amount() / FlaskItem.CAPACITY);
                yield new RecipeResult(fluidOutput.fluid().flask(), count);
            }
        };
    }

    private record RecipeResult(ItemLike item, int count) {}
}