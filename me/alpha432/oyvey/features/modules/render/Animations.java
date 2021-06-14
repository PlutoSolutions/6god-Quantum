//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Animations extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Swing> swing;
    private final Setting<Boolean> slow;
    
    public Animations() {
        super("Animations", "Change animations", Category.RENDER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Low));
        this.swing = (Setting<Swing>)this.register(new Setting("Swing", (T)Swing.Mainhand));
        this.slow = (Setting<Boolean>)this.register(new Setting("Slow", (T)true));
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.swing.getValue() == Swing.Offhand) {
            Animations.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.High && Animations.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Animations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animations.mc.entityRenderer.itemRenderer.itemStackMainHand = Animations.mc.player.getHeldItemMainhand();
        }
    }
    
    @Override
    public void onEnable() {
        if (this.slow.getValue()) {
            Animations.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000));
        }
    }
    
    @Override
    public void onDisable() {
        if (this.slow.getValue()) {
            Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final Packet<?> raw = event.getPacket();
        if (raw instanceof CPacketAnimation) {
            final CPacketAnimation packet = (CPacketAnimation)raw;
            if (this.swing.getValue() == Swing.Packet) {
                event.setCanceled(true);
            }
        }
    }
    
    private enum Swing
    {
        Mainhand, 
        Offhand, 
        Packet;
    }
    
    private enum Mode
    {
        Low, 
        High;
    }
}
