//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.util.Util;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class BackTP extends Module
{
    private final Setting<RubbeMode> mode;
    private final Setting<Integer> Ym;
    
    public BackTP() {
        super("BackTP", "Teleports u to the latest ground pos", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<RubbeMode>)this.register(new Setting("Mode", (T)RubbeMode.Motion));
        this.Ym = (Setting<Integer>)this.register(new Setting("Motion", (T)5, (T)1, (T)15, v -> this.mode.getValue() == RubbeMode.Motion));
    }
    
    @Override
    public void onEnable() {
        if (Feature.fullNullCheck()) {
            return;
        }
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case Motion: {
                Util.mc.player.motionY = this.Ym.getValue();
                break;
            }
            case Packet: {
                BackTP.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(BackTP.mc.player.posX, BackTP.mc.player.posY + this.Ym.getValue(), BackTP.mc.player.posZ, true));
                break;
            }
            case Teleport: {
                BackTP.mc.player.setPositionAndUpdate(BackTP.mc.player.posX, BackTP.mc.player.posY + this.Ym.getValue(), BackTP.mc.player.posZ);
                break;
            }
        }
        this.toggle();
    }
    
    public enum RubbeMode
    {
        Motion, 
        Teleport, 
        Packet;
    }
}
