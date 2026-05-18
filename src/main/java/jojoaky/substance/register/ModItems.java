package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.consumable.JointItem;
import jojoaky.substance.flask.EmptyFlaskItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
            new Item(new FabricItemSettings()
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .nutrition(1)
                            .saturationMod(8)
                            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60*20, 2), 1.0f)
                            .effect(new MobEffectInstance(MobEffects.JUMP, 20*20), 1.0f)
                            .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 90*20), 1.0f)
                            .effect(new MobEffectInstance(MobEffects.CONFUSION, 10*20), 1.0f)
                            .effect(new MobEffectInstance(MobEffects.LEVITATION, 20, 4), 1.0f)
                            .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30), 1.0f)
                            .effect(new MobEffectInstance(MobEffects.HARM, 5), 1.0f)
                            .build())
                    .rarity(Rarity.RARE)
            ),
            "white_crystals"
    );

    public static final Item WHITE_CRYSTALS_CHILI = register(
            new Item(new FabricItemSettings()
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .nutrition(1)
                            .saturationMod(8)
                            .build())
                    .rarity(Rarity.RARE)
            ),
            "white_crystals_chili"
    );

    public static final Item BLUE_CRYSTALS = register(
            new Item(new FabricItemSettings()
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .nutrition(1)
                            .saturationMod(8)
                            .build())
                    .rarity(Rarity.EPIC)
            ),
            "blue_crystals"
    );

    public static final Item HERBAL_ROLL = register(
            new JointItem(new FabricItemSettings()
                    .durability(128)
                    .rarity(Rarity.RARE)
            ),
            "herbal_roll"
    );

    public static final Item THICK_HERBAL_ROLL = register(
            new JointItem(new FabricItemSettings()
                    .durability(256)
                    .rarity(Rarity.EPIC)
            ),
            "thick_herbal_roll"
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
            ),
            "white_oil_tray"
    );
    public static final Item BLUE_OIL_TRAY = register(
            new Item(new FabricItemSettings()
                    .stacksTo(1)
            ),
            "blue_oil_tray"
    );

    public static final Item SCULK_CATALYST_CRYSTAL = register(
            new Item(new FabricItemSettings()) {
                @Override
                public ItemStack getRecipeRemainder(ItemStack stack) {
                    return stack; // returns itself
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

    public static final Item FLASK = register(
            new EmptyFlaskItem(new FabricItemSettings()
                    .stacksTo(16)
            ),
            "flask"
    );

    public static final Item GAS_BOTTLE = register(
            new Item(new FabricItemSettings()
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
                    itemGroup.accept(ModItems.HERBAL_ROLL);
                    itemGroup.accept(ModItems.THICK_HERBAL_ROLL);

                    // tools
                    itemGroup.accept(ModItems.TRAY);
                    itemGroup.accept(ModItems.WHITE_OIL_TRAY);
                    itemGroup.accept(ModItems.BLUE_OIL_TRAY);
                    itemGroup.accept(ModItems.SCULK_CATALYST_CRYSTAL);

                    // plants
                    itemGroup.accept(ModItems.EPHEDRA_BUNDLE);
                    itemGroup.accept(ModItems.EPHEDRA_SEEDS);
                    itemGroup.accept(ModItems.HERB_SEEDS);
                    itemGroup.accept(ModItems.HERB_BUD);
                    itemGroup.accept(ModItems.DRIED_HERB_BUD);

                    // chemicals
                    itemGroup.accept(ModItems.SUDAFED_PILL);
                    itemGroup.accept(ModItems.CYANIDE);
                    itemGroup.accept(ModItems.IODINE);
                    itemGroup.accept(ModItems.PSEUDO);
                    itemGroup.accept(ModItems.WHITE_PHOSPHORUS);
                    itemGroup.accept(ModItems.RED_PHOSPHORUS);

                    // flasks
                    itemGroup.accept(ModItems.FLASK);

                    itemGroup.accept(ModItems.GAS_BOTTLE);
                    itemGroup.accept(ModItems.GAS_BOTTLE_OXYGEN);
                    itemGroup.accept(ModItems.GAS_BOTTLE_HYDROGEN);
                    itemGroup.accept(ModItems.GAS_BOTTLE_NITROGEN);
                });
    }
}