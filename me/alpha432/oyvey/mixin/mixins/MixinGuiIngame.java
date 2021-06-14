//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.OyVey;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.alpha432.oyvey.features.modules.render.NoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiIngame.class })
public class MixinGuiIngame extends Gui
{
    @Shadow
    @Final
    public GuiNewChat persistantChatGUI;
    
    @Inject(method = { "renderPortal" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPortalHook(final float n, final ScaledResolution scaledResolution, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().portal.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPumpkinOverlay" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPumpkinOverlayHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().pumpkin.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPotionEffects" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPotionEffectsHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        if (OyVey.moduleManager != null) {
            info.cancel();
        }
    }
}
