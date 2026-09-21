package jojoaky.substance.content.consumable.framework;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PowderComponent implements ConsumableComponent {
    private static final int FIRST_SNIFF_TICK = 6;
    private static final int SNIFF_INTERVAL_TICKS = 10;

    @Override
    public void onConsumeTick(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        // Broadcast once from the server, in time with the animation's inhale pulses.
        if (!level.isClientSide && useDuration >= FIRST_SNIFF_TICK
                && (useDuration - FIRST_SNIFF_TICK) % SNIFF_INTERVAL_TICKS == 0
                && useDuration < stack.getUseDuration() - 5) {
            level.playSound(
                    null,
                    entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.HORSE_BREATHE,
                    SoundSource.PLAYERS,
                    0.22f,
                    1.65f + level.random.nextFloat() * 0.1f
            );
        }
    }

    @Override
    public void onStopConsuming(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        if (level.isClientSide || useDuration < FIRST_SNIFF_TICK) return;

        level.playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.AMETHYST_BLOCK_STEP,
                SoundSource.PLAYERS,
                0.25f,
                1.1f + level.random.nextFloat() * 0.1f
        );
    }
}
