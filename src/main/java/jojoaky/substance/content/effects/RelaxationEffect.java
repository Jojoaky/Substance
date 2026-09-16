package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;

public final class RelaxationEffect extends VisualMobEffect {
    public RelaxationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean isAppliedTo(LivingEntity entity) {
        return entity.hasEffect(this);
    }

    public void punishAttack(Entity source, Entity target) {
        if (!(source.level() instanceof ServerLevel level)) return;

        if (!(source instanceof Player player) || !isAppliedTo(player)) return;

        if (source == target) return;

        boolean isLiving = target instanceof Mob || target instanceof Player;
        if (!isLiving) return;

        player.removeEffect(this);
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, Config.gameplay().relaxationDarknessDuration(), 0));

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning == null) {
            return;
        }

        lightning.moveTo(player.position());
        lightning.setVisualOnly(true);
        level.addFreshEntity(lightning);
    }
}
