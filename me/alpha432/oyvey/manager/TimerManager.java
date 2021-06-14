//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.player.TimerSpeed;
import me.alpha432.oyvey.features.Feature;

public class TimerManager extends Feature
{
    private float timer;
    private TimerSpeed module;
    
    public TimerManager() {
        this.timer = 1.0f;
    }
    
    public void init() {
        this.module = OyVey.moduleManager.getModuleByClass(TimerSpeed.class);
    }
    
    public void unload() {
        this.timer = 1.0f;
        TimerManager.mc.timer.tickLength = 50.0f;
    }
    
    public void update() {
        if (this.module != null && this.module.isEnabled()) {
            this.timer = this.module.speed;
        }
        TimerManager.mc.timer.tickLength = 50.0f / ((this.timer <= 0.0f) ? 0.1f : this.timer);
    }
    
    public float getTimer() {
        return this.timer;
    }
    
    public void setTimer(final float timer) {
        if (timer > 0.0f) {
            this.timer = timer;
        }
    }
    
    @Override
    public void reset() {
        this.timer = 1.0f;
    }
}
