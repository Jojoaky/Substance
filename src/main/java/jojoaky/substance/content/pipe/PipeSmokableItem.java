package jojoaky.substance.content.pipe;

import jojoaky.substance.util.StackingEffect;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public record PipeSmokableItem(Item item, Consumer<PipeSmokableConsumeContext> onConsume) {

    public static PipeSmokableItem effectGiving(Item item, StackingEffect... effects) {
        return new PipeSmokableItem(item, context -> {
            for (StackingEffect stackingEffect : effects) {
                SubstanceEffectHelper.applyStackingEffect(
                        context.entity(),
                        context.consumeDuration(),
                        stackingEffect
                );
            }
        });
    }
}
