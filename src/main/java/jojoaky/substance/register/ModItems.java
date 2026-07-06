package jojoaky.substance.register;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import jojoaky.substance.content.consumable.*;
import jojoaky.substance.content.gas_bottle.EmptyGasBottleItem;
import jojoaky.substance.content.pipe.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ModItems {
    public static Item register(Item item, String id) {
        ResourceLocation itemID = new ResourceLocation(Substance.MOD_ID, id);
        return Registry.register(BuiltInRegistries.ITEM, itemID, item);
    }

    // --- consumables ---
    public static final Item WHITE_CRYSTALS = register(
            new CrystalsItem(new FabricItemSettings()
                    .rarity(Rarity.UNCOMMON)
            ),
            "white_crystals"
    );

    public static final Item WHITE_CRYSTALS_CHILI = register(
            new CrystalsItem(new FabricItemSettings()
                    .rarity(Rarity.RARE)
            ),
            "white_crystals_chili"
    );

    public static final Item BLUE_CRYSTALS = register(
            new CrystalsItem(new FabricItemSettings()
                    .rarity(Rarity.EPIC)
            ),
            "blue_crystals"
    );

    public static final Item HERBAL_ROLL = register(
            new JointItem(new FabricItemSettings()
                    .durability(Config.get().herbalRollDurability)
                    .rarity(Rarity.UNCOMMON)
            ),
            "herbal_roll"
    );

    public static final Item THICK_HERBAL_ROLL = register(
            new ThickJointItem(new FabricItemSettings()
                    .durability(Config.get().thickHerbalRollDurability)
                    .rarity(Rarity.RARE)
            ),
            "thick_herbal_roll"
    );

    public static final Item WOODEN_PIPE = register(
            new PipeItem(new FabricItemSettings()
                    .stacksTo(1)
                    .durability(Config.get().woodenPipeDurability)
                    .rarity(Rarity.RARE)
            ),
            "wooden_pipe"
    );

    public static final Item BUBBLE_PIPE = register(
            new PipeItem(new FabricItemSettings()
                    .stacksTo(1)
                    .durability(Config.get().bubblePipeDurability)
                    .rarity(Rarity.RARE)
            ),
            "bubble_pipe"
    );

    public static final Item CIGARETTE = register(
            new CigaretteItem(new FabricItemSettings()
                    .stacksTo(1)
                    .durability(Config.get().cigaretteDurability)
                    .rarity(Rarity.RARE)
            ),
            "cigarette"
    );


    // --- production ---
    public static final Item TRAY = register(
            new Item(new FabricItemSettings()
                    .stacksTo(1)
            ),
            "tray"
    );
    public static final Item WHITE_OIL_TRAY = register(
            new Item(new FabricItemSettings()
                    .stacksTo(1)
                    .craftRemainder(TRAY)
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .effect(new MobEffectInstance(ModEffects.WARP, 64 * 20, 255), 1.f)
                            .build()
                    )
            ),
            "white_oil_tray"
    );
    public static final Item BLUE_OIL_TRAY = register(
            new Item(new FabricItemSettings()
                    .stacksTo(1)
                    .craftRemainder(TRAY)
            ),
            "blue_oil_tray"
    );

    public static final Item SCULK_CATALYST_CRYSTAL = register(
            new Item(new FabricItemSettings()) {
                @Override
                public ItemStack getRecipeRemainder(ItemStack stack) {
                    return new ItemStack(SCULK_CATALYST_CRYSTAL);
                }
            },
            "sculk_catalyst_crystal"
    );

    // --- chemicals ---
    public static final Item SUDAFED_PILL = register(
            new Item(new FabricItemSettings()),
            "sudafed_pill"
    );

    public static final Item CYANIDE = register(
            new Item(new FabricItemSettings()),
            "cyanide"
    );

    public static final Item IODINE = register(
            new Item(new FabricItemSettings()),
            "iodine"
    );

    public static final Item PSEUDO = register(
            new Item(new FabricItemSettings()),
            "pseudoephedrine"
    );

    public static final Item WHITE_PHOSPHORUS = register(
            new Item(new FabricItemSettings()),
            "white_phosphorus"
    );

    public static final Item RED_PHOSPHORUS = register(
            new Item(new FabricItemSettings()),
            "red_phosphorus"
    );

    // plants
    public static final Item HERB_SEEDS = register(
            new ItemNameBlockItem(ModBlocks.LARGE_HERB, new FabricItemSettings()),
            "herb_seeds"
    );

    public static final Item HERB_BUD = register(
            new Item(new FabricItemSettings()),
            "herb_bud"
    );

    public static final Item DRIED_HERB_BUD = register(
            new Item(new FabricItemSettings()),
            "dried_herb_bud"
    );

    public static final Item EPHEDRA_SEEDS = register(
            new ItemNameBlockItem(ModBlocks.EPHEDRA_CROP, new FabricItemSettings()),
            "ephedra_seeds"
    );

    public static final Item EPHEDRA_BUNDLE = register(
            new Item(new FabricItemSettings()),
            "ephedra_bundle"
    );

    public static final Item TOBACCO_SEEDS = register(
            new ItemNameBlockItem(ModBlocks.TOBACCO, new FabricItemSettings()),
            "tobacco_seeds"
    );

    public static final Item RIPE_TOBACCO_LEAF = register(
            new Item(new FabricItemSettings()),
            "ripe_tobacco_leaf"
    );

    public static final Item DRIED_TOBACCO_LEAF = register(
            new Item(new FabricItemSettings()),
            "dried_tobacco_leaf"
    );


    // gas bottles

    public static final Item GAS_BOTTLE = register(
            new EmptyGasBottleItem(new FabricItemSettings()
                    .stacksTo(16)
            ),
            "gas_bottle"
    );

    public static final Item GAS_BOTTLE_OXYGEN = register(
            new Item(new FabricItemSettings()
                    .craftRemainder(ModItems.GAS_BOTTLE)
                    .stacksTo(16)
            ),
            "gas_bottle_oxygen"
    );

    public static final Item GAS_BOTTLE_HYDROGEN = register(
            new Item(new FabricItemSettings()
                    .craftRemainder(ModItems.GAS_BOTTLE)
                    .stacksTo(16)
            ),
            "gas_bottle_hydrogen"
    );

    public static final Item GAS_BOTTLE_NITROGEN = register(
            new Item(new FabricItemSettings()
                    .craftRemainder(ModItems.GAS_BOTTLE)
                    .stacksTo(16)
            ),
            "gas_bottle_nitrogen"
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.SUSPICIOUS_ITEM_GROUP_KEY)
                .register((itemGroup) -> {
                    // consumables
                    itemGroup.accept(ModItems.WHITE_CRYSTALS);
                    itemGroup.accept(ModItems.WHITE_CRYSTALS_CHILI);
                    itemGroup.accept(ModItems.BLUE_CRYSTALS);
                    itemGroup.accept(ModItems.BUBBLE_PIPE);
                    itemGroup.accept(ModItems.HERBAL_ROLL);
                    itemGroup.accept(ModItems.THICK_HERBAL_ROLL);
                    itemGroup.accept(ModItems.CIGARETTE);
                    itemGroup.accept(ModItems.WOODEN_PIPE);

                    // tools
                    itemGroup.accept(ModItems.TRAY);
                    itemGroup.accept(ModItems.WHITE_OIL_TRAY);
                    itemGroup.accept(ModItems.BLUE_OIL_TRAY);
                    itemGroup.accept(ModItems.SCULK_CATALYST_CRYSTAL);

                    // plants
                    itemGroup.accept(ModItems.EPHEDRA_BUNDLE);
                    itemGroup.accept(ModItems.EPHEDRA_SEEDS);
                    itemGroup.accept(ModItems.HERB_BUD);
                    itemGroup.accept(ModItems.DRIED_HERB_BUD);
                    itemGroup.accept(ModItems.HERB_SEEDS);
                    itemGroup.accept(ModItems.RIPE_TOBACCO_LEAF);
                    itemGroup.accept(ModItems.DRIED_TOBACCO_LEAF);
                    itemGroup.accept(ModItems.TOBACCO_SEEDS);

                    // chemicals
                    itemGroup.accept(ModItems.SUDAFED_PILL);
                    itemGroup.accept(ModItems.CYANIDE);
                    itemGroup.accept(ModItems.IODINE);
                    itemGroup.accept(ModItems.PSEUDO);
                    itemGroup.accept(ModItems.WHITE_PHOSPHORUS);
                    itemGroup.accept(ModItems.RED_PHOSPHORUS);

                    itemGroup.accept(ModItems.GAS_BOTTLE);
                    itemGroup.accept(ModItems.GAS_BOTTLE_OXYGEN);
                    itemGroup.accept(ModItems.GAS_BOTTLE_HYDROGEN);
                    itemGroup.accept(ModItems.GAS_BOTTLE_NITROGEN);
                });
    }
}