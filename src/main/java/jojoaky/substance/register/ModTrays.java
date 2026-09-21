package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.chemical_fluid.ChemicalFluid;
import jojoaky.substance.content.flask.FilledFlaskItem;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.tray.EmptyTrayBlock;
import jojoaky.substance.content.tray.EmptyTrayItem;
import jojoaky.substance.content.tray.EmptyingTrayDispenserBehavior;
import jojoaky.substance.content.tray.FillingTrayDispenserBehavior;
import jojoaky.substance.content.tray.PlaceTrayBehavior;
import jojoaky.substance.content.tray.ShatteringTrayDispenserBehavior;
import jojoaky.substance.content.tray.TrayBlock;
import jojoaky.substance.content.tray.TrayItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ModTrays {
    private static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Substance.MOD_ID);
    private static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Substance.MOD_ID);

    public static final DeferredBlock<EmptyTrayBlock> EMPTY_TRAY = BLOCKS.register(
            "tray",
            () -> new EmptyTrayBlock(trayProperties())
    );
    public static final DeferredItem<EmptyTrayItem> EMPTY_TRAY_ITEM = ITEMS.register(
            "tray",
            () -> new EmptyTrayItem(
                    EMPTY_TRAY.get(),
                    new Item.Properties().stacksTo(16)
            )
    );

    public static final TrayEntry WHITE_CRYSTAL_OIL = register(
            "white_oil_tray",
            ModFluids.WHITE_CRYSTAL_OIL
    );
    public static final TrayEntry BLUE_CRYSTAL_OIL = register(
            "blue_oil_tray",
            ModFluids.BLUE_CRYSTAL_OIL
    );
    public static final List<TrayEntry> ALL_TRAYS = List.of(
            WHITE_CRYSTAL_OIL,
            BLUE_CRYSTAL_OIL
    );

    private ModTrays() {}

    private static TrayEntry register(String name, ModFluids.ChemicalFluidSet fluid) {
        DeferredBlock<TrayBlock> block = BLOCKS.register(
                name,
                () -> new TrayBlock(
                        trayProperties(),
                        fluid.flask(),
                        ModFlasks.EMPTY_FLASK,
                        Substance.resource("gameplay/" + name + "_shatter")
                )
        );
        DeferredItem<TrayItem> item = ITEMS.register(
                name,
                () -> new TrayItem(
                        block.get(),
                        new Item.Properties()
                                .stacksTo(16)
                                .craftRemainder(EMPTY_TRAY_ITEM.get())
                )
        );

        return new TrayEntry(
                fluid.still(),
                fluid.flask(),
                block,
                item
        );
    }

    private static BlockBehaviour.Properties trayProperties() {
        return BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(0.5F)
                .noOcclusion();
    }

    public static @Nullable TrayEntry getForFlask(Item item) {
        for (TrayEntry tray : ALL_TRAYS) {
            if (tray.filledFlask() == item) {
                return tray;
            }
        }
        return null;
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModTrays::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    ModFlasks.EMPTY_FLASK.get(),
                    new EmptyingTrayDispenserBehavior()
            );
            DispenserBlock.registerBehavior(
                    EMPTY_TRAY_ITEM.get(),
                    new PlaceTrayBehavior()
            );

            for (TrayEntry tray : ALL_TRAYS) {
                DispenserBlock.registerBehavior(
                        tray.filledFlask(),
                        new FillingTrayDispenserBehavior()
                );
                DispenserBlock.registerBehavior(
                        tray.filledTray(),
                        new PlaceTrayBehavior()
                );
            }

            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof PickaxeItem) {
                    DispenserBlock.registerBehavior(
                            item,
                            new ShatteringTrayDispenserBehavior()
                    );
                }
            }
        });
    }

    public record TrayEntry(
            DeferredHolder<net.minecraft.world.level.material.Fluid, ChemicalFluid.Source> fluidHolder,
            DeferredItem<FilledFlaskItem> filledFlaskHolder,
            DeferredBlock<TrayBlock> blockHolder,
            DeferredItem<TrayItem> filledTrayHolder
    ) {
        public FlowingFluid fluid() {
            return fluidHolder.get();
        }

        public Item filledFlask() {
            return filledFlaskHolder.get();
        }

        public Item emptyFlask() {
            return ModFlasks.EMPTY_FLASK.get();
        }

        public TrayItem filledTray() {
            return filledTrayHolder.get();
        }

        public TrayBlock block() {
            return blockHolder.get();
        }
    }
}
