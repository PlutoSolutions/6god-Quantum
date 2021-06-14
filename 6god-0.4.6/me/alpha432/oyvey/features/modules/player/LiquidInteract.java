// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class LiquidInteract extends Module
{
    private static LiquidInteract INSTANCE;
    
    public LiquidInteract() {
        super("LiquidInteract", "li", Category.PLAYER, false, false, false);
        this.setInstance();
    }
    
    public static LiquidInteract getInstance() {
        if (LiquidInteract.INSTANCE == null) {
            LiquidInteract.INSTANCE = new LiquidInteract();
        }
        return LiquidInteract.INSTANCE;
    }
    
    private void setInstance() {
        LiquidInteract.INSTANCE = this;
    }
    
    static {
        LiquidInteract.INSTANCE = new LiquidInteract();
    }
}
