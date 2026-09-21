package jojoaky.substance.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.phys.Vec3;

public final class DispenserHelper {

    private DispenserHelper() {
    }

    /**
     * Inserts as much of {@code stack} as possible into the dispenser,
     * first merging with existing stacks and then using empty slots.
     *
     * @return the part of the stack that could not be inserted
     */
    public static ItemStack insert(DispenserBlockEntity dispenser, ItemStack stack) {
        ItemStack remaining = stack.copy();

        // Merge into existing stacks first.
        for (int i = 0; i < dispenser.getContainerSize(); i++) {
            ItemStack existing = dispenser.getItem(i);

            if (!ItemStack.isSameItem(existing, remaining)) {
                continue;
            }

            int maxSize = Math.min(
                    existing.getMaxStackSize(),
                    dispenser.getMaxStackSize()
            );

            int amount = Math.min(
                    remaining.getCount(),
                    maxSize - existing.getCount()
            );

            if (amount <= 0) {
                continue;
            }

            existing.grow(amount);
            remaining.shrink(amount);

            if (remaining.isEmpty()) {
                dispenser.setChanged();
                return ItemStack.EMPTY;
            }
        }

        // Fill empty slots.
        for (int i = 0; i < dispenser.getContainerSize(); i++) {
            if (!dispenser.getItem(i).isEmpty()) {
                continue;
            }

            int amount = Math.min(
                    remaining.getCount(),
                    Math.min(remaining.getMaxStackSize(), dispenser.getMaxStackSize())
            );

            ItemStack inserted = remaining.copy();
            inserted.setCount(amount);

            dispenser.setItem(i, inserted);
            remaining.shrink(amount);

            if (remaining.isEmpty()) {
                dispenser.setChanged();
                return ItemStack.EMPTY;
            }
        }

        dispenser.setChanged();
        return remaining;
    }

    /**
     * Equivalent to ItemUtils.createFilledResult for dispensers:
     *
     * consumes one input item and either returns the result in the currently
     * dispensing slot, puts it back into the dispenser, or dispenses it if
     * there is no room.
     */
    private static final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

    public static ItemStack createFilledResult(
            BlockSource source,
            ItemStack emptyStack,
            ItemStack filledStack
    ) {
        emptyStack.shrink(1);

        if (emptyStack.isEmpty()) {
            return filledStack;
        }

        ItemStack remainder = insert(source.blockEntity(), filledStack);

        if (!remainder.isEmpty()) {
            defaultBehavior.dispense(source, remainder);
        }

        return emptyStack;
    }

    public static BlockPos getTargetPos(BlockSource source) {
        return source.pos().relative(
                source.state().getValue(DispenserBlock.FACING)
        );
    }

    public static Vec3 getFacePos(BlockSource source) {
        Direction facing = source.state().getValue(DispenserBlock.FACING);
        return Vec3.atCenterOf(source.pos()).add(
                facing.getStepX() * 0.5,
                facing.getStepY() * 0.5,
                facing.getStepZ() * 0.5
        );
    }

    public static Vec3 getDirection(BlockSource source) {
        Direction facing = source.state().getValue(DispenserBlock.FACING);
        return new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
    }

    public static InteractionResult placeBlock(BlockSource source, ItemStack stack) {
        Level level = source.level();
        Direction facing = source.state().getValue(DispenserBlock.FACING);
        BlockPos targetPos = DispenserHelper.getTargetPos(source);

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return InteractionResult.FAIL;
        }

        DirectionalPlaceContext context = new DirectionalPlaceContext(
                level,
                targetPos,
                facing,
                stack,
                Direction.UP
        );

        return blockItem.place(context);
    }
}