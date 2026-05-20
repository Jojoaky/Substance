package jojoaky.substance.flask;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class FullFlaskFluidStorage implements SingleSlotStorage<FluidVariant> {
    private final ContainerItemContext context;
    private final ModFlasks.FlaskEntry entry;
    private final FluidVariant fluidVariant;

    public FullFlaskFluidStorage(ContainerItemContext context, ModFlasks.FlaskEntry entry) {
        this.context = context;
        this.entry = entry;
        this.fluidVariant = FluidVariant.of(entry.still());
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (!resource.equals(fluidVariant)) return 0;

        long capacity = FlaskItem.CAPACITY;
        if (maxAmount < capacity) return 0;

        long exchanged = context.exchange(ItemVariant.of(ModFlasks.EMPTY_FLASK), 1, transaction);
        if (exchanged == 1) {
            return capacity;
        }
        return 0;
    }

    @Override
    public boolean isResourceBlank() { return false; }

    @Override
    public FluidVariant getResource() { return fluidVariant; }

    @Override
    public long getAmount() { return FlaskItem.CAPACITY; }

    @Override
    public long getCapacity() { return FlaskItem.CAPACITY; }
}