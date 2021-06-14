//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.events.PushEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.init.Blocks;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Velocity extends Module
{
    private static Velocity INSTANCE;
    public Setting<Boolean> knockBack;
    public Setting<Boolean> noPush;
    public Setting<Float> horizontal;
    public Setting<Float> vertical;
    public Setting<Boolean> explosions;
    public Setting<Boolean> bobbers;
    public Setting<Boolean> water;
    public Setting<Boolean> blocks;
    public Setting<Boolean> ice;
    
    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Category.MOVEMENT, true, false, false);
        this.knockBack = (Setting<Boolean>)this.register(new Setting("KnockBack", (T)true));
        this.noPush = (Setting<Boolean>)this.register(new Setting("NoPush", (T)true));
        this.horizontal = (Setting<Float>)this.register(new Setting("Horizontal", (T)0.0f, (T)0.0f, (T)100.0f));
        this.vertical = (Setting<Float>)this.register(new Setting("Vertical", (T)0.0f, (T)0.0f, (T)100.0f));
        this.explosions = (Setting<Boolean>)this.register(new Setting("Explosions", (T)true));
        this.bobbers = (Setting<Boolean>)this.register(new Setting("Bobbers", (T)true));
        this.water = (Setting<Boolean>)this.register(new Setting("Water", (T)false));
        this.blocks = (Setting<Boolean>)this.register(new Setting("Blocks", (T)false));
        this.ice = (Setting<Boolean>)this.register(new Setting("Ice", (T)false));
        this.setInstance();
    }
    
    public static Velocity getINSTANCE() {
        if (Velocity.INSTANCE == null) {
            Velocity.INSTANCE = new Velocity();
        }
        return Velocity.INSTANCE;
    }
    
    private void setInstance() {
        Velocity.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (IceSpeed.getINSTANCE().isOff() && this.ice.getValue()) {
            Blocks.ICE.slipperiness = 0.6f;
            Blocks.PACKED_ICE.slipperiness = 0.6f;
            Blocks.FROSTED_ICE.slipperiness = 0.6f;
        }
    }
    
    @Override
    public void onDisable() {
        if (IceSpeed.getINSTANCE().isOff()) {
            Blocks.ICE.slipperiness = 0.98f;
            Blocks.PACKED_ICE.slipperiness = 0.98f;
            Blocks.FROSTED_ICE.slipperiness = 0.98f;
        }
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && Velocity.mc.player != null) {
            final SPacketEntityVelocity velocity;
            if (this.knockBack.getValue() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = event.getPacket()).getEntityID() == Velocity.mc.player.entityId) {
                if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }
                velocity.motionX *= (int)(Object)this.horizontal.getValue();
                velocity.motionY *= (int)(Object)this.vertical.getValue();
                velocity.motionZ *= (int)(Object)this.horizontal.getValue();
            }
            final SPacketEntityStatus packet;
            final Entity entity;
            if (event.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue() && (packet = event.getPacket()).getOpCode() == 31 && (entity = packet.getEntity((World)Velocity.mc.world)) instanceof EntityFishHook) {
                final EntityFishHook fishHook = (EntityFishHook)entity;
                if (fishHook.caughtEntity == Velocity.mc.player) {
                    event.setCanceled(true);
                }
            }
            if (this.explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
                final SPacketExplosion sPacketExplosion;
                final SPacketExplosion velocity_ = sPacketExplosion = event.getPacket();
                sPacketExplosion.motionX *= this.horizontal.getValue();
                final SPacketExplosion sPacketExplosion2 = velocity_;
                sPacketExplosion2.motionY *= this.vertical.getValue();
                final SPacketExplosion sPacketExplosion3 = velocity_;
                sPacketExplosion3.motionZ *= this.horizontal.getValue();
            }
        }
    }
    
    @SubscribeEvent
    public void onPush(final PushEvent event) {
        if (event.getStage() == 0 && this.noPush.getValue() && event.entity.equals((Object)Velocity.mc.player)) {
            if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }
            event.x = -event.x * this.horizontal.getValue();
            event.y = -event.y * this.vertical.getValue();
            event.z = -event.z * this.horizontal.getValue();
        }
        else if (event.getStage() == 1 && this.blocks.getValue()) {
            event.setCanceled(true);
        }
        else if (event.getStage() == 2 && this.water.getValue() && Velocity.mc.player != null && Velocity.mc.player.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
    
    static {
        Velocity.INSTANCE = new Velocity();
    }
}
