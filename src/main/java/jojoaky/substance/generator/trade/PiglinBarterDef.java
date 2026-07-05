package jojoaky.substance.generator.trade;

import jojoaky.substance.generator.trade.TradeRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class PiglinBarterDef implements TradeRegistry.TradeDef {
    private static final ResourceLocation PIGLIN_BARTERING_ID = new ResourceLocation("minecraft", "gameplay/piglin_bartering");

    private final ItemLike item;
    private int weight = 10;
    private float minCount = 1.0f;
    private float maxCount = 1.0f;

    private PiglinBarterDef(ItemLike item) {
        this.item = item;
    }

    public static PiglinBarterDef bartersFor(ItemLike item) {
        return new PiglinBarterDef(item);
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

    public LootPoolEntryContainer.Builder<?> toLootPoolEntry() {
        return LootItem.lootTableItem(this.item)
                .setWeight(this.weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(this.minCount, this.maxCount)));
    }

    @Override
    public void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && PIGLIN_BARTERING_ID.equals(id)) {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder.add(toLootPoolEntry()));
            }
        });
    }
}