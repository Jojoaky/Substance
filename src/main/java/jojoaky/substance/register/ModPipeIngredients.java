package jojoaky.substance.register;

import jojoaky.substance.Config;
import jojoaky.substance.content.pipe.PipeRegistry;
import jojoaky.substance.content.pipe.PipeSmokableItem;
import jojoaky.substance.util.StackingEffect;
import jojoaky.substance.util.SubstanceEffectHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public final class ModPipeIngredients {
    private ModPipeIngredients() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModPipeIngredients::initialize);
    }

    private static void initialize(FMLCommonSetupEvent event) {
        event.enqueueWork(ModPipeIngredients::registerIngredients);
    }

    private static void registerIngredients() {
        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.DRIED_HERB_BUD.get(),
                new StackingEffect(ModEffects.RELAXATION.get(), 8, 1000, 4)
        ));
        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.DRIED_TOBACCO_LEAF.get(),
                new StackingEffect(ModEffects.KEEN.get(), 6, 550, 3)
        ));
        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.WHITE_CRYSTALS.get(),
                new StackingEffect(ModEffects.SURGE.get(), 8, 1000, 0)
        ));
        PipeRegistry.register(new PipeSmokableItem(
                ModItems.WHITE_CRYSTALS_CHILI.get(),
                context -> {
                    SubstanceEffectHelper.applyStackingEffect(
                            context.entity(),
                            context.consumeDuration(),
                            new StackingEffect(ModEffects.SURGE.get(), 8, 1000, 1)
                    );
                    if (!context.level().isClientSide) {
                        context.entity().hurt(context.level().damageSources().magic(), 1.0F);
                    }
                }
        ));
        PipeRegistry.register(PipeSmokableItem.effectGiving(
                ModItems.BLUE_CRYSTALS.get(),
                new StackingEffect(ModEffects.SURGE.get(), 8, 700, 3)
        ));
        PipeRegistry.register(new PipeSmokableItem(
                Items.RED_MUSHROOM,
                context -> {
                    if (context.level().random.nextFloat() < Config.gameplay().horrorTripChance()) {
                        if (context.entity().hasEffect(ModEffects.HALLUCINATION)) {
                            context.entity().removeEffect(ModEffects.HALLUCINATION);
                        }
                        context.level().playSound(
                                context.entity(),
                                context.entity().blockPosition(),
                                SoundEvents.LIGHTNING_BOLT_THUNDER,
                                SoundSource.PLAYERS,
                                5.0F,
                                0.8F
                        );
                        SubstanceEffectHelper.applyEffectBase(
                                context.entity(),
                                ModEffects.DREAD.get(),
                                context.consumeDuration() * 5,
                                0
                        );
                    } else {
                        SubstanceEffectHelper.applyStackingEffect(
                                context.entity(),
                                context.consumeDuration(),
                                new StackingEffect(ModEffects.HALLUCINATION.get(), 8, 1000, 1)
                        );
                    }
                }
        ));
    }
}
