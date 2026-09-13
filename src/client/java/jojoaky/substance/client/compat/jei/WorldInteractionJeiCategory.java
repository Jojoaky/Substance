package jojoaky.substance.client.compat.jei;

import jojoaky.substance.compat.recipeviewer.WorldInteractionDisplay;
import jojoaky.substance.register.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class WorldInteractionJeiCategory implements IRecipeCategory<WorldInteractionDisplay> {
    private static final int WIDTH = 166;
    private static final int HEIGHT = 64;

    private final IDrawable icon;
    private final IDrawable arrow;

    public WorldInteractionJeiCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(ModItems.GAS_BOTTLE);
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public RecipeType<WorldInteractionDisplay> getRecipeType() {
        return SubstanceJeiPlugin.WORLD_INTERACTION;
    }

    @Override
    public Component getTitle() {
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

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WorldInteractionDisplay interaction, IFocusGroup focuses) {
        int index = 0;

        // Inputs
        for (WorldInteractionDisplay.SizedIngredient input : interaction.inputs()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 6 + index++ * 20, 7)
                    .setStandardSlotBackground()
                    .addItemStacks(stacks(input));
        }

        // Catalysts
        for (WorldInteractionDisplay.SizedIngredient catalyst : interaction.catalysts()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 6 + index++ * 20, 7)
                    .setStandardSlotBackground()
                    .addItemStacks(stacks(catalyst));
        }

        // Outputs
        List<ItemStack> outputs = interaction.outputs();
        boolean singleOutput = outputs.size() == 1;

        for (int i = 0; i < outputs.size(); i++) {
            int xPos = singleOutput ? 114 : 108 + (i * 18);

            var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, 7)
                    .addItemStack(outputs.get(i));

            if (singleOutput) {
                slot.setOutputSlotBackground();
            } else {
                slot.setStandardSlotBackground();
            }
        }
    }

    @Override
    public void draw(WorldInteractionDisplay interaction, IRecipeSlotsView slots, GuiGraphics graphics,
                     double mouseX, double mouseY) {
        final var font = Minecraft.getInstance().font;

        arrow.draw(graphics, 78, 8);
        List<FormattedCharSequence> lines = Minecraft.getInstance().font
                .split(interaction.instruction(), WIDTH - 10);

        for (int i = 0; i < Math.min(lines.size(), 2); i++) {
            FormattedCharSequence line = lines.get(i);

            int x = (WIDTH - font.width(line)) / 2;
            int y = 39 + i * 10;

            graphics.drawString(font, line, x, y, 0xffffff, false);
        }
    }

    @Override
    public net.minecraft.resources.ResourceLocation getRegistryName(WorldInteractionDisplay interaction) {
        return interaction.id();
    }

    private static List<ItemStack> stacks(WorldInteractionDisplay.SizedIngredient sized) {
        return Arrays.stream(sized.ingredient().getItems())
                .map(ItemStack::copy)
                .peek(stack -> stack.setCount(sized.amount()))
                .toList();
    }
}
