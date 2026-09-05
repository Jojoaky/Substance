package jojoaky.substance.mixin;

import jojoaky.substance.content.mob.equipment.MobEquipmentRegistry;
import jojoaky.substance.content.mob.MobUseConsumableGoal;
import jojoaky.substance.register.ModEffects;
import jojoaky.substance.register.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Shadow @Final protected GoalSelector goalSelector;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addConsumableGoal(EntityType<? extends Mob> entityType, Level level, CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob.getType().is(ModTags.EntityTypes.CAN_SMOKE)) {
            this.goalSelector.addGoal(3, new MobUseConsumableGoal(mob));
        }
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void preventTargetingRelaxedEntities(@Nullable LivingEntity target, CallbackInfo ci) {
        if (target != null && ModEffects.RELAXATION.isAppliedTo(target)) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void forgetRelaxedTarget(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob.getTarget() != null && ModEffects.RELAXATION.isAppliedTo(mob.getTarget())) {
            mob.setTarget(null);
        }
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void markForCustomEquipment(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroupData,
            @Nullable CompoundTag tag,
            CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        Mob mob = (Mob) (Object) this;

        if (level.getServer() == null) return;

        level.getServer().execute(() -> {
            if (!mob.isAlive()) return;
            MobEquipmentRegistry.applyRandomEquipment(mob);
        });
    }
}
