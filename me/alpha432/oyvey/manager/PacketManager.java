//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager;

import java.util.ArrayList;
import net.minecraft.network.Packet;
import java.util.List;
import me.alpha432.oyvey.features.Feature;

public class PacketManager extends Feature
{
    private final List<Packet<?>> noEventPackets;
    public static boolean skipTick;
    public static int debugSwings;
    public static int swings;
    
    public PacketManager() {
        this.noEventPackets = new ArrayList<Packet<?>>();
    }
    
    public void sendPacketNoEvent(final Packet<?> packet) {
        if (packet != null && !Feature.nullCheck()) {
            this.noEventPackets.add(packet);
            PacketManager.mc.player.connection.sendPacket((Packet)packet);
        }
    }
    
    public boolean shouldSendPacket(final Packet<?> packet) {
        if (this.noEventPackets.contains(packet)) {
            this.noEventPackets.remove(packet);
            return false;
        }
        return true;
    }
    
    public static void updateTicks(final boolean in) {
        PacketManager.skipTick = in;
    }
    
    public static void updateSwings() {
        ++PacketManager.debugSwings;
        ++PacketManager.swings;
    }
    
    static {
        PacketManager.skipTick = false;
        PacketManager.debugSwings = 0;
        PacketManager.swings = 0;
    }
}
