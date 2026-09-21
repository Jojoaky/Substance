package jojoaky.substance.content;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class ItemContainer extends SimpleContainer {
    private final ItemStack stack;

    public ItemContainer(ItemStack stack, int size) {
        super(size);
        this.stack = stack;

        ItemContainerContents contents = stack.getOrDefault(
                DataComponents.CONTAINER,
                ItemContainerContents.EMPTY
        );

        contents.copyInto(this.getItems());
    }

    @Override
    public void setChanged() {
        super.setChanged();

        this.stack.set(
                DataComponents.CONTAINER,
                ItemContainerContents.fromItems(this.getItems())
        );
    }

    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }
}