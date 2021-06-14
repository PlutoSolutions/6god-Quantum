// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.alpha432.oyvey.event.events.BlockCollisionBoundingBoxEvent;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockWeb;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockWeb.class })
public class MixinBlockWeb
{
    @Inject(method = { "getCollisionBoundingBox" }, at = { @At("HEAD") }, cancellable = true)
    public void getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> callbackInfoReturnable) {
        final BlockCollisionBoundingBoxEvent bb = new BlockCollisionBoundingBoxEvent(pos);
        MinecraftForge.EVENT_BUS.post((Event)bb);
        if (bb.isCanceledE()) {
            callbackInfoReturnable.setReturnValue(bb.getBoundingBox());
            callbackInfoReturnable.cancel();
        }
    }
}
