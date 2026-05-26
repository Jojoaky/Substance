package jojoaky.substance.datagen.recipe;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.datagen.recipe_generator.ShapelessRecipeDef;
import jojoaky.substance.datagen.recipe_generator.RecipeGeneratorRegistry;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import static jojoaky.substance.datagen.recipe.RecipeConstants.BASE_FLUID;

public class ChemicalFluidRecipes {
    public static void initialize() {
        RecipeGeneratorRegistry.accept(
                ShapelessRecipeDef.named("acetic_anhydride")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ModItems.GAS_BOTTLE_OXYGEN)
                        .require(Items.SUGAR)
                        .output(ModFluids.ACETIC_ANHYDRIDE, BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("ammonia")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ModItems.GAS_BOTTLE_NITROGEN)
                        .require(ModItems.GAS_BOTTLE_HYDROGEN)
                        .output(ModFluids.AMMONIA, BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("methanol")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ItemTags.COALS)
                        .require(ModItems.GAS_BOTTLE_HYDROGEN)
                        .require(ModItems.SCULK_CATALYST_CRYSTAL)
                        .output(ModFluids.METHANOL, BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("methylamine")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ModFluids.AMMONIA, BASE_FLUID)
                        .require(ModFluids.METHANOL, BASE_FLUID)
                        .require(ModItems.SCULK_CATALYST_CRYSTAL)
                        .output(ModFluids.METHYLAMINE, 2 * BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("phenylacetic_acid")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(Fluids.WATER, BASE_FLUID)
                        .require(ModItems.CYANIDE)
                        .output(ModFluids.PHENYLACETIC_ACID, BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("phenylacetone")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ModFluids.PHENYLACETIC_ACID, BASE_FLUID)
                        .require(ModFluids.ACETIC_ANHYDRIDE, BASE_FLUID)
                        .output(ModFluids.PHENYLACETONE, 2 * BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("white_crystal_oil")
                        .createMixing(HeatCondition.HEATED)
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ModItems.PSEUDO)
                        .require(ModItems.RED_PHOSPHORUS)
                        .require(ModItems.IODINE)
                        .require(Fluids.WATER, BASE_FLUID)
                        .output(ModFluids.WHITE_CRYSTAL_OIL, BASE_FLUID)
                        .build(),

                ShapelessRecipeDef.named("blue_crystal_oil")
                        .createMixing()
                        .generateVanillaMixing()
                        .require(ModFluids.PHENYLACETONE, BASE_FLUID)
                        .require(ModFluids.METHYLAMINE, BASE_FLUID)
                        .output(ModFluids.BLUE_CRYSTAL_OIL, BASE_FLUID)
                        .build(),


                // Gas Bottles
                ShapelessRecipeDef.named("nitrogen")
                        .createMixing()
                        .generateVanillaMixing()
                        .manualOnly()
                        .require(ModItems.GAS_BOTTLE)
                        .require(Items.POTATO)
                        .output(ModItems.GAS_BOTTLE_NITROGEN)
                        .build(),

                ShapelessRecipeDef.named("hydrogen_oxygen")
                        .createMixing()
                        .require(ModItems.GAS_BOTTLE, 3)
                        .require(Fluids.WATER, BASE_FLUID)
                        .require(ModItems.SCULK_CATALYST_CRYSTAL)
                        .output(ModItems.GAS_BOTTLE_HYDROGEN, 2)
                        .output(ModItems.GAS_BOTTLE_OXYGEN)
                        .build()

                // TODO: Create vanilla way of obtaining oxygen and hydrogen
                // maybe by right clicking with an empty gas bottle in air
        );
    }
}
