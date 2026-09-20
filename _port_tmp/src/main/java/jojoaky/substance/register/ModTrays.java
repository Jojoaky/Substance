package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.tray.EmptyTrayBlock;
import jojoaky.substance.content.tray.EmptyTrayItem;
import jojoaky.substance.content.tray.TrayBlock;
import jojoaky.substance.content.tray.TrayItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModTrays {
    public record TrayEntry(FlowingFluid fluid, Item filledFlask, Item emptyFlask, TrayItem filledTray, TrayBlock block) {}

    private static final Map<Item, TrayEntry> BY_FLASK = new HashMap<>();

    public static final Block EMPTY_TRAY = registerEmptyTray("tray");

    public static final TrayEntry WHITE_CRYSTAL_OIL = register("white_oil_tray",
            ModFluids.WHITE_CRYSTAL_OIL, ModFluids.WHITE_CRYSTAL_OIL_FLASK);
    public static final TrayEntry BLUE_CRYSTAL_OIL = register("blue_oil_tray",
            ModFluids.BLUE_CRYSTAL_OIL, ModFluids.BLUE_CRYSTAL_OIL_FLASK);


    private static TrayEntry register(String name, FlowingFluid fluid, Item flask) {
        TrayBlock block = Registry.register(BuiltInRegistries.BLOCK, Substance.resource(name), new TrayBlock(
                BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(0.5F).noOcclusion(),
                flask,
                ModFlasks.EMPTY_FLASK,
                Substance.resource("gameplay/" + name + "_shatter")
        ));

        TrayItem filledTray = Registry.register(BuiltInRegistries.ITEM, Substance.resource(name),
                new TrayItem(block, new Item.Properties()
                        .stacksTo(16)
                        .craftRemainder(EMPTY_TRAY.asItem())));

        TrayEntry entry = new TrayEntry(fluid, flask, ModFlasks.EMPTY_FLASK, filledTray, block);
        BY_FLASK.put(flask, entry);
        return entry;
    }

    private static EmptyTrayBlock registerEmptyTray(String name) {
        EmptyTrayBlock block = Registry.register(BuiltInRegistries.BLOCK, Substance.resource(name),
            new EmptyTrayBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL)
                    .strength(0.5F)
                    .noOcclusion())
            );

        EmptyTrayItem item = Registry.register(BuiltInRegistries.ITEM, Substance.resource(name),
                new EmptyTrayItem(block, new Item.Properties()
                        .stacksTo(16)
                ));

        return block;
    }

    public static @Nullable TrayEntry getForFlask(Item item) {
        return BY_FLASK.get(item);
    }

    public static void initialize() {
    }
}