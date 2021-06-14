//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.Overwrite;
import org.lwjgl.opengl.GL11;
import me.alpha432.oyvey.features.modules.render.TexturedChams;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.alpha432.oyvey.OyVey;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void renderEntityNameHook(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (OyVey.moduleManager.isModuleEnabled("NameTags")) {
            info.cancel();
        }
    }
    
    @Overwrite
    public ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        if (OyVey.moduleManager.isModuleEnabled("TexturedChams")) {
            GL11.glColor4f(TexturedChams.red.getValue() / 255.0f, TexturedChams.green.getValue() / 255.0f, TexturedChams.blue.getValue() / 255.0f, TexturedChams.alpha.getValue() / 255.0f);
            return new ResourceLocation("minecraft:steve_skin1.png");
        }
        return entity.getLocationSkin();
    }
}
