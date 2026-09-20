package jojoaky.substance.client.compat.jei;

import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import jojoaky.substance.register.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class WorldInteractionJeiCategory implements IRecipeCategory<WorldInteractionDisplay> {
    private static final int WIDTH = 166;
    private static final int HEIGHT = 64;

    private static final int SLOT_SIZE = 18;
    private static final int SLOT_PITCH = 20;          // distance between successive slot starts
    private static final int PLUS_WIDTH = 17;           // width allocated for the plus section
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;
    private static final int ARROW_GAP = 6;             // gap between slots and the arrow
    private static final int OUTPUT_BG_OFFSET = 4;      // JEI single output slot extends 4px left (26x26 box)
    private static final int OUTPUT_PITCH = 18;

    private static final int ICON_TOP = 7;

    private final IDrawable icon;
    private final IDrawable arrow;

    public WorldInteractionJeiCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(ModItems.GAS_BOTTLE);
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public @NotNull RecipeType<WorldInteractionDisplay> getRecipeType() {
        return SubstanceJeiPlugin.WORLD_INTERACTION;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("category.substance.world_interaction");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, WorldInteractionDisplay interaction, @NotNull IFocusGroup focuses) {
        Layout layout = computeLayout(interaction);

        List<WorldInteractionDisplay.InteractionInput> leftInputs = interaction.leftInputs();
        for (int i = 0; i < leftInputs.size(); i++) {
            WorldInteractionDisplay.InteractionInput input = leftInputs.get(i);
            RecipeIngredientRole role = input.isCatalyst() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT;
            IRecipeSlotBuilder slot = builder.addSlot(role, layout.leftX()[i], ICON_TOP)
                    .setStandardSlotBackground();
            addInput(slot, input);
        }

        List<WorldInteractionDisplay.InteractionInput> rightInputs = interaction.rightInputs();
        for (int i = 0; i < rightInputs.size(); i++) {
            WorldInteractionDisplay.InteractionInput input = rightInputs.get(i);
            RecipeIngredientRole role = input.isCatalyst() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT;
            IRecipeSlotBuilder slot = builder.addSlot(role, layout.rightX()[i], ICON_TOP)
                    .setStandardSlotBackground();
            addInput(slot, input);
        }

        List<ItemStack> outputs = interaction.outputs();
        for (int i = 0; i < outputs.size(); i++) {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.OUTPUT, layout.outputX()[i], ICON_TOP);

            if (layout.singleOutput()) {
                slot.setOutputSlotBackground();
            } else {
                slot.setStandardSlotBackground();
            }
            slot.addItemStack(outputs.get(i));
        }
    }

    @Override
    public void draw(WorldInteractionDisplay interaction, @NotNull IRecipeSlotsView slots, @NotNull GuiGraphics graphics,
                     double mouseX, double mouseY) {
        final var font = Minecraft.getInstance().font;
        Layout layout = computeLayout(interaction);

        if (layout.plusX() >= 0) {
            int plusHeight = font.lineHeight;
            int plusY = ICON_TOP + (SLOT_SIZE - plusHeight) / 2;
            int plusX = layout.plusX() + (PLUS_WIDTH - font.width("+")) / 2;
            graphics.drawString(font, "+", plusX, plusY, 0xffffff, false);
        }

        int arrowY = ICON_TOP + (SLOT_SIZE - ARROW_HEIGHT) / 2;
        arrow.draw(graphics, layout.arrowX(), arrowY);

        List<FormattedCharSequence> lines = font.split(interaction.instruction(), WIDTH - 10);
        int maxLines = Math.min(lines.size(), 2);

        int textBlockHeight = maxLines * font.lineHeight;
        int iconsBottom = ICON_TOP + SLOT_SIZE;
        int availableSpace = HEIGHT - iconsBottom;
        int startY = iconsBottom + Math.max(2, (availableSpace - textBlockHeight) / 2);

        for (int i = 0; i < maxLines; i++) {
            FormattedCharSequence line = lines.get(i);
            int x = (WIDTH - font.width(line)) / 2;
            int y = startY + i * font.lineHeight;
            graphics.drawString(font, line, x, y, 0xffffff, false);
        }
    }

    @Override
    public ResourceLocation getRegistryName(WorldInteractionDisplay interaction) {
        return interaction.id();
    }

    private static List<ItemStack> getItemStacks(WorldInteractionDisplay.ItemInput itemInput) {
        return Arrays.stream(itemInput.ingredient().getItems())
                .map(ItemStack::copy)
                .peek(stack -> stack.setCount(itemInput.count()))
                .toList();
    }

    private static void addInput(IRecipeSlotBuilder slot, WorldInteractionDisplay.InteractionInput input) {
        if (input instanceof WorldInteractionDisplay.ItemInput itemInput) {
            slot.addItemStacks(getItemStacks(itemInput));
        } else if (input instanceof WorldInteractionDisplay.FluidInput fluidInput) {
            for (var fluid : fluidInput.fluids()) {
                slot.addFluidStack(fluid, fluidInput.amount());
            }
        }
    }
}