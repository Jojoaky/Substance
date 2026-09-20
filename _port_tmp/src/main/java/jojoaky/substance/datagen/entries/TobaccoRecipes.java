package jojoaky.substance.datagen.entries;

import jojoaky.substance.data.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.data.generator.recipe.ShapedRecipeDef;
import jojoaky.substance.data.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.register.ModItems;
import net.minecraft.world.item.Items;

public class TobaccoRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("dried_tobacco_leaf")
                        .smoking(150, 1)
                        .smelting(300,1)
                        .require(ModItems.RIPE_TOBACCO_LEAF)
                        .output(ModItems.DRIED_TOBACCO_LEAF)
                        .build(),

                ShapedRecipeDef.named("cigarette")
                        .vanillaShaped()
                        .pattern("TPT")
                        .key('P', Items.PAPER)
                        .key('T', ModItems.DRIED_TOBACCO_LEAF)
                        .output(ModItems.CIGARETTE)
                        .build()
        );
    }
}
