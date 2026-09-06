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
                                .binding(Config.DEFAULT_WOODEN_PIPE_DURABILITY, () -> Config.get().woodenPipeDurability, val -> Config.get().woodenPipeDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.bubblePipeDurability"))
                                .binding(Config.DEFAULT_BUBBLE_PIPE_DURABILITY, () -> Config.get().bubblePipeDurability, val -> Config.get().bubblePipeDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.herbalRollDurability"))
                                .binding(Config.DEFAULT_HERBAL_ROLL_DURABILITY, () -> Config.get().herbalRollDurability, val -> Config.get().herbalRollDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.thickHerbalRollDurability"))
                                .binding(Config.DEFAULT_THICK_HERBAL_ROLL_DURABILITY, () -> Config.get().thickHerbalRollDurability, val -> Config.get().thickHerbalRollDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.cigaretteDurability"))
                                .binding(Config.DEFAULT_CIGARETTE_DURABILITY, () -> Config.get().cigaretteDurability, val -> Config.get().cigaretteDurability = val)
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
                                .binding(Config.DEFAULT_MAX_SMOKE_DURATION, () -> Config.get().maxSmokeDuration, val -> Config.get().maxSmokeDuration = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.5f, 120.0f).step(0.5f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.smokeCooldown"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.smokeCooldown.desc")))
                                .binding(Config.DEFAULT_SMOKE_COOLDOWN, () -> Config.get().smokeCooldown, val -> Config.get().smokeCooldown = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 30.0f).step(0.1f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.maxSniffDuration"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.maxSniffDuration.desc")))
                                .binding(Config.DEFAULT_MAX_SNIFF_DURATION, () -> Config.get().maxSniffDuration, val -> Config.get().maxSniffDuration = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.5f, 30.0f).step(0.5f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.sniffCooldown"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.sniffCooldown.desc")))
                                .binding(Config.DEFAULT_SNIFF_COOLDOWN, () -> Config.get().sniffCooldown, val -> Config.get().sniffCooldown = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 30.0f).step(0.1f)
                                        .formatValue(val -> Component.literal(String.format("%.1f s", val))))
                                .build())

                        // Added missing pipe item consumption probability option
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.pipeItemConsumeProbability"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.pipeItemConsumeProbability.desc")))
                                .binding(Config.DEFAULT_PIPE_ITEM_CONSUME_PROBABILITY, () -> Config.get().pipeItemConsumeProbability, val -> Config.get().pipeItemConsumeProbability = val)
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
                                .binding(Config.DEFAULT_SURGE_MOVEMENT_SPEED_BONUS, () -> Config.get().surgeMovementSpeedBonus, val -> Config.get().surgeMovementSpeedBonus = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 2.0f).step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("+%.0f%%", val * 100))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.surgeElytraBoost"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.surgeElytraBoost.desc")))
                                .binding(Config.DEFAULT_SURGE_ELYTRA_BOOST, () -> Config.get().surgeElytraBoost, val -> Config.get().surgeElytraBoost = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 0.2f).step(0.005f)
                                        .formatValue(val -> Component.literal(String.format("%.3f/tick", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.surgeElytraMaxSpeed"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.surgeElytraMaxSpeed.desc")))
                                .binding(Config.DEFAULT_SURGE_ELYTRA_MAX_SPEED, () -> Config.get().surgeElytraMaxSpeed, val -> Config.get().surgeElytraMaxSpeed = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.25f, 5.0f).step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("%.2f blocks/tick", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.surgeElytraMaxSpeedPerLevel"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.surgeElytraMaxSpeedPerLevel.desc")))
                                .binding(Config.DEFAULT_SURGE_ELYTRA_MAX_SPEED_PER_LEVEL, () -> Config.get().surgeElytraMaxSpeedPerLevel, val -> Config.get().surgeElytraMaxSpeedPerLevel = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 2.0f).step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("+%.2f blocks/tick", val))))
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.keenMiningSpeedMultiplier"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.keenMiningSpeedMultiplier.desc")))
                                .binding(Config.DEFAULT_KEEN_MINING_SPEED_MULTIPLIER, () -> Config.get().keenMiningSpeedMultiplier, val -> Config.get().keenMiningSpeedMultiplier = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(1.0f, 10.0f).step(0.25f)
                                        .formatValue(val -> Component.literal(String.format("%.2fx", val))))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.relaxationDarknessDuration"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.relaxationDarknessDuration.desc")))
                                .binding(Config.DEFAULT_RELAXATION_DARKNESS_DURATION, () -> Config.get().relaxationDarknessDuration, val -> Config.get().relaxationDarknessDuration = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 1200).step(20)
                                        .formatValue(val -> Component.literal(String.format("%.0f s", val / 20.0f))))
                                .build())
                        .build())

                .option(Option.<Float>createBuilder()
                        .name(Component.translatable("text.config.substance.option.horrorTripChance"))
                        .description(OptionDescription.of(Component.translatable("text.config.substance.option.horrorTripChance.desc")))
                        .binding(
                                Config.DEFAULT_HORROR_TRIP_CHANCE,
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
                                        Config.DEFAULT_ENABLE_SHADER_EFFECTS,
                                        () -> Config.get().enableShaderEffects,
                                        val -> Config.get().enableShaderEffects = val
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.visualEffectStrength"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.visualEffectStrength.desc")))
                                .binding(
                                        Config.DEFAULT_VISUAL_EFFECT_STRENGTH,
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
                                        Config.DEFAULT_VISUAL_EFFECTS_IN_MENUS,
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
                                        Config.DEFAULT_ENABLE_AUDIO_EFFECTS,
                                        () -> Config.get().enableAudioEffects,
                                        val -> Config.get().enableAudioEffects = val
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.config.substance.option.audioEffectStrength"))
                                .description(OptionDescription.of(Component.translatable("text.config.substance.option.audioEffectStrength.desc")))
                                .binding(
                                        Config.DEFAULT_AUDIO_EFFECT_STRENGTH,
                                        () -> Config.get().audioEffectStrength,
                                        val -> Config.get().audioEffectStrength = val
                                )
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 2.0f)
                                        .step(0.05f)
                                        .formatValue(val -> Component.literal(String.format("%.0f%%", val * 100))))
                                .build())

                        .option(booleanOption("enableAmbientSounds", Config.DEFAULT_ENABLE_AMBIENT_SOUNDS,
                                () -> Config.get().enableAmbientSounds, val -> Config.get().enableAmbientSounds = val))

                        .option(secondsOption("ambientSoundInterval", Config.DEFAULT_AMBIENT_SOUND_INTERVAL, 1.0f, 120.0f, 1.0f,
                                () -> Config.get().ambientSoundInterval, val -> Config.get().ambientSoundInterval = val))
                        .build())
                .group(buildHallucinationVisualsGroup())
                .group(buildDreadVisualsGroup())
                .build();
    }

    private OptionGroup buildHallucinationVisualsGroup() {
        return OptionGroup.createBuilder()
                .name(Component.translatable("text.config.substance.group.hallucinationVisuals"))
                .description(OptionDescription.of(Component.translatable("text.config.substance.group.hallucinationVisuals.desc")))
                .option(booleanOption("enableHallucinationVisuals", Config.DEFAULT_ENABLE_HALLUCINATION_VISUALS,
                        () -> Config.get().enableHallucinationVisuals, val -> Config.get().enableHallucinationVisuals = val))
                .option(percentOption("hallucinationVisualStrength", Config.DEFAULT_HALLUCINATION_VISUAL_STRENGTH, 0.0f, 2.0f,
                        () -> Config.get().hallucinationVisualStrength, val -> Config.get().hallucinationVisualStrength = val))
                .option(secondsOption("hallucinationApparitionInterval", Config.DEFAULT_HALLUCINATION_APPARITION_INTERVAL,
                        () -> Config.get().hallucinationApparitionInterval, val -> Config.get().hallucinationApparitionInterval = val))
                .option(integerOption("hallucinationMaxApparitions", Config.DEFAULT_HALLUCINATION_MAX_APPARITIONS, 0, 20,
                        () -> Config.get().hallucinationMaxApparitions, val -> Config.get().hallucinationMaxApparitions = val))
                .option(percentOption("hallucinationVillagerChance", Config.DEFAULT_HALLUCINATION_VILLAGER_CHANCE, 0.0f, 1.0f,
                        () -> Config.get().hallucinationVillagerChance, val -> Config.get().hallucinationVillagerChance = val))
                .build();
    }

    private OptionGroup buildDreadVisualsGroup() {
        return OptionGroup.createBuilder()
                .name(Component.translatable("text.config.substance.group.dreadVisuals"))
                .description(OptionDescription.of(Component.translatable("text.config.substance.group.dreadVisuals.desc")))
                .option(booleanOption("enableDreadVisuals", Config.DEFAULT_ENABLE_DREAD_VISUALS,
                        () -> Config.get().enableDreadVisuals, val -> Config.get().enableDreadVisuals = val))
                .option(percentOption("dreadVisualStrength", Config.DEFAULT_DREAD_VISUAL_STRENGTH, 0.0f, 2.0f,
                        () -> Config.get().dreadVisualStrength, val -> Config.get().dreadVisualStrength = val))
                .option(secondsOption("dreadApparitionInterval", Config.DEFAULT_DREAD_APPARITION_INTERVAL,
                        () -> Config.get().dreadApparitionInterval, val -> Config.get().dreadApparitionInterval = val))
                .option(integerOption("dreadMaxApparitions", Config.DEFAULT_DREAD_MAX_APPARITIONS, 0, 20,
                        () -> Config.get().dreadMaxApparitions, val -> Config.get().dreadMaxApparitions = val))
                .option(percentOption("dreadCreeperChance", Config.DEFAULT_DREAD_CREEPER_CHANCE, 0.0f, 1.0f,
                        () -> Config.get().dreadCreeperChance, val -> Config.get().dreadCreeperChance = val))
                .option(floatOption("dreadAnimalDistance", Config.DEFAULT_DREAD_ANIMAL_DISTANCE, 4.0f, 128.0f, 1.0f, "%.0f blocks",
                        () -> Config.get().dreadAnimalDistance, val -> Config.get().dreadAnimalDistance = val))
                .option(floatOption("dreadAnimalFadeDistance", Config.DEFAULT_DREAD_ANIMAL_FADE_DISTANCE, 1.0f, 64.0f, 1.0f, "%.0f blocks",
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
        return secondsOption(key, defaultValue, 0.5f, 60.0f, 0.5f, getter, setter);
    }

    private Option<Float> secondsOption(String key, float defaultValue, float min, float max, float step,
                                        java.util.function.Supplier<Float> getter,
                                        java.util.function.Consumer<Float> setter) {
        return floatOption(key, defaultValue, min, max, step, "%.1f s", getter, setter);
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
