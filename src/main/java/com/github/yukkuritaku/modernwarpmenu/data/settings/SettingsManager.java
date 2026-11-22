package com.github.yukkuritaku.modernwarpmenu.data.settings;

import com.github.yukkuritaku.modernwarpmenu.ModernWarpMenu;
import com.github.yukkuritaku.modernwarpmenu.data.settings.categories.DebugCategory;
import com.github.yukkuritaku.modernwarpmenu.data.settings.categories.GeneralCategory;
import com.github.yukkuritaku.modernwarpmenu.state.EnvironmentDetails;
import com.google.gson.FieldNamingPolicy;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class SettingsManager {

    public static final int SETTINGS_VERSION = 1;
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("modernwarpmenu.json");
    private static final ConfigClassHandler<Settings> HANDLER = ConfigClassHandler.createBuilder(Settings.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_FILE)
                    .setJson5(false)
                    .appendGsonBuilder(builder -> builder
                            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                            //.registerTypeHierarchyAdapter(Identifier.class, new Identifier.Serializer())
                    )
                    .build())
            .build();

    public static Settings get() {
        return HANDLER.instance();
    }

    public static void save(){
        HANDLER.save();
    }

    public static void init(){
        if (StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass() != ModernWarpMenu.class){
            throw new IllegalCallerException("Called init from wrong class!");
        }
        HANDLER.load();
        if (EnvironmentDetails.isDevelopmentEnvironment()) {
            get().debug.debugModeEnabled = true;
        }
    }

    public static Screen createSettingsScreen(Screen parent){
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) ->
            builder.title(Component.translatable("modernwarpmenu.config.title"))
                    .category(GeneralCategory.create(defaults, config))
                    .category(DebugCategory.create(defaults, config))
        ).generateScreen(parent);
    }
}
