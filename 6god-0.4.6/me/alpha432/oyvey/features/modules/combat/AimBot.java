// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.util.RotationUtil;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AimBot extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Float> range;
    private final Setting<Boolean> onlyBow;
    EntityPlayer aimTarget;
    RotationUtil aimbotRotation;
    
    public AimBot() {
        super("AimBot", "ab", Category.COMBAT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Rotate", (T)Mode.None));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)8.0f, (T)0.0f, (T)20.0f));
        this.onlyBow = (Setting<Boolean>)this.register(new Setting("Bow Only", (T)true));
        this.aimTarget = null;
        this.aimbotRotation = null;
    }
    
    public enum Mode
    {
        Legit, 
        Packet, 
        None;
    }
}
