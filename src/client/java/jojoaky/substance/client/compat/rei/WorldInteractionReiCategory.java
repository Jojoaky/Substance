package jojoaky.substance.client.compat.rei;

import jojoaky.substance.register.ModItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public final class WorldInteractionReiCategory implements DisplayCategory<WorldInteractionReiDisplay> {
    private static final int WIDTH = 166;
    private static final int HEIGHT = 64;

    @Override
    public CategoryIdentifier<? extends WorldInteractionReiDisplay> getCategoryIdentifier() {
        return SubstanceReiPlugin.WORLD_INTERACTION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.substance.world_interaction");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.GAS_BOTTLE);
    }

    @Override
    public int getDisplayWidth(WorldInteractionReiDisplay display) {
        return WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public List<Widget> setupDisplay(WorldInteractionReiDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        int inputY = bounds.y + 7;
        for (int i = 0; i < display.getInputEntries().size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.x + 6 + i * 22, inputY))
                    .entries(display.getInputEntries().get(i))
                    .markInput());
        }

        widgets.add(Widgets.createArrow(new Point(bounds.x + 78, inputY + 1)));
        for (int i = 0; i < display.getOutputEntries().size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.x + 110 + i * 22, inputY))
                    .entries(display.getOutputEntries().get(i))
                    .markOutput());
        }

        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            var font = Minecraft.getInstance().font;

            List<FormattedCharSequence> lines =
                    font.split(display.interaction().instruction(), WIDTH - 10);

            for (int i = 0; i < Math.min(lines.size(), 2); i++) {
                FormattedCharSequence line = lines.get(i);

                int x = bounds.getCenterX() - font.width(line) / 2;
                int y = bounds.y + 39 + i * 10;

                graphics.drawString(font, line, x, y, 0xffffff, false);
            }
        }));
        return widgets;
    }
}
