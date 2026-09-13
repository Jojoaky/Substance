package jojoaky.substance.client.compat.rei;

import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import jojoaky.substance.register.ModItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class WorldInteractionReiCategory implements DisplayCategory<WorldInteractionReiDisplay> {
    private static final int WIDTH = 166;
    private static final int HEIGHT = 64;

    private static final int SLOT_SIZE = 18;
    private static final int SLOT_PITCH = 20;          // distance between successive slot starts
    private static final int PLUS_WIDTH = 17;           // width allocated for the plus section
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;
    private static final int ARROW_GAP = 6;             // gap between slots and the arrow
    private static final int OUTPUT_BG_OFFSET = 4;      // single output slot extends 4px left
    private static final int OUTPUT_PITCH = 18;

    private static final int ICON_TOP = 10;

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

    private static Layout computeLayout(WorldInteractionDisplay interaction) {
        int leftCount = interaction.leftInputs().size();
        int rightCount = interaction.rightInputs().size();
        int outputCount = interaction.outputs().size();
        boolean showPlus = leftCount > 0 && rightCount > 0;

        int leftBlockWidth = leftCount == 0 ? 0 : (leftCount - 1) * SLOT_PITCH + SLOT_SIZE;
        int plusBlockWidth = showPlus ? PLUS_WIDTH : 0;
        int rightBlockWidth = rightCount == 0 ? 0 : (rightCount - 1) * SLOT_PITCH + SLOT_SIZE;
        int inputsWidth = leftBlockWidth + plusBlockWidth + rightBlockWidth;

        boolean singleOutput = outputCount == 1;
        int outputsWidth = outputCount == 0 ? 0
                : singleOutput ? SLOT_SIZE
                : (outputCount - 1) * OUTPUT_PITCH + SLOT_SIZE;

        int outputMarginLeft = singleOutput ? OUTPUT_BG_OFFSET : 0;

        int totalWidth = inputsWidth + ARROW_GAP + ARROW_WIDTH + ARROW_GAP + outputMarginLeft + outputsWidth;
        int startX = Math.max(0, (WIDTH - totalWidth) / 2);

        int cursor = startX;
        int[] leftX = new int[leftCount];
        for (int i = 0; i < leftCount; i++) {
            leftX[i] = cursor;
            cursor += SLOT_PITCH;
        }
        if (leftCount > 0) {
            cursor = leftX[leftCount - 1] + SLOT_SIZE;
        }

        int plusX = -1;
        if (showPlus) {
            plusX = cursor;
            cursor += PLUS_WIDTH;
        }

        int[] rightX = new int[rightCount];
        for (int i = 0; i < rightCount; i++) {
            rightX[i] = cursor;
            cursor += SLOT_PITCH;
        }
        if (rightCount > 0) {
            cursor = rightX[rightCount - 1] + SLOT_SIZE;
        }

        int arrowX = cursor + ARROW_GAP;
        int outputStartX = arrowX + ARROW_WIDTH + ARROW_GAP + outputMarginLeft;

        int[] outputX = new int[outputCount];
        for (int i = 0; i < outputCount; i++) {
            outputX[i] = outputStartX + (singleOutput ? 0 : i * OUTPUT_PITCH);
        }

        return new Layout(leftX, rightX, outputX, plusX, arrowX, singleOutput);
    }

    private record Layout(int[] leftX, int[] rightX, int[] outputX, int plusX, int arrowX, boolean singleOutput) {
    }

    @Override
        public List<Widget> setupDisplay(WorldInteractionReiDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        WorldInteractionDisplay interaction = display.interaction();
        Layout layout = computeLayout(interaction);
        int iconY = bounds.y + ICON_TOP;

        List<WorldInteractionDisplay.InteractionInput> leftInputs = interaction.leftInputs();
        for (int i = 0; i < leftInputs.size(); i++) {
            WorldInteractionDisplay.InteractionInput input = leftInputs.get(i);
            widgets.add(Widgets.createSlot(new Point(bounds.x + layout.leftX()[i], iconY))
                    .entries(WorldInteractionReiDisplay.toEntry(input))
                    .markInput());
        }

        List<WorldInteractionDisplay.InteractionInput> rightInputs = interaction.rightInputs();
        for (int i = 0; i < rightInputs.size(); i++) {
            WorldInteractionDisplay.InteractionInput input = rightInputs.get(i);
            widgets.add(Widgets.createSlot(new Point(bounds.x + layout.rightX()[i], iconY))
                    .entries(WorldInteractionReiDisplay.toEntry(input))
                    .markInput());
        }

        int arrowY = iconY + (SLOT_SIZE - ARROW_HEIGHT) / 2;
        widgets.add(Widgets.createArrow(new Point(bounds.x + layout.arrowX(), arrowY)));

        List<ItemStack> outputs = interaction.outputs();
        for (int i = 0; i < outputs.size(); i++) {
            int slotX = bounds.x + layout.outputX()[i];

            if (layout.singleOutput()) {
                widgets.add(Widgets.createResultSlotBackground(new Point(slotX, iconY)));
            }

            var slotWidget = Widgets.createSlot(new Point(slotX, iconY))
                    .entries(EntryIngredients.of(outputs.get(i)))
                    .markOutput();

            if (layout.singleOutput()) {
                slotWidget.disableBackground();
            }

            widgets.add(slotWidget);
        }

        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            var font = Minecraft.getInstance().font;

            if (layout.plusX() >= 0) {
                int plusHeight = font.lineHeight;
                int plusY = iconY + (SLOT_SIZE - plusHeight) / 2;
                int plusX = bounds.x + layout.plusX() + (PLUS_WIDTH - font.width("+")) / 2;
                graphics.drawString(font, "+", plusX, plusY, 0xffffff, false);
            }

            List<FormattedCharSequence> lines = font.split(interaction.instruction(), WIDTH - 10);
            int maxLines = Math.min(lines.size(), 2);

            int textBlockHeight = maxLines * font.lineHeight;
            int iconsBottom = ICON_TOP + SLOT_SIZE;
            int availableSpace = HEIGHT - iconsBottom;
            int startY = bounds.y + iconsBottom + Math.max(2, (availableSpace - textBlockHeight) / 2);

            for (int i = 0; i < maxLines; i++) {
                FormattedCharSequence line = lines.get(i);
                int x = bounds.x + (WIDTH - font.width(line)) / 2;
                int y = startY + i * font.lineHeight;
                graphics.drawString(font, line, x, y, 0xffffff, false);
            }
        }));

        return widgets;
    }
}