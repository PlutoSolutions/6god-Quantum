// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventStage extends Event
{
    private int stage;
    private boolean canceled;
    
    public EventStage() {
    }
    
    public EventStage(final int stage) {
        this.stage = stage;
    }
    
    public int getStage() {
        return this.stage;
    }
    
    public void setStage(final int stage) {
        this.stage = stage;
    }
    
    public void setCanceledE(final boolean c) {
        this.canceled = c;
    }
    
    public boolean isCanceledE() {
        return this.canceled;
    }
}
