// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Container.class })
public interface IContainer
{
    @Accessor("transactionID")
    void setTransactionID(final short p0);
}
