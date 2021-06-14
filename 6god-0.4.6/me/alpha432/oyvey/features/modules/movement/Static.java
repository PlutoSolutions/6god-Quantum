//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import java.lang.reflect.Field;
import net.minecraft.util.Timer;
import me.alpha432.oyvey.manager.Mapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Static extends Module
{
    private final Setting<Mode> mode;
    
    public Static() {
        super("AntiVoid", "av", Category.MOVEMENT, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Freeze));
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (Static.mc.player.posY <= 0.0) {
            switch (this.mode.getValue()) {
                case Float: {
                    Static.mc.player.motionY = 0.5;
                    break;
                }
                case Freeze: {
                    Static.mc.player.motionY = 0.0;
                    break;
                }
                case SlowFall: {
                    final EntityPlayerSP player = Static.mc.player;
                    player.motionY /= 4.0;
                    break;
                }
                case TP: {
                    Static.mc.player.setPosition(Static.mc.player.posX, Static.mc.player.posY + 2.0, Static.mc.player.posZ);
                    break;
                }
            }
        }
    }
    
    private void setTimer(final float value) {
        try {
            final Field timer = Minecraft.class.getDeclaredField(Mapping.timer);
            timer.setAccessible(true);
            final Field tickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(Static.mc), 50.0f / value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return " " + this.mode.getValue();
    }
    
    public enum Mode
    {
        Float, 
        Freeze, 
        SlowFall, 
        TP;
    }
}
