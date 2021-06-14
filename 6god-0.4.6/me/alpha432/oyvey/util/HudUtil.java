//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;

public class HudUtil implements Util
{
    public static String getPingSatus() {
        String line = "";
        final int ping = OyVey.serverManager.getPing();
        if (ping > 100) {
            line += ChatFormatting.RED;
        }
        else if (ping > 50) {
            line += ChatFormatting.YELLOW;
        }
        else {
            line += ChatFormatting.GREEN;
        }
        return line + " " + ping;
    }
    
    public static String getTpsStatus() {
        String line = "";
        final double tps = Math.ceil(OyVey.serverManager.getTPS());
        if (tps > 16.0) {
            line += ChatFormatting.GREEN;
        }
        else if (tps > 10.0) {
            line += ChatFormatting.YELLOW;
        }
        else {
            line += ChatFormatting.RED;
        }
        return line + " " + tps;
    }
    
    public static String getFpsStatus() {
        String line = "";
        final int fps = Minecraft.getDebugFPS();
        if (fps > 120) {
            line += ChatFormatting.GREEN;
        }
        else if (fps > 60) {
            line += ChatFormatting.YELLOW;
        }
        else {
            line += ChatFormatting.RED;
        }
        return line + " " + fps;
    }
}
