package jojoaky.substance.content.consumable.framework;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PowderComponent implements ConsumableComponent {
    @Override
    public void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
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

    @Override
    public void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
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
