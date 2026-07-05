package jojoaky.substance.trades;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.generator.trade.WanderingTraderTradeDef;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import jojoaky.substance.generator.trade.PiglinBarterDef;
import jojoaky.substance.generator.trade.TradeRegistry;
import jojoaky.substance.generator.trade.VillagerTradeDef;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;

public class ModTrades {
    public static void register() {
        TradeRegistry.register(
                WanderingTraderTradeDef.pool(1)
                        .buys(Items.EMERALD, 5)
                        .sells(ModItems.EPHEDRA_SEEDS, 3)
                        .maxUses(8),

                VillagerTradeDef.profession(VillagerProfession.FARMER, 2)
                        .buys(Items.EMERALD, 12)
                        .sells(ModItems.EPHEDRA_BUNDLE, 1)
                        .xp(3)
                        .maxUses(12),

                VillagerTradeDef.profession(VillagerProfession.FARMER, 2)
                        .buys(Items.EMERALD, 4)
                        .sells(ModItems.TOBACCO_SEEDS, 3)
                        .xp(3)
                        .maxUses(12),

                PiglinBarterDef.bartersFor(ModItems.HERB_SEEDS)
                        .weight(20)
                        .count(1.0f, 3.0f),

                PiglinBarterDef.bartersFor(ModItems.RIPE_TOBACCO_LEAF)
                        .weight(5)
                        .count(1.0f, 3.0f),

                VillagerTradeDef.profession(VillagerProfession.CLERIC, 1)
                        .buys(ModFlasks.EMPTY_FLASK, 4)
                        .sells(Items.EMERALD, 1)
                        .xp(2)
                        .maxUses(12),

                VillagerTradeDef.profession(VillagerProfession.CLERIC, 1)
                        .buys(Items.EMERALD, 4)
                        .buys(ModFlasks.EMPTY_FLASK, 4)
                        .sells(ModFluids.METHANOL_FLASK, 1)
                        .xp(2)
                        .maxUses(5),

                VillagerTradeDef.profession(VillagerProfession.TOOLSMITH, 2)
                        .buys(Items.EMERALD, 3)
                        .sells(ModItems.TRAY, 1)
                        .maxUses(6)
        );
    }
}