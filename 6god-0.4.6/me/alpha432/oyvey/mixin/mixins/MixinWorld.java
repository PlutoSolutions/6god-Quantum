//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.features.modules.render.NoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import org.spongepowered.asm.mixin.injection.Inject;
import me.alpha432.oyvey.features.modules.misc.Tracker;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ World.class })
public class MixinWorld
{
    @Redirect(method = { "getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"))
    public <T extends Entity> void getEntitiesOfTypeWithinAABBHook(final Chunk chunk, final Class<? extends T> entityClass, final AxisAlignedBB aabb, final List<T> listToFill, final Predicate<? super T> filter) {
        try {
            chunk.getEntitiesOfTypeWithinAABB((Class)entityClass, aabb, (List)listToFill, (Predicate)filter);
        }
        catch (Exception ex) {}
    }
    
    @Inject(method = { "onEntityAdded" }, at = { @At("HEAD") })
    private void onEntityAdded(final Entity entityIn, final CallbackInfo ci) {
        Tracker.getInstance().onSpawnEntity(entityIn);
    }
    
    @Inject(method = { "checkLightFor" }, at = { @At("HEAD") }, cancellable = true)
    private void updateLightmapHook(final EnumSkyBlock lightType, final BlockPos pos, final CallbackInfoReturnable<Boolean> info) {
        if (lightType == EnumSkyBlock.SKY && NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.WORLD || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
    
    @Redirect(method = { "handleMaterialAcceleration" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean isPushedbyWaterHook(final Entity entity) {
        final PushEvent event = new PushEvent(2, entity);
        MinecraftForge.EVENT_BUS.post((Event)event);
        return entity.isPushedByWater() && !event.isCanceled();
    }
}
