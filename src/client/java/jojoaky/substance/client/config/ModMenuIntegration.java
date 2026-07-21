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
                .category(buildGameplayCategory())
                .category(buildClientCategory())
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
                                .binding(96, () -> Config.get().herbalRollDurability, val -> Config.get().herbalRollDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.thickHerbalRollDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(128, () -> Config.get().thickHerbalRollDurability, val -> Config.get().thickHerbalRollDurability = val)
                                .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(1))
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.config.substance.option.cigaretteDurability"))
                                .flag(OptionFlag.GAME_RESTART)
                                .binding(128, () -> Config.get().cigaretteDurability, val -> Config.get().cigaretteDurability = val)
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
                                .binding(24.0f, () -> Config.get().maxSmokeDuration, val -> Config.get().maxSmokeDuration = val)
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
                .build();
    }
}