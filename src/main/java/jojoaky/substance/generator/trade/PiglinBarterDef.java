package jojoaky.substance.generator.trade;

import jojoaky.substance.generator.trade.TradeRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class PiglinBarterDef {
    private String name;
    private ItemLike item;
    private int weight = 10;
    private float minCount = 1.0f;
    private float maxCount = 1.0f;

    private PiglinBarterDef() {}

    public static PiglinBarterDef named(String name) {
        PiglinBarterDef def = new PiglinBarterDef();
        def.name = name;
        return def;
    }

    public PiglinBarterDef drops(ItemLike item) {
        this.item = item;
        return this;
    }

    public String getName() {
        return name;
    }

    public PiglinBarterDef weight(int weight) {
        this.weight = weight;
        return this;
    }

    public PiglinBarterDef count(float min, float max) {
        this.minCount = min;
        this.maxCount = max;
        return this;
    }

    public ItemLike getItem() { return item; }
    public int getWeight() { return weight; }
    public float getMinCount() { return minCount; }
    public float getMaxCount() { return maxCount; }
}