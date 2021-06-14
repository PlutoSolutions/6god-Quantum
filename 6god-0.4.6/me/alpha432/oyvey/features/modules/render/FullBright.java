//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class FullBright extends Module
{
    private final Setting<Mode> mode;
    float oldBright;
    
    public FullBright() {
        super("FullBright", "", Category.RENDER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Gamma));
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.Potion) {
            FullBright.mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
        }
    }
    
    @Override
    public void onEnable() {
        if (nullCheck()) {
            return;
        }
        this.oldBright = FullBright.mc.gameSettings.gammaSetting;
        if (this.mode.getValue() == Mode.Gamma) {
            FullBright.mc.gameSettings.gammaSetting = 100.0f;
        }
    }
    
    @Override
    public void onDisable() {
        FullBright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        if (this.mode.getValue() == Mode.Gamma) {
            FullBright.mc.gameSettings.gammaSetting = this.oldBright;
        }
    }
    
    public enum Mode
    {
        Gamma, 
        Potion;
    }
}
