package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.chemical_fluid.ChemicalBucket;
import jojoaky.substance.content.flask.FilledFlaskItem;
import jojoaky.substance.content.chemical_fluid.ChemicalFluid;
import jojoaky.substance.content.chemical_fluid.ChemicalFluidBlock;
import jojoaky.substance.content.flask.ModFlasks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;

import java.util.ArrayList;
import java.util.List;

public class ModFluids {
    public record ChemicalFluidSet(
            FlowingFluid still,
            FlowingFluid flowing,
            Block block,
            Item bucket,
            FilledFlaskItem flask,
            int tint
    ) {}
    public static final List<ChemicalFluidSet> ALL_FLUIDS = new ArrayList<>();


    public static final ChemicalFluidSet water = new ChemicalFluidSet(
            Fluids.WATER,
            Fluids.FLOWING_WATER,
            Blocks.WATER,
            Items.WATER_BUCKET,
            ModFlasks.WATER_FLASK,
            0x000000
    );

    public static final ChemicalFluidSet lava = new ChemicalFluidSet(
            Fluids.LAVA,
            Fluids.FLOWING_LAVA,
            Blocks.LAVA,
            Items.LAVA_BUCKET,
            ModFlasks.LAVA_FLASK,
            0x000000
    );

    // Phenylacetic Acid
    public static final ChemicalFluidSet phenylaceticAcid = registerChemicalFluid("phenylacetic_acid", 1.0f, 1.2f, 0xFFe8d9b0);
    public static final FlowingFluid PHENYLACETIC_ACID = phenylaceticAcid.still();
    public static final FlowingFluid PHENYLACETIC_ACID_FLOWING = phenylaceticAcid.flowing();
    public static final Block PHENYLACETIC_ACID_BLOCK = phenylaceticAcid.block;
    public static final Item PHENYLACETIC_ACID_BUCKET = phenylaceticAcid.bucket;
    public static final FilledFlaskItem PHENYLACETIC_ACID_FLASK = phenylaceticAcid.flask;

    // Acetic Anhydride
    public static final ChemicalFluidSet aceticAnhydride = registerChemicalFluid("acetic_anhydride", 1.0f, 2.5f, 0xFFf5e8c7);
    public static final FlowingFluid ACETIC_ANHYDRIDE = aceticAnhydride.still();
    public static final FlowingFluid ACETIC_ANHYDRIDE_FLOWING = aceticAnhydride.flowing();
    public static final Block ACETIC_ANHYDRIDE_BLOCK = aceticAnhydride.block;
    public static final Item ACETIC_ANHYDRIDE_BUCKET = aceticAnhydride.bucket;
    public static final Item ACETIC_ANHYDRIDE_FLASK = aceticAnhydride.flask;

    // Methanol
    public static final ChemicalFluidSet methanol = registerChemicalFluid("methanol", 1.0f, 3.5f, 0xFFa8d4ff);
    public static final FlowingFluid METHANOL = methanol.still();
    public static final FlowingFluid METHANOL_FLOWING = methanol.flowing();
    public static final Block METHANOL_BLOCK = methanol.block;
    public static final Item METHANOL_BUCKET = methanol.bucket;
    public static final Item METHANOL_FLASK = methanol.flask;

    // Methylamine
    public static final ChemicalFluidSet methylamine = registerChemicalFluid("methylamine", 1.0f, 3.0f, 0xFFb0ffe8);
    public static final FlowingFluid METHYLAMINE = methylamine.still();
    public static final FlowingFluid METHYLAMINE_FLOWING = methylamine.flowing();
    public static final Block METHYLAMINE_BLOCK = methylamine.block;
    public static final Item METHYLAMINE_BUCKET = methylamine.bucket;
    public static final Item METHYLAMINE_FLASK = methylamine.flask;

    // Phenylacetone
    public static final ChemicalFluidSet phenylacetone = registerChemicalFluid("phenylacetone", 1.0f, 0.8f, 0xFFffe0a0);
    public static final FlowingFluid PHENYLACETONE = phenylacetone.still();
    public static final FlowingFluid PHENYLACETONE_FLOWING = phenylacetone.flowing();
    public static final Block PHENYLACETONE_BLOCK = phenylacetone.block;
    public static final Item PHENYLACETONE_BUCKET = phenylacetone.bucket;
    public static final Item PHENYLACETONE_FLASK = phenylacetone.flask;

