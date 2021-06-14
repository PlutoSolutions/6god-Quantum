// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.OyVey;
import java.awt.Color;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class GlintModify extends Module
{
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Boolean> rainbow;
    
    public GlintModify() {
        super("GlintModify", "Changes the enchant glint color", Category.RENDER, true, false, true);
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false));
    }
    
    public static Color getColor(final long offset, final float fade) {
        if (!OyVey.moduleManager.getModuleT(GlintModify.class).rainbow.getValue()) {
            return new Color(OyVey.moduleManager.getModuleT(GlintModify.class).red.getValue(), OyVey.moduleManager.getModuleT(GlintModify.class).green.getValue(), OyVey.moduleManager.getModuleT(GlintModify.class).blue.getValue());
        }
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    @Override
    public void onUpdate() {
        if (this.rainbow.getValue()) {
            this.cycleRainbow();
        }
    }
    
    public void cycleRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.red.setValue(color_rgb_o >> 16 & 0xFF);
        this.green.setValue(color_rgb_o >> 8 & 0xFF);
        this.blue.setValue(color_rgb_o & 0xFF);
    }
}
