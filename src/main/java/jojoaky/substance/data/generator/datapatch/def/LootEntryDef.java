package jojoaky.substance.data.generator.datapatch.def;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class LootEntryDef {
    private String name;
    private ResourceLocation targetTable; // Added field
    private ItemLike item;
    private int weight = 10;
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

    public LootEntryDef weight(int weight) {
        this.weight = weight;
        return this;
    }

    public LootEntryDef count(float min, float max) {
        this.minCount = min;
        this.maxCount = max;
        return this;
    }

    public ItemLike getItem() { return item; }
    public int getWeight() { return weight; }
    public float getMinCount() { return minCount; }
    public float getMaxCount() { return maxCount; }
}