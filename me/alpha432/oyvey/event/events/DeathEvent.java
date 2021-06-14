// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.event.EventStage;

public class DeathEvent extends EventStage
{
    public EntityPlayer player;
    
    public DeathEvent(final EntityPlayer player) {
        this.player = player;
    }
}
