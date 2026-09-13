package jojoaky.substance.register;

import jojoaky.substance.Config;
import jojoaky.substance.content.pipe.PipeRegistry;
import jojoaky.substance.content.pipe.PipeSmokableItem;
import jojoaky.substance.util.StackingEffect;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;

public class ModPipeIngredients {
    public static void initialize() {
        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.DRIED_HERB_BUD,
                new StackingEffect(ModEffects.RELAXATION, 8, 1000, 4)
        ));

        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.DRIED_TOBACCO_LEAF,
                new StackingEffect(ModEffects.KEEN, 6, 550, 3)
        ));

        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.WHITE_CRYSTALS,
                new StackingEffect(ModEffects.SURGE, 8, 1000, 1)
        ));

        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.BLUE_CRYSTALS,
                new StackingEffect(ModEffects.SURGE, 8, 700, 3)
        ));

        PipeRegistry.register(new PipeSmokableItem(
                Items.RED_MUSHROOM,
                (context) -> {
                    boolean horrorTrip = context.level().random.nextFloat() < Config.gameplay().horrorTripChance();

                    if (horrorTrip) {
                        if (context.entity().hasEffect(ModEffects.HALLUCINATION)) {
                            context.entity().removeEffect(ModEffects.HALLUCINATION);
                        }

                        context.level().playSound(
                                context.entity(),
                                context.entity().blockPosition(),
                                SoundEvents.LIGHTNING_BOLT_THUNDER,
                                SoundSource.PLAYERS,
                                5.0f,
                                0.8f
                        );

                        SubstanceEffectHelper.applyEffectBase(
                                context.entity(),
                                ModEffects.DREAD,
                                context.consumeDuration() * 5,
                                0
                        );
                    } else {
                        SubstanceEffectHelper.applyStackingEffect(
                                context.entity(),
                                context.consumeDuration(),
                                new StackingEffect(ModEffects.HALLUCINATION, 8, 1000, 1)
                        );
                    }
                }
        ));
    }
}
