package jojoaky.substance.client.compat.rei;

import jojoaky.substance.Substance;
import jojoaky.substance.compat.recipeviewer.ModWorldInteractions;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public final class SubstanceReiPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<WorldInteractionReiDisplay> WORLD_INTERACTION =
            CategoryIdentifier.of(Substance.MOD_ID, "world_interaction");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new WorldInteractionReiCategory(),
                configuration -> configuration.setPlusButtonArea(bounds -> null));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ModWorldInteractions.all().stream()
                .map(WorldInteractionReiDisplay::new)
                .forEach(registry::add);
    }
}
