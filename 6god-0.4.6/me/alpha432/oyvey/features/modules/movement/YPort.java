//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.entity.EntityLivingBase;
import me.alpha432.oyvey.util.MotionUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class YPort extends Module
{
    public Setting<Mode> modes;
    private final Setting<Double> yPortSpeed;
    public Setting<Boolean> motionyonoff;
    public Setting<Boolean> stepyport;
    private Timer timer;
    private float stepheight;
    
    public YPort() {
        super("YPort", "Speed.", Category.MOVEMENT, true, false, false);
        this.modes = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Yport));
        this.yPortSpeed = (Setting<Double>)this.register(new Setting("YPort Speed", (T)0.1, (T)0.0, (T)1.0, v -> this.modes.getValue() == Mode.Yport));
        this.motionyonoff = (Setting<Boolean>)this.register(new Setting("MotionOff", (T)true));
        this.stepyport = (Setting<Boolean>)this.register(new Setting("OnStep", (T)true));
        this.timer = new Timer();
        this.stepheight = 2.0f;
    }
    
    @Override
    public String getDisplayInfo() {
        return this.modes.currentEnumName();
    }
    
    @Override
    public void onDisable() {
        this.timer.reset();
        EntityUtil.resetTimer();
    }
    
    @Override
    public void onUpdate() {
        if (YPort.mc.player.isSneaking() || YPort.mc.player.isInWater() || YPort.mc.player.isInLava() || YPort.mc.player.isOnLadder()) {
            return;
        }
        if (YPort.mc.player == null || YPort.mc.world == null) {
            this.disable();
            return;
        }
        if (this.modes.getValue() == Mode.Yport) {
            this.handleYPortSpeed();
        }
        if ((!YPort.mc.player.isOnLadder() || YPort.mc.player.isInWater() || YPort.mc.player.isInLava()) && this.stepyport.getValue()) {
            Step.mc.player.stepHeight = this.stepheight;
            StepTwo.mc.player.stepHeight = this.stepheight;
        }
    }
    
    @Override
    public void onToggle() {
        Step.mc.player.stepHeight = 0.6f;
        StepTwo.mc.player.stepHeight = 0.6f;
        if (this.modes.getValue() == Mode.Yport && this.motionyonoff.getValue()) {
            YPort.mc.player.motionY = -3.0;
        }
    }
    
    private void handleYPortSpeed() {
        if (!MotionUtil.isMoving((EntityLivingBase)YPort.mc.player) || (YPort.mc.player.isInWater() && YPort.mc.player.isInLava()) || YPort.mc.player.collidedHorizontally) {
            return;
        }
        if (YPort.mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            YPort.mc.player.jump();
            MotionUtil.setSpeed((EntityLivingBase)YPort.mc.player, MotionUtil.getBaseMoveSpeed() + this.yPortSpeed.getValue());
        }
        else {
            YPort.mc.player.motionY = -1.0;
            EntityUtil.resetTimer();
        }
    }
    
    public enum Mode
    {
        Yport;
    }
}
