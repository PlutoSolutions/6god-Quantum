// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;

public class PlayerJumpEvent extends EventStage
{
    public double motionX;
    public double motionY;
    
    public PlayerJumpEvent(final double motionX, final double motionY) {
        this.motionX = motionX;
        this.motionY = motionY;
    }
}
