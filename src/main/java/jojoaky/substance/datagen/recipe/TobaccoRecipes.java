package jojoaky.substance.datagen.recipe;

import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.datagen.recipe_generator.ShapedRecipeDef;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
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
                        .build()
        );
    }
}
