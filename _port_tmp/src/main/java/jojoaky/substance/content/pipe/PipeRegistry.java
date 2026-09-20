package jojoaky.substance.content.pipe;

import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class PipeRegistry {

    public static final ResourceKey<Registry<PipeSmokableItem>> PIPE_DATA_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(Substance.MOD_ID, "pipe_smokables"));

    public static final Registry<PipeSmokableItem> PIPE_DATA_REGISTRY =
            FabricRegistryBuilder.createSimple(PIPE_DATA_KEY)
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();

    public static PipeSmokableItem register(PipeSmokableItem entry) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(entry.item());
        return Registry.register(PIPE_DATA_REGISTRY, itemId, entry);
    }

    public static boolean isPipeSmokableItem(Item item) {
        return getItem(item) != null;
    }

    public static @Nullable PipeSmokableItem getItem(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return PIPE_DATA_REGISTRY.get(id);
    }

    public static void initialize() {
    }
}