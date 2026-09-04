package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import jojoaky.substance.register.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class GameplayEffects {
    public static final String SURGE_MOVEMENT_SPEED_MODIFIER_UUID = "8a30c778-1851-4ef4-881e-669f27ef9b21";

    private GameplayEffects() {
    }

    public static boolean isRelaxed(LivingEntity entity) {
        return entity.hasEffect(ModEffects.RELAXATION);
    }

    public static float applyKeenMiningSpeed(Player player, float speed) {
        return player.hasEffect(ModEffects.KEEN) ? speed * Config.get().keenMiningSpeedMultiplier : speed;
    }

    public static boolean tryStartSurgeFallFlying(Player player) {
        if (!player.hasEffect(ModEffects.SURGE)
                || player.onGround()
                || player.isFallFlying()
                || player.isInWater()
                || player.hasEffect(MobEffects.LEVITATION)) {
            return false;
        }

        player.startFallFlying();
        return true;
    }

    public static void applySurgeElytraBoost(Player player) {
        if (player.hasEffect(ModEffects.SURGE) && player.isFallFlying()) {
            player.setDeltaMovement(player.getDeltaMovement().add(
                    player.getLookAngle().scale(Config.get().surgeElytraBoost)
            ));
        }
    }

    public static void punishRelaxedAttack(Player player) {
        if (!(player.level() instanceof ServerLevel level) || !isRelaxed(player)) {
            return;
        }

        player.removeEffect(ModEffects.RELAXATION);
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, Config.get().relaxationDarknessDuration, 0));

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
