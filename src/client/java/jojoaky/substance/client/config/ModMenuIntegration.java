package jojoaky.substance.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import jojoaky.substance.Config;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Used for narration. Could be used to render a title in the future."))
                .save(() -> Config.HANDLER.save())
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .tooltip(Component.literal("General settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Options"))
                                .description(OptionDescription.of(Component.literal("Miscellaneous options.")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Boolean Option"))
                                        .description(OptionDescription.of(Component.literal("Does a cool boolean thing.")))
                                        .binding(
                                                true,
                                                () -> Config.HANDLER.instance().myCoolBoolean,
                                                val -> Config.HANDLER.instance().myCoolBoolean = val
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}