// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.Packet;
import me.alpha432.oyvey.features.modules.Module;

public class PacketLogger extends Module
{
    private Packet[] packets;
    public Setting<Boolean> incoming;
    public Setting<Boolean> outgoing;
    public Setting<Boolean> data;
    
    public PacketLogger() {
        super("PacketLogger", "", Category.MISC, true, false, false);
        this.incoming = (Setting<Boolean>)this.register(new Setting("Incoming", (T)true));
        this.outgoing = (Setting<Boolean>)this.register(new Setting("Outgoing", (T)true));
        this.data = (Setting<Boolean>)this.register(new Setting("Data", (T)true));
    }
}
