package jojoaky.substance.datagen.generator.recipe;

import jojoaky.substance.datagen.generator.recipe.create_processing_types.*;
import jojoaky.substance.datagen.generator.recipe.vanilla_processing_types.CookingGen;
import jojoaky.substance.datagen.generator.recipe.vanilla_processing_types.ShapedGen;
import jojoaky.substance.datagen.generator.recipe.vanilla_processing_types.ShapelessGen;
import jojoaky.substance.datagen.generator.recipe.vanilla_processing_types.VanillaWashingGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    static final List<RecipeProvider> GENERATORS = new ArrayList<>();

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
    }

    public static DataProvider registerAll(FabricDataOutput output) {
        GENERATORS.add(new ShapelessGen(output));
        GENERATORS.add(new ShapedGen(output));
        GENERATORS.add(new CookingGen(output));
        GENERATORS.add(new VanillaWashingGen(output));

        GENERATORS.add(new CompactingGen(output));
        GENERATORS.add(new CrushingGen(output));
        GENERATORS.add(new MixingGen(output));
        GENERATORS.add(new MillingGen(output));
        GENERATORS.add(new PressingGen(output));
        GENERATORS.add(new EmptyingGen(output));
        GENERATORS.add(new FillingGen(output));
        GENERATORS.add(new WashingGen(output));
        GENERATORS.add(new HauntingGen(output));

        GENERATORS.add(new MechanicalCraftingGen(output));

        return new DataProvider() {
            @Override
            public @NotNull String getName() {
                return "Substance Recipes";
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
