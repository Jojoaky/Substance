package jojoaky.substance.datagen.recipe;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.FlaskItem;
import jojoaky.substance.datagen.recipe_generator.RecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class ChemicalFluidRecipes {
    public static final long BASE_FLUID = FlaskItem.CAPACITY;

    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                RecipeDef.named("acetic_anhydride")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModItems.GAS_BOTTLE_OXYGEN)
                        .require(Items.SUGAR)
                        .output(ModFluids.ACETIC_ANHYDRIDE, BASE_FLUID)
                        .build(),

                RecipeDef.named("ammonia")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModItems.GAS_BOTTLE_NITROGEN)
                        .require(ModItems.GAS_BOTTLE_HYDROGEN)
                        .output(ModFluids.AMMONIA, BASE_FLUID)
                        .build(),

                RecipeDef.named("methanol")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ItemTags.COALS)
                        .require(ModItems.GAS_BOTTLE_HYDROGEN)
                        .require(ModItems.SCULK_CATALYST_CRYSTAL)
                        .output(ModFluids.METHANOL, BASE_FLUID)
                        .build(),

                RecipeDef.named("methylamine")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModFluids.AMMONIA, BASE_FLUID)
                        .require(ModFluids.METHANOL, BASE_FLUID)
                        .require(ModItems.SCULK_CATALYST_CRYSTAL)
                        .output(ModFluids.METHYLAMINE, 2 * BASE_FLUID)
                        .build(),

                RecipeDef.named("phenylacetic_acid")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(Fluids.WATER, BASE_FLUID)
                        .require(ModItems.CYANIDE)
                        .output(ModFluids.PHENYLACETIC_ACID, BASE_FLUID)
                        .build(),

                RecipeDef.named("phenylacetone")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModFluids.PHENYLACETIC_ACID, BASE_FLUID)
                        .require(ModFluids.ACETIC_ANHYDRIDE, BASE_FLUID)
                        .output(ModFluids.PHENYLACETONE, 2 * BASE_FLUID)
                        .build(),

                RecipeDef.named("white_crystal_oil")
                        .createMixing(HeatCondition.HEATED)
                        .generateVanillaMixing()
                        .require(ModItems.PSEUDO)
                        .require(ModItems.RED_PHOSPHORUS)
                        .require(ModItems.IODINE)
                        .require(Fluids.WATER, BASE_FLUID)
                        .output(ModFluids.WHITE_CRYSTAL_OIL, BASE_FLUID)
                        .build(),

                RecipeDef.named("blue_crystal_oil")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModFluids.PHENYLACETONE, BASE_FLUID)
                        .require(ModFluids.METHYLAMINE, BASE_FLUID)
                        .output(ModFluids.BLUE_CRYSTAL_OIL, BASE_FLUID)
                        .build(),

                RecipeDef.named("nitrogen")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModItems.GAS_BOTTLE)
                        .require(Items.POTATO)
                        .output(ModItems.GAS_BOTTLE_NITROGEN)
                        .build()
        );
    }
}
