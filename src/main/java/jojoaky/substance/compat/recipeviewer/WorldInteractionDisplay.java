package jojoaky.substance.compat.recipeviewer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Viewer-neutral description of an interaction performed directly in the world.
 * Recipe-viewer integrations should adapt this data instead of defining their own copies.
 */
public record WorldInteractionDisplay(
        ResourceLocation id,
        List<SizedIngredient> inputs,
        List<SizedIngredient> catalysts,
        List<ItemStack> outputs,
        String instructionKey,
        List<Object> instructionArguments,
        int durationTicks
) {
    public WorldInteractionDisplay {
        if (id == null) throw new IllegalArgumentException("Interaction id cannot be null");
        inputs = List.copyOf(inputs);
        catalysts = List.copyOf(catalysts);
        outputs = outputs.stream().map(ItemStack::copy).toList();
        instructionArguments = List.copyOf(instructionArguments);
        if (inputs.isEmpty()) throw new IllegalArgumentException(id + " must have at least one input");
        if (outputs.isEmpty()) throw new IllegalArgumentException(id + " must have at least one output");
        if (instructionKey == null || instructionKey.isBlank()) {
            throw new IllegalArgumentException(id + " must have an instruction translation key");
        }
        if (durationTicks < 0) throw new IllegalArgumentException(id + " has a negative duration");
    }

    public Component instruction() {
        return Component.translatable(instructionKey, instructionArguments.toArray());
    }

    @Override
    public @NotNull List<ItemStack> outputs() {
        return outputs.stream().map(ItemStack::copy).toList();
    }

    public record SizedIngredient(Ingredient ingredient, int amount) {
        public SizedIngredient {
            if (ingredient == null || ingredient.isEmpty()) {
                throw new IllegalArgumentException("Interaction ingredient cannot be empty");
            }
            if (amount < 1) throw new IllegalArgumentException("Interaction ingredient amount must be positive");
        }

        public static SizedIngredient of(Ingredient ingredient) {
            return new SizedIngredient(ingredient, 1);
        }
    }
}
