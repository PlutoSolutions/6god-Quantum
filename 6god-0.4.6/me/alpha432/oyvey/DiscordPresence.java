//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey;

import me.alpha432.oyvey.features.modules.misc.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;
    
    public static void start() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordPresence.rpc.Discord_Initialize("851151223578165298", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = ((Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) ? "In the main menu." : ("Playing " + ((Minecraft.getMinecraft().getCurrentServerData() != null) ? (RPC.INSTANCE.showIP.getValue() ? ("on " + Minecraft.getMinecraft().getCurrentServerData().serverIP + ".") : " multiplayer.") : " singleplayer.")));
        DiscordPresence.presence.state = RPC.INSTANCE.state.getValue();
        DiscordPresence.presence.largeImageKey = "Quantum";
        DiscordPresence.presence.largeImageText = "Quantum 0.4.5";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
        String string;
        StringBuilder sb;
        DiscordRichPresence presence;
        String string2;
        (DiscordPresence.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DiscordPresence.rpc.Discord_RunCallbacks();
                string = "";
                sb = new StringBuilder();
                presence = DiscordPresence.presence;
                new StringBuilder().append("Playing ");
                if (Minecraft.getMinecraft().getCurrentServerData() != null) {
                    if (RPC.INSTANCE.showIP.getValue()) {
                        string2 = "on " + Minecraft.getMinecraft().getCurrentServerData().serverIP + ".";
                    }
                    else {
                        string2 = " multiplayer.";
                    }
                }
                else {
                    string2 = " singleplayer.";
                }
                presence.details = sb.append(string2).toString();
                DiscordPresence.presence.state = RPC.INSTANCE.state.getValue();
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException ex) {}
            }
        }, "RPC-Callback-Handler")).start();
    }
    
    public static void stop() {
        if (DiscordPresence.thread != null && !DiscordPresence.thread.isInterrupted()) {
            DiscordPresence.thread.interrupt();
        }
        DiscordPresence.rpc.Discord_Shutdown();
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
    }
}
