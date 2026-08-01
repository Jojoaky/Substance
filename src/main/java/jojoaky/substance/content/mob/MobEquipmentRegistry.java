package jojoaky.substance.content.mob;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class MobEquipmentRegistry {
    private static final String DIRECTORY = "mob_equipment";
    private static final LootContextParamSet MOB_EQUIPMENT_LOOT_PARAMS = LootContextParamSet.builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .build();

    private static volatile List<MobEquipment> EQUIPMENT_DEFINITIONS = List.of();

    private static final Codec<EquipmentSlot> SLOT_CODEC = Codec.STRING.comapFlatMap(
            name -> {
                try {
                    return DataResult.success(EquipmentSlot.byName(name));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Unknown equipment slot: " + name);
                }
            },
            EquipmentSlot::getName
    );

    private MobEquipmentRegistry() {
    }

    public static void initialize() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new Loader());
    }

    public static void applyRandomEquipment(Mob mob) {
        if (!(mob.level() instanceof ServerLevel serverLevel)) return;

        for (MobEquipment definition : EQUIPMENT_DEFINITIONS) {
            if (!definition.matches(mob.getType())) {
                continue;
            }

            for (Map.Entry<EquipmentSlot, ResourceLocation> entry : definition.equipment().entrySet()) {
                List<ItemStack> generatedItems = generateItems(serverLevel, mob, entry.getValue());

                if (generatedItems.isEmpty()) {
                    continue;
                }

                if (generatedItems.size() > 1) {
                    Substance.LOGGER.error(
                            "Mob equipment loot table {} for definition {} generated {} items; expected at most one.",
                            entry.getValue(),
                            definition.id(),
                            generatedItems.size()
                    );
                    continue;
                }

                mob.setItemSlot(entry.getKey(), generatedItems.get(0));
            }
        }
    }

    private static List<ItemStack> generateItems(ServerLevel level, Mob mob, ResourceLocation lootTableId) {
        LootTable lootTable = level.getServer().getLootData().getLootTable(lootTableId);
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, mob)
                .withParameter(LootContextParams.ORIGIN, mob.position())
                .create(MOB_EQUIPMENT_LOOT_PARAMS);
        return lootTable.getRandomItems(params);
    }

    private static void replaceDefinitions(List<MobEquipment> definitions) {
        EQUIPMENT_DEFINITIONS = List.copyOf(definitions);
    }

    private record RawMobEquipment(List<String> entities, Map<EquipmentSlot, ResourceLocation> equipment) {
        public static final Codec<RawMobEquipment> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec
                                .STRING
                                .listOf()
                                .fieldOf("entities")
                                .forGetter(RawMobEquipment::entities),
                        Codec
                                .unboundedMap(SLOT_CODEC, ResourceLocation.CODEC)
                                .fieldOf("equipment")
                                .forGetter(RawMobEquipment::equipment)
                ).apply(instance, RawMobEquipment::new)
        );

        public Optional<MobEquipment> toMobEquipment(ResourceLocation id) {
            List<Predicate<EntityType<?>>> predicates = new ArrayList<>();
            int priority = 0;

            for (String rawEntity : entities) {
                if (rawEntity.startsWith("#")) {
                    ResourceLocation tagId = ResourceLocation.tryParse(rawEntity.substring(1));
                    if (tagId != null) {
                        TagKey<EntityType<?>> tagKey = TagKey.create(Registries.ENTITY_TYPE, tagId);
                        predicates.add(type -> type.is(tagKey));
                    }
                } else {
                    ResourceLocation entityId = ResourceLocation.tryParse(rawEntity);
                    if (entityId != null && BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
                        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entityId);
                        priority++;
                        predicates.add(type -> type == entityType);
                    }
                }
            }

            if (predicates.isEmpty()) {
                Substance.LOGGER.error("Skipping mob equipment definition {}: no valid entities found", id);
                return Optional.empty();
            }

            Predicate<EntityType<?>> combinedPredicate = type -> predicates.stream().anyMatch(p -> p.test(type));
            return Optional.of(new MobEquipment(id, priority, combinedPredicate, equipment));
        }
    }

    private static final class Loader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
        private Loader() {
            super(Substance.GSON, DIRECTORY);
        }

        @Override
        public ResourceLocation getFabricId() {
            return Substance.resource(DIRECTORY);
        }

        @Override
        protected void apply(Map<ResourceLocation, com.google.gson.JsonElement> loaded, @NotNull ResourceManager resourceManager, ProfilerFiller profiler) {
            List<MobEquipment> definitions = new ArrayList<>();

            loaded.forEach((id, jsonElement) -> {
                RawMobEquipment.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                        .resultOrPartial(error -> Substance.LOGGER.error("Error parsing mob equipment {}: {}", id, error))
                        .flatMap(raw -> raw.toMobEquipment(id))
                        .ifPresent(definitions::add);
            });

            definitions.sort(Comparator
                    .comparingInt(MobEquipment::priority)
                    .thenComparing(MobEquipment::id));

            replaceDefinitions(definitions);
            Substance.LOGGER.info("Loaded {} mob equipment definitions", definitions.size());
        }
    }
}