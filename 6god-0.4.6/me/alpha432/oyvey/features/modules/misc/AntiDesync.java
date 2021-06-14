//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.Wrapper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AntiDesync extends Module
{
    private final Setting<Boolean> crystal;
    private final Setting<Boolean> sneakp;
    
    public AntiDesync() {
        super("AntiDesync", "ad", Category.MISC, true, false, false);
        this.crystal = (Setting<Boolean>)this.register(new Setting("Crystal", (T)true));
        this.sneakp = (Setting<Boolean>)this.register(new Setting("SneakPacket", (T)false));
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent event) {
        if (this.crystal.getValue() && event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                try {
                    for (final Entity e : Wrapper.getWorld().loadedEntityList) {
                        if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                            e.setDead();
                        }
                    }
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @Subscribe
    @Override
    public void onUpdate() {
        if (this.sneakp.getValue()) {
            AntiDesync.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AntiDesync.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            AntiDesync.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AntiDesync.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
