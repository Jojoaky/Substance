package jojoaky.substance.content.flask;

import jojoaky.substance.Substance;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ModFlasks {
    private static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Substance.MOD_ID);

    private static final List<FlaskEntry> MUTABLE_FLASK_ENTRIES =
            new ArrayList<>();

    public static final DeferredItem<EmptyFlaskItem> EMPTY_FLASK = ITEMS.registerItem(
            "flask",
            EmptyFlaskItem::new,
            new Item.Properties().stacksTo(16)
    );

    public static final DeferredItem<FilledFlaskItem> WATER_FLASK = registerFlask(
            "water",
            () -> Fluids.WATER,
            () -> Blocks.WATER,
            0xFF4F96F4,
            false
    );

    public static final DeferredItem<FilledFlaskItem> LAVA_FLASK = registerFlask(
            "lava",
            () -> Fluids.LAVA,
            () -> Blocks.LAVA,
            0,
            true
    );

    public static final List<FlaskEntry> ALL_FLASK_ENTRIES =
            Collections.unmodifiableList(MUTABLE_FLASK_ENTRIES);

    private ModFlasks() {}

    public static DeferredItem<FilledFlaskItem> registerForChemicalFluid(
            String name,
            Supplier<? extends FlowingFluid> still,
            Supplier<? extends Block> block,
            int tint
    ) {
        return registerFlask(name, still, block, tint, false);
    }

    private static DeferredItem<FilledFlaskItem> registerFlask(
            String name,
            Supplier<? extends FlowingFluid> still,
            Supplier<? extends Block> block,
            int tint,
            boolean useCustomModel
    ) {
        DeferredItem<FilledFlaskItem> flask = ITEMS.register(
                name + "_flask",
                () -> new FilledFlaskItem(
                        still.get(),
                        new Item.Properties()
                                .craftRemainder(EMPTY_FLASK.get())
                                .stacksTo(16),
                        tint,
                        useCustomModel
                )
        );

        FlaskEntry entry = new FlaskEntry(
                still,
                flask,
                block,
                tint,
                useCustomModel
        );
        MUTABLE_FLASK_ENTRIES.add(entry);
        return flask;
    }

    public static @Nullable FlaskEntry getEntry(Fluid fluid) {
        Fluid source = fluid instanceof FlowingFluid flowingFluid
                ? flowingFluid.getSource()
                : fluid;

        for (FlaskEntry entry : ALL_FLASK_ENTRIES) {
            if (entry.still() == source) {
                return entry;
            }
        }
        return null;
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModFlasks::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ignored) -> new FlaskFluidHandler(stack, null),
                EMPTY_FLASK.get()
        );

        for (FlaskEntry entry : ALL_FLASK_ENTRIES) {
            event.registerItem(
                    Capabilities.FluidHandler.ITEM,
                    (stack, ignored) -> new FlaskFluidHandler(stack, entry),
                    entry.flask().get()
            );
        }
    }

    public record FlaskEntry(
            Supplier<? extends FlowingFluid> stillSupplier,
            DeferredItem<FilledFlaskItem> flask,
            Supplier<? extends Block> blockSupplier,
            int tint,
            boolean useCustomModel
    ) {
        public FlowingFluid still() {
            return stillSupplier.get();
        }

        public Block block() {
            return blockSupplier.get();
        }
    }
}
