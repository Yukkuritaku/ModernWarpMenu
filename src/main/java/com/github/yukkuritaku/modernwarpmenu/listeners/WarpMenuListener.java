package com.github.yukkuritaku.modernwarpmenu.listeners;

import com.github.yukkuritaku.modernwarpmenu.ModernWarpMenu;
import com.github.yukkuritaku.modernwarpmenu.client.gui.screens.FastTravelScreen;
import com.github.yukkuritaku.modernwarpmenu.client.gui.screens.RiftFastTravelScreen;
import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.menu.Menu;
import com.github.yukkuritaku.modernwarpmenu.data.settings.SettingsManager;
import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.SkyBlockConstants;
import com.github.yukkuritaku.modernwarpmenu.event.InputEvents;
import com.github.yukkuritaku.modernwarpmenu.state.GameState;
import com.github.yukkuritaku.modernwarpmenu.state.ModernWarpMenuState;
import com.github.yukkuritaku.modernwarpmenu.utils.GameCheckUtils;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.slf4j.Logger;

import java.util.Objects;

public class WarpMenuListener {

    /** The minimum time in milliseconds after a hotkey press before the player can use the hotkey again*/
    private static final int HOTKEY_PRESS_DELAY = 2000;

    /**
     * Time the user last pressed the fancy warp menu hotkey, used to prevent command spamming from
     * spam pressing the hotkey
     */
    private long lastWarpMenuHotkeyPress;

    public void registerEvents(){
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.gui.screen() == null){
                if (ModernWarpMenuState.isOpenConfigMenuRequested()){
                    client.gui.setScreen(SettingsManager.createSettingsScreen(null));
                    ModernWarpMenuState.setOpenConfigMenuRequested(false);
                }
            }
        });
        InputEvents.KEY_PRESSED.register((key, scanCode, action, modifiers) -> {
            if (SettingsManager.get().general.warpMenuEnabled &&
                    GameState.isOnSkyBlock() &&
            ModernWarpMenu.getInstance().getKeyOpenWarpMenu().isDown() &&
                    Util.getMillis() - this.lastWarpMenuHotkeyPress > HOTKEY_PRESS_DELAY){
                this.lastWarpMenuHotkeyPress = Util.getMillis();
                if (Minecraft.getInstance().player != null)
                    Minecraft.getInstance().player.connection.sendCommand(SkyBlockConstants.WARP_COMMAND_BASE.substring(1));
            }
        });
        ScreenEvents.AFTER_INIT.register((minecraft, screen, scaledWidth, scaledHeight) -> {
            if (GameState.isOnSkyBlock() && screen instanceof ContainerScreen containerScreen){
                Menu menu = GameCheckUtils.determineOpenMenu(containerScreen.getTitle());
                if (menu == Menu.FAST_TRAVEL){
                    minecraft.gui.setScreen(new FastTravelScreen(containerScreen.getMenu(), Objects.requireNonNull(minecraft.player).getInventory(), ModernWarpMenuState.getOverworldLayout()));
                }else if (menu == Menu.PORHTAL){
                    minecraft.gui.setScreen(new RiftFastTravelScreen(containerScreen.getMenu(), Objects.requireNonNull(minecraft.player).getInventory(), ModernWarpMenuState.getRiftLayout()));
                }
            }
        });
    }
}
