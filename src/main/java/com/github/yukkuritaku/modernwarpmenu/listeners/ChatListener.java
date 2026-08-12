package com.github.yukkuritaku.modernwarpmenu.listeners;

import com.github.yukkuritaku.modernwarpmenu.ModernWarpMenu;
import com.github.yukkuritaku.modernwarpmenu.client.gui.screens.ModernWarpScreen;
import com.github.yukkuritaku.modernwarpmenu.data.settings.SettingsManager;
import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.WarpCommandVariant;
import com.github.yukkuritaku.modernwarpmenu.state.ModernWarpMenuState;
import com.github.yukkuritaku.modernwarpmenu.utils.ChatUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.List;
import java.util.Locale;

public class ChatListener {

    private final Minecraft mc;
    private boolean chatMessageSendDetected;

    public ChatListener(){
        this.mc = Minecraft.getInstance();
    }

    public void registerEvents(){
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (ModernWarpMenuState.isModernWarpMenuOpen()){
                String text = message.getString();
                if (ModernWarpMenu.getInstance().getSkyBlockConstantsManager().getSkyBlockConstants().warpMessages().warpFailMessages().containsKey(text)){
                    String failMessageKey = ModernWarpMenu.getInstance().getSkyBlockConstantsManager()
                            .getSkyBlockConstants().warpMessages().warpFailMessages().get(text);
                    if (mc.gui.screen() != null && mc.gui.screen() instanceof ModernWarpScreen)
                        ((ModernWarpScreen) mc.gui.screen()).onWarpFail(failMessageKey);
                }
            }
        });
        //TODO port throwable copy

        /*ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof ChatScreen) {
                ScreenMouseEvents.allowMouseClick(screen).register((scr, mouseX, mouseY, button) -> {
                    Style style = client.gui.getChat().getClickedComponentStyleAt(mouseX, mouseY);
                    if (style != null){
                        String insertion = style.getInsertion();
                        if (insertion != null && insertion.equals(ChatUtils.COPY_TO_CLIPBOARD_TRANSLATION_KEY) && style.getClickEvent() != null
                                && style.getClickEvent().action() == ClickEvent.Action.COPY_TO_CLIPBOARD){
                            //String clickValue = style.getClickEvent().action();

                            TextFieldHelper.setClipboardContents(client, clickValue);
                            ChatUtils.sendMessageWithModNamePrefix(Component.translatable("modernwarpmenu.gui.buttons.copyToClipboard.copied")
                                    .withStyle(ChatFormatting.GREEN));
                            return false;
                        }
                    }
                    return true;
                });
            }
        });*/
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (this.chatMessageSendDetected && client.gui.screen() instanceof ChatScreen && screen == null) {
                this.chatMessageSendDetected = false;
                List<String> sentMessages = mc.gui.hud.getChat().getRecentChat();
                if (!sentMessages.isEmpty()) {
                    checkChatMessageForReminder(sentMessages.getLast());
                }
            }
        });
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof ChatScreen) {
                ScreenKeyboardEvents.allowKeyPress(screen).register((chatScreen, context) -> {
                    if (context.key() == InputConstants.KEY_RETURN || context.key() == InputConstants.KEY_NUMPADENTER){
                        this.chatMessageSendDetected = true;
                        return true;
                    }
                    return true;
                });
            }
        });

    }

    /**
     * If the reminder feature is enabled, check a given chat message for a warp command variant.
     * If the message is a warp command variant, remind the player to use the Fancy Warp Menu instead of commands.
     *
     * @param sentChatMessage the chat message that was just sent
     //* @see Settings#shouldSuggestWarpMenuOnWarpCommand()
     */
    private void checkChatMessageForReminder(String sentChatMessage) {
        if (SettingsManager.get().general.suggestWarpMenuOnWarpCommand && getWarpCommandVariant(sentChatMessage) != null) {
            sendReminderToUseModernMenu();
        }
    }

    /**
     * Checks if a given command is the warp command or any of its variants and returns the corresponding
     * {@code WarpCommandVariant} object if one is found.
     *
     * @param command the command the player sent
     * @return a {@link WarpCommandVariant} if one with the same command is found, or {@code null} otherwise
     */
    private WarpCommandVariant getWarpCommandVariant(String command) {
        // Trim off the slash and all arguments
        String baseCommand = command.toLowerCase(Locale.US).substring(1).split(" ")[0];

        for (WarpCommandVariant commandVariant : ModernWarpMenu.getInstance().getSkyBlockConstantsManager()
                .getSkyBlockConstants().warpCommandVariants()) {
            if (commandVariant.command().equals(baseCommand)) {
                return commandVariant;
            }
        }

        return null;
    }

    private void sendReminderToUseModernMenu() {
        ChatUtils.sendMessageWithModNamePrefix(Component.translatable(ModernWarpMenu
                .getFullLanguageKey("messages.useWarpMenuInsteadOfCommand"))
                .withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
    }
}
