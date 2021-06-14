// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;

@Mixin({ LayerBipedArmor.class })
public abstract class MixinLayerBipedArmor extends LayerArmorBase<ModelBiped>
{
    public MixinLayerBipedArmor(final RenderLivingBase<?> rendererIn) {
        super((RenderLivingBase)rendererIn);
    }
}