    // Ammonia
    public static final ChemicalFluidSet ammonia = registerChemicalFluid("ammonia", 0.75f, 4.2f, 0xFFd0f0ff);
    public static final FlowingFluid AMMONIA = ammonia.still();
    public static final FlowingFluid AMMONIA_FLOWING = ammonia.flowing();
    public static final Block AMMONIA_BLOCK = ammonia.block;
    public static final Item AMMONIA_BUCKET = ammonia.bucket;
    // Ammonia
    public static final Item AMMONIA_FLASK = ammonia.flask;

    // White Crystal Oil
    public static final ChemicalFluidSet whiteCrystalOil = registerChemicalFluid("white_crystal_oil", 2.0f, 3.8f, 0xFFf8f8f8);
    public static final FlowingFluid WHITE_CRYSTAL_OIL = whiteCrystalOil.still();
    public static final FlowingFluid WHITE_CRYSTAL_OIL_FLOWING = whiteCrystalOil.flowing();
    public static final Block WHITE_CRYSTAL_OIL_BLOCK = whiteCrystalOil.block;
    public static final Item WHITE_CRYSTAL_OIL_BUCKET = whiteCrystalOil.bucket;
    public static final Item WHITE_CRYSTAL_OIL_FLASK = whiteCrystalOil.flask;

    // Blue Crystal Oil
    public static final ChemicalFluidSet blueCrystalOil = registerChemicalFluid("blue_crystal_oil", 2.0f, 4.0f, 0xFF40c0ff);
    public static final FlowingFluid BLUE_CRYSTAL_OIL = blueCrystalOil.still();
    public static final FlowingFluid BLUE_CRYSTAL_OIL_FLOWING = blueCrystalOil.flowing();
    public static final Block BLUE_CRYSTAL_OIL_BLOCK = blueCrystalOil.block;
    public static final Item BLUE_CRYSTAL_OIL_BUCKET = blueCrystalOil.bucket;
    public static final Item BLUE_CRYSTAL_OIL_FLASK = blueCrystalOil.flask;

    public static ChemicalFluidSet registerChemicalFluid(String name, float thickness, float toxicity, int tint) {
        FlowingFluid[] still = new FlowingFluid[1];
        FlowingFluid[] flowing = new FlowingFluid[1];
        Block[] block = new Block[1];
        ChemicalBucket[] bucket = new ChemicalBucket[1];
        FilledFlaskItem[] flask  = new FilledFlaskItem[1];

        // Look up the bucket lazily so ModItems can be initialised independently
        still[0] = register(name,
                new ChemicalFluid.Source(thickness,
                        () -> still[0], () -> flowing[0], () -> block[0], () -> bucket[0]));

        flowing[0] = register("flowing_" + name,
                new ChemicalFluid.Flowing(thickness,
                        () -> still[0], () -> flowing[0], () -> block[0], () -> bucket[0]));

        block[0] = ModBlocks.register(
                new ChemicalFluidBlock(
                        still[0],
                        BlockBehaviour.Properties.copy(Blocks.WATER),
                        toxicity),
                name,
                false);

        bucket[0] = registerChemicalBucket(name, still[0], tint);

        flask[0] = ModFlasks.registerForChemicalFluid(name, still[0], block[0], tint);

        ChemicalFluidSet set =
                new ChemicalFluidSet(still[0], flowing[0], block[0], bucket[0], flask[0], tint);

        ALL_FLUIDS.add(set);

        return set;
    }

    private static FlowingFluid register(String name, FlowingFluid fluid) {
        return Registry.register(BuiltInRegistries.FLUID,
                Substance.resource(name), fluid);
    }

    private static ChemicalBucket registerChemicalBucket(String name, FlowingFluid fluid, int color) {
        return Registry.register(
                BuiltInRegistries.ITEM,
                Substance.resource(name + "_bucket"),
                new ChemicalBucket(
                        fluid,
                        new Item.Properties()
                                .craftRemainder(Items.BUCKET)
                                .stacksTo(1),
                        color
                )
        );
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> {
                    for (ChemicalFluidSet fluid : ALL_FLUIDS) {
                        entries.addAfter(Items.MILK_BUCKET, fluid.bucket());
                    }
                });

        ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.SUSPICIOUS_ITEM_GROUP_KEY)
                .register(entries -> {
                    for (ChemicalFluidSet fluid : ALL_FLUIDS) {
                        entries.accept(fluid.bucket);
                    }
                });
    }
}