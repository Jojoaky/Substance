package jojoaky.substance.mixin;

import jojoaky.substance.content.effects.GameplayEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void applySurgeElytraBoost(CallbackInfo ci) {
        GameplayEffects.applySurgeElytraBoost((Player) (Object) this);
    }

    @Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
    private void allowSurgeFallFlying(CallbackInfoReturnable<Boolean> cir) {
        if (GameplayEffects.tryStartSurgeFallFlying((Player) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void applyKeenMiningSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        cir.setReturnValue(GameplayEffects.applyKeenMiningSpeed(player, cir.getReturnValue()));
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void punishRelaxedAttack(Entity target, CallbackInfo ci) {
        GameplayEffects.punishRelaxedAttack((Player) (Object) this);
    }
}
