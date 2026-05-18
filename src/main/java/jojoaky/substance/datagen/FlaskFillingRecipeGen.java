package jojoaky.substance.datagen;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import jojoaky.substance.register.ModFlasks;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;

public class FlaskFillingRecipeGen extends FillingRecipeGen {

    public FlaskFillingRecipeGen(PackOutput output) {
        super(output, "substance");
    }

    private GeneratedRecipe flaskFill(ModFlasks.FlaskEntry flaskEntry) {
        String name = BuiltInRegistries.FLUID
                .getKey(flaskEntry.still().getSource())
                .getPath();

        return create(name + "_flask_filling", b -> b
                .require(ModItems.FLASK)
                .require(flaskEntry.still(), FluidConstants.BOTTLE)
                .output(flaskEntry.flask())
        );
    }

    {
        for (ModFlasks.FlaskEntry flaskEntry : ModFlasks.ALL_FLASK_ENTRIES) {
            flaskFill(flaskEntry);
        }
    }
}