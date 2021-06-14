//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.mixin.mixins;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.OpenGlHelper;
import me.alpha432.oyvey.util.ColorUtil;
import net.minecraft.client.Minecraft;
import me.alpha432.oyvey.OyVey;
import org.lwjgl.opengl.GL11;
import me.alpha432.oyvey.features.modules.render.Wireframe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Shadow;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    @Shadow
    private static final Logger LOGGER;
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean renderMarker;
    float red;
    float green;
    float blue;
    
    protected MixinRenderLivingBase(final RenderManager renderManager) {
        super(renderManager);
        this.red = 0.0f;
        this.green = 0.0f;
        this.blue = 0.0f;
    }
    
    @Overwrite
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!MinecraftForge.EVENT_BUS.post((Event)new RenderLivingEvent.Pre((EntityLivingBase)entity, (RenderLivingBase)RenderLivingBase.class.cast(this), partialTicks, x, y, z))) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            final boolean shouldSit = entity.isRiding() && entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
            this.mainModel.isRiding = shouldSit;
            this.mainModel.isChild = entity.isChild();
            try {
                float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                final float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapDegrees(f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                    f3 = f2 - f;
                }
                final float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                this.renderLivingAt(entity, x, y, z);
                final float f6 = this.handleRotationFloat(entity, partialTicks);
                this.applyRotations(entity, f6, f, partialTicks);
                final float f7 = this.prepareScale(entity, partialTicks);
                float f8 = 0.0f;
                float f9 = 0.0f;
                if (!entity.isRiding()) {
                    f8 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f9 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                    if (entity.isChild()) {
                        f9 *= 3.0f;
                    }
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    f3 = f2 - f;
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations((EntityLivingBase)entity, f9, f8, partialTicks);
                this.mainModel.setRotationAngles(f9, f8, f6, f3, f5, f7, (Entity)entity);
                if (this.renderOutlines) {
                    final boolean flag1 = this.setScoreTeamColor(entity);
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(this.getTeamColor((Entity)entity));
                    if (!this.renderMarker) {
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                    }
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
                    if (flag1) {
                        this.unsetScoreTeamColor();
                    }
                }
                else {
                    if (Wireframe.getInstance().isOn() && Wireframe.getInstance().players.getValue() && entity instanceof EntityPlayer && Wireframe.getInstance().mode.getValue().equals(Wireframe.RenderMode.SOLID)) {
                        this.red = Wireframe.getInstance().red.getValue() / 255.0f;
                        this.green = Wireframe.getInstance().green.getValue() / 255.0f;
                        this.blue = Wireframe.getInstance().blue.getValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        if (OyVey.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Wireframe.getInstance().alpha.getValue() / 255.0f);
                        }
                        else {
                            GL11.glColor4f(((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : this.red, ((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : this.green, ((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : this.blue, Wireframe.getInstance().alpha.getValue() / 255.0f);
                        }
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glDisable(2896);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        if (OyVey.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Wireframe.getInstance().alpha.getValue() / 255.0f);
                        }
                        else {
                            GL11.glColor4f(((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : this.red, ((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : this.green, ((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : this.blue, Wireframe.getInstance().alpha.getValue() / 255.0f);
                        }
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                    final boolean flag1 = this.setDoRenderBrightness(entity, partialTicks);
                    if (!(entity instanceof EntityPlayer) || (Wireframe.getInstance().isOn() && Wireframe.getInstance().mode.getValue().equals(Wireframe.RenderMode.WIREFRAME) && Wireframe.getInstance().playerModel.getValue()) || Wireframe.getInstance().isOff()) {
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                    }
                    if (flag1) {
                        this.unsetBrightness();
                    }
                    GlStateManager.depthMask(true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                    if (Wireframe.getInstance().isOn() && Wireframe.getInstance().players.getValue() && entity instanceof EntityPlayer && Wireframe.getInstance().mode.getValue().equals(Wireframe.RenderMode.WIREFRAME)) {
                        this.red = Wireframe.getInstance().red.getValue() / 255.0f;
                        this.green = Wireframe.getInstance().green.getValue() / 255.0f;
                        this.blue = Wireframe.getInstance().blue.getValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        if (OyVey.friendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Wireframe.getInstance().alpha.getValue() / 255.0f);
                        }
                        else {
                            GL11.glColor4f(((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : this.red, ((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : this.green, ((boolean)Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : this.blue, Wireframe.getInstance().alpha.getValue() / 255.0f);
                        }
                        GL11.glLineWidth((float)Wireframe.getInstance().lineWidth.getValue());
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception var20) {
                MixinRenderLivingBase.LOGGER.error("Couldn't render entity", (Throwable)var20);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
            MinecraftForge.EVENT_BUS.post((Event)new RenderLivingEvent.Post((EntityLivingBase)entity, (RenderLivingBase)RenderLivingBase.class.cast(this), partialTicks, x, y, z));
        }
    }
    
    @Shadow
    protected abstract boolean isVisible(final EntityLivingBase p0);
    
    @Shadow
    protected abstract float getSwingProgress(final T p0, final float p1);
    
    @Shadow
    protected abstract float interpolateRotation(final float p0, final float p1, final float p2);
    
    @Shadow
    protected abstract float handleRotationFloat(final T p0, final float p1);
    
    @Shadow
    protected abstract void applyRotations(final T p0, final float p1, final float p2, final float p3);
    
    @Shadow
    public abstract float prepareScale(final T p0, final float p1);
    
    @Shadow
    protected abstract void unsetScoreTeamColor();
    
    @Shadow
    protected abstract boolean setScoreTeamColor(final T p0);
    
    @Shadow
    protected abstract void renderLivingAt(final T p0, final double p1, final double p2, final double p3);
    
    @Shadow
    protected abstract void unsetBrightness();
    
    @Shadow
    protected abstract void renderModel(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    @Shadow
    protected abstract void renderLayers(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    @Shadow
    protected abstract boolean setDoRenderBrightness(final T p0, final float p1);
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
