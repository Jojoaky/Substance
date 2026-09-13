package jojoaky.substance.client.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import jojoaky.substance.compat.recipeviewer.ModWorldInteractions;
import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import net.minecraft.resources.ResourceLocation;

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
                .leftInput(toEmi(interaction.inputs().get(0)),
                        slot -> slot.appendTooltip(interaction.instruction()))
                .supportsRecipeTree(false);

        interaction.inputs().stream().skip(1)
                .forEach(input -> builder.rightInput(toEmi(input), false));
        interaction.catalysts()
                .forEach(catalyst -> builder.rightInput(toEmi(catalyst), true));
        interaction.outputs()
                .forEach(output -> builder.output(EmiStack.of(output)));
        return builder.build();
    }

    private static ResourceLocation syntheticId(ResourceLocation id) {
        String path = id.getPath();
        return path.startsWith("/") ? id : new ResourceLocation(id.getNamespace(), "/" + path);
    }

    private static EmiIngredient toEmi(WorldInteractionDisplay.SizedIngredient ingredient) {
        return EmiIngredient.of(ingredient.ingredient(), ingredient.amount());
    }
}
