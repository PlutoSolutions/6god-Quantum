// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ CPacketUseEntity.class })
public interface AccessorCPacketUseEntity
{
    @Accessor("entityId")
    void setEntityId(final int p0);
    
    @Accessor("action")
    void setAction(final CPacketUseEntity.Action p0);
}
