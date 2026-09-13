package jojoaky.substance.compat.recipeviewer;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.tray.TrayBlock;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class ModWorldInteractions {
    private ModWorldInteractions() {
    }

    public static List<WorldInteractionDisplay> all() {
        return List.of(
                gas("oxygen_bottling", Blocks.SOUL_SAND, ModItems.GAS_BOTTLE_OXYGEN),
                gas("hydrogen_bottling", Blocks.MAGMA_BLOCK, ModItems.GAS_BOTTLE_HYDROGEN),
                trayFilling("white_oil_tray_filling", ModFluids.WHITE_CRYSTAL_OIL_FLASK, ModItems.WHITE_OIL_TRAY),
                trayFilling("blue_oil_tray_filling", ModFluids.BLUE_CRYSTAL_OIL_FLASK, ModItems.BLUE_OIL_TRAY),
                trayDrying("white_crystal_extraction", ModItems.WHITE_OIL_TRAY, ModItems.WHITE_CRYSTALS),
                trayDrying("blue_crystal_extraction", ModItems.BLUE_OIL_TRAY, ModItems.BLUE_CRYSTALS)
        );
    }

    private static WorldInteractionDisplay gas(String name, net.minecraft.world.level.ItemLike bubbleSource,
                                               net.minecraft.world.level.ItemLike output) {
        return WorldInteractionDisplay.builder(Substance.resource("world_interaction/" + name))
                .leftItem(Ingredient.of(ModItems.GAS_BOTTLE))
                .rightFluidCatalyst(Fluids.WATER, FluidConstants.BLOCK)
                .rightItemCatalyst(Ingredient.of(bubbleSource))
                .output(new ItemStack(output))
                .instruction("recipe.substance.world_interaction." + name)
                .build();
    }

    private static WorldInteractionDisplay trayFilling(String name, net.minecraft.world.level.ItemLike filledFlask,
                                                       net.minecraft.world.level.ItemLike filledTray) {
        return WorldInteractionDisplay.builder(Substance.resource("world_interaction/" + name))
                .leftItem(Ingredient.of(ModItems.TRAY))
                .rightItem(Ingredient.of(filledFlask), 3)
                .output(new ItemStack(filledTray))
                .output(new ItemStack(ModFlasks.EMPTY_FLASK, 3))
                .instruction("recipe.substance.world_interaction." + name, 3)
                .build();
    }

    private static WorldInteractionDisplay trayDrying(String name, net.minecraft.world.level.ItemLike filledTray,
                                                      net.minecraft.world.level.ItemLike crystals) {
        return WorldInteractionDisplay.builder(Substance.resource("world_interaction/" + name))
                .leftItem(Ingredient.of(filledTray))
                .rightItem(Ingredient.of(ItemTags.PICKAXES))
                .output(new ItemStack(crystals))
                .output(new ItemStack(ModItems.TRAY))
                .instruction("recipe.substance.world_interaction." + name, TrayBlock.DRY_DELAY_TICKS / 20.0F)
                .duration(TrayBlock.DRY_DELAY_TICKS)
                .build();
    }
}