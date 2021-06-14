//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.alpha432.oyvey.event.EventStage;

@Cancelable
public class StepEvent extends EventStage
{
    private final Entity entity;
    private float height;
    
    public StepEvent(final int stage, final Entity entity) {
        super(stage);
        this.entity = entity;
        this.height = entity.stepHeight;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
}
