package jojoaky.substance.datagen;

import jojoaky.substance.Substance;
import jojoaky.substance.register.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SimpleItemModelProvider extends ItemModelProvider {

    private static final Item[] SIMPLE_MODEL_ITEMS = {
            /*
            ModItems.TRAY,
            ModItems.WHITE_OIL_TRAY,
            ModItems.BLUE_OIL_TRAY,
             */
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
            /*
            ModItems.GAS_BOTTLE,
            ModItems.GAS_BOTTLE_OXYGEN,
            ModItems.GAS_BOTTLE_HYDROGEN,
            ModItems.GAS_BOTTLE_NITROGEN,
            ModFlasks.EMPTY_FLASK,
            ModFlasks.LAVA_FLASK,
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
    }
}
