// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;

public class KeyEvent extends EventStage
{
    private final int key;
    
    public KeyEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
