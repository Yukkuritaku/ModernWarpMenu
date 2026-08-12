package com.github.yukkuritaku.modernwarpmenu.state;

import com.github.yukkuritaku.modernwarpmenu.client.gui.screens.ModernWarpScreen;
import com.github.yukkuritaku.modernwarpmenu.data.layout.Layout;
import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.menu.Menu;
import net.minecraft.client.Minecraft;

public class ModernWarpMenuState {
    private static Layout overworldLayout;
    private static Layout riftLayout;
    private static boolean openConfigMenuRequested;

    /**
     * Gets the constants corresponding to the given SkyBlock menu.
     *
     * @param menu the SkyBlock menu to get a constants for
     * @return {@code riftLayout} if {@code Menu.PORHTAL} is provided, {@code overworldLayout} otherwise
     */
    public static Layout getLayoutForMenu(Menu menu) {
        if (menu == Menu.PORHTAL) {
            return getRiftLayout();
        } else {
            return getOverworldLayout();
        }
    }

    public static Layout getOverworldLayout() {
        return overworldLayout;
    }

    public static Layout getRiftLayout() {
        return riftLayout;
    }

    public static boolean isModernWarpMenuOpen() {
        return Minecraft.getInstance().gui.screen() instanceof ModernWarpScreen;
    }

    public static boolean isOpenConfigMenuRequested() {
        return openConfigMenuRequested;
    }

    public static void setOverworldLayout(Layout overworldLayout) {
        ModernWarpMenuState.overworldLayout = overworldLayout;
    }

    public static void setRiftLayout(Layout riftLayout) {
        ModernWarpMenuState.riftLayout = riftLayout;
    }

    public static void setOpenConfigMenuRequested(boolean openConfigMenuRequested) {
        ModernWarpMenuState.openConfigMenuRequested = openConfigMenuRequested;
    }
}
