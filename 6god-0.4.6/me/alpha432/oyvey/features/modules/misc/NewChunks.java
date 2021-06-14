//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import org.lwjgl.opengl.GL11;
import me.alpha432.oyvey.util.RusherHackUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChunkData;
import me.alpha432.oyvey.event.events.PacketEvent;
import java.util.Iterator;
import me.alpha432.oyvey.event.events.Render3DEvent;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import me.alpha432.oyvey.features.modules.Module;

public class NewChunks extends Module
{
    public static ArrayList<Chunk> coords;
    
    public NewChunks() {
        super("NewChunks", "nw", Category.MISC, true, false, false);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        for (final Chunk chunk : NewChunks.coords) {
            final int x = chunk.x * 16;
            final int y = 0;
            final int z = chunk.z * 16;
            chunkESP(x, y, z);
        }
    }
    
    @SubscribeEvent
    public void eventSPacketChunk(final PacketEvent e) {
        if (e.getPacket() instanceof SPacketChunkData && !e.getPacket().isFullChunk()) {
            NewChunks.coords.add(NewChunks.mc.world.getChunk(e.getPacket().getChunkX(), e.getPacket().getChunkZ()));
        }
    }
    
    public static void chunkESP(final double x, final double y, final double z) {
        final double posX = x - RusherHackUtil.getRenderPosX();
        final double posY = y - RusherHackUtil.getRenderPosY();
        final double posZ = z - RusherHackUtil.getRenderPosZ();
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glColor3f(189.0f, 0.0f, 0.0f);
        GL11.glBegin(2);
        GL11.glVertex3d(posX, posY, posZ);
        GL11.glVertex3d(posX + 16.0, posY, posZ);
        GL11.glVertex3d(posX + 16.0, posY, posZ);
        GL11.glVertex3d(posX, posY, posZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(posX, posY, posZ);
        GL11.glVertex3d(posX, posY, posZ + 16.0);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(posX, posY, posZ + 16.0);
        GL11.glVertex3d(posX + 16.0, posY, posZ + 16.0);
        GL11.glVertex3d(posX + 16.0, posY, posZ + 16.0);
        GL11.glVertex3d(posX, posY, posZ + 16.0);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(posX + 16.0, posY, posZ + 16.0);
        GL11.glVertex3d(posX + 16.0, posY, posZ);
        GL11.glVertex3d(posX + 16.0, posY, posZ);
        GL11.glVertex3d(posX + 16.0, posY, posZ + 16.0);
        GL11.glColor3f(189.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        NewChunks.coords = new ArrayList<Chunk>();
    }
}
