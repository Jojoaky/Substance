package jojoaky.substance.datagen.entries;

import jojoaky.substance.data.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.data.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.register.ModItems;
import net.minecraft.world.item.Items;

public class ChemicalPowderRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("cyanide")
                        .createPressing()
                        .vanillaShapeless()
                        .require(Items.APPLE)
                        .output(ModItems.CYANIDE)
                        .output(ModItems.CYANIDE, 1, 0.2f)
                        .build(),

                ShapelessRecipeDef.named("white_phosphorus")
                        .smelting(200, 1)
                        .blasting(100, 1)
                        .require(Items.BONE_MEAL)
                        .output(ModItems.WHITE_PHOSPHORUS)
                        .build(),

                ShapelessRecipeDef.named("red_phosphorus")
                        .smelting(200, 1)
                        .blasting(100, 1)
                        .require(ModItems.WHITE_PHOSPHORUS)
                        .output(ModItems.RED_PHOSPHORUS)
                        .build(),

                ShapelessRecipeDef.named("iodine")
                        .createWashing()
                        .generateVanillaWashing()
                        .manualOnly()
                        .require(Items.DRIED_KELP)
                        .output(ModItems.IODINE)
                        .output(ModItems.IODINE, 2, 0.25f)
                        .build(),

                ShapelessRecipeDef.named("pseudoephedrine_ephedra_create")
                        .createMilling()
                        .createCrushing()
                        .require(ModItems.EPHEDRA_BUNDLE)
                        .output(ModItems.PSEUDO)
                        .output(ModItems.PSEUDO, 1, 0.5f)
                        .output(ModItems.EPHEDRA_SEEDS, 2, 0.4f)
                        .build(),

                ShapelessRecipeDef.named("pseudoephedrine_ephedra_vanilla")
                        .vanillaShapeless()
                        .manualOnly()
                        .require(ModItems.EPHEDRA_BUNDLE, 4)
                        .output(ModItems.PSEUDO, 4)
                        .build()
        );
    }
}
