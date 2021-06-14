//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.init.Blocks;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class IceSpeed extends Module
{
    private static IceSpeed INSTANCE;
    private final Setting<Float> speed;
    
    public IceSpeed() {
        super("IceSpeed", "Speeds you up on ice.", Category.MOVEMENT, false, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (T)0.4f, (T)0.2f, (T)1.5f));
        IceSpeed.INSTANCE = this;
    }
    
    public static IceSpeed getINSTANCE() {
        if (IceSpeed.INSTANCE == null) {
            IceSpeed.INSTANCE = new IceSpeed();
        }
        return IceSpeed.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.speed.getValue();
        Blocks.PACKED_ICE.slipperiness = this.speed.getValue();
        Blocks.FROSTED_ICE.slipperiness = this.speed.getValue();
    }
    
    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
    
    static {
        IceSpeed.INSTANCE = new IceSpeed();
    }
}
