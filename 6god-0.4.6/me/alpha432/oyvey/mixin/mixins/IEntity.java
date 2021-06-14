// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public interface IEntity
{
    @Accessor("isInWeb")
    boolean getIsInWeb();
}
