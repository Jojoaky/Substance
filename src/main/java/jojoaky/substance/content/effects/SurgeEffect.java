package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import jojoaky.substance.Substance;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class SurgeEffect extends VisualMobEffect {
    private static final ResourceLocation MOVEMENT_SPEED_MODIFIER_ID =
            Substance.resource("surge_movement_speed_bonus");
    private static final double STARTUP_BOOST_MULTIPLIER = 3.0;

    public SurgeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributes, int amplifier) {
        AttributeInstance movementSpeed = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }

        movementSpeed.removeModifier(MOVEMENT_SPEED_MODIFIER_ID);
        movementSpeed.addTransientModifier(new AttributeModifier(
                MOVEMENT_SPEED_MODIFIER_ID,
                Config.gameplay().surgeMovementSpeedBonus() * (amplifier + 1),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        ));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributes) {
        AttributeInstance movementSpeed = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.removeModifier(MOVEMENT_SPEED_MODIFIER_ID);
        }
    }

    public boolean tryStartFallFlying(Player player) {
        if (!player.hasEffect(effectHolder())
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
        MobEffectInstance surge = player.getEffect(effectHolder());
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
        return player.hasEffect(effectHolder())
                && player.isFallFlying()
                && !hasUsableFlightEquipment(player);
    }

    private boolean hasUsableFlightEquipment(Player player) {
        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        return chestItem.canElytraFly(player);
    }

    public ItemStack useAsElytra(Player player, ItemStack equipped) {
        if (player.hasEffect(effectHolder())
                && !hasUsableFlightEquipment(player)) {
            return new ItemStack(Items.ELYTRA);
        }
        return equipped;
    }

    private Holder<MobEffect> effectHolder() {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this);
    }
}
