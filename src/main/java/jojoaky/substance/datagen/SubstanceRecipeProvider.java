package jojoaky.substance.datagen;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SubstanceRecipeProvider extends FabricRecipeProvider {
    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public SubstanceRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
    }

    public static DataProvider registerAllProcessing(FabricDataOutput output) {
        GENERATORS.add(new FlaskFillingRecipeGen(output));
        GENERATORS.add(new FlaskEmptyingRecipeGen(output));

        return new DataProvider() {

            @Override
            public @NotNull String getName() {
                return "Substance Processing Recipes";
            }

            @Override
            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        };
    }
}
