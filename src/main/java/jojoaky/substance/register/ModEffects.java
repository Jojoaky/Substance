package jojoaky.substance.register;

import jojoaky.substance.Substance;
import jojoaky.substance.content.effects.KeenEffect;
import jojoaky.substance.content.effects.RelaxationEffect;
import jojoaky.substance.content.effects.SurgeEffect;
import jojoaky.substance.content.effects.VisualMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Substance.MOD_ID);

    public static final DeferredHolder<MobEffect, VisualMobEffect> HAZE = registerVisual(
            "haze", MobEffectCategory.NEUTRAL, 0xCCDDFF
    );
    public static final DeferredHolder<MobEffect, VisualMobEffect> WARP = registerVisual(
            "warp", MobEffectCategory.NEUTRAL, 0xAA88FF
    );
    public static final DeferredHolder<MobEffect, KeenEffect> KEEN = EFFECTS.register(
            "keen", () -> new KeenEffect(MobEffectCategory.BENEFICIAL, 0xEECC88)
    );
    public static final DeferredHolder<MobEffect, RelaxationEffect> RELAXATION = EFFECTS.register(
            "relaxation", () -> new RelaxationEffect(MobEffectCategory.BENEFICIAL, 0xD9C27A)
    );
    public static final DeferredHolder<MobEffect, SurgeEffect> SURGE = EFFECTS.register(
            "surge", () -> new SurgeEffect(MobEffectCategory.BENEFICIAL, 0xFFEE44)
    );
    public static final DeferredHolder<MobEffect, VisualMobEffect> HALLUCINATION = registerVisual(
            "hallucination", MobEffectCategory.NEUTRAL, 0xCC44CC
    );
    public static final DeferredHolder<MobEffect, VisualMobEffect> DREAD = registerVisual(
            "dread", MobEffectCategory.HARMFUL, 0x223344
    );

    private ModEffects() {
    }

    public static void register(IEventBus modEventBus) {
        EFFECTS.register(modEventBus);
    }

    private static DeferredHolder<MobEffect, VisualMobEffect> registerVisual(
            String name, MobEffectCategory category, int color
    ) {
        return EFFECTS.register(name, () -> new VisualMobEffect(category, color));
    }
}
