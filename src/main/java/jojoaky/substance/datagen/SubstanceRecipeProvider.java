package jojoaky.substance.datagen;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import jojoaky.substance.datagen.recipes.mixing.MixingCraftingRecipeGen;
import jojoaky.substance.datagen.recipes.mixing.MixingRecipeGen;
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
    static final List<ProcessingRecipeGen> CREATE_GENERATORS = new ArrayList<>();
    static final List<FabricRecipeProvider> FABRIC_GENERATORS = new ArrayList<>();

    public SubstanceRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
    }

    public static DataProvider registerAll(FabricDataOutput output) {
        CREATE_GENERATORS.add(new MixingRecipeGen(output));

        FABRIC_GENERATORS.add(new MixingCraftingRecipeGen(output));

        return new DataProvider() {
            @Override
            public @NotNull String getName() {
                return "Substance Processing & Crafting Recipes";
            }

            @Override
            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
                CompletableFuture<?> createProcessingFutures = CompletableFuture.allOf(
                        CREATE_GENERATORS.stream()
                                .map(gen -> gen.run(dc))
                                .toArray(CompletableFuture[]::new)
                );

                CompletableFuture<?> fabricProcessingFutures = CompletableFuture.allOf(
                        FABRIC_GENERATORS.stream()
                                .map(gen -> gen.run(dc))
                                .toArray(CompletableFuture[]::new)
                );

                return CompletableFuture.allOf(createProcessingFutures, fabricProcessingFutures);
            }
        };
    }
}
