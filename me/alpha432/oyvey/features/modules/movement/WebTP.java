//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class WebTP extends Module
{
    private final Setting<Mode> mode;
    
    public WebTP() {
        super("WebTP", "", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Vanilla));
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (WebTP.mc.player.isInWeb) {
            switch (this.mode.getValue()) {
                case Normal: {
                    for (int i = 0; i < 10; ++i) {
                        final EntityPlayerSP player = WebTP.mc.player;
                        --player.motionY;
                    }
                    break;
                }
                case Vanilla: {
                    WebTP.mc.player.isInWeb = false;
                    break;
                }
            }
        }
    }
    
    public enum Mode
    {
        Normal, 
        Vanilla;
    }
}
