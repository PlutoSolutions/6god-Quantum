//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.DamageUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Collection;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.features.modules.client.Managers;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.Feature;

public class SafetyManager extends Feature implements Runnable
{
    private final Timer syncTimer;
    private final AtomicBoolean SAFE;
    private ScheduledExecutorService service;
    
    public SafetyManager() {
        this.syncTimer = new Timer();
        this.SAFE = new AtomicBoolean(false);
    }
    
    @Override
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }
    
    public void doSafetyCheck() {
        if (!Feature.fullNullCheck()) {
            boolean safe = true;
            final EntityPlayer entityPlayer;
            final EntityPlayer closest = entityPlayer = (Managers.getInstance().safety.getValue() ? EntityUtil.getClosestEnemy(18.0) : null);
            if (Managers.getInstance().safety.getValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }
            final ArrayList<Entity> crystals = new ArrayList<Entity>(SafetyManager.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal && DamageUtil.calculateDamage(crystal, (Entity)SafetyManager.mc.player) > 4.0) {
                    if (closest != null && closest.getDistanceSq(crystal) >= 40.0) {
                        continue;
                    }
                    safe = false;
                    break;
                }
            }
            if (safe) {
                for (final BlockPos pos : BlockUtil.possiblePlacePositionsCa(4.0f, false, Managers.getInstance().oneDot15.getValue())) {
                    if (DamageUtil.calculateDamage(pos, (Entity)SafetyManager.mc.player) > 4.0) {
                        if (closest != null && closest.getDistanceSq(pos) >= 40.0) {
                            continue;
                        }
                        safe = false;
                        break;
                    }
                }
            }
            this.SAFE.set(safe);
        }
    }
    
    public void onUpdate() {
        this.run();
    }
    
    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "§aSecure";
        }
        return "§cUnsafe";
    }
    
    public boolean isSafe() {
        return this.SAFE.get();
    }
    
    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().safetyCheck.getValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}
