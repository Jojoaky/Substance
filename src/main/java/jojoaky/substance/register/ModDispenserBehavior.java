package jojoaky.substance.register;

import jojoaky.substance.content.flask.ModFlasks;
import jojoaky.substance.content.gas_bottle.EmptyGasBottleItem;
import jojoaky.substance.content.gas_bottle.FilledGasBottleItem;
import jojoaky.substance.content.tray.*;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;

public class ModDispenserBehavior {
    public static void initialize() {
        DispenserBlock.registerBehavior(ModItems.GAS_BOTTLE, new EmptyGasBottleItem.DispenserBehavior());
        DispenserBlock.registerBehavior(ModFlasks.EMPTY_FLASK, new EmptyingTrayDispenserBehavior());


        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof FilledGasBottleItem) {
                DispenserBlock.registerBehavior(item, new FilledGasBottleItem.DispenserBehavior());
            }

            if (ModTrays.getForFlask(item) != null) {
                DispenserBlock.registerBehavior(item, new FillingTrayDispenserBehavior());
            }

            if (item instanceof PickaxeItem) {
                DispenserBlock.registerBehavior(item, new ShatteringTrayDispenserBehavior());
            }

            if (item instanceof TrayItem || item instanceof EmptyTrayItem) {
                DispenserBlock.registerBehavior(item, new PlaceTrayBehavior());
            }
        }
    }
}
