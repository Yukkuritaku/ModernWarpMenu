package com.github.yukkuritaku.modernwarpmenu.data.settings.categories;

import com.github.yukkuritaku.modernwarpmenu.data.settings.Settings;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.network.chat.Component;

public class GeneralCategory {

    public static ConfigCategory create(Settings defaults, Settings config){
        return ConfigCategory.createBuilder()
                .name(Component.translatable("modernwarpmenu.config.categories.general"))
                .tooltip(Component.translatable("modernwarpmenu.config.categories.general.tooltip"))
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.warpMenuEnabled"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.warpMenuEnabled.tooltip")))
                        .binding(defaults.general.warpMenuEnabled,
                                () -> config.general.warpMenuEnabled,
                                value -> config.general.warpMenuEnabled = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.showIslandLabels"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.showIslandLabels.tooltip")))
                        .binding(defaults.general.showIslandLabels,
                                () -> config.general.showIslandLabels,
                                value -> config.general.showIslandLabels = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.hideWarpLabelsUntilIslandHovered"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.hideWarpLabelsUntilIslandHovered.tooltip")))
                        .binding(defaults.general.hideWarpLabelsUntilIslandHovered,
                                () -> config.general.hideWarpLabelsUntilIslandHovered,
                                value -> config.general.hideWarpLabelsUntilIslandHovered = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.hideWarpLabelForIslandsWithOneWarp"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.hideWarpLabelForIslandsWithOneWarp.tooltip")))
                        .binding(defaults.general.hideWarpLabelForIslandsWithOneWarp,
                                () -> config.general.hideWarpLabelForIslandsWithOneWarp,
                                value -> config.general.hideWarpLabelForIslandsWithOneWarp = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.suggestWarpMenuOnWarpCommand"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.suggestWarpMenuOnWarpCommand.tooltip")))
                        .binding(defaults.general.suggestWarpMenuOnWarpCommand,
                                () -> config.general.suggestWarpMenuOnWarpCommand,
                                value -> config.general.suggestWarpMenuOnWarpCommand = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.addWarpCommandToChatHistory"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.addWarpCommandToChatHistory.tooltip")))
                        .binding(defaults.general.addWarpCommandToChatHistory,
                                () -> config.general.addWarpCommandToChatHistory,
                                value -> config.general.addWarpCommandToChatHistory = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.showJerryIsland"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.showJerryIsland.tooltip")))
                        .binding(defaults.general.showJerryIsland,
                                () -> config.general.showJerryIsland,
                                value -> config.general.showJerryIsland = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.hideUnobtainableWarps"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.hideUnobtainableWarps.tooltip")))
                        .binding(defaults.general.hideUnobtainableWarps,
                                () -> config.general.hideUnobtainableWarps,
                                value -> config.general.hideUnobtainableWarps = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.enableUpdateNotification"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.enableUpdateNotification.tooltip")))
                        .binding(defaults.general.enableUpdateNotification,
                                () -> config.general.enableUpdateNotification,
                                value -> config.general.enableUpdateNotification = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.showRegularWarpMenuButton"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.showRegularWarpMenuButton.tooltip")))
                        .binding(defaults.general.showRegularWarpMenuButton,
                                () -> config.general.showRegularWarpMenuButton,
                                value -> config.general.showRegularWarpMenuButton = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.showIslandBrowserButton"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.showIslandBrowserButton.tooltip")))
                        .binding(defaults.general.showIslandBrowserButton,
                                () -> config.general.showIslandBrowserButton,
                                value -> config.general.showIslandBrowserButton = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("modernwarpmenu.config.general.useHypixelAPI"))
                        .description(OptionDescription.of(Component.translatable("modernwarpmenu.config.general.useHypixelAPI.tooltip")))
                        .binding(defaults.general.useHypixelAPI,
                                () -> config.general.useHypixelAPI,
                                value -> config.general.useHypixelAPI = value)
                        .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                        .build())
                .build();
    }
}
