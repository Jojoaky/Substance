package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.crops.EphedraCropBlock;
import jojoaky.substance.content.crops.LargeHerbBlock;
import jojoaky.substance.content.crops.TobaccoBlock;
import jojoaky.substance.content.tray.EmptyTrayBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        ResourceLocation id = Substance.resource(name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static final Block LARGE_HERB = register(
            new LargeHerbBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.GRASS)
                            .noCollission()
                            .instabreak()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
            ),
            "large_herb",
            true
    );

    public static final Block TRAY = register(
            new EmptyTrayBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL)
                    .strength(0.5F)
                    .noOcclusion()),
            "tray",
            true
    );

    public static final Block EPHEDRA_CROP = register(
            new EphedraCropBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.CROP)
                            .noCollission()
                            .instabreak()
                    ),
            "ephedra",
            false
        );

    public static final Block TOBACCO = register(
            new TobaccoBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.GRASS)
                            .noCollission()
                            .instabreak()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
            ),
            "tobacco",
            false
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.SUSPICIOUS_ITEM_GROUP_KEY).register((itemGroup) -> {
            itemGroup.accept(ModBlocks.LARGE_HERB.asItem());
        });
    }
}