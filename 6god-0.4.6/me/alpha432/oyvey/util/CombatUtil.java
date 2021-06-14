//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CombatUtil implements Util
{
    public static EntityPlayer getTarget(final float range) {
        EntityPlayer currentTarget = null;
        for (int size = Util.mc.world.playerEntities.size(), i = 0; i < size; ++i) {
            final EntityPlayer player = Util.mc.world.playerEntities.get(i);
            if (!EntityUtil.isntValid((Entity)player, range)) {
                if (currentTarget == null) {
                    currentTarget = player;
                }
                else if (Util.mc.player.getDistanceSq((Entity)player) < Util.mc.player.getDistanceSq((Entity)currentTarget)) {
                    currentTarget = player;
                }
            }
        }
        return currentTarget;
    }
}
