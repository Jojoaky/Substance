package jojoaky.substance.datagen;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.EmptyingRecipeGen;
import jojoaky.substance.flask.FlaskItem;
import jojoaky.substance.flask.ModFlasks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;

public class FlaskEmptyingRecipeGen extends EmptyingRecipeGen {

    public FlaskEmptyingRecipeGen(PackOutput output) {
        super(output, "substance");
    }

    private GeneratedRecipe flaskEmpty(ModFlasks.FlaskEntry flaskEntry) {
        String name = BuiltInRegistries.FLUID
                .getKey(flaskEntry.still().getSource())
                .getPath();

        return create(name + "_flask_emptying", b -> b
                .require(flaskEntry.flask())
                .output(flaskEntry.still(), FlaskItem.CAPACITY)
                .output(ModFlasks.EMPTY_FLASK)
                .whenModLoaded(Create.ID)
        );
    }

    {
        for (ModFlasks.FlaskEntry flaskEntry : ModFlasks.ALL_FLASK_ENTRIES) {
            flaskEmpty(flaskEntry);
        }
    }
}