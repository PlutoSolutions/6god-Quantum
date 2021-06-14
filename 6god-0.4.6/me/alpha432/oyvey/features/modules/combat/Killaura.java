//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.DamageUtil;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Killaura extends Module
{
    private final Setting<Settings> setting;
    public static Entity target;
    private final Timer timer;
    private final Setting<Boolean> delay;
    public Setting<Float> range;
    public Setting<Boolean> rotate;
    public Setting<Boolean> onlySharp;
    public Setting<Float> raytrace;
    public Setting<Boolean> tps;
    public Setting<Boolean> packet;
    public Setting<Boolean> info;
    private final Setting<TargetMode> targetMode;
    public Setting<Float> health;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    
    public Killaura() {
        super("KillAura", "ka", Category.COMBAT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.MAIN));
        this.timer = new Timer();
        this.delay = (Setting<Boolean>)this.register(new Setting("HitDelay", (T)true, v -> this.setting.getValue() == Settings.MAIN));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)6.0f, (T)0.1f, (T)7.0f, v -> this.setting.getValue() == Settings.MAIN));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true, v -> this.setting.getValue() == Settings.MAIN));
        this.onlySharp = (Setting<Boolean>)this.register(new Setting("SwordOnly", (T)true, v -> this.setting.getValue() == Settings.MAIN));
        this.raytrace = (Setting<Float>)this.register(new Setting("Raytrace", (T)6.0f, (T)0.1f, (T)7.0f, v -> this.setting.getValue() == Settings.MAIN));
        this.tps = (Setting<Boolean>)this.register(new Setting("TpsSync", (T)true, v -> this.setting.getValue() == Settings.MAIN));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", (T)false, v -> this.setting.getValue() == Settings.MAIN));
        this.info = (Setting<Boolean>)this.register(new Setting("Info", (T)true, v -> this.setting.getValue() == Settings.MAIN));
        this.targetMode = (Setting<TargetMode>)this.register(new Setting("Target", (T)TargetMode.CLOSEST, v -> this.setting.getValue() == Settings.MAIN));
        this.health = (Setting<Float>)this.register(new Setting("Health", (T)6.0f, (T)0.1f, (T)36.0f, v -> this.targetMode.getValue() == TargetMode.SMART));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (T)true, v -> this.setting.getValue() == Settings.TARGETS));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Mobs", (T)false, v -> this.setting.getValue() == Settings.TARGETS));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", (T)false, v -> this.setting.getValue() == Settings.TARGETS));
        this.vehicles = (Setting<Boolean>)this.register(new Setting("Entities", (T)false, v -> this.setting.getValue() == Settings.TARGETS));
        this.projectiles = (Setting<Boolean>)this.register(new Setting("Projectiles", (T)false, v -> this.setting.getValue() == Settings.TARGETS));
    }
    
    @Override
    public void onTick() {
        if (!this.rotate.getValue()) {
            this.doKillaura();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue()) {
            this.doKillaura();
        }
    }
    
    private void doKillaura() {
        if (this.onlySharp.getValue() && !EntityUtil.holdingWeapon((EntityPlayer)Killaura.mc.player)) {
            Killaura.target = null;
            return;
        }
        if (this.targetMode.getValue() != TargetMode.FOCUS || Killaura.target == null || (Killaura.mc.player.getDistanceSq(Killaura.target) >= MathUtil.square(this.range.getValue()) && !EntityUtil.canEntityFeetBeSeen(Killaura.target) && Killaura.mc.player.getDistanceSq(Killaura.target) >= MathUtil.square(this.raytrace.getValue()))) {
            Killaura.target = this.getTarget();
        }
        final int wait = this.delay.getValue() ? ((int)(DamageUtil.getCooldownByWeapon((EntityPlayer)Killaura.mc.player) * (this.tps.getValue() ? OyVey.serverManager.getTpsFactor() : 1.0f))) : 0;
        if (!this.timer.passedMs(wait)) {
            return;
        }
        Killaura.target = this.getTarget();
        if (Killaura.target == null) {
            return;
        }
        if (this.rotate.getValue()) {
            OyVey.rotationManager.lookAtEntity(Killaura.target);
        }
        EntityUtil.attackEntity(Killaura.target, this.packet.getValue(), true);
        this.timer.reset();
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : Killaura.mc.world.loadedEntityList) {
            if (((this.players.getValue() && entity instanceof EntityPlayer) || (this.animals.getValue() && EntityUtil.isPassive(entity)) || (this.mobs.getValue() && EntityUtil.isMobAggressive(entity)) || (this.vehicles.getValue() && EntityUtil.isVehicle(entity)) || (this.projectiles.getValue() && EntityUtil.isProjectile(entity))) && (!(entity instanceof EntityLivingBase) || !EntityUtil.isntValid(entity, distance))) {
                if (!Killaura.mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && Killaura.mc.player.getDistanceSq(entity) > MathUtil.square(this.raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = Killaura.mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (this.targetMode.getValue() == TargetMode.SMART && EntityUtil.getHealth(entity) < this.health.getValue()) {
                        target = entity;
                        break;
                    }
                    if (this.targetMode.getValue() != TargetMode.HEALTH && Killaura.mc.player.getDistanceSq(entity) < distance) {
                        target = entity;
                        distance = Killaura.mc.player.getDistanceSq(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (this.targetMode.getValue() != TargetMode.HEALTH) {
                        continue;
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = Killaura.mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.info.getValue() && Killaura.target instanceof EntityPlayer) {
            return Killaura.target.getName();
        }
        return null;
    }
    
    public enum Settings
    {
        MAIN, 
        TARGETS;
    }
    
    public enum TargetMode
    {
        FOCUS, 
        CLOSEST, 
        HEALTH, 
        SMART;
    }
}
