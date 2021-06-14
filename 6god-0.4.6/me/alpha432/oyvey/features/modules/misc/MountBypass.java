//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.BlockWeb;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.BlockChest;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.world.World;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.combat.AutoWeb;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class MountBypass extends Module
{
    public Setting<Type> type;
    private int lastHotbarSlot;
    
    public MountBypass() {
        super("MountBypass", "mb", Category.MISC, true, false, false);
        this.type = (Setting<Type>)this.register(new Setting("Type", (T)Type.Old));
    }
    
    @Override
    public void onEnable() {
        if (this.type.getValue() == Type.New) {
            if (fullNullCheck()) {
                return;
            }
            this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.type.getValue() == Type.Old && event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = event.getPacket();
            if (packet.getEntityFromWorld((World)MountBypass.mc.world) instanceof AbstractChestHorse && packet.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                event.setCanceled(true);
                Command.sendMessage("<" + this.getDisplayName() + "> attempted a mountbypass");
            }
        }
        if (this.type.getValue() == Type.New && event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = event.getPacket();
            final int chestSlot = InventoryUtil.findHotbarBlock(BlockChest.class);
            if (chestSlot == -1) {
                Command.sendMessage("<" + this.getDisplayName() + "> you are out of chests");
                this.disable();
                return;
            }
            if (MountBypass.mc.player.inventory.currentItem != this.lastHotbarSlot && MountBypass.mc.player.inventory.currentItem != chestSlot) {
                this.lastHotbarSlot = MountBypass.mc.player.inventory.currentItem;
            }
            final int originalSlot = AutoWeb.mc.player.inventory.currentItem;
            final int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
            if (webSlot == -1) {
                this.toggle();
            }
            MountBypass.mc.player.inventory.currentItem = ((webSlot == -1) ? webSlot : webSlot);
            MountBypass.mc.playerController.updateController();
            MountBypass.mc.player.inventory.currentItem = originalSlot;
            MountBypass.mc.playerController.updateController();
            Command.sendMessage("<" + this.getDisplayName() + "> attempted a mountbypass");
        }
    }
    
    public enum Type
    {
        Old, 
        New;
    }
}
