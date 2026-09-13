package jojoaky.substance.client.compat.rei;

import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class WorldInteractionReiDisplay extends BasicDisplay {
    private final WorldInteractionDisplay interaction;

    public WorldInteractionReiDisplay(WorldInteractionDisplay interaction) {
        super(inputs(interaction), outputs(interaction), Optional.of(interaction.id()));
        this.interaction = interaction;
    }

    public WorldInteractionDisplay interaction() {
        return interaction;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SubstanceReiPlugin.WORLD_INTERACTION;
    }

    private static List<EntryIngredient> inputs(WorldInteractionDisplay interaction) {
        List<EntryIngredient> entries = new ArrayList<>();
        interaction.leftInputs().stream().map(WorldInteractionReiDisplay::toEntry).forEach(entries::add);
        interaction.rightInputs().stream().map(WorldInteractionReiDisplay::toEntry).forEach(entries::add);
        return List.copyOf(entries);
    }

    private static List<EntryIngredient> outputs(WorldInteractionDisplay interaction) {
        return interaction.outputs().stream().map(EntryIngredients::of).toList();
    }

    public static EntryIngredient toEntry(WorldInteractionDisplay.InteractionInput input) {
        if (input instanceof WorldInteractionDisplay.ItemInput itemInput) {
            List<ItemStack> stacks = Arrays.stream(itemInput.ingredient().getItems())
                    .map(ItemStack::copy)
                    .peek(stack -> stack.setCount(itemInput.count()))
                    .toList();
            return EntryIngredients.ofItemStacks(stacks);
        } else if (input instanceof WorldInteractionDisplay.FluidInput fluid) {
            var entries = fluid.fluids().stream()
                    .flatMap(f -> EntryIngredients.of(f, fluid.amount()).stream())
                    .toList();
            return EntryIngredient.of(entries);
        }
        return EntryIngredient.empty();
    }
}