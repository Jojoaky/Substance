package jojoaky.substance.datagen.recipes.mixing;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.PackOutput;

import java.util.stream.Stream;

public class MixingRecipeGen extends com.simibubi.create.api.data.recipe.MixingRecipeGen {
    public MixingRecipeGen(PackOutput output) {
        super(output, "substance");
        registerAll();
    }

    private void registerAll() {
        Stream.concat(
                MixingRecipeSet.AUTO.stream(),
                MixingRecipeSet.CREATE_ONLY.stream()
        ).forEach(this::generateRecipe);
    }

    private void generateRecipe(MixingRecipeDef def) {
        create(def.name() + "_mixing", b -> {
            applyIngredients(b, def);
            applyOutput(b, def);
            b.requiresHeat(def.requiredHeat());
            b.whenModLoaded(Create.ID);
            return b;
        });
    }

    private void applyIngredients(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, MixingRecipeDef def) {
        for (MixingRecipeIngredient ing : def.ingredients()) {
            switch (ing) {
                case MixingRecipeIngredient.Item item -> {
                    for (int i = 0; i < item.count(); i++) {
                        b.require(item.item());
                    }
                }
                case MixingRecipeIngredient.Tag tag -> {
                    for (int i = 0; i < tag.count(); i++) {
                        b.require(tag.tag());
                    }
                }
                case MixingRecipeIngredient.Fluid fluid -> {
                    b.require(FluidIngredient.fromFluid(fluid.fluid().still(), fluid.amount()));
                }
            }
        }
    }

    private void applyOutput(ProcessingRecipeBuilder<ProcessingRecipe<?>> b, MixingRecipeDef def) {
        switch (def.output()) {
            case MixingRecipeOutput.Fluid fluid -> {
                b.output(fluid.fluid().still(), fluid.amount());
            }
            case MixingRecipeOutput.Item item -> {
                b.output(item.stack());
            }
        }
    }
}