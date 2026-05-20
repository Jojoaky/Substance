package jojoaky.substance.flask;

import jojoaky.substance.Substance;
import jojoaky.substance.register.ModCreativeTab;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static jojoaky.substance.register.ModItems.register;

public class ModFlasks {
    public record FlaskEntry(
            FlowingFluid still,
            FilledFlaskItem flask,
            Block block,
            int tint
    ) {}

    public static final Item EMPTY_FLASK = register(
            new EmptyFlaskItem(new FabricItemSettings()
                    .stacksTo(16)
            ),
            "flask"
    );

    public static final List<FlaskEntry> ALL_FLASK_ENTRIES = new ArrayList<>();

    public static final FilledFlaskItem WATER_FLASK = registerFlask("water", Fluids.WATER, Blocks.WATER, 0xFF4F96F4);
    public static final FilledFlaskItem LAVA_FLASK  = registerFlask("lava",  Fluids.LAVA,  Blocks.LAVA,  0xFFFF6600);

    public static FilledFlaskItem registerForChemicalFluid(
            String name,
            FlowingFluid still,
            Block block,
            int tint
    ) {
        return registerFlask(name, still, block, tint);
    }

    private static FilledFlaskItem registerFlask(String name, FlowingFluid still, Block block, int tint) {
        FilledFlaskItem flask = Registry.register(
                BuiltInRegistries.ITEM,
                Substance.resource(name + "_flask"),
                new FilledFlaskItem(still, new Item.Properties()
                        .craftRemainder(EMPTY_FLASK)
                        .stacksTo(16), tint)
        );

        FlaskEntry entry = new FlaskEntry(still, flask, block, tint);

        ALL_FLASK_ENTRIES.add(entry);

        FluidStorage.ITEM.registerForItems(
                (itemStack, context) -> new FullFlaskFluidStorage(context, entry),
                flask
        );

        return flask;
    }

    public static void initialize() {
        CreateCompat.initialize();


        FluidStorage.ITEM.registerForItems(
                (itemStack, context) -> new EmptyFlaskFluidStorage(context),
                EMPTY_FLASK
        );

        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.SUSPICIOUS_ITEM_GROUP_KEY)
                .register(entries -> {
                    entries.accept(EMPTY_FLASK);
                    for (FlaskEntry entry : ALL_FLASK_ENTRIES) {
                        entries.accept(entry.flask());
                    }
                });
    }

    private static @Nullable FlowingFluid lookupFluid(String modId, String path) {
        if (!FabricLoader.getInstance().isModLoaded(modId)) return null;

        var fluid = BuiltInRegistries.FLUID
                .get(new ResourceLocation(modId, path));

        if (fluid == Fluids.EMPTY || !(fluid instanceof FlowingFluid)) return null;

        return (FlowingFluid) fluid;
    }


    // Currently not used
    public static final class CreateCompat {
        public static @Nullable FilledFlaskItem HONEY_FLASK;
        public static @Nullable FilledFlaskItem CHOCOLATE_FLASK;

        private static @Nullable FilledFlaskItem fromModFluid(
                String modId, String path, int tint
        ) {
            FlowingFluid fluid = lookupFluid(modId, path);

            if (fluid == null) return null;

            Block block = fluid.defaultFluidState().createLegacyBlock().getBlock();

            return registerFlask(path, fluid, block, tint);
        }

        public static void initialize() {
            HONEY_FLASK     = fromModFluid("create", "honey",     0xFFffc000);
            CHOCOLATE_FLASK = fromModFluid("create", "chocolate", 0xFF3d1c02);
        }
    }
}