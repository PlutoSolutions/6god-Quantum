//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.mixin.mixins.accessors.ICPacketPlayer;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;

public class FeetXP extends Module
{
    public FeetXP() {
        super("FeetXP", "fxp", Category.COMBAT, true, false, false);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer && FeetXP.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            final CPacketPlayer packet = event.getPacket();
            ((ICPacketPlayer)packet).setPitch(90.0f);
        }
    }
}
