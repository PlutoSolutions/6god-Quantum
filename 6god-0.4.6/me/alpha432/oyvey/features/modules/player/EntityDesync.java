//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSetPassengers;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketVehicleMove;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.modules.Module;

public class EntityDesync extends Module
{
    private Entity Riding;
    
    public EntityDesync() {
        super("EntityDesync", "ed", Category.PLAYER, true, false, false);
        this.Riding = null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (EntityDesync.mc.player == null) {
            this.Riding = null;
            this.toggle();
            return;
        }
        if (!EntityDesync.mc.player.isRiding()) {
            Command.sendMessage("You are not riding an entity");
            this.Riding = null;
            this.toggle();
            return;
        }
        this.Riding = EntityDesync.mc.player.getRidingEntity();
        EntityDesync.mc.player.dismountRidingEntity();
        EntityDesync.mc.world.removeEntity(this.Riding);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (this.Riding != null) {
            this.Riding.isDead = false;
            if (!EntityDesync.mc.player.isRiding()) {
                EntityDesync.mc.world.spawnEntity(this.Riding);
                EntityDesync.mc.player.startRiding(this.Riding, true);
            }
            this.Riding = null;
            Command.sendMessage("Forced a remount");
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.Riding == null) {
            return;
        }
        if (EntityDesync.mc.player.isRiding()) {
            return;
        }
        EntityDesync.mc.player.onGround = true;
        this.Riding.setPosition(EntityDesync.mc.player.posX, EntityDesync.mc.player.posY, EntityDesync.mc.player.posZ);
        EntityDesync.mc.player.connection.sendPacket((Packet)new CPacketVehicleMove(this.Riding));
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSetPassengers) {
            if (this.Riding == null) {
                return;
            }
            final SPacketSetPassengers l_Packet = event.getPacket();
            final Entity en = EntityDesync.mc.world.getEntityByID(l_Packet.getEntityId());
            if (en == this.Riding) {
                for (final int i : l_Packet.getPassengerIds()) {
                    final Entity ent = EntityDesync.mc.world.getEntityByID(i);
                    if (ent == EntityDesync.mc.player) {
                        return;
                    }
                }
                Command.sendMessage("You dismounted");
                this.toggle();
            }
        }
        else if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities l_Packet2 = event.getPacket();
            for (final int l_EntityId : l_Packet2.getEntityIDs()) {
                if (l_EntityId == this.Riding.getEntityId()) {
                    Command.sendMessage("Entity is now null SPacketDestroyEntities");
                    return;
                }
            }
        }
    }
}
