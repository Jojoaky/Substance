package jojoaky.substance.mixin;

import jojoaky.substance.register.ModEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    /**
     * Let vanilla's fall-flying lifecycle treat Surge as a usable Elytra. A real, usable Elytra is
     * returned unchanged so that it still takes its normal durability damage while flying.
     */
    @Redirect(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack useSurgeAsElytra(LivingEntity entity, EquipmentSlot slot) {
        ItemStack equipped = entity.getItemBySlot(slot);
        if (entity instanceof Player player
                && player.hasEffect(ModEffects.SURGE)
                && (!equipped.is(Items.ELYTRA) || !ElytraItem.isFlyEnabled(equipped))) {
            return new ItemStack(Items.ELYTRA);
        }
        return equipped;
    }
}
