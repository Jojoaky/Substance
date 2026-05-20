package jojoaky.substance.datagen;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import jojoaky.substance.flask.FlaskItem;
import jojoaky.substance.flask.ModFlasks;
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
                .require(ModFlasks.EMPTY_FLASK)
                .require(flaskEntry.still(), FlaskItem.CAPACITY)
                .output(flaskEntry.flask())
        );
    }

    {
        for (ModFlasks.FlaskEntry flaskEntry : ModFlasks.ALL_FLASK_ENTRIES) {
            flaskFill(flaskEntry);
        }
    }
}