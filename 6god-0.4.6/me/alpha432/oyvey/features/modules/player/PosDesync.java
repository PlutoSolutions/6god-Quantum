// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;

public class PosDesync extends Module
{
    public PosDesync() {
        super("PosDesync", "pd", Category.PLAYER, true, false, false);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation || event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}
