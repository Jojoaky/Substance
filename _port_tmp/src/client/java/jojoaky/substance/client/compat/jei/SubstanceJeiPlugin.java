package jojoaky.substance.client.compat.jei;

import jojoaky.substance.Substance;
import jojoaky.substance.compat.recipeviewer.ModWorldInteractions;
import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class SubstanceJeiPlugin implements IModPlugin {
    public static final RecipeType<WorldInteractionDisplay> WORLD_INTERACTION = RecipeType.create(
            Substance.MOD_ID,
            "world_interaction",
            WorldInteractionDisplay.class
    );

    @Override
    public ResourceLocation getPluginUid() {
        return Substance.resource("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WorldInteractionJeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(WORLD_INTERACTION, ModWorldInteractions.all());
    }
}
