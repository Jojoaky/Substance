package jojoaky.substance.datagen.recipes.mixing;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import jojoaky.substance.content.flask.FlaskItem;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class MixingRecipeSet {
    public static final List<MixingRecipeDef> AUTO = List.of(
            MixingRecipeDef.named("acetic_anhydride")
                    .require(ModItems.GAS_BOTTLE_OXYGEN)
                    .require(Items.SUGAR)
                    .output(ModFluids.aceticAnhydride)
                    .build(),

            MixingRecipeDef.named("ammonia")
                    .require(ModItems.GAS_BOTTLE_NITROGEN)
                    .require(ModItems.GAS_BOTTLE_HYDROGEN)
                    .output(ModFluids.ammonia)
                    .build(),

            MixingRecipeDef.named("methanol")
                    .require(ItemTags.COALS)
                    .require(ModItems.GAS_BOTTLE_HYDROGEN)
                    .require(ModItems.SCULK_CATALYST_CRYSTAL)
                    .output(ModFluids.methanol)
                    .build(),

            MixingRecipeDef.named("methylamine")
                    .require(ModFluids.ammonia)
                    .require(ModFluids.methanol)
                    .require(ModItems.SCULK_CATALYST_CRYSTAL)
                    .output(ModFluids.methylamine, 2)
                    .build(),

            MixingRecipeDef.named("phenylacetic_acid")
                    .require(ModFluids.water)
                    .require(ModItems.CYANIDE)
                    .output(ModFluids.phenylaceticAcid)
                    .build(),

            MixingRecipeDef.named("phenylacetone")
                    .require(ModFluids.phenylaceticAcid)
                    .require(ModFluids.aceticAnhydride)
                    .output(ModFluids.phenylacetone, 2)
                    .build(),

            MixingRecipeDef.named("white_crystal_oil")
                    .require(ModItems.PSEUDO)
                    .require(ModItems.RED_PHOSPHORUS)
                    .require(ModItems.IODINE)
                    .require(ModFluids.water)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(ModFluids.whiteCrystalOil)
                    .build(),

            MixingRecipeDef.named("blue_crystal_oil")
                    .require(ModFluids.phenylacetone)
                    .require(ModFluids.methylamine)
                    .output(ModFluids.blueCrystalOil)
                    .build(),

            MixingRecipeDef.named("nitrogen")
                    .require(ModItems.GAS_BOTTLE)
                    .require(Items.POTATO)
                    .output(ModItems.GAS_BOTTLE_NITROGEN.getDefaultInstance())
                    .build()
    );

    public static final List<MixingRecipeDef> CREATE_ONLY = List.of();

    public static final List<MixingRecipeDef> VANILLA_ONLY = List.of();

    private MixingRecipeSet() {}
}