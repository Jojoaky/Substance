package jojoaky.substance.datagen.recipe;

import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
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
                        .createCrushing()
                        .vanillaShapeless()
                        .disableVanillaIfCreate()
                        .require(Items.CALCITE)
                        .output(ModItems.WHITE_PHOSPHORUS)
                        .output(ModItems.WHITE_PHOSPHORUS, 1, 0.6f)
                        .build(),

                ShapelessRecipeDef.named("red_phosphorus")
                        .smelting(100, 1)
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
                        .build(),

                ShapelessRecipeDef.named("pseudoephedrine_pill")
                        .createMilling()
                        .createCrushing()
                        .vanillaShapeless()
                        .manualOnly()
                        .require(ModItems.SUDAFED_PILL)
                        .output(ModItems.PSEUDO, 2)
                        .output(ModItems.PSEUDO, 1, 0.7f)
                        .build()
        );
    }
}
