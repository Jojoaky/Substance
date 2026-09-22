package jojoaky.substance.datagen;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.register.ModFluids;
import jojoaky.substance.register.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SimpleItemModelProvider extends ItemModelProvider {

    private static final Item[] SIMPLE_MODEL_ITEMS = {
            ModItems.TRAY.get(),
            ModItems.WHITE_OIL_TRAY.get(),
            ModItems.BLUE_OIL_TRAY.get(),
            ModItems.SCULK_CATALYST_CRYSTAL.get(),
            ModItems.CYANIDE.get(),
            ModItems.IODINE.get(),
            ModItems.PSEUDO.get(),
            ModItems.WHITE_PHOSPHORUS.get(),
            ModItems.RED_PHOSPHORUS.get(),
            ModItems.HERB_SEEDS.get(),
            ModItems.HERB_BUD.get(),
            ModItems.DRIED_HERB_BUD.get(),
            ModItems.EPHEDRA_SEEDS.get(),
            ModItems.EPHEDRA_BUNDLE.get(),
            ModItems.CHILI_SEEDS.get(),
            ModItems.CHILI_PEPPER.get(),
            ModItems.TOBACCO_SEEDS.get(),
            ModItems.RIPE_TOBACCO_LEAF.get(),
            ModItems.DRIED_TOBACCO_LEAF.get(),
            ModItems.GAS_BOTTLE.get(),
            ModItems.GAS_BOTTLE_OXYGEN.get(),
            ModItems.GAS_BOTTLE_HYDROGEN.get(),
            ModItems.GAS_BOTTLE_NITROGEN.get(),
            ModFlasks.EMPTY_FLASK.get(),
            ModFlasks.LAVA_FLASK.get(),
            /*
            ModItems.WHITE_CRYSTALS,
            ModItems.WHITE_CRYSTALS_CHILI,
            ModItems.BLUE_CRYSTALS,
            */
    };

    public SimpleItemModelProvider(
            PackOutput output,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, Substance.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Item item : SIMPLE_MODEL_ITEMS) {
            basicItem(item);
        }

        for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
            withExistingParent(
                    fluid.bucket().getId().getPath(),
                    modLoc("item/_template_bucket")
            );
        }

        basicItem(ModFlasks.EMPTY_FLASK.get());
        for (ModFlasks.FlaskEntry entry : ModFlasks.ALL_FLASK_ENTRIES) {
            if (entry.useCustomModel()) {
                basicItem(entry.flask().get());
            } else {
                withExistingParent(
                        entry.flask().getId().toString(),
                        modLoc("item/_template_flask")
                );
            }
        }
    }
}
