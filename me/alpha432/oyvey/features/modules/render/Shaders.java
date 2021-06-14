//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Shaders extends Module
{
    public Setting<Mode> shader;
    private static Shaders INSTANCE;
    
    public Shaders() {
        super("Shaders", "i dont even know anymore", Category.RENDER, false, false, false);
        this.shader = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.green));
    }
    
    @Override
    public void onUpdate() {
        if (OpenGlHelper.shadersSupported && Shaders.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (Shaders.mc.entityRenderer.getShaderGroup() != null) {
                Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                Shaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.shader.getValue() + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Shaders.mc.entityRenderer.getShaderGroup() != null && Shaders.mc.currentScreen == null) {
            Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.shader.currentEnumName();
    }
    
    @Override
    public void onDisable() {
        if (Shaders.mc.entityRenderer.getShaderGroup() != null) {
            Shaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    static {
        Shaders.INSTANCE = new Shaders();
    }
    
    public enum Mode
    {
        notch, 
        antialias, 
        art, 
        bits, 
        blobs, 
        blobs2, 
        blur, 
        bumpy, 
        color_convolve, 
        creeper, 
        deconverge, 
        desaturate, 
        entity_outline, 
        flip, 
        fxaa, 
        green, 
        invert, 
        ntsc, 
        outline, 
        pencil, 
        phosphor, 
        scan_pincusion, 
        sobel, 
        spider, 
        wobble;
    }
}
