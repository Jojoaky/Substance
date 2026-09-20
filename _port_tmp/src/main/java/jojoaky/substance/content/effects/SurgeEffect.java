package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import jojoaky.substance.config.ConfigDependentEffect;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class SurgeEffect extends VisualMobEffect implements ConfigDependentEffect {
    public static final String MOVEMENT_SPEED_MODIFIER_UUID = "8a30c778-1851-4ef4-881e-669f27ef9b21";
    private static final UUID MOVEMENT_SPEED_MODIFIER_ID = UUID.fromString(MOVEMENT_SPEED_MODIFIER_UUID);
    private static final double STARTUP_BOOST_MULTIPLIER = 3.0;

    public SurgeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        AttributeInstance movementSpeed = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }

        movementSpeed.removeModifier(MOVEMENT_SPEED_MODIFIER_ID);
        movementSpeed.addTransientModifier(new AttributeModifier(
                MOVEMENT_SPEED_MODIFIER_ID,
                "Substance surge movement speed bonus",
                Config.gameplay().surgeMovementSpeedBonus() * (amplifier + 1),
                AttributeModifier.Operation.MULTIPLY_TOTAL
        ));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        AttributeInstance movementSpeed = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.removeModifier(MOVEMENT_SPEED_MODIFIER_ID);
        }
    }

    @Override
    public void refreshAfterConfigChange(LivingEntity entity, int amplifier) {
        addAttributeModifiers(entity, entity.getAttributes(), amplifier);
    }

    public boolean tryStartFallFlying(Player player) {
        if (!player.hasEffect(this)
                || player.onGround()
                || player.isFallFlying()
                || player.isInWater()
                || player.hasEffect(MobEffects.LEVITATION)) {
            return false;
        }

        player.startFallFlying();
        return true;
    }

    public void applyElytraBoost(Player player) {
        MobEffectInstance surge = player.getEffect(this);
        if (surge == null || !player.isFallFlying()) {
            return;
        }

        var config = Config.gameplay();
        double maxSpeed = config.surgeElytraMaxSpeed()
                + surge.getAmplifier() * config.surgeElytraMaxSpeedPerLevel();
        if (maxSpeed <= 0.0) {
            return;
        }

        Vec3 velocity = player.getDeltaMovement();
        Vec3 lookDirection = player.getLookAngle();
        double speedInBoostDirection = velocity.dot(lookDirection);
        if (speedInBoostDirection >= maxSpeed) {
            return;
        }

        double speedRatio = Math.max(speedInBoostDirection / maxSpeed, 0.0);
        double boostMultiplier = STARTUP_BOOST_MULTIPLIER
                - (STARTUP_BOOST_MULTIPLIER - 1.0) * speedRatio;
        double boost = Math.min(
                config.surgeElytraBoost() * boostMultiplier,
                maxSpeed - speedInBoostDirection
        );
        player.setDeltaMovement(velocity.add(lookDirection.scale(boost)));
    }

    public boolean isFlyingWithoutUsableElytra(Player player) {
        return player.hasEffect(this)
                && player.isFallFlying()
                && !hasUsableFlightEquipment(player);
    }

    private boolean hasUsableFlightEquipment(Player player) {
        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        return (chestItem.is(Items.ELYTRA) && ElytraItem.isFlyEnabled(chestItem))
                || EntityElytraEvents.CUSTOM.invoker().useCustomElytra(player, false);
    }

    public ItemStack useAsElytra(Player player, ItemStack equipped) {
        if (player.hasEffect(this)
                && !hasUsableFlightEquipment(player)) {
            return new ItemStack(Items.ELYTRA);
        }
        return equipped;
    }
}
