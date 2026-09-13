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
        interaction.inputs().stream().map(WorldInteractionReiDisplay::toEntry).forEach(entries::add);
        interaction.catalysts().stream().map(WorldInteractionReiDisplay::toEntry).forEach(entries::add);
        return List.copyOf(entries);
    }

    private static List<EntryIngredient> outputs(WorldInteractionDisplay interaction) {
        return interaction.outputs().stream().map(EntryIngredients::of).toList();
    }

    private static EntryIngredient toEntry(WorldInteractionDisplay.SizedIngredient sized) {
        List<ItemStack> stacks = Arrays.stream(sized.ingredient().getItems())
                .map(ItemStack::copy)
                .peek(stack -> stack.setCount(sized.amount()))
                .toList();
        return EntryIngredients.ofItemStacks(stacks);
    }
}
