//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.event.events.RenderEntityModelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import me.alpha432.oyvey.event.events.PacketEvent;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Map;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class CrystalScale extends Module
{
    public static CrystalScale INSTANCE;
    public Setting<Boolean> animateScale;
    public Setting<Boolean> chams;
    public Setting<Boolean> throughWalls;
    public Setting<Boolean> wireframeThroughWalls;
    public Setting<Boolean> glint;
    public Setting<Boolean> wireframe;
    public Setting<Float> scale;
    public Setting<Float> lineWidth;
    public Setting<Boolean> rainbow;
    public Setting<Integer> saturation;
    public Setting<Integer> brightness;
    public Setting<Integer> speed;
    public Setting<Boolean> xqz;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public Setting<Integer> hiddenRed;
    public Setting<Integer> hiddenGreen;
    public Setting<Integer> hiddenBlue;
    public Setting<Integer> hiddenAlpha;
    public Map<EntityEnderCrystal, Float> scaleMap;
    
    public CrystalScale() {
        super("CrystalMod", "Modifies crystal rendering in different ways", Category.RENDER, true, false, false);
        this.animateScale = (Setting<Boolean>)this.register(new Setting("AnimateScale", (T)false));
        this.chams = (Setting<Boolean>)this.register(new Setting("Chams", (T)false));
        this.throughWalls = (Setting<Boolean>)this.register(new Setting("ThroughWalls", (T)true));
        this.wireframeThroughWalls = (Setting<Boolean>)this.register(new Setting("WireThroughWalls", (T)true));
        this.glint = (Setting<Boolean>)this.register(new Setting("Glint", (T)false, v -> this.chams.getValue()));
        this.wireframe = (Setting<Boolean>)this.register(new Setting("Wireframe", (T)false));
        this.scale = (Setting<Float>)this.register(new Setting("Scale", (T)1.0f, (T)0.1f, (T)10.0f));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)3.0f));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false));
        this.saturation = (Setting<Integer>)this.register(new Setting("Saturation", (T)50, (T)0, (T)100, v -> this.rainbow.getValue()));
        this.brightness = (Setting<Integer>)this.register(new Setting("Brightness", (T)100, (T)0, (T)100, v -> this.rainbow.getValue()));
        this.speed = (Setting<Integer>)this.register(new Setting("Speed", (T)40, (T)1, (T)100, v -> this.rainbow.getValue()));
        this.xqz = (Setting<Boolean>)this.register(new Setting("XQZ", (T)false, v -> !this.rainbow.getValue() && this.throughWalls.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> !this.rainbow.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.hiddenRed = (Setting<Integer>)this.register(new Setting("Hidden Red", (T)255, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenGreen = (Setting<Integer>)this.register(new Setting("Hidden Green", (T)0, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenBlue = (Setting<Integer>)this.register(new Setting("Hidden Blue", (T)255, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.hiddenAlpha = (Setting<Integer>)this.register(new Setting("Hidden Alpha", (T)255, (T)0, (T)255, v -> this.xqz.getValue() && !this.rainbow.getValue()));
        this.scaleMap = new ConcurrentHashMap<EntityEnderCrystal, Float>();
        CrystalScale.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        for (final Entity crystal : CrystalScale.mc.world.loadedEntityList) {
            if (!(crystal instanceof EntityEnderCrystal)) {
                continue;
            }
            if (!this.scaleMap.containsKey(crystal)) {
                this.scaleMap.put((EntityEnderCrystal)crystal, 3.125E-4f);
            }
            else {
                this.scaleMap.put((EntityEnderCrystal)crystal, this.scaleMap.get(crystal) + 3.125E-4f);
            }
            if (this.scaleMap.get(crystal) < 0.0625f * this.scale.getValue()) {
                continue;
            }
            this.scaleMap.remove(crystal);
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities packet = event.getPacket();
            for (final int id : packet.getEntityIDs()) {
                final Entity entity = CrystalScale.mc.world.getEntityByID(id);
                if (entity instanceof EntityEnderCrystal) {
                    this.scaleMap.remove(entity);
                }
            }
        }
    }
    
    public void onRenderModel(final RenderEntityModelEvent event) {
        if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal) || !this.wireframe.getValue()) {
            return;
        }
        final Color color = this.rainbow.getValue() ? ColorUtil.rainbow(this.saturation.getValue()) : EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), false);
        final boolean fancyGraphics = CrystalScale.mc.gameSettings.fancyGraphics;
        CrystalScale.mc.gameSettings.fancyGraphics = false;
        final float gamma = CrystalScale.mc.gameSettings.gammaSetting;
        CrystalScale.mc.gameSettings.gammaSetting = 10000.0f;
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glPolygonMode(1032, 6913);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        if (this.wireframeThroughWalls.getValue()) {
            GL11.glDisable(2929);
        }
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GlStateManager.glLineWidth((float)this.lineWidth.getValue());
        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
