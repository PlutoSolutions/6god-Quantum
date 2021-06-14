//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.Packet;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    public static Minecraft mc;
    public static FontRenderer fontRenderer;
    public static FontRenderer fr;
    public static volatile Wrapper INSTANCE;
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static Minecraft getMinecraft() {
        return Wrapper.mc;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().player;
    }
    
    public WorldClient world() {
        return Wrapper.INSTANCE.mc().world;
    }
    
    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }
    
    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRenderer;
    }
    
    public void sendPacket(final Packet packet) {
        this.player().connection.sendPacket(packet);
    }
    
    public PlayerControllerMP controller() {
        return Wrapper.INSTANCE.mc().playerController;
    }
    
    public void swingArm() {
        this.player().swingArm(EnumHand.MAIN_HAND);
    }
    
    public void attack(final Entity entity) {
        this.controller().attackEntity((EntityPlayer)this.player(), entity);
    }
    
    public void copy(final String content) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
    }
    
    static {
        Wrapper.mc = Minecraft.getMinecraft();
        Wrapper.fontRenderer = Wrapper.mc.fontRenderer;
        Wrapper.fr = Minecraft.getMinecraft().fontRenderer;
        Wrapper.INSTANCE = new Wrapper();
    }
}
