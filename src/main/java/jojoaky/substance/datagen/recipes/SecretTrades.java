package jojoaky.substance.datagen.recipes;

import jojoaky.substance.datagen.generator.trade.TradeRegistry;
import jojoaky.substance.datagen.generator.trade.VillagerTradeDef;
import jojoaky.substance.register.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;

import java.util.Arrays;

public class SecretTrades {
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
        TradeRegistry.accept(
            Arrays.stream(whiteCrystalsProfessions)
                .map(profession -> VillagerTradeDef.named("white_crystals_" + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession).getPath(), profession, 2)
                    .buys(ModItems.WHITE_CRYSTALS, 2)
                    .sells(Items.EMERALD, 14)
                    .maxUses(4)
                )
        );

        TradeRegistry.accept(
            Arrays.stream(chiliCrystalsProfessions)
                .map(profession -> VillagerTradeDef.named("chili_crystals_" + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession).getPath(), profession, 2)
                    .buys(ModItems.WHITE_CRYSTALS_CHILI, 3)
                    .sells(Items.EMERALD, 24)
                    .maxUses(6)
                )
        );

        TradeRegistry.accept(
            Arrays.stream(blueCrystalsProfessions)
                .map(profession -> VillagerTradeDef.named("blue_crystals_" + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession).getPath(), profession, 3)
                    .buys(ModItems.BLUE_CRYSTALS, 2)
                    .sells(Items.EMERALD, 36)
                    .maxUses(4)
                )
        );

        TradeRegistry.accept(
                Arrays.stream(herbProfessions)
                        .map(profession -> VillagerTradeDef.named("herb_secret_" + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession).getPath(), profession, 1)
                                .buys(ModItems.DRIED_HERB_BUD, 3)
                                .sells(Items.EMERALD, 8)
                                .maxUses(6)
                        )
        );

        TradeRegistry.accept(
                Arrays.stream(cigaretteProfessions)
                        .map(profession -> VillagerTradeDef.named("cigarette_secret_" + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession).getPath(), profession, 1)
                                .buys(ModItems.CIGARETTE, 12)
                                .sells(Items.EMERALD, 24)
                                .maxUses(2)
                        )
        );

        TradeRegistry.accept(
                Arrays.stream(pipeProfessions)
                        .map(profession -> VillagerTradeDef.named("pipe_secret_" + BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession).getPath(), profession, 1)
                                .buys(ModItems.WOODEN_PIPE, 1)
                                .sells(Items.EMERALD, 6)
                                .maxUses(2)
                        )
        );
    }
}