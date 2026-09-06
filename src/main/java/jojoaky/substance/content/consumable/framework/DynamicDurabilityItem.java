package jojoaky.substance.content.consumable.framework;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface DynamicDurabilityItem {
    int getMaxDamage(Item item, ItemStack stack);
}