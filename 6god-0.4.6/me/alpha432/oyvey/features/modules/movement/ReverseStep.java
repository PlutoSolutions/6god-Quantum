//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ReverseStep extends Module
{
    private final Setting<Integer> speed;
    private final Setting<Cancel> canceller;
    private static ReverseStep INSTANCE;
    
    public ReverseStep() {
        super("ReverseStep", "rs", Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Integer>)this.register(new Setting("Speed", (T)8, (T)1, (T)20));
        this.canceller = (Setting<Cancel>)this.register(new Setting("CancelType", (T)Cancel.None));
        this.setInstance();
    }
    
    public static ReverseStep getInstance() {
        if (ReverseStep.INSTANCE == null) {
            ReverseStep.INSTANCE = new ReverseStep();
        }
        return ReverseStep.INSTANCE;
    }
    
    private void setInstance() {
        ReverseStep.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isSneaking() || ReverseStep.mc.player.isDead || ReverseStep.mc.player.collidedHorizontally || !ReverseStep.mc.player.onGround || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isOnLadder() || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() || OyVey.moduleManager.isModuleEnabled("Burrow") || ReverseStep.mc.player.noClip || OyVey.moduleManager.isModuleEnabled("Packetfly") || OyVey.moduleManager.isModuleEnabled("Phase") || (ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Shift) || (ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Both) || (ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Space) || (ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Both) || OyVey.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        if (ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player = ReverseStep.mc.player;
            player.motionY -= this.speed.getValue() / 10.0f;
        }
    }
    
    static {
        ReverseStep.INSTANCE = new ReverseStep();
    }
    
    public enum Cancel
    {
        None, 
        Space, 
        Shift, 
        Both;
    }
}
