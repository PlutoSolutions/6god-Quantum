//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import java.lang.reflect.Field;

public class RusherHackUtil implements Util
{
    public static Field renderPosX;
    public static Field renderPosY;
    public static Field renderPosZ;
    
    public static double getRenderPosX() {
        try {
            return (double)RusherHackUtil.renderPosX.get(Wrapper.mc.getRenderManager());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
    
    public static double getRenderPosY() {
        try {
            return (double)RusherHackUtil.renderPosY.get(Wrapper.mc.getRenderManager());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
    
    public static double getRenderPosZ() {
        try {
            return (double)RusherHackUtil.renderPosZ.get(Wrapper.mc.getRenderManager());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
