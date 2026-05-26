package jojoaky.substance.datagen;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.register.ModFluids;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class ModelProvider extends FabricModelProvider {

    private static final ModelTemplate CHEMICAL_BUCKET_TEMPLATE =
            new ModelTemplate(
                    Optional.of(Substance.resource("item/template_bucket")),
                    Optional.empty()
            );

    private static final ModelTemplate CHEMICAL_FLASK_TEMPLATE =
            new ModelTemplate(
                    Optional.of(Substance.resource("item/template_flask")),
                    Optional.empty()
            );

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
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
        for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
            itemModelGenerators.generateFlatItem(
                    fluid.bucket(),
                    CHEMICAL_BUCKET_TEMPLATE
            );
        }
        for (ModFlasks.FlaskEntry flaskEntry: ModFlasks.ALL_FLASK_ENTRIES) {
            if (flaskEntry.flask().useCustomModel) continue;
            itemModelGenerators.generateFlatItem(
                    flaskEntry.flask(),
                    CHEMICAL_FLASK_TEMPLATE
            );
        }
    }
}