package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/** Drives registered trip visual types without knowing how they spawn or render. */
public final class TripVisuals {
    private static final RandomSource RANDOM = RandomSource.create();
    private static final List<TripVisual> ACTIVE_VISUALS = new ArrayList<>();
    private static final Map<TripVisualType, Integer> SPAWN_TIMERS = new IdentityHashMap<>();
    private static ClientLevel level;

    private TripVisuals() {
    }

    public static boolean isRendering(LivingEntity entity) {
        return TripVisualRenderer.isRendering(entity);
    }

    public static void init() {
        resetTimers();
        ClientTickEvents.END_CLIENT_TICK.register(TripVisuals::tick);
        WorldRenderEvents.AFTER_ENTITIES.register(TripVisuals::render);
    }

    private static boolean enabled(Minecraft minecraft) {
        return minecraft.level != null && minecraft.player != null && minecraft.player.isAlive()
                && Config.get().enableShaderEffects && Config.get().visualEffectStrength > 0;
    }

    private static void tick(Minecraft minecraft) {
        if (level != minecraft.level || !enabled(minecraft)) {
            reset(minecraft.level);
        }
        if (!enabled(minecraft) || minecraft.isPaused()) {
            return;
        }

        Config config = Config.get();
        ACTIVE_VISUALS.removeIf(visual -> !visual.type().isActive(minecraft, config));
        ACTIVE_VISUALS.forEach(visual -> visual.tick(minecraft, level, config));
        ACTIVE_VISUALS.removeIf(visual -> visual.shouldRemove(minecraft, level, config));

        for (TripVisualType type : TripVisualRegistry.values()) {
            if (!type.isActive(minecraft, config)) {
                continue;
            }

            int timer = SPAWN_TIMERS.getOrDefault(type, type.initialDelay()) - 1;
            if (timer <= 0) {
                timer = nextInterval(type.intervalSeconds(config));
                long count = ACTIVE_VISUALS.stream().filter(visual -> visual.type() == type).count();
                if (count < Mth.clamp(type.maxInstances(config), 0, 20)) {
                    type.spawn(minecraft, level, config, RANDOM, ACTIVE_VISUALS::add);
                }
            }
            SPAWN_TIMERS.put(type, timer);
        }
    }

    private static void render(WorldRenderContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!enabled(minecraft) || level != minecraft.level || context.consumers() == null) {
            return;
        }

        Config config = Config.get();
        for (TripVisual visual : ACTIVE_VISUALS) {
            if (visual.type().isActive(minecraft, config)) {
                visual.render(context, minecraft, level, config);
            }
        }
    }

    private static int nextInterval(float seconds) {
        int average = Math.max(1, Math.round(Mth.clamp(seconds, 0.5f, 60.0f) * 20));
        int variance = Math.max(1, average * 2 / 5);
        return average - variance + RANDOM.nextInt(variance * 2 + 1);
    }

    private static void reset(ClientLevel newLevel) {
        ACTIVE_VISUALS.clear();
        level = newLevel;
        resetTimers();
    }

    private static void resetTimers() {
        SPAWN_TIMERS.clear();
        for (TripVisualType type : TripVisualRegistry.values()) {
            SPAWN_TIMERS.put(type, type.initialDelay());
        }
    }
}
