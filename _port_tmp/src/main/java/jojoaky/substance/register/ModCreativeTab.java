package jojoaky.substance.register;

import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final ResourceKey<CreativeModeTab> SUSPICIOUS_ITEM_GROUP_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Substance.resource("item_group")
    );

    public static final CreativeModeTab SUSPICIOUS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.BLUE_CRYSTALS))
            .title(Component.translatable("itemGroup.substance"))
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SUSPICIOUS_ITEM_GROUP_KEY, SUSPICIOUS_ITEM_GROUP);
    }
}
