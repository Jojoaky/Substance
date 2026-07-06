package jojoaky.substance.content.pipe;

import jojoaky.substance.Substance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PipeMenu extends AbstractContainerMenu {
    private final Container container;
    private final InteractionHand hand;

    public PipeMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, new SimpleContainer(PipeItem.PIPE_SIZE), buf.readEnum(InteractionHand.class));
    }

    public boolean canContainStack(@NotNull ItemStack stack) {
        return PipeRegistry.isPipeSmokableItem(stack.getItem());
    }

    public PipeMenu(int syncId, Inventory playerInventory, Container container, InteractionHand hand) {
        super(Substance.PIPE_MENU, syncId);
        this.container = container;
        this.hand = hand;

        checkContainerSize(container, PipeItem.PIPE_SIZE);
        container.startOpen(playerInventory.player);

        // Pipe Container
        for (int i = 0; i < PipeItem.PIPE_SIZE; i++) {
            this.addSlot(new Slot(container, i, 44 + i * 18, 20) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return canContainStack(stack);
                }
            });
        }

        // Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 51 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 109) {
                @Override
                public boolean mayPickup(@NotNull Player player) {
                    return !ItemStack.matches(this.getItem(), player.getItemInHand(hand));
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(this.hand).getItem() instanceof PipeItem;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemStack = sourceStack.copy();

            if (ItemStack.matches(sourceStack, player.getItemInHand(this.hand))) {
                return ItemStack.EMPTY;
            }

            if (index < PipeItem.PIPE_SIZE) {
                if (!this.moveItemStackTo(sourceStack, PipeItem.PIPE_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!canContainStack(sourceStack)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(sourceStack, 0, PipeItem.PIPE_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (sourceStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (sourceStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, sourceStack);
        }
        return itemStack;
    }
}