package jojoaky.substance.client.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import jojoaky.substance.compat.recipeviewer.ModWorldInteractions;
import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class SubstanceEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        ModWorldInteractions.all().stream()
                .map(SubstanceEmiPlugin::adapt)
                .forEach(registry::addRecipe);
    }

    private static EmiWorldInteractionRecipe adapt(WorldInteractionDisplay interaction) {
        EmiWorldInteractionRecipe.Builder builder = EmiWorldInteractionRecipe.builder()
                .id(syntheticId(interaction.id()))
                .supportsRecipeTree(false);

        boolean first = true;
        for (WorldInteractionDisplay.InteractionInput input : interaction.leftInputs()) {
            if (first) {
                builder.leftInput(toEmi(input), slot -> slot.appendTooltip(interaction.instruction()));
                first = false;
            } else {
                builder.leftInput(toEmi(input));
            }
        }

        for (WorldInteractionDisplay.InteractionInput input : interaction.rightInputs()) {
            builder.rightInput(toEmi(input), input.isCatalyst());
        }

        for (var output : interaction.outputs()) {
            builder.output(EmiStack.of(output));
        }

        return builder.build();
    }

    private static ResourceLocation syntheticId(ResourceLocation id) {
        String path = id.getPath();
        return path.startsWith("/") ? id : new ResourceLocation(id.getNamespace(), "/" + path);
    }

    private static EmiIngredient toEmi(WorldInteractionDisplay.InteractionInput input) {
        if (input instanceof WorldInteractionDisplay.ItemInput item) {
            return EmiIngredient.of(item.ingredient(), item.count());
        } else if (input instanceof WorldInteractionDisplay.FluidInput fluid) {
            if (fluid.fluids().size() == 1) {
                return EmiStack.of(fluid.fluids().get(0), fluid.amount());
            }
            List<EmiIngredient> stacks = fluid.fluids().stream()
                    .map(f -> (EmiIngredient) EmiStack.of(f, fluid.amount()))
                    .toList();
            return EmiIngredient.of(stacks);
        }
        throw new IllegalArgumentException("Unsupported interaction input type: " + input.getClass());
    }
}