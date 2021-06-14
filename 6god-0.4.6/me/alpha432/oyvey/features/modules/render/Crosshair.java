//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Crosshair extends Module
{
    private final Setting<Boolean> dynamic;
    private final Setting<Float> width;
    private final Setting<Float> gap;
    private final Setting<Float> length;
    private final Setting<Float> dynamicGap;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    public static Crosshair INSTANCE;
    
    public Crosshair() {
        super("Crosshair", "", Category.RENDER, true, false, false);
        this.dynamic = (Setting<Boolean>)this.register(new Setting("Dynamic", (T)true));
        this.width = (Setting<Float>)this.register(new Setting("Width", (T)1.0f, (T)0.5f, (T)10.0f));
        this.gap = (Setting<Float>)this.register(new Setting("Gap", (T)3.0f, (T)0.5f, (T)10.0f));
        this.length = (Setting<Float>)this.register(new Setting("Length", (T)7.0f, (T)0.5f, (T)100.0f));
        this.dynamicGap = (Setting<Float>)this.register(new Setting("DynamicGap", (T)1.5f, (T)0.5f, (T)10.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        Crosshair.INSTANCE = this;
    }
    
    @SubscribeEvent
    @Override
    public void onRender2D(final Render2DEvent event) {
        final int color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB();
        final ScaledResolution resolution = new ScaledResolution(Crosshair.mc);
        final float middlex = resolution.getScaledWidth() / 2.0f;
        final float middley = resolution.getScaledHeight() / 2.0f;
        RenderUtil.drawBordered(middlex - this.width.getValue(), middley - (this.gap.getValue() + this.length.getValue()) - ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), middlex + this.width.getValue(), middley - this.gap.getValue() - ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), 0.5f, color, -16777216);
        RenderUtil.drawBordered(middlex - this.width.getValue(), middley + this.gap.getValue() + ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), middlex + this.width.getValue(), middley + (this.gap.getValue() + this.length.getValue()) + ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), 0.5f, color, -16777216);
        RenderUtil.drawBordered(middlex - (this.gap.getValue() + this.length.getValue()) - ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), middley - this.width.getValue(), middlex - this.gap.getValue() - ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), middley + this.width.getValue(), 0.5f, color, -16777216);
        RenderUtil.drawBordered(middlex + this.gap.getValue() + ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), middley - this.width.getValue(), middlex + (this.gap.getValue() + this.length.getValue()) + ((this.isMoving() && this.dynamic.getValue()) ? this.dynamicGap.getValue() : 0.0f), middley + this.width.getValue(), 0.5f, color, -16777216);
    }
    
    public boolean isMoving() {
        return Crosshair.mc.player.moveForward != 0.0f || Crosshair.mc.player.moveStrafing != 0.0f || Crosshair.mc.player.moveVertical != 0.0f;
    }
    
    public int color(final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(this.red.getValue(), this.green.getValue(), this.blue.getValue(), hsb);
        float brightness = Math.abs((getOffset() + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.4f + 0.4f * brightness;
        hsb[2] = brightness % 1.0f;
        final Color clr = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), this.alpha.getValue()).getRGB();
    }
    
    private static float getOffset() {
        return System.currentTimeMillis() % 2000L / 1000.0f;
    }
}
