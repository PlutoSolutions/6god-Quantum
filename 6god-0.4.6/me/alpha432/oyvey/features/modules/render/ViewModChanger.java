// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ViewModChanger extends Module
{
    public final Setting<Float> size;
    public final Setting<Float> offsetX;
    public final Setting<Float> offsetY;
    public final Setting<Float> offsetZ;
    public final Setting<Float> offhandX;
    public final Setting<Float> offhandY;
    public final Setting<Float> offhandZ;
    public final Setting<Float> armPitch;
    private static ViewModChanger INSTANCE;
    
    public ViewModChanger() {
        super("ViewMod", "Changes ViewModelChanger of items", Category.RENDER, false, false, false);
        this.size = (Setting<Float>)this.register(new Setting("Size", (T)10.0f, (T)0.0f, (T)15.0f));
        this.offsetX = (Setting<Float>)this.register(new Setting("OffsetX", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.offsetY = (Setting<Float>)this.register(new Setting("OffsetY", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.offsetZ = (Setting<Float>)this.register(new Setting("OffsetZ", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.offhandX = (Setting<Float>)this.register(new Setting("OffhandX", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.offhandY = (Setting<Float>)this.register(new Setting("OffhandY", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.offhandZ = (Setting<Float>)this.register(new Setting("OffhandZ", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.armPitch = (Setting<Float>)this.register(new Setting("Arm Pitch", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.setInstance();
    }
    
    public static ViewModChanger getInstance() {
        if (ViewModChanger.INSTANCE == null) {
            ViewModChanger.INSTANCE = new ViewModChanger();
        }
        return ViewModChanger.INSTANCE;
    }
    
    private void setInstance() {
        ViewModChanger.INSTANCE = this;
    }
    
    static {
        ViewModChanger.INSTANCE = new ViewModChanger();
    }
}
