package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

public final class KeenEffect extends VisualMobEffect {
    public KeenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public float applyMiningSpeed(Player player, float speed) {
        return player.hasEffect(this) ? speed * Config.gameplay().keenMiningSpeedMultiplier() : speed;
    }
}
