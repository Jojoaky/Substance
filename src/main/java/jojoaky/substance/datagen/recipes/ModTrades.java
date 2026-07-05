package jojoaky.substance.datagen.recipes;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.datagen.generator.trade.WanderingTraderTradeDef;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import jojoaky.substance.datagen.generator.trade.PiglinBarterDef;
import jojoaky.substance.datagen.generator.trade.TradeRegistry;
import jojoaky.substance.datagen.generator.trade.VillagerTradeDef;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;

public class ModTrades {
    public static void register() {
        TradeRegistry.accept(
                WanderingTraderTradeDef.named("ephedra_seeds", 1)
                        .buys(Items.EMERALD, 5)
                        .sells(ModItems.EPHEDRA_SEEDS, 3)
                        .maxUses(8),

                VillagerTradeDef.named("farmer_ephedra_bundle", VillagerProfession.FARMER, 2)
                        .buys(Items.EMERALD, 12)
                        .sells(ModItems.EPHEDRA_BUNDLE, 1)
                        .xp(3)
                        .maxUses(12),

                VillagerTradeDef.named("farmer_tobacco_seeds", VillagerProfession.FARMER, 2)
                        .buys(Items.EMERALD, 4)
                        .sells(ModItems.TOBACCO_SEEDS, 3)
                        .xp(3)
                        .maxUses(12),

                PiglinBarterDef.named("piglin_herb_seeds")
                        .drops(ModItems.HERB_SEEDS)
                        .weight(40)
                        .count(2.0f, 4.0f),

                PiglinBarterDef.named("piglin_dried_herb_bud")
                        .drops(ModItems.DRIED_HERB_BUD)
                        .weight(5)
                        .count(2.0f, 3.0f),

                VillagerTradeDef.named("cleric_empty_flask", VillagerProfession.CLERIC, 1)
                        .buys(ModFlasks.EMPTY_FLASK, 4)
                        .sells(Items.EMERALD, 1)
                        .xp(2)
                        .maxUses(12),

                VillagerTradeDef.named("cleric_methanol_flask", VillagerProfession.CLERIC, 1)
                        .buys(Items.EMERALD, 4)
                        .secondaryCost(ModFlasks.EMPTY_FLASK, 4)
                        .sells(ModFluids.METHANOL_FLASK, 1)
                        .xp(2)
                        .maxUses(5),

                VillagerTradeDef.named("toolsmith_tray", VillagerProfession.TOOLSMITH, 2)
                        .buys(Items.EMERALD, 3)
                        .sells(ModItems.TRAY, 1)
                        .maxUses(6)
        );
    }
}