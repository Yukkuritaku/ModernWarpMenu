package com.github.yukkuritaku.modernwarpmenu.client.gui.screens;

import com.github.yukkuritaku.modernwarpmenu.ModernWarpMenu;
import com.github.yukkuritaku.modernwarpmenu.client.gui.components.IslandButton;
import com.github.yukkuritaku.modernwarpmenu.client.gui.components.WarpButton;
import com.github.yukkuritaku.modernwarpmenu.data.layout.Layout;
import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.menu.Menu;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class RiftFastTravelScreen extends ModernWarpScreen{

    public RiftFastTravelScreen(ChestMenu menu, Inventory playerInventory, Layout layout) {
        super(Menu.PORHTAL, menu, playerInventory, layout);
        this.lastSlotIndexToCheck = ModernWarpMenu.getInstance().getSkyBlockConstantsManager()
                .getSkyBlockConstants().getLastMatchConditionInventorySlotIndex(this.warpMenu);
    }

    @Override
    protected void addIslandBrowserButton() {}

    @Override
    protected void warpButtonHandler(WarpButton button) {
        if (Util.getMillis() > this.warpFailCoolDownExpiryTime) {
            clickSlot(button.getWarpSlotIndex());
        }
    }

    @Override
    protected void islandButtonHandler(IslandButton button) {
        if (Util.getMillis() > this.warpFailCoolDownExpiryTime) {
            clickSlot(button.island.warpList.getFirst().slotIndex());
        }
    }
}
