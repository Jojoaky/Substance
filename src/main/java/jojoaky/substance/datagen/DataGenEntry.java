package jojoaky.substance.datagen;

import jojoaky.substance.Substance;
import jojoaky.substance.register.ModFluids;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class DataGenEntry implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(CustomModelProvider::new);
    }

    private static final ModelTemplate CHEMICAL_BUCKET_TEMPLATE =
            new ModelTemplate(
                    Optional.of(Substance.resource("item/template_bucket")),
                    Optional.empty(),
                    new TextureSlot[0]
            );

    private static final ModelTemplate CHEMICAL_FLASK_TEMPLATE =
            new ModelTemplate(
                    Optional.of(Substance.resource("item/template_flask")),
                    Optional.empty(),
                    new TextureSlot[0]
            );

    private static class CustomModelProvider extends FabricModelProvider {

        private CustomModelProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
            for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(fluid.block());
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
                itemModelGenerators.generateFlatItem(
                        fluid.flask(),
                        CHEMICAL_FLASK_TEMPLATE
                );
            }
        }
    }
}