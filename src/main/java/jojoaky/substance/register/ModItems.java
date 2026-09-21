package jojoaky.substance.register;

import jojoaky.substance.Substance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Substance.MOD_ID);

    // --- production ---
    public static final DeferredItem<Item> SCULK_CATALYST_CRYSTAL = ITEMS.register(
            "sculk_catalyst_crystal",
            () -> new Item(new Item.Properties()) {
                @Override
                public @NotNull ItemStack getCraftingRemainingItem(@NotNull ItemStack stack) {
                    return new ItemStack(this);
                }

                @Override
                public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
                    return true;
                }
            }
    );

    // --- chemicals ---
    public static final DeferredItem<Item> CYANIDE =
            ITEMS.registerSimpleItem("cyanide");

    public static final DeferredItem<Item> IODINE =
            ITEMS.registerSimpleItem("iodine");

    public static final DeferredItem<Item> PSEUDO =
            ITEMS.registerSimpleItem("pseudoephedrine");

    public static final DeferredItem<Item> WHITE_PHOSPHORUS =
            ITEMS.registerSimpleItem("white_phosphorus");

    public static final DeferredItem<Item> RED_PHOSPHORUS =
            ITEMS.registerSimpleItem("red_phosphorus");

    // plants
    public static final DeferredItem<ItemNameBlockItem> HERB_SEEDS =
            ITEMS.register(
                    "herb_seeds",
                    () -> new ItemNameBlockItem(
                            ModBlocks.LARGE_HERB.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> HERB_BUD =
            ITEMS.registerSimpleItem("herb_bud");

    public static final DeferredItem<Item> DRIED_HERB_BUD =
            ITEMS.registerSimpleItem("dried_herb_bud");


    public static final DeferredItem<ItemNameBlockItem> EPHEDRA_SEEDS =
            ITEMS.register(
                    "ephedra_seeds",
                    () -> new ItemNameBlockItem(
                            ModBlocks.EPHEDRA_CROP.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> EPHEDRA_BUNDLE =
            ITEMS.registerSimpleItem("ephedra_bundle");


    public static final DeferredItem<ItemNameBlockItem> CHILI_SEEDS =
            ITEMS.register(
                    "chili_seeds",
                    () -> new ItemNameBlockItem(
                            ModBlocks.CHILI_CROP.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> CHILI_PEPPER =
            ITEMS.registerSimpleItem(
                    "chili_pepper",
                    new Item.Properties().food(
                            new FoodProperties.Builder()
                                    .nutrition(1)
                                    .saturationModifier(0.2f)
                                    .fast()
                                    .build()
                    )
            );


    public static final DeferredItem<ItemNameBlockItem> TOBACCO_SEEDS =
            ITEMS.register(
                    "tobacco_seeds",
                    () -> new ItemNameBlockItem(
                            ModBlocks.TOBACCO.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> RIPE_TOBACCO_LEAF =
            ITEMS.registerSimpleItem("ripe_tobacco_leaf");

    public static final DeferredItem<Item> DRIED_TOBACCO_LEAF =
            ITEMS.registerSimpleItem("dried_tobacco_leaf");

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
