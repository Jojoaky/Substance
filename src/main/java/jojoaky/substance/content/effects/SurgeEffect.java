package jojoaky.substance.content.effects;

import jojoaky.substance.Config;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class SurgeEffect extends FixedStrengthMobEffect {
    public static final String MOVEMENT_SPEED_MODIFIER_UUID = "8a30c778-1851-4ef4-881e-669f27ef9b21";

    public SurgeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public SurgeEffect addAttributeModifier(
            Attribute attribute,
            String uuid,
            double amount,
            AttributeModifier.Operation operation
    ) {
        super.addAttributeModifier(attribute, uuid, amount, operation);
        return this;
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
        if (player.hasEffect(this) && player.isFallFlying()) {
            player.setDeltaMovement(player.getDeltaMovement().add(
                    player.getLookAngle().scale(Config.get().surgeElytraBoost)
            ));
        }
    }

    public ItemStack useAsElytra(Player player, ItemStack equipped) {
        if (player.hasEffect(this)
                && (!equipped.is(Items.ELYTRA) || !ElytraItem.isFlyEnabled(equipped))) {
            return new ItemStack(Items.ELYTRA);
        }
        return equipped;
    }
}
