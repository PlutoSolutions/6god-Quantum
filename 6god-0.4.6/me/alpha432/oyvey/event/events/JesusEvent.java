// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.event.events;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.alpha432.oyvey.event.EventStage;

@Cancelable
public class JesusEvent extends EventStage
{
    private BlockPos pos;
    private AxisAlignedBB boundingBox;
    
    public JesusEvent(final int stage, final BlockPos pos) {
        super(stage);
        this.pos = pos;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}
