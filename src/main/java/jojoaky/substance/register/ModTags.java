package jojoaky.substance.register;

import jojoaky.substance.Substance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
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

    public static final class EntityTypes {
        public static final TagKey<EntityType<?>> CAN_SMOKE = register(Registries.ENTITY_TYPE, "can_smoke");

        public static final TagKey<EntityType<?>> ZOMBIES = register(Registries.ENTITY_TYPE, "zombies");
        public static final TagKey<EntityType<?>> SKELETONS = register(Registries.ENTITY_TYPE, "skeletons");
        public static final TagKey<EntityType<?>> PIGLINS = register(Registries.ENTITY_TYPE, "piglins");
        public static final TagKey<EntityType<?>> ILLAGERS = register(Registries.ENTITY_TYPE, "illagers");
    }
}
