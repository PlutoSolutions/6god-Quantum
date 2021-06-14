//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import java.io.IOException;
import java.io.FileWriter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChunkData;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import java.io.File;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class StashLogger extends Module
{
    private final Setting<Integer> chestsToImportantNotify;
    private final Setting<Boolean> chests;
    private final Setting<Boolean> Shulkers;
    private final Setting<Boolean> donkeys;
    private final Setting<Boolean> writeToFile;
    File mainFolder;
    final Iterator<NBTTagCompound> iterator;
    
    public StashLogger() {
        super("StashLogger", "sl", Category.MISC, true, false, false);
        this.chestsToImportantNotify = (Setting<Integer>)this.register(new Setting("Chests", (T)15, (T)1, (T)30));
        this.chests = (Setting<Boolean>)this.register(new Setting("Chests", (T)true));
        this.Shulkers = (Setting<Boolean>)this.register(new Setting("Shulkers", (T)true));
        this.donkeys = (Setting<Boolean>)this.register(new Setting("Donkeys", (T)false));
        this.writeToFile = (Setting<Boolean>)this.register(new Setting("CoordsSaver", (T)true));
        this.mainFolder = new File(Minecraft.getMinecraft().gameDir + File.separator + "Quantum");
        this.iterator = null;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketChunkData) {
            final SPacketChunkData l_Packet = event.getPacket();
            int l_ChestsCount = 0;
            int shulkers = 0;
            for (final NBTTagCompound l_Tag : l_Packet.getTileEntityTags()) {
                final String l_Id = l_Tag.getString("id");
                if (l_Id.equals("minecraft:chest") && this.chests.getValue()) {
                    ++l_ChestsCount;
                }
                else {
                    if (!l_Id.equals("minecraft:shulker_box") || !this.Shulkers.getValue()) {
                        continue;
                    }
                    ++shulkers;
                }
            }
            if (l_ChestsCount >= this.chestsToImportantNotify.getValue()) {
                this.SendMessage(String.format("%s chests located at X: %s, Z: %s", l_ChestsCount, l_Packet.getChunkX() * 16, l_Packet.getChunkZ() * 16), true);
            }
            if (shulkers > 0) {
                this.SendMessage(String.format("%s shulker boxes at X: %s, Z: %s", shulkers, l_Packet.getChunkX() * 16, l_Packet.getChunkZ() * 16), true);
            }
        }
    }
    
    private void SendMessage(final String message, final boolean save) {
        final String server = Minecraft.getMinecraft().isSingleplayer() ? "singleplayer".toUpperCase() : StashLogger.mc.getCurrentServerData().serverIP;
        if (this.writeToFile.getValue() && save) {
            try {
                final FileWriter writer = new FileWriter(this.mainFolder + "/stashes.txt", true);
                writer.write("[" + server + "]: " + message + "\n");
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        StashLogger.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
        StashLogger.mc.player.sendMessage((ITextComponent)new TextComponentString(ChatFormatting.RED + "[StashLogger] " + ChatFormatting.GREEN + message));
    }
}
