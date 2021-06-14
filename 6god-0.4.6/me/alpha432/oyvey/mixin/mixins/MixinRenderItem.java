//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import me.alpha432.oyvey.features.modules.render.ViewModChanger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import me.alpha432.oyvey.features.modules.render.GlintModify;
import me.alpha432.oyvey.OyVey;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    @Shadow
    private void renderModel(final IBakedModel model, final int color, final ItemStack stack) {
    }
    
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(final int oldValue) {
        return OyVey.moduleManager.getModuleByName("GlintModify").isEnabled() ? GlintModify.getColor(1L, 1.0f).getRGB() : oldValue;
    }
    
    @Inject(method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V" }, at = { @At("INVOKE") })
    public void renderItem(final ItemStack stack, final EntityLivingBase entitylivingbaseIn, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        if (ViewModChanger.getInstance().isEnabled() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            final ViewModChanger changer = ViewModChanger.getInstance();
            final float size = changer.size.getValue() / 10.0f;
            GL11.glScalef(size, size, size);
            if (transform.equals((Object)ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)) {
                GL11.glTranslated((double)(changer.offhandX.getValue() / 4.0f), (double)(changer.offhandY.getValue() / 4.0f), (double)(changer.offhandZ.getValue() / 4.0f));
            }
            else {
                GL11.glTranslated((double)(changer.offsetX.getValue() / 4.0f), (double)(changer.offsetY.getValue() / 4.0f), (double)(changer.offsetZ.getValue() / 4.0f));
            }
        }
    }
}
