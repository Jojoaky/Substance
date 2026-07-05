package jojoaky.substance.recipes;

import jojoaky.substance.generator.recipe.RecipeGeneratorRegistry;
import jojoaky.substance.generator.recipe.ShapedRecipeDef;
import jojoaky.substance.generator.recipe.ShapelessRecipeDef;
import jojoaky.substance.register.ModItems;
import net.minecraft.tags.ItemTags;
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


                ShapedRecipeDef.named("pipe")
                        .vanillaShaped()
                        .pattern("N P")
                        .pattern("PP ")
                        .key('P', ItemTags.PLANKS)
                        .key('N', Items.IRON_NUGGET)
                        .output(ModItems.PIPE)
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
