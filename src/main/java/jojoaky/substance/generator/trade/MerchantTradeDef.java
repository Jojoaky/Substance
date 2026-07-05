package jojoaky.substance.generator.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

public abstract class MerchantTradeDef<T extends MerchantTradeDef<T>> implements TradeRegistry.TradeDef {
    protected ItemStack costA = ItemStack.EMPTY;
    protected ItemStack costB = ItemStack.EMPTY;
    protected ItemStack result = ItemStack.EMPTY;
    protected int maxUses = 12;
    protected int villagerXp = 2;
    protected float priceMultiplier = 0.05f;

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }

    public T buys(ItemLike item, int count) {
        this.costA = new ItemStack(item, count);
        return self();
    }

    public T secondaryCost(ItemLike item, int count) {
        this.costB = new ItemStack(item, count);
        return self();
    }

    public T sells(ItemLike item, int count) {
        this.result = new ItemStack(item, count);
        return self();
    }

    public T maxUses(int maxUses) {
        this.maxUses = maxUses;
        return self();
    }

    public T xp(int xp) {
        this.villagerXp = xp;
        return self();
    }

    public T priceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
        return self();
    }

    public VillagerTrades.ItemListing toItemListing() {
        return (Entity entity, RandomSource random) -> new MerchantOffer(
                this.costA, this.costB, this.result, this.maxUses, this.villagerXp, this.priceMultiplier
        );
    }
}