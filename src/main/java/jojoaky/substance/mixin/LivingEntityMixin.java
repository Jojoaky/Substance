package jojoaky.substance.mixin;

import jojoaky.substance.register.ModEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    /**
     * Let vanilla's fall-flying lifecycle treat Surge as a usable Elytra. Usable vanilla or
     * Fabric-compatible custom flight equipment is left in control of the flight lifecycle.
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
        if (entity instanceof Player player) {
            return ModEffects.SURGE.useAsElytra(player, equipped);
        }
        return equipped;
    }
}
