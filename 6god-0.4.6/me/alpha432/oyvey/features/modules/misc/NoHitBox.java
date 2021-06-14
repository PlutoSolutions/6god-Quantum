// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class NoHitBox extends Module
{
    private static NoHitBox INSTANCE;
    public Setting<Boolean> pickaxe;
    public Setting<Boolean> crystal;
    public Setting<Boolean> gapple;
    
    public NoHitBox() {
        super("NoHitBox", "nhb", Category.MISC, true, false, false);
        this.pickaxe = (Setting<Boolean>)this.register(new Setting("Pickaxe", (T)true));
        this.crystal = (Setting<Boolean>)this.register(new Setting("Crystal", (T)true));
        this.gapple = (Setting<Boolean>)this.register(new Setting("Gapple", (T)true));
        this.setInstance();
    }
    
    public static NoHitBox getINSTANCE() {
        if (NoHitBox.INSTANCE == null) {
            NoHitBox.INSTANCE = new NoHitBox();
        }
        return NoHitBox.INSTANCE;
    }
    
    private void setInstance() {
        NoHitBox.INSTANCE = this;
    }
    
    static {
        NoHitBox.INSTANCE = new NoHitBox();
    }
}
