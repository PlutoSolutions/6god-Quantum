//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import me.alpha432.oyvey.features.modules.Module;

public class HasteHack extends Module
{
    public HasteHack() {
        super("HasteTest", "hh", Category.PLAYER, true, false, true);
    }
    
    @Override
    public void onUpdate() {
        HasteHack.mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE));
    }
}
