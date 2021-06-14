//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.util.MovementInput;
import net.minecraft.client.Minecraft;

public class MovementUtil implements Util
{
    private static final Minecraft mc;
    
    public static double[] directionSpeed(final double speed) {
        float forward = MovementUtil.mc.player.movementInput.moveForward;
        float side = MovementUtil.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtil.mc.player.prevRotationYaw + (MovementUtil.mc.player.rotationYaw - MovementUtil.mc.player.prevRotationYaw) * MovementUtil.mc.getRenderPartialTicks();
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
    
    public static double[] dirSpeedNew(final double speed) {
        float moveForward = MovementUtil.mc.player.movementInput.moveForward;
        float moveStrafe = MovementUtil.mc.player.movementInput.moveStrafe;
        float rotationYaw = MovementUtil.mc.player.prevRotationYaw + (MovementUtil.mc.player.rotationYaw - MovementUtil.mc.player.prevRotationYaw) * MovementUtil.mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        final double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[] { posX, posZ };
    }
    
    public static double[] futureCalc1(final double d) {
        final MovementInput movementInput = MovementUtil.mc.player.movementInput;
        double d2 = movementInput.moveForward;
        double d3 = movementInput.moveStrafe;
        float f = MovementUtil.mc.player.rotationYaw;
        double d5;
        double d4;
        if (d2 == 0.0 && d3 == 0.0) {
            d4 = (d5 = 0.0);
        }
        else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += ((d2 > 0.0) ? -45 : 45);
                }
                else if (d3 < 0.0) {
                    f += ((d2 > 0.0) ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                }
                else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            d4 = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
            d5 = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
        }
        return new double[] { d4, d5 };
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
