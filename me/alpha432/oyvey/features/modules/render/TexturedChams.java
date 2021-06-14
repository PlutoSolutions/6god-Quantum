// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class TexturedChams extends Module
{
    public static Setting<Integer> red;
    public static Setting<Integer> green;
    public static Setting<Integer> blue;
    public static Setting<Integer> alpha;
    public static Setting<Boolean> rainbow;
    public static Setting<Integer> rainbowhue;
    
    public TexturedChams() {
        super("TexturedChams", "hi yes", Category.RENDER, true, false, true);
        TexturedChams.red = (Setting<Integer>)this.register(new Setting("Red", (T)168, (T)0, (T)255));
        TexturedChams.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        TexturedChams.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)232, (T)0, (T)255));
        TexturedChams.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)150, (T)0, (T)255));
        TexturedChams.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false));
        TexturedChams.rainbowhue = (Setting<Integer>)this.register(new Setting("Brightness", (T)150, (T)0, (T)255));
    }
}
