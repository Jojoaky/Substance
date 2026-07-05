package jojoaky.substance.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ItemContainer extends SimpleContainer {
    private final ItemStack stack;

    public ItemContainer(ItemStack stack, int size) {
        super(size);
        this.stack = stack;
        this.readFromNbt();
    }

    public void readFromNbt() {
        CompoundTag tag = this.stack.getOrCreateTag();
        if (tag.contains("Inventory", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("Inventory", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag itemTag = listTag.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < this.getContainerSize()) {
                    this.setItem(slot, ItemStack.of(itemTag));
                }
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.writeToNbt();
    }

    public void writeToNbt() {
        CompoundTag tag = this.stack.getOrCreateTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack itemStack = this.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                itemStack.save(itemTag);
                listTag.add(itemTag);
            }
        }
        tag.put("Inventory", listTag);
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }
}