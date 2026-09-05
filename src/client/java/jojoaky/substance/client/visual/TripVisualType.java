package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

/** A registry entry that owns the rules for one effect's trip visuals. */
public abstract class TripVisualType {
    private final MobEffect effect;
    private final int initialDelay;

    protected TripVisualType(MobEffect effect, int initialDelay) {
        this.effect = effect;
        this.initialDelay = initialDelay;
    }

    public final MobEffect effect() {
        return effect;
    }

    public final int initialDelay() {
        return initialDelay;
    }

    public final boolean isActive(Minecraft minecraft, Config config) {
        return minecraft.player != null && isEnabled(config) && minecraft.player.hasEffect(effect);
    }

    protected abstract boolean isEnabled(Config config);

    public abstract int maxInstances(Config config);

    public abstract float intervalSeconds(Config config);

    public abstract float strength(Config config);

    public abstract void spawn(
            Minecraft minecraft,
            ClientLevel level,
            Config config,
            RandomSource random,
            Consumer<TripVisual> sink
    );

    protected final LivingEntity createEntity(ClientLevel level, EntityType<?> entityType, Vec3 position) {
        if (!level.hasChunkAt(BlockPos.containing(position))) {
            return null;
        }
        if (!(entityType.create(level) instanceof LivingEntity entity)) {
            return null;
        }

        entity.setPos(position);
        entity.xo = position.x;
        entity.yo = position.y;
        entity.zo = position.z;
        return entity;
    }
}
