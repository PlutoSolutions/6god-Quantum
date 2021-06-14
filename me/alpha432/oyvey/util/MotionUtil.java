//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class MotionUtil implements Util
{
    public static boolean isMoving(final EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }
    
    public static boolean isMovings() {
        return Minecraft.getMinecraft().player.moveForward != 0.0 || Minecraft.getMinecraft().player.moveStrafing != 0.0;
    }
    
    public static void setSpeed(final EntityLivingBase entity, final double speed) {
        final double[] dir = forward(speed);
        entity.motionX = dir[0];
        entity.motionZ = dir[1];
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isPotionActive(Potion.getPotionById(1))) {
            final int amplifier = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static double[] forward(final double speed) {
        float forward = Minecraft.getMinecraft().player.movementInput.moveForward;
        float side = Minecraft.getMinecraft().player.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().player.prevRotationYaw + (Minecraft.getMinecraft().player.rotationYaw - Minecraft.getMinecraft().player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static double[] getMoveSpeed(final double speed) {
        float forward = MotionUtil.mc.player.movementInput.moveForward;
        float strafe = MotionUtil.mc.player.movementInput.moveStrafe;
        float yaw = MotionUtil.mc.player.rotationYaw;
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double motionX = forward * speed * cos + strafe * speed * sin;
        double motionZ = forward * speed * sin - strafe * speed * cos;
        if (!isMovingMomentum()) {
            motionX = 0.0;
            motionZ = 0.0;
        }
        return new double[] { motionX, motionZ };
    }
    
    public static boolean isMovingMomentum() {
        return MotionUtil.mc.player.moveForward != 0.0 || MotionUtil.mc.player.moveStrafing != 0.0;
    }
}
