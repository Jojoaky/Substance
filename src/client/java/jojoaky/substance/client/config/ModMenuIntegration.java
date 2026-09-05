package jojoaky.substance.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import jojoaky.substance.Config;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.config.substance.title"))
                .save(() -> Config.HANDLER.save())
                .category(buildClientCategory())
                .category(buildGameplayCategory())
                .build()
                .generateScreen(parentScreen);
    }

    private ConfigCategory buildGameplayCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.config.substance.category.gameplay"))
                .tooltip(Component.translatable("text.config.substance.category.gameplay.tooltip"))

                // Durabilities Group
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.config.substance.group.durabilities"))
                        .description(OptionDescription.of(Component.translatable("text.config.substance.group.durabilities.desc")))

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.woodenPipeDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(2048, () -> Config.get().woodenPipeDurability, val -> Config.get().woodenPipeDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.bubblePipeDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(512, () -> Config.get().bubblePipeDurability, val -> Config.get().bubblePipeDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.herbalRollDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(460, () -> Config.get().herbalRollDurability, val -> Config.get().herbalRollDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.thickHerbalRollDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(570, () -> Config.get().thickHerbalRollDurability, val -> Config.get().thickHerbalRollDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.cigaretteDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(520, () -> Config.get().cigaretteDurability, val -> Config.get().cigaretteDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())
                        .build())

                // Mechanics Group
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.config.substance.group.mechanics"))
                        .description(OptionDescription.of(Component.translatable("text.config.substance.group.mechanics.desc")))

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.maxSmokeDuration"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.maxSmokeDuration.desc")))
                                .binding(6.0f, () -> Config.get().maxSmokeDuration, val -> Config.get().maxSmokeDuration = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.5f, 120.0f).step(0.5f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.smokeCooldown"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.smokeCooldown.desc")))
                                .binding(1.5f, () -> Config.get().smokeCooldown, val -> Config.get().smokeCooldown = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 30.0f).step(0.1f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.maxSniffDuration"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.maxSniffDuration.desc")))
                                .binding(4.0f, () -> Config.get().maxSniffDuration, val -> Config.get().maxSniffDuration = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.5f, 30.0f).step(0.5f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.sniffCooldown"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.sniffCooldown.desc")))
                                .binding(3.0f, () -> Config.get().sniffCooldown, val -> Config.get().sniffCooldown = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 30.0f).step(0.1f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        // Added missing pipe item consumption probability option
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.pipeItemConsumeProbability"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.pipeItemConsumeProbability.desc")))
                                .binding(0.4f, () -> Config.get().pipeItemConsumeProbability, val -> Config.get().pipeItemConsumeProbability = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 1.0f).step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("%.0f%%", val * 100))))
                                .build())
                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.config.substance.group.effectBehavior"))
                        .description(OptionDescription.of(Component.translatable("text.config.substance.group.effectBehavior.desc")))

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.surgeMovementSpeedBonus"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.surgeMovementSpeedBonus.desc")))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(0.3f, () -> Config.get().surgeMovementSpeedBonus, val -> Config.get().surgeMovementSpeedBonus = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 2.0f).step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("+%.0f%%", val * 100))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.surgeElytraBoost"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.surgeElytraBoost.desc")))
                                .binding(0.02f, () -> Config.get().surgeElytraBoost, val -> Config.get().surgeElytraBoost = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 0.2f).step(0.005f)
                                        .formatValue(val -> Component.literal(String.format("%.3f/tick", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.keenMiningSpeedMultiplier"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.keenMiningSpeedMultiplier.desc")))
                                .binding(3.0f, () -> Config.get().keenMiningSpeedMultiplier, val -> Config.get().keenMiningSpeedMultiplier = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(1.0f, 10.0f).step(0.25f)
                                        .formatValue(val -> Component.literal(String.format("%.2fx", val))))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.relaxationDarknessDuration"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.relaxationDarknessDuration.desc")))
                                .binding(200, () -> Config.get().relaxationDarknessDuration, val -> Config.get().relaxationDarknessDuration = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 1200).step(20)
                                        .formatValue(val -> Component.literal(String.format("%.0f s", val / 20.0f))))
                                .build())
                        .build())

                .option(Option.<Float>createBuilder()
                        .name(Component.translatable("text.config.substance.option.horrorTripChance"))
                        .description(OptionDescription.of(Component.translatable("text.config.substance.option.horrorTripChance.desc")))
                        .binding(
                                1.0f,
                                () -> Config.get().horrorTripChance,
                                val -> Config.get().horrorTripChance = val
                        )
                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                .range(0.0f, 1.0f)
                                .step(0.01f)
                                .formatValue(val -> Component.literal(String.format("%.1f%%", val * 100))))
                        .build())
                .build();
    }

    private ConfigCategory buildClientCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.config.substance.category.client"))
                .tooltip(Component.translatable("text.config.substance.category.client.tooltip"))
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.config.substance.group.immersion"))
                        .description(OptionDescription.of(Component.translatable("text.config.substance.group.immersion.desc")))

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("text.config.substance.option.enableShaderEffects"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.enableShaderEffects.desc")))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(
                                        true,
                                        () -> Config.get().enableShaderEffects,
                                        val -> Config.get().enableShaderEffects = val
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.visualEffectStrength"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.visualEffectStrength.desc")))
                                .binding(
                                        1.0f,
                                        () -> Config.get().visualEffectStrength,
                                        val -> Config.get().visualEffectStrength = val
                                )
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 2.0f)
                                        .step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("%.0f%%", val * 100))))
                                .build())

                        // Added missing menu visuals toggle option
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("text.config.substance.option.visualEffectsInMenus"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.visualEffectsInMenus.desc")))
                                .binding(
                                        true,
                                        () -> Config.get().visualEffectsInMenus,
                                        val -> Config.get().visualEffectsInMenus = val
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("text.config.substance.option.enableAudioEffects"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.enableAudioEffects.desc")))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(
                                        true,
                                        () -> Config.get().enableAudioEffects,
                                        val -> Config.get().enableAudioEffects = val
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.audioEffectStrength"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.audioEffectStrength.desc")))
                                .binding(
                                        1.0f,
                                        () -> Config.get().audioEffectStrength,
                                        val -> Config.get().audioEffectStrength = val
                                )
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 2.0f)
                                        .step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("%.0f%%", val * 100))))
                                .build())
                        .build())
                .group(buildHallucinationVisualsGroup())
                .group(buildDreadVisualsGroup())
                .build();
    }

    private OptionGroup buildHallucinationVisualsGroup() {
        return OptionGroup.createBuilder()
                .name(Component.translatable("text.config.substance.group.hallucinationVisuals"))
                .description(OptionDescription.of(Component.translatable("text.config.substance.group.hallucinationVisuals.desc")))
                .option(booleanOption("enableHallucinationVisuals", true,
                        () -> Config.get().enableHallucinationVisuals, val -> Config.get().enableHallucinationVisuals = val))
                .option(percentOption("hallucinationVisualStrength", 1.0f, 0.0f, 2.0f,
                        () -> Config.get().hallucinationVisualStrength, val -> Config.get().hallucinationVisualStrength = val))
                .option(secondsOption("hallucinationApparitionInterval", 6.0f,
                        () -> Config.get().hallucinationApparitionInterval, val -> Config.get().hallucinationApparitionInterval = val))
                .option(integerOption("hallucinationMaxApparitions", 5, 0, 20,
                        () -> Config.get().hallucinationMaxApparitions, val -> Config.get().hallucinationMaxApparitions = val))
                .option(percentOption("hallucinationVillagerChance", 0.33f, 0.0f, 1.0f,
                        () -> Config.get().hallucinationVillagerChance, val -> Config.get().hallucinationVillagerChance = val))
                .build();
    }

    private OptionGroup buildDreadVisualsGroup() {
        return OptionGroup.createBuilder()
                .name(Component.translatable("text.config.substance.group.dreadVisuals"))
                .description(OptionDescription.of(Component.translatable("text.config.substance.group.dreadVisuals.desc")))
                .option(booleanOption("enableDreadVisuals", true,
                        () -> Config.get().enableDreadVisuals, val -> Config.get().enableDreadVisuals = val))
                .option(percentOption("dreadVisualStrength", 1.0f, 0.0f, 2.0f,
                        () -> Config.get().dreadVisualStrength, val -> Config.get().dreadVisualStrength = val))
                .option(secondsOption("dreadApparitionInterval", 10.0f,
                        () -> Config.get().dreadApparitionInterval, val -> Config.get().dreadApparitionInterval = val))
                .option(integerOption("dreadMaxApparitions", 6, 0, 20,
                        () -> Config.get().dreadMaxApparitions, val -> Config.get().dreadMaxApparitions = val))
                .option(percentOption("dreadCreeperChance", 0.5f, 0.0f, 1.0f,
                        () -> Config.get().dreadCreeperChance, val -> Config.get().dreadCreeperChance = val))
                .option(floatOption("dreadAnimalDistance", 95.0f, 32.0f, 128.0f, 1.0f, "%.0f blocks",
                        () -> Config.get().dreadAnimalDistance, val -> Config.get().dreadAnimalDistance = val))
                .option(floatOption("dreadAnimalFadeDistance", 24.0f, 4.0f, 64.0f, 1.0f, "%.0f blocks",
                        () -> Config.get().dreadAnimalFadeDistance, val -> Config.get().dreadAnimalFadeDistance = val))
                .build();
    }

    private Option<Boolean> booleanOption(String key, boolean defaultValue,
                                          java.util.function.Supplier<Boolean> getter,
                                          java.util.function.Consumer<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(Component.translatable("text.config.substance.option." + key))
                .description(OptionDescription.of(Component.translatable("text.config.substance.option." + key + ".desc")))
                .binding(defaultValue, getter, setter)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    private Option<Float> percentOption(String key, float defaultValue, float min, float max,
                                        java.util.function.Supplier<Float> getter,
                                        java.util.function.Consumer<Float> setter) {
        return floatOption(key, defaultValue, min, max, 0.01f, "%.0f%%", getter, setter, 100.0f);
    }

    private Option<Float> secondsOption(String key, float defaultValue,
                                        java.util.function.Supplier<Float> getter,
                                        java.util.function.Consumer<Float> setter) {
        return floatOption(key, defaultValue, 0.5f, 60.0f, 0.5f, "%.1f s", getter, setter);
    }

    private Option<Float> floatOption(String key, float defaultValue, float min, float max, float step,
                                      String format, java.util.function.Supplier<Float> getter,
                                      java.util.function.Consumer<Float> setter) {
        return floatOption(key, defaultValue, min, max, step, format, getter, setter, 1.0f);
    }

    private Option<Float> floatOption(String key, float defaultValue, float min, float max, float step,
                                      String format, java.util.function.Supplier<Float> getter,
                                      java.util.function.Consumer<Float> setter, float displayMultiplier) {
        return Option.<Float>createBuilder()
                .name(Component.translatable("text.config.substance.option." + key))
                .description(OptionDescription.of(Component.translatable("text.config.substance.option." + key + ".desc")))
                .binding(defaultValue, getter, setter)
                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(min, max).step(step)
                        .formatValue(val -> Component.literal(String.format(format, val * displayMultiplier))))
                .build();
    }

    private Option<Integer> integerOption(String key, int defaultValue, int min, int max,
                                          java.util.function.Supplier<Integer> getter,
                                          java.util.function.Consumer<Integer> setter) {
        return Option.<Integer>createBuilder()
                .name(Component.translatable("text.config.substance.option." + key))
                .description(OptionDescription.of(Component.translatable("text.config.substance.option." + key + ".desc")))
                .binding(defaultValue, getter, setter)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(1))
                .build();
    }
}
