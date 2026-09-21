package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.crops.ChiliCropBlock;
import jojoaky.substance.content.crops.EphedraCropBlock;
import jojoaky.substance.content.crops.LargeHerbBlock;
import jojoaky.substance.content.crops.TobaccoBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Substance.MOD_ID);


    public static final DeferredBlock<LargeHerbBlock> LARGE_HERB =
            BLOCKS.registerBlock(
                    "large_herb",
                    LargeHerbBlock::new,
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.GRASS)
                            .noCollission()
                            .instabreak()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
            );


    public static final DeferredBlock<EphedraCropBlock> EPHEDRA_CROP =
            BLOCKS.registerBlock(
                    "ephedra",
                    EphedraCropBlock::new,
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.CROP)
                            .noCollission()
                            .instabreak()
            );


    public static final DeferredBlock<ChiliCropBlock> CHILI_CROP =
            BLOCKS.registerBlock(
                    "chili",
                    ChiliCropBlock::new,
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.CROP)
                            .noCollission()
                            .instabreak()
            );


    public static final DeferredBlock<TobaccoBlock> TOBACCO =
            BLOCKS.registerBlock(
                    "tobacco",
                    TobaccoBlock::new,
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.GRASS)
                            .noCollission()
                            .instabreak()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
            );


    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}