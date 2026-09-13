package jojoaky.substance.compat.recipeviewer;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.tray.TrayBlock;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

import static jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay.SizedIngredient;

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
        return new WorldInteractionDisplay(
                Substance.resource("world_interaction/" + name),
                List.of(SizedIngredient.of(Ingredient.of(ModItems.GAS_BOTTLE))),
                List.of(SizedIngredient.of(Ingredient.of(bubbleSource))),
                List.of(new ItemStack(output)),
                "recipe.substance.world_interaction." + name,
                List.of(),
                0
        );
    }

    private static WorldInteractionDisplay trayFilling(String name, net.minecraft.world.level.ItemLike filledFlask,
                                                        net.minecraft.world.level.ItemLike filledTray) {
        return new WorldInteractionDisplay(
                Substance.resource("world_interaction/" + name),
                List.of(
                        SizedIngredient.of(Ingredient.of(ModItems.TRAY)),
                        new SizedIngredient(Ingredient.of(filledFlask), 3)
                ),
                List.of(),
                List.of(new ItemStack(filledTray), new ItemStack(ModFlasks.EMPTY_FLASK, 3)),
                "recipe.substance.world_interaction." + name,
                List.of(3),
                0
        );
    }

    private static WorldInteractionDisplay trayDrying(String name, net.minecraft.world.level.ItemLike filledTray,
                                                       net.minecraft.world.level.ItemLike crystals) {
        return new WorldInteractionDisplay(
                Substance.resource("world_interaction/" + name),
                List.of(SizedIngredient.of(Ingredient.of(filledTray))),
                List.of(SizedIngredient.of(Ingredient.of(ItemTags.PICKAXES))),
                List.of(new ItemStack(crystals), new ItemStack(ModItems.TRAY)),
                "recipe.substance.world_interaction." + name,
                List.of(TrayBlock.DRY_DELAY_TICKS / 20.0F),
                TrayBlock.DRY_DELAY_TICKS
        );
    }
}
