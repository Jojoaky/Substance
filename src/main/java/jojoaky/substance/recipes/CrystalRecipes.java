package jojoaky.substance.recipes;

import jojoaky.substance.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;

import static jojoaky.substance.recipes.RecipeConstants.BASE_FLUID;

public class CrystalRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("blue_oil_tray")
                        .createFilling()
                        .vanillaShapeless()
                        .manualOnly()
                        .require(ModFluids.BLUE_CRYSTAL_OIL, 3 * BASE_FLUID)
                        .require(ModItems.TRAY)
                        .output(ModItems.BLUE_OIL_TRAY)
                        .build()
        );

        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("white_oil_tray")
                        .createFilling()
                        .vanillaShapeless()
                        .manualOnly()
                        .require(ModFluids.WHITE_CRYSTAL_OIL, 3 * BASE_FLUID)
                        .require(ModItems.TRAY)
                        .output(ModItems.WHITE_OIL_TRAY)
                        .build()
        );


        // TODO: replace vanilla with in world interaction, such as right clicking with pickaxe
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("blue_crystals")
                        .createPressing()
                        .disableVanillaIfCreate()
                        .output(ModItems.BLUE_CRYSTALS)
                        .require(ModItems.BLUE_OIL_TRAY)
                        .build()
        );

        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("white_crystals")
                        .createPressing()
                        .disableVanillaIfCreate()
                        .output(ModItems.WHITE_CRYSTALS)
                        .require(ModItems.WHITE_OIL_TRAY)
                        .build()
        );
    }
}
