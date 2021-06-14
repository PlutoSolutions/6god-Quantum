//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import org.lwjgl.opengl.GL11;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import me.alpha432.oyvey.event.events.Render3DEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import java.util.HashMap;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Map;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Trails extends Module
{
    private final Setting<Float> lineWidth;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Map<Entity, List<Vec3d>> renderMap;
    
    public Trails() {
        super("Trails", "Draws trails on projectiles", Category.RENDER, true, false, false);
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.5f, (T)0.1f, (T)5.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.renderMap = new HashMap<Entity, List<Vec3d>>();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityThrowable) && !(entity instanceof EntityArrow)) {
                continue;
            }
            final List<Vec3d> vectors = (this.renderMap.get(entity) != null) ? this.renderMap.get(entity) : new ArrayList<Vec3d>();
            vectors.add(new Vec3d(entity.posX, entity.posY, entity.posZ));
            this.renderMap.put(entity, vectors);
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (!this.renderMap.containsKey(entity)) {
                continue;
            }
            GlStateManager.pushMatrix();
            RenderUtil.GLPre(this.lineWidth.getValue());
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GL11.glColor4f(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glLineWidth((float)this.lineWidth.getValue());
            GL11.glBegin(1);
            for (int i = 0; i < this.renderMap.get(entity).size() - 1; ++i) {
                GL11.glVertex3d(this.renderMap.get(entity).get(i).x, this.renderMap.get(entity).get(i).y, this.renderMap.get(entity).get(i).z);
                GL11.glVertex3d(this.renderMap.get(entity).get(i + 1).x, this.renderMap.get(entity).get(i + 1).y, this.renderMap.get(entity).get(i + 1).z);
            }
            GL11.glEnd();
            GlStateManager.resetColor();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            RenderUtil.GlPost();
            GlStateManager.popMatrix();
        }
    }
}
