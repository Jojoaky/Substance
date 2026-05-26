package jojoaky.substance.datagen.recipe;

import jojoaky.substance.content.flask.FlaskItem;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;

import static jojoaky.substance.datagen.recipe.RecipeConstants.BASE_FLUID;

public class CrystalRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("blue_oil_tray")
                        .createFilling()
                        .vanillaShapeless()
                        .require(ModFluids.BLUE_CRYSTAL_OIL, 3 * BASE_FLUID)
                        .require(ModItems.TRAY)
                        .output(ModItems.BLUE_OIL_TRAY)
                        .build()
        );

        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("white_oil_tray")
                        .createFilling()
                        .vanillaShapeless()
                        .require(ModFluids.WHITE_CRYSTAL_OIL, 3 * BASE_FLUID)
                        .require(ModItems.TRAY)
                        .output(ModItems.WHITE_OIL_TRAY)
                        .build()
        );


        // TODO: replace vanilla with in world interaction, such as right clicking with pickaxe
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("blue_crystals")
                        .createPressing()
                        .vanillaShapeless()
                        .useWeakReplacements()
                        .output(ModItems.BLUE_CRYSTALS)
                        .require(ModItems.BLUE_OIL_TRAY)
                        .build()
        );

        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("white_crystals")
                        .createPressing()
                        .vanillaShapeless()
                        .useWeakReplacements()
                        .output(ModItems.WHITE_CRYSTALS)
                        .require(ModItems.WHITE_OIL_TRAY)
                        .build()
        );
    }
}
