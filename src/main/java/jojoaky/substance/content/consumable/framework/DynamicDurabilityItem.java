package jojoaky.substance.content.consumable.framework;

import net.minecraft.world.item.ItemStack;

public interface DynamicDurabilityItem {
    int getMaxDamage(ItemStack stack);
}
