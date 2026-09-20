package jojoaky.substance.data.generator.datapatch.def;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class LootEntryDef {
    public enum Placement {
        INDEPENDENT_POOL,
        EXISTING_POOL
    }

    private String name;
    private ResourceLocation targetTable;
    private ItemLike item;
    private Placement placement;
    private float chance;
    private int poolIndex = -1;
    private int weight;
    private float minCount = 1.0f;
    private float maxCount = 1.0f;

    private LootEntryDef() {}

    public static LootEntryDef named(String name, ResourceLocation targetTable) {
        LootEntryDef def = new LootEntryDef();
        def.name = name;
        def.targetTable = targetTable;
        return def;
    }

    public LootEntryDef drops(ItemLike item) {
        this.item = item;
        return this;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getTargetTable() {
        return targetTable;
    }

    /**
     * Adds one independent roll to the target table. The item drops at most once
     * per table evaluation, regardless of how many pools the original table has.
     */
    public LootEntryDef chance(float chance) {
        if (chance < 0.0f || chance > 1.0f) {
            throw new IllegalArgumentException("Loot chance must be between 0 and 1");
        }
        selectPlacement(Placement.INDEPENDENT_POOL);
        this.chance = chance;
        return this;
    }

    /**
     * Adds a weighted choice to one specific existing pool. Use this for tables
     * such as piglin bartering, where the patched item must replace a vanilla
     * result instead of being an additional drop.
     */
    public LootEntryDef weightedInPool(int poolIndex, int weight) {
        if (poolIndex < 0) {
            throw new IllegalArgumentException("Loot pool index cannot be negative");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Loot weight must be positive");
        }
        selectPlacement(Placement.EXISTING_POOL);
        this.poolIndex = poolIndex;
        this.weight = weight;
        return this;
    }

    public LootEntryDef count(float min, float max) {
        if (min < 0.0f || max < min) {
            throw new IllegalArgumentException("Loot count range must be non-negative and ordered");
        }
        this.minCount = min;
        this.maxCount = max;
        return this;
    }

    private void selectPlacement(Placement placement) {
        if (this.placement != null && this.placement != placement) {
            throw new IllegalStateException("A loot patch cannot use more than one placement strategy");
        }
        this.placement = placement;
    }

    public ItemLike getItem() { return item; }
    public Placement getPlacement() {
        if (placement == null) {
            throw new IllegalStateException("Loot patch " + name + " has no placement strategy");
        }
        return placement;
    }
    public float getChance() { return chance; }
    public int getPoolIndex() { return poolIndex; }
    public int getWeight() { return weight; }
    public float getMinCount() { return minCount; }
    public float getMaxCount() { return maxCount; }
}
