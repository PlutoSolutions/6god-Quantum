//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.item.Item;
import me.alpha432.oyvey.util.ItemUtil;
import net.minecraft.init.Items;
import me.alpha432.oyvey.features.modules.Module;

public class AutoFish extends Module
{
    private int rodSlot;
    
    public AutoFish() {
        super("AutoFish", "af", Category.MISC, true, false, false);
        this.rodSlot = -1;
    }
    
    @Override
    public void onEnable() {
        if (this.isNull()) {
            this.setEnabled(false);
            return;
        }
        this.rodSlot = ItemUtil.getItemFromHotbar((Item)Items.FISHING_ROD);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = event.getPacket();
            if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                final int startSlot = AutoFish.mc.player.inventory.currentItem;
                AutoFish.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(this.rodSlot));
                AutoFish.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoFish.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (startSlot != -1) {
                    AutoFish.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(startSlot));
                }
            }
        }
    }
}
