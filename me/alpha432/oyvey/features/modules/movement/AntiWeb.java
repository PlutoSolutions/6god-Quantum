//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;
import me.alpha432.oyvey.OyVey;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import me.alpha432.oyvey.event.events.BlockCollisionBoundingBoxEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AntiWeb extends Module
{
    public Setting<Boolean> disableBB;
    public Setting<Float> bbOffset;
    public Setting<Boolean> onGround;
    public Setting<Float> motionY;
    public Setting<Float> motionX;
    
    public AntiWeb() {
        super("AntiWeb", "aw", Category.MOVEMENT, true, false, false);
        this.disableBB = (Setting<Boolean>)this.register(new Setting("AddBB", (T)true));
        this.bbOffset = (Setting<Float>)this.register(new Setting("BBOffset", (T)0.4f, (T)(-2.0f), (T)2.0f));
        this.onGround = (Setting<Boolean>)this.register(new Setting("On Ground", (T)true));
        this.motionY = (Setting<Float>)this.register(new Setting("Set MotionY", (T)0.0f, (T)0.0f, (T)20.0f));
        this.motionX = (Setting<Float>)this.register(new Setting("Set MotionX", (T)0.8f, (T)(-1.0f), (T)5.0f));
    }
    
    @SubscribeEvent
    public void bbEvent(final BlockCollisionBoundingBoxEvent event) {
        if (nullCheck()) {
            return;
        }
        if (AntiWeb.mc.world.getBlockState(event.getPos()).getBlock() instanceof BlockWeb && this.disableBB.getValue()) {
            event.setCanceledE(true);
            event.setBoundingBox(Block.FULL_BLOCK_AABB.contract(0.0, (double)this.bbOffset.getValue(), 0.0));
        }
    }
    
    @Override
    public void onUpdate() {
        if (OyVey.moduleManager.isModuleEnabled("WebTP")) {
            return;
        }
        if ((AntiWeb.mc.player.isInWeb && !OyVey.moduleManager.isModuleEnabled("Step")) || (AntiWeb.mc.player.isInWeb && !OyVey.moduleManager.isModuleEnabled("StepTwo"))) {
            if (Keyboard.isKeyDown(AntiWeb.mc.gameSettings.keyBindSneak.keyCode)) {
                AntiWeb.mc.player.isInWeb = true;
                final EntityPlayerSP player = AntiWeb.mc.player;
                player.motionY *= this.motionY.getValue();
            }
            else if (this.onGround.getValue()) {
                AntiWeb.mc.player.onGround = false;
            }
            if (Keyboard.isKeyDown(AntiWeb.mc.gameSettings.keyBindForward.keyCode) || Keyboard.isKeyDown(AntiWeb.mc.gameSettings.keyBindBack.keyCode) || Keyboard.isKeyDown(AntiWeb.mc.gameSettings.keyBindLeft.keyCode) || Keyboard.isKeyDown(AntiWeb.mc.gameSettings.keyBindRight.keyCode)) {
                AntiWeb.mc.player.isInWeb = false;
                final EntityPlayerSP player2 = AntiWeb.mc.player;
                player2.motionX *= this.motionX.getValue();
                final EntityPlayerSP player3 = AntiWeb.mc.player;
                player3.motionZ *= this.motionX.getValue();
            }
        }
    }
}
