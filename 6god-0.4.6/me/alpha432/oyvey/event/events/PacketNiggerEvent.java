// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PacketNiggerEvent extends PacketEvent
{
    public PacketNiggerEvent(final int stage, final Packet<?> packet) {
        super(stage, packet);
    }
}
