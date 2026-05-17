package jojoaky.substance.register;

import jojoaky.substance.Substance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static <T> TagKey<T> register(ResourceKey<? extends Registry<T>> resourceKey, String name) {
        ResourceLocation id = Substance.resource(name);
        return TagKey.create(resourceKey, id);
    }

    public static void initialize() {

    }

    public static final TagKey<Item> SMOKABLE_ITEM = register(Registries.ITEM, "smokables");
    public static final TagKey<Item> DRUG_ITEM = register(Registries.ITEM, "drugs");
}
