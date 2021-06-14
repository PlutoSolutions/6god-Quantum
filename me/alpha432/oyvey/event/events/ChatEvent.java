// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.alpha432.oyvey.event.EventStage;

@Cancelable
public class ChatEvent extends EventStage
{
    private final String msg;
    
    public ChatEvent(final String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return this.msg;
    }
}
