//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.alpha432.oyvey.features.modules.Module;

public class AutoWalk extends Module
{
    public AutoWalk() {
        super("AutoWalk", "Automatically walks in a straight line", Category.MOVEMENT, true, false, false);
    }
    
    @SubscribeEvent
    public void onUpdateInput(final InputUpdateEvent event) {
        event.getMovementInput().moveForward = 1.0f;
    }
}
