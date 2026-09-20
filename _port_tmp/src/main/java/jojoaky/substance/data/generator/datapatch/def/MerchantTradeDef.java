package jojoaky.substance.data.generator.datapatch.def;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public abstract class MerchantTradeDef<T extends MerchantTradeDef<T>> {
    protected String name;
    protected ItemStack costA = ItemStack.EMPTY;
    protected ItemStack costB = ItemStack.EMPTY;
    protected ItemStack result = ItemStack.EMPTY;
    protected int maxUses = 12;
    protected int villagerXp = 2;
    protected float priceMultiplier = 0.05f;

    public static String toPathSafe(String name) {
        return name.toLowerCase().replace(" ", "_");
    }

    public T named(String name) {
        this.name = name;
        return self();
    }

    public String getName() {
        return name;
    }

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

    public ItemStack getCostA() { return costA; }
    public ItemStack getCostB() { return costB; }
    public ItemStack getResult() { return result; }
    public int getMaxUses() { return maxUses; }
    public int getVillagerXp() { return villagerXp; }
    public float getPriceMultiplier() { return priceMultiplier; }
}