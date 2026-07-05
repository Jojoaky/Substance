package jojoaky.substance.content.consumable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PowderConsumableItem extends ConsumableItem {
    public PowderConsumableItem(Properties properties) {
        super(properties, 64, 40);
    }

    @Override
    @NotNull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    protected void onStartConsuming(ItemStack stack, Level level, LivingEntity entity) {}

    protected void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (level.random.nextFloat() < 0.15f) {
            level.playSound(
                    null,
                    entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.SAND_BREAK,
                    SoundSource.PLAYERS,
                    0.18f,
                    1.4f + level.random.nextFloat() * 0.4f
            );
        }
    }

    protected void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        level.playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                0.6f,
                0.8f + level.random.nextFloat() * 0.4f
        );
    }
}