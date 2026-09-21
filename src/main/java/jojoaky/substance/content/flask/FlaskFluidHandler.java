package jojoaky.substance.content.flask;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FlaskFluidHandler implements IFluidHandlerItem {
    private ItemStack container;
    private @Nullable ModFlasks.FlaskEntry entry;

    public FlaskFluidHandler(
            ItemStack container,
            @Nullable ModFlasks.FlaskEntry entry
    ) {
        this.container = container;
        this.entry = entry;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return container;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if (tank != 0 || entry == null) {
            return FluidStack.EMPTY;
        }

        return new FluidStack(entry.still(), FlaskItem.CAPACITY);
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank == 0 ? FlaskItem.CAPACITY : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if (tank != 0 || stack.isEmpty() || !stack.getComponents().isEmpty()) {
            return false;
        }

        if (entry == null) {
            return ModFlasks.getEntry(stack.getFluid()) != null;
        }

        return stack.is(entry.still());
    }

    @Override
    public int fill(@NotNull FluidStack resource, IFluidHandler.@NotNull FluidAction action) {
        if (entry != null) {
            return 0;
        }

        if (resource.isEmpty()
                || !resource.getComponents().isEmpty()
                || resource.getAmount() < FlaskItem.CAPACITY) {
            return 0;
        }

        ModFlasks.FlaskEntry target = ModFlasks.getEntry(resource.getFluid());

        if (target == null) {
            return 0;
        }

        if (container.getCount() != 1) {
            return 0;
        }

        if (action.execute()) {
            container = new ItemStack(target.flask().get());
            entry = target;
        }

        return FlaskItem.CAPACITY;
    }

    @Override
    public @NotNull FluidStack drain(
            @NotNull FluidStack resource,
            IFluidHandler.@NotNull FluidAction action
    ) {
        if (entry == null
                || resource.isEmpty()
                || !FluidStack.isSameFluidSameComponents(
                        getFluidInTank(0),
                        resource
                )
                || resource.getAmount() < FlaskItem.CAPACITY) {
            return FluidStack.EMPTY;
        }

        return drain(FlaskItem.CAPACITY, action);
    }

    @Override
    public @NotNull FluidStack drain(
            int maxDrain,
            IFluidHandler.@NotNull FluidAction action
    ) {
        if (entry == null || maxDrain < FlaskItem.CAPACITY) {
            return FluidStack.EMPTY;
        }

        if (container.getCount() != 1) {
            return FluidStack.EMPTY;
        }

        FluidStack drained =
                new FluidStack(entry.still(), FlaskItem.CAPACITY);

        if (action.execute()) {
            container = new ItemStack(ModFlasks.EMPTY_FLASK.get());
            entry = null;
        }

        return drained;
    }
}
