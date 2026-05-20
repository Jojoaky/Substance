package jojoaky.substance.datagen.recipe_generator;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import jojoaky.substance.datagen.recipe_generator.processingTypes.*;
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

public class CreateRecipeProvider extends FabricRecipeProvider {
    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public CreateRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
    }

    public static DataProvider registerAll(FabricDataOutput output) {
        GENERATORS.add(new CompactingGen(output));
        GENERATORS.add(new CrushingGen(output));
        GENERATORS.add(new MixingGen(output));
        GENERATORS.add(new MillingGen(output));
        GENERATORS.add(new PressingGen(output));
        GENERATORS.add(new EmptyingGen(output));
        GENERATORS.add(new FillingGen(output));
        // TODO: Haunting and Splashing

        return new DataProvider() {
            @Override
            public @NotNull String getName() {
                return "Substance Processing & Crafting Recipes";
            }

            @Override
            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
                return CompletableFuture.allOf(
                        GENERATORS.stream()
                                .map(gen -> gen.run(dc))
                                .toArray(CompletableFuture[]::new)
                );
            }
        };
    }
}
