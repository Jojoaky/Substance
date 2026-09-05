package jojoaky.substance.content.mob.equipment;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import jojoaky.substance.Substance;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MobEquipmentReloadListener extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private final Consumer<List<MobEquipment>> consumer;

    public MobEquipmentReloadListener(Consumer<List<MobEquipment>> consumer) {
        super(Substance.GSON, MobEquipmentRegistry.DIRECTORY);
        this.consumer = consumer;
    }

    @Override
    public ResourceLocation getFabricId() {
        return Substance.resource(MobEquipmentRegistry.DIRECTORY);
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> loaded,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profiler
    ) {
        List<MobEquipment> definitions = new ArrayList<>();

        loaded.forEach((id, jsonElement) ->
                RawMobEquipment.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                        .resultOrPartial(error -> Substance.LOGGER.error("Error parsing mob equipment {}: {}", id, error))
                        .ifPresent(raw -> definitions.addAll(raw.toMobEquipment(id)))
        );

        definitions.sort(Comparator
                .comparingInt(MobEquipment::priority).reversed()
                .thenComparing(MobEquipment::id));

        consumer.accept(definitions);
        Substance.LOGGER.info("Loaded {} mob equipment definitions", definitions.size());
    }
}