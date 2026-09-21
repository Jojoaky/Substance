package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.chemical_fluid.ChemicalBucket;
import jojoaky.substance.content.chemical_fluid.ChemicalFluid;
import jojoaky.substance.content.chemical_fluid.ChemicalFluidBlock;
import jojoaky.substance.content.flask.FilledFlaskItem;
import jojoaky.substance.content.flask.ModFlasks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModFluids {
    private static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Substance.MOD_ID);
    private static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, Substance.MOD_ID);
    private static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Substance.MOD_ID);
    private static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Substance.MOD_ID);

    private static final List<ChemicalFluidSet> MUTABLE_FLUIDS = new ArrayList<>();
    public static final List<ChemicalFluidSet> ALL_FLUIDS =
            Collections.unmodifiableList(MUTABLE_FLUIDS);

    public static final ChemicalFluidSet PHENYLACETIC_ACID = registerChemicalFluid(
            "phenylacetic_acid", 1.0F, 1.2F, 0xFFE8D9B0);
    public static final ChemicalFluidSet ACETIC_ANHYDRIDE = registerChemicalFluid(
            "acetic_anhydride", 1.0F, 2.5F, 0xFFF5E8C7);
    public static final ChemicalFluidSet METHANOL = registerChemicalFluid(
            "methanol", 1.0F, 3.5F, 0xFFA8D4FF);
    public static final ChemicalFluidSet METHYLAMINE = registerChemicalFluid(
            "methylamine", 1.0F, 3.0F, 0xFFB0FFE8);
    public static final ChemicalFluidSet PHENYLACETONE = registerChemicalFluid(
            "phenylacetone", 1.0F, 0.8F, 0xFFFFE0A0);
    public static final ChemicalFluidSet AMMONIA = registerChemicalFluid(
            "ammonia", 0.75F, 4.2F, 0xFFD0F0FF);
    public static final ChemicalFluidSet WHITE_CRYSTAL_OIL = registerChemicalFluid(
            "white_crystal_oil", 2.0F, 3.8F, 0xFFF8F8F8);
    public static final ChemicalFluidSet BLUE_CRYSTAL_OIL = registerChemicalFluid(
            "blue_crystal_oil", 2.0F, 4.0F, 0xFF40C0FF);

    private ModFluids() {}

    private static ChemicalFluidSet registerChemicalFluid(
            String name,
            float thickness,
            float toxicity,
            int tint
    ) {
        DeferredHolder<FluidType, FluidType> fluidType = FLUID_TYPES.register(
                name,
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(Math.round(1000 * thickness))
                                .viscosity(Math.round(1000 * thickness))
                )
        );

        ChemicalFluidSet[] setReference = new ChemicalFluidSet[1];

        DeferredHolder<Fluid, ChemicalFluid.Source> still = FLUIDS.register(
                name,
                () -> new ChemicalFluid.Source(
                        thickness,
                        fluidType,
                        () -> setReference[0].still().get(),
                        () -> setReference[0].flowing().get(),
                        () -> setReference[0].block().get(),
                        () -> setReference[0].bucket().get()
                )
        );
        DeferredHolder<Fluid, ChemicalFluid.Flowing> flowing = FLUIDS.register(
                "flowing_" + name,
                () -> new ChemicalFluid.Flowing(
                        thickness,
                        fluidType,
                        () -> setReference[0].still().get(),
                        () -> setReference[0].flowing().get(),
                        () -> setReference[0].block().get(),
                        () -> setReference[0].bucket().get()
                )
        );
        DeferredBlock<ChemicalFluidBlock> block = BLOCKS.register(
                name,
                () -> new ChemicalFluidBlock(
                        still.get(),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.WATER),
                        toxicity
                )
        );
        DeferredItem<ChemicalBucket> bucket = ITEMS.register(
                name + "_bucket",
                () -> new ChemicalBucket(
                        still.get(),
                        new Item.Properties()
                                .craftRemainder(Items.BUCKET)
                                .stacksTo(1),
                        tint
                )
        );
        DeferredItem<FilledFlaskItem> flask = ModFlasks.registerForChemicalFluid(
                name,
                () -> still.get(),
                block,
                tint
        );

        ChemicalFluidSet set = new ChemicalFluidSet(
                fluidType,
                still,
                flowing,
                block,
                bucket,
                flask,
                tint
        );
        setReference[0] = set;
        MUTABLE_FLUIDS.add(set);
        return set;
    }

    public static void register(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModFluids::addCreativeTabContents);
    }

    private static void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) {
            return;
        }

        for (ChemicalFluidSet fluid : ALL_FLUIDS) {
            event.accept(fluid.bucket().get());
        }
    }

    public record ChemicalFluidSet(
            DeferredHolder<FluidType, FluidType> fluidType,
            DeferredHolder<Fluid, ChemicalFluid.Source> still,
            DeferredHolder<Fluid, ChemicalFluid.Flowing> flowing,
            DeferredBlock<ChemicalFluidBlock> block,
            DeferredItem<ChemicalBucket> bucket,
            DeferredItem<FilledFlaskItem> flask,
            int tint
    ) {}
}
