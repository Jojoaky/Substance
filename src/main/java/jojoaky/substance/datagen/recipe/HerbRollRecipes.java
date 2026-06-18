package jojoaky.substance.datagen.recipe;

import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipe_generator.ShapedRecipeDef;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.register.ModItems;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.world.item.Items;

public class HerbRollRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("dried_herb")
                        .smoking(150, 1)
                        .smelting(300, 1)
                        .require(ModItems.HERB_BUD)
                        .output(ModItems.DRIED_HERB_BUD)
                        .build(),

                ShapedRecipeDef.named("herbal_roll")
                        .vanillaShaped()
                        .key('p', Items.PAPER)
                        .key('w', ModItems.DRIED_HERB_BUD)
                        .pattern("pwp")
                        .output(ModItems.HERBAL_ROLL),

                ShapedRecipeDef.named("thick_herbal_roll")
                        .vanillaShaped()
                        .key('p', Items.PAPER)
                        .key('w', ModItems.DRIED_HERB_BUD)
                        .pattern(" p ")
                        .pattern("www")
                        .pattern(" p ")
                        .condition(DefaultResourceConditions.not(DefaultResourceConditions.anyModLoaded("create")))
                        .output(ModItems.THICK_HERBAL_ROLL),

                ShapedRecipeDef.named("thick_herbal_roll_create")
                        .createMechanicalCrafting()
                        .key('p', Items.PAPER)
                        .key('w', ModItems.DRIED_HERB_BUD)
                        .pattern("pwwwp")
                        .output(ModItems.THICK_HERBAL_ROLL)
        );
    }
}
