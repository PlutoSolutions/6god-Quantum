// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.alpha432.oyvey.event.EventStage;

@Cancelable
public class ProcessRightClickBlockEvent extends EventStage
{
    public BlockPos pos;
    public EnumHand hand;
    public ItemStack stack;
    
    public ProcessRightClickBlockEvent(final BlockPos pos, final EnumHand hand, final ItemStack stack) {
        this.pos = pos;
        this.hand = hand;
        this.stack = stack;
    }
}
