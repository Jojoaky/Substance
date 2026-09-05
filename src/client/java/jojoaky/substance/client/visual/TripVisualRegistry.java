package jojoaky.substance.client.visual;

import net.minecraft.world.effect.MobEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TripVisualRegistry {
    private static final Map<MobEffect, TripVisualType> TYPES = new LinkedHashMap<>();

    static {
        register(new HallucinationTripVisualType());
        register(new DreadTripVisualType());
    }

    private TripVisualRegistry() {
    }

    public static <T extends TripVisualType> T register(T type) {
        if (TYPES.putIfAbsent(type.effect(), type) != null) {
            throw new IllegalArgumentException("A trip visual is already registered for " + type.effect());
        }
        return type;
    }

    public static Collection<TripVisualType> values() {
        return Collections.unmodifiableCollection(TYPES.values());
    }
}
