//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraft.item.ItemBlock;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class FastPlace extends Module
{
    private final Setting<Boolean> noBlock;
    private final Setting<Integer> delay;
    
    public FastPlace() {
        super("FastPlace", "fp", Category.PLAYER, true, false, false);
        this.noBlock = (Setting<Boolean>)this.register(new Setting("NoBlocks", (T)true));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)10));
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (FastPlace.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && this.noBlock.getValue()) {
            return;
        }
        FastPlace.mc.rightClickDelayTimer = this.delay.getValue();
    }
}
