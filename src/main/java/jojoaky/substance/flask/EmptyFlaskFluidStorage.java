package jojoaky.substance.flask;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import static jojoaky.substance.flask.ModFlasks.ALL_FLASK_ENTRIES;

public class EmptyFlaskFluidStorage implements SingleSlotStorage<FluidVariant> {
    private final ContainerItemContext context;

    public EmptyFlaskFluidStorage(ContainerItemContext context) {
        this.context = context;
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long capacity = FluidConstants.BOTTLE;
        if (maxAmount < capacity) return 0;

        ModFlasks.FlaskEntry targetEntry = null;
        for (ModFlasks.FlaskEntry entry : ALL_FLASK_ENTRIES) {
            if (entry.still() == resource.getFluid()) {
                targetEntry = entry;
                break;
            }
        }

        if (targetEntry == null) return 0;

        long exchanged = context.exchange(ItemVariant.of(targetEntry.flask()), 1, transaction);
        if (exchanged == 1) {
            return capacity;
        }
        return 0;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public boolean isResourceBlank() { return true; }

    @Override
    public FluidVariant getResource() { return FluidVariant.blank(); }

    @Override
    public long getAmount() { return 0; }

    @Override
    public long getCapacity() { return FluidConstants.BOTTLE; }
}