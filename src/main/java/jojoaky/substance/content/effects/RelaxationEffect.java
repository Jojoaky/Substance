package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class RelaxationEffect extends VisualMobEffect {
    public RelaxationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean isAppliedTo(LivingEntity entity) {
        return entity.hasEffect(this);
    }

    public void punishAttack(Player player) {
        if (!(player.level() instanceof ServerLevel level) || !isAppliedTo(player)) {
            return;
        }

        player.removeEffect(this);
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, Config.gameplay().relaxationDarknessDuration(), 0));

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning == null) {
            return;
        }

        lightning.moveTo(player.position());
        if (player instanceof ServerPlayer serverPlayer) {
            lightning.setCause(serverPlayer);
        }
        level.addFreshEntity(lightning);
    }
}
