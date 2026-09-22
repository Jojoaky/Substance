package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.flask.ModFlasks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(
                    Registries.CREATIVE_MODE_TAB,
                    Substance.MOD_ID
            );


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SUSPICIOUS_ITEM_GROUP =
            CREATIVE_TABS.register(
                    "item_group",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.substance"))

                            .icon(() -> new ItemStack(
                                    // TODO: Replace with blue crystals
                                    ModItems.CHILI_PEPPER.get()
                            ))

                            .displayItems((parameters, output) -> {
                                // consumables
                                output.accept(ModItems.WHITE_CRYSTALS.get());
                                output.accept(ModItems.WHITE_CRYSTALS_CHILI.get());
                                output.accept(ModItems.BLUE_CRYSTALS.get());
                                output.accept(ModItems.HERBAL_ROLL.get());
                                output.accept(ModItems.THICK_HERBAL_ROLL.get());
                                output.accept(ModItems.CIGARETTE.get());
                                output.accept(ModItems.BUBBLE_PIPE.get());
                                output.accept(ModItems.WOODEN_PIPE.get());

                                // tools
                                output.accept(ModItems.TRAY.get());
                                output.accept(ModItems.WHITE_OIL_TRAY.get());
                                output.accept(ModItems.BLUE_OIL_TRAY.get());
                                output.accept(ModItems.SCULK_CATALYST_CRYSTAL.get());
                                output.accept(ModFlasks.EMPTY_FLASK.get());
                                for (ModFlasks.FlaskEntry entry : ModFlasks.ALL_FLASK_ENTRIES) {
                                    output.accept(entry.flask().get());
                                }
                                for (ModFluids.ChemicalFluidSet fluid : ModFluids.ALL_FLUIDS) {
                                    output.accept(fluid.bucket().get());
                                }

                                // plants
                                output.accept(ModItems.EPHEDRA_BUNDLE.get());
                                output.accept(ModItems.EPHEDRA_SEEDS.get());
                                output.accept(ModItems.CHILI_PEPPER.get());
                                output.accept(ModItems.CHILI_SEEDS.get());
                                output.accept(ModItems.HERB_BUD.get());
                                output.accept(ModItems.DRIED_HERB_BUD.get());
                                output.accept(ModItems.HERB_SEEDS.get());
                                output.accept(ModItems.RIPE_TOBACCO_LEAF.get());
                                output.accept(ModItems.DRIED_TOBACCO_LEAF.get());
                                output.accept(ModItems.TOBACCO_SEEDS.get());

                                // chemicals
                                output.accept(ModItems.CYANIDE.get());
                                output.accept(ModItems.IODINE.get());
                                output.accept(ModItems.PSEUDO.get());
                                output.accept(ModItems.WHITE_PHOSPHORUS.get());
                                output.accept(ModItems.RED_PHOSPHORUS.get());

                                output.accept(ModItems.GAS_BOTTLE.get());
                                output.accept(ModItems.GAS_BOTTLE_OXYGEN.get());
                                output.accept(ModItems.GAS_BOTTLE_HYDROGEN.get());
                                output.accept(ModItems.GAS_BOTTLE_NITROGEN.get());
                            })

                            .build()
            );


    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}
