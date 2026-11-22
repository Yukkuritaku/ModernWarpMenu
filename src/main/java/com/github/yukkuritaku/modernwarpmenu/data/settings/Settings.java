package com.github.yukkuritaku.modernwarpmenu.data.settings;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class Settings {

    @SerialEntry
    public int settingsVersion = SettingsManager.SETTINGS_VERSION;

    @SerialEntry
    public GeneralSettings general = new GeneralSettings();
    @SerialEntry
    public DebugSettings debug = new DebugSettings();

    public static class GeneralSettings{

        @SerialEntry
        public boolean warpMenuEnabled = true;
        @SerialEntry
        public boolean showIslandLabels = true;
        @SerialEntry
        public boolean hideWarpLabelsUntilIslandHovered = false;
        @SerialEntry
        public boolean hideWarpLabelForIslandsWithOneWarp = true;
        @SerialEntry
        public boolean suggestWarpMenuOnWarpCommand = false;
        @SerialEntry
        public boolean addWarpCommandToChatHistory = true;
        @SerialEntry
        public boolean showJerryIsland = true;
        @SerialEntry
        public boolean hideUnobtainableWarps = true;
        @SerialEntry
        public boolean enableUpdateNotification = true;
        @SerialEntry
        public boolean showRegularWarpMenuButton = false;
        @SerialEntry
        public boolean showIslandBrowserButton = true;
        @SerialEntry
        public boolean useHypixelAPI = true;
    }

    public static class DebugSettings {
        @SerialEntry
        public boolean debugModeEnabled = false;
        @SerialEntry
        public boolean showDebugOverlay = true;
        @SerialEntry
        public boolean drawBorders = true;
        @SerialEntry
        public boolean skipSkyBlockCheck = false;
        @SerialEntry
        public boolean alwaysShowJerryIsland = true;
    }
}
