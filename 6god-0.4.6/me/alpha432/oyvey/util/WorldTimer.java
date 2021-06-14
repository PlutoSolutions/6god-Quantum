//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

public class WorldTimer
{
    private float OverrideSpeed;
    
    public WorldTimer() {
        this.OverrideSpeed = 1.0f;
    }
    
    private void useTimer() {
        if (this.OverrideSpeed != 1.0f && this.OverrideSpeed > 0.1f) {
            Wrapper.mc.timer.tickLength = 50.0f / this.OverrideSpeed;
        }
    }
    
    public void SetOverrideSpeed(final float f) {
        this.OverrideSpeed = f;
        this.useTimer();
    }
    
    public void resetTime() {
        Wrapper.mc.timer.tickLength = 50.0f;
    }
}
