// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.alpha432.oyvey.features.modules.render.NoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LayerArmorBase.class })
public class MixinLayerArmorBase
{
    @Inject(method = { "doRenderLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noArmor.getValue() == NoRender.NoArmor.ALL) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderArmorLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void renderArmorLayer(final EntityLivingBase entityLivingBaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final EntityEquipmentSlot slotIn, final CallbackInfo ci) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noArmor.getValue() == NoRender.NoArmor.HELMET && slotIn == EntityEquipmentSlot.HEAD) {
            ci.cancel();
        }
    }
}
