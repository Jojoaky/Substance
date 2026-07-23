package jojoaky.substance.content.pipe;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record PipeSmokableConsumeContext(
        ItemStack pipeItem,
        ItemStack ingredient,
        Level level,
        LivingEntity entity,
        int consumeDuration
) {
}