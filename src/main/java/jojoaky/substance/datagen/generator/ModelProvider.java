package jojoaky.substance.datagen.generator;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.FilledFlaskItem;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class ModelProvider extends FabricModelProvider {

    private static final ModelTemplate CHEMICAL_BUCKET_TEMPLATE =
            new ModelTemplate(
                    Optional.of(Substance.resource("item/_template_bucket")),
                    Optional.empty()
            );

    private static final ModelTemplate CHEMICAL_FLASK_TEMPLATE =
            new ModelTemplate(
                    Optional.of(Substance.resource("item/_template_flask")),
                    Optional.empty()
            );

    public static final Item[] SIMPLE_MODEL_ITEMS = {
            ModItems.TRAY,
            ModItems.WHITE_OIL_TRAY,
            ModItems.BLUE_OIL_TRAY,
            ModItems.SCULK_CATALYST_CRYSTAL,
            ModItems.SUDAFED_PILL,
            ModItems.CYANIDE,
            ModItems.IODINE,
            ModItems.PSEUDO,
            ModItems.WHITE_PHOSPHORUS,
            ModItems.RED_PHOSPHORUS,
            ModItems.HERB_SEEDS,
            ModItems.HERB_BUD,
            ModItems.DRIED_HERB_BUD,
            ModItems.EPHEDRA_SEEDS,
            ModItems.EPHEDRA_BUNDLE,
            ModItems.TOBACCO_SEEDS,
            ModItems.RIPE_TOBACCO_LEAF,
            ModItems.DRIED_TOBACCO_LEAF,
            ModItems.GAS_BOTTLE,
            ModItems.GAS_BOTTLE_OXYGEN,
            ModItems.GAS_BOTTLE_HYDROGEN,
            ModItems.GAS_BOTTLE_NITROGEN,
            ModFlasks.EMPTY_FLASK,
            ModFlasks.LAVA_FLASK,
            ModItems.WHITE_CRYSTALS,
            ModItems.WHITE_CRYSTALS_CHILI,
            ModItems.BLUE_CRYSTALS,
    };

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        // Fluids
        for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
            blockModelGenerators.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(
                            fluid.block(),
                            Variant.variant().with(VariantProperties.MODEL,
                                    new ResourceLocation("minecraft", "block/air"))
                    )
            );
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        // Basic flat items
        for (Item item : SIMPLE_MODEL_ITEMS) {
            itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }

        // Buckets
        for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
            itemModelGenerators.generateFlatItem(
                    fluid.bucket(),
                    CHEMICAL_BUCKET_TEMPLATE
            );
        }

        // Flasks
        for (FilledFlaskItem flaskEntry: ModFlasks.ALL_FLASK_ENTRIES.stream().map(e -> e.flask()).toList()) {
            if (flaskEntry.useCustomModel) continue;
            itemModelGenerators.generateFlatItem(
                    flaskEntry,
                    CHEMICAL_FLASK_TEMPLATE
            );
        }
    }
}