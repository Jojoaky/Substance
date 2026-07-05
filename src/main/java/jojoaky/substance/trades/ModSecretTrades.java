package jojoaky.substance.trades;

import jojoaky.substance.generator.trade.TradeRegistry;
import jojoaky.substance.generator.trade.VillagerTradeDef;
import jojoaky.substance.register.ModItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;

import java.util.Arrays;

public class ModSecretTrades {
    private static final VillagerProfession[] whiteCrystalsProfessions = {
            VillagerProfession.FISHERMAN,
            VillagerProfession.SHEPHERD,
            VillagerProfession.CLERIC,
            VillagerProfession.ARMORER,
            VillagerProfession.WEAPONSMITH,
            VillagerProfession.TOOLSMITH,
            VillagerProfession.LEATHERWORKER,
    };

    private static final VillagerProfession[] chiliCrystalsProfessions = {
            VillagerProfession.FISHERMAN,
            VillagerProfession.FLETCHER,
            VillagerProfession.BUTCHER,
            VillagerProfession.MASON
    };

    private static final VillagerProfession[] blueCrystalsProfessions = {
            VillagerProfession.SHEPHERD,
            VillagerProfession.LIBRARIAN,
            VillagerProfession.CARTOGRAPHER,
            VillagerProfession.CLERIC,
            VillagerProfession.BUTCHER,
            VillagerProfession.LEATHERWORKER,
    };

    private static final VillagerProfession[] herbProfessions = {
            VillagerProfession.FARMER,
            VillagerProfession.TOOLSMITH,
    };

    private static final VillagerProfession[] cigaretteProfessions = {
            VillagerProfession.CLERIC,
            VillagerProfession.ARMORER,

    };

    private static final VillagerProfession[] pipeProfessions = {
            VillagerProfession.CARTOGRAPHER,
            VillagerProfession.BUTCHER,
            VillagerProfession.WEAPONSMITH,
    };

    public static void register() {
        TradeRegistry.register(
            Arrays.stream(whiteCrystalsProfessions)
                .map(profession -> VillagerTradeDef.profession(profession, 2)
                    .buys(ModItems.WHITE_CRYSTALS, 2)
                    .sells(Items.EMERALD, 14)
                    .maxUses(4)
                )
        );

        TradeRegistry.register(
            Arrays.stream(chiliCrystalsProfessions)
                .map(profession -> VillagerTradeDef.profession(profession, 2)
                    .buys(ModItems.WHITE_CRYSTALS_CHILI, 3)
                    .sells(Items.EMERALD, 24)
                    .maxUses(6)
                )
        );

        TradeRegistry.register(
            Arrays.stream(blueCrystalsProfessions)
                .map(profession -> VillagerTradeDef.profession(profession, 3)
                    .buys(ModItems.BLUE_CRYSTALS, 2)
                    .sells(Items.EMERALD, 36)
                    .maxUses(4)
                )
        );

        TradeRegistry.register(
                Arrays.stream(herbProfessions)
                        .map(profession -> VillagerTradeDef.profession(profession, 1)
                                .buys(ModItems.DRIED_HERB_BUD, 3)
                                .sells(Items.EMERALD, 8)
                                .maxUses(6)
                        )
        );

        TradeRegistry.register(
                Arrays.stream(cigaretteProfessions)
                        .map(profession -> VillagerTradeDef.profession(profession, 1)
                                .buys(ModItems.CIGARETTE, 12)
                                .sells(Items.EMERALD, 24)
                                .maxUses(2)
                        )
        );

        TradeRegistry.register(
                Arrays.stream(pipeProfessions)
                        .map(profession -> VillagerTradeDef.profession(profession, 1)
                                .buys(ModItems.PIPE, 1)
                                .sells(Items.EMERALD, 6)
                                .maxUses(2)
                        )
        );
    }
}