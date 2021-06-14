//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import java.util.Objects;
import net.minecraft.potion.Potion;
import me.alpha432.oyvey.features.modules.Module;

public class AntiLevitate extends Module
{
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation", Category.MOVEMENT, false, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
            AntiLevitate.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
        }
    }
}
