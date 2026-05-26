package jojoaky.substance.datagen.recipe;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.datagen.recipe_generator.ShapedRecipeDef;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.register.ModItems;
import net.minecraft.world.item.Items;

import static jojoaky.substance.datagen.recipe.RecipeConstants.BASE_FLUID;

public class MiscRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("sculk_crystal_shard")
                        .createCompacting(HeatCondition.HEATED)
                        .generateVanillaCompacting()
                        .disableVanillaIfCreate()
                        .require(Items.SCULK_CATALYST)
                        .require(Items.ECHO_SHARD)
                        .output(ModItems.SCULK_CATALYST_CRYSTAL, 2)
                        .build(),

                ShapelessRecipeDef.named("lava_flask")
                        .vanillaShapeless()
                        .require(ModFlasks.EMPTY_FLASK, 4)
                        .require(Items.LAVA_BUCKET)
                        .require(ModFlasks.EMPTY_FLASK, 4)
                        .output(ModFlasks.LAVA_FLASK, 8)
                        .build(),

                ShapelessRecipeDef.named("water_flask")
                        .vanillaShapeless()
                        .require(ModFlasks.EMPTY_FLASK, 4)
                        .require(Items.WATER_BUCKET)
                        .require(ModFlasks.EMPTY_FLASK, 4)
                        .output(ModFlasks.WATER_FLASK, 8)
                        .build(),

                ShapedRecipeDef.named("gas_bottle")
                        .vanillaShaped()
                        .key('g', Items.GLASS)
                        .pattern("  g")
                        .pattern(" g ")
                        .pattern("g  ")
                        .output(ModItems.GAS_BOTTLE, 3)
                        .build(),

                ShapedRecipeDef.named("empty_flask")
                        .vanillaShaped()
                        .key('g', Items.GLASS)
                        .pattern(" g ")
                        .pattern("g g")
                        .pattern("ggg")
                        .output(ModFlasks.EMPTY_FLASK, 6)
                        .build(),

                ShapedRecipeDef.named("tray")
                        .vanillaShaped()
                        .key('x', Items.IRON_INGOT)
                        .pattern("xxx")
                        .output(ModItems.TRAY)
                        .build()
        );
    }
}
