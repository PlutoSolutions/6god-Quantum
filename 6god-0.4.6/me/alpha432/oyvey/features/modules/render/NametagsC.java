//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.OyVey;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.DamageUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Items;
import net.minecraft.item.ItemTool;
import java.awt.Color;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.RotationUtil;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class NametagsC extends Module
{
    public static NametagsC INSTANCE;
    private final Setting<Boolean> health;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> reverseArmor;
    private final Setting<Boolean> topEnchant;
    private final Setting<Boolean> altEnchantNames;
    private final Setting<Float> scaling;
    private final Setting<Boolean> invisibles;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> totemPops;
    private final Setting<Boolean> rect;
    private final Setting<Boolean> outline;
    private final Setting<Boolean> colorSync;
    private final Setting<Integer> redSetting;
    private final Setting<Integer> greenSetting;
    private final Setting<Integer> blueSetting;
    private final Setting<Integer> alphaSetting;
    private final Setting<Float> lineWidth;
    private final Setting<Boolean> sneak;
    private final Setting<Boolean> heldStackName;
    private final Setting<Boolean> whiter;
    private final Setting<Boolean> onlyFov;
    private final Setting<Boolean> scaleing;
    private final Setting<Float> factor;
    private final Setting<Boolean> smartScale;
    
    public NametagsC() {
        super("Nametags", "ntc", Category.RENDER, true, false, true);
        this.health = (Setting<Boolean>)this.register(new Setting("Health", (T)true));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", (T)true));
        this.reverseArmor = (Setting<Boolean>)this.register(new Setting("ReverseArmor", (T)false));
        this.topEnchant = (Setting<Boolean>)this.register(new Setting("TopEnchant", (T)false));
        this.altEnchantNames = (Setting<Boolean>)this.register(new Setting("AltEnchantNames", (T)false));
        this.scaling = (Setting<Float>)this.register(new Setting("Size", (T)0.3f, (T)0.1f, (T)20.0f));
        this.invisibles = (Setting<Boolean>)this.register(new Setting("Invisibles", (T)false));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", (T)true));
        this.totemPops = (Setting<Boolean>)this.register(new Setting("TotemPops", (T)true));
        this.rect = (Setting<Boolean>)this.register(new Setting("Rectangle", (T)true));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)Boolean.FALSE, v -> this.rect.getValue()));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", (T)Boolean.FALSE, v -> this.outline.getValue()));
        this.redSetting = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.greenSetting = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.blueSetting = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.alphaSetting = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.5f, (T)0.1f, (T)5.0f, v -> this.outline.getValue()));
        this.sneak = (Setting<Boolean>)this.register(new Setting("SneakColor", (T)false));
        this.heldStackName = (Setting<Boolean>)this.register(new Setting("StackName", (T)false));
        this.whiter = (Setting<Boolean>)this.register(new Setting("White", (T)false));
        this.onlyFov = (Setting<Boolean>)this.register(new Setting("OnlyFov", (T)false));
        this.scaleing = (Setting<Boolean>)this.register(new Setting("Scale", (T)false));
        this.factor = (Setting<Float>)this.register(new Setting("Factor", (T)0.3f, (T)0.1f, (T)1.0f, v -> this.scaleing.getValue()));
        this.smartScale = (Setting<Boolean>)this.register(new Setting("SmartScale", (T)Boolean.FALSE, v -> this.scaleing.getValue()));
        NametagsC.INSTANCE = this;
    }
    
    public static NametagsC getInstance() {
        if (NametagsC.INSTANCE == null) {
            NametagsC.INSTANCE = new NametagsC();
        }
        return NametagsC.INSTANCE;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (!Feature.fullNullCheck()) {
            for (final EntityPlayer player : NametagsC.mc.world.playerEntities) {
                if (player != null && !player.equals((Object)NametagsC.mc.player) && player.isEntityAlive() && (!player.isInvisible() || this.invisibles.getValue())) {
                    if (this.onlyFov.getValue() && !RotationUtil.isInFov((Entity)player)) {
                        continue;
                    }
                    final double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - NametagsC.mc.getRenderManager().renderPosX;
                    final double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - NametagsC.mc.getRenderManager().renderPosY;
                    final double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - NametagsC.mc.getRenderManager().renderPosZ;
                    this.renderProperNameTag(player, x, y, z, event.getPartialTicks());
                }
            }
        }
    }
    
    private void renderProperNameTag(final EntityPlayer player, final double x, final double y, final double z, final float delta) {
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = NametagsC.mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        final String displayTag = this.getDisplayTag(player);
        final double distance = camera.getDistance(x + NametagsC.mc.getRenderManager().viewerPosX, y + NametagsC.mc.getRenderManager().viewerPosY, z + NametagsC.mc.getRenderManager().viewerPosZ);
        final int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + this.scaling.getValue() * (distance * this.factor.getValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue()) {
            scale = this.scaling.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.4f, (float)z);
        GlStateManager.rotate(-NametagsC.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(NametagsC.mc.getRenderManager().playerViewX, (NametagsC.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        if (this.rect.getValue()) {
            this.drawRect((float)(-width - 2), (float)(-(NametagsC.mc.fontRenderer.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, 1426063360);
            if (this.outline.getValue()) {
                final int color = this.colorSync.getValue() ? ClickGui.getInstance().getColor() : new Color(this.redSetting.getValue(), this.greenSetting.getValue(), this.blueSetting.getValue(), this.alphaSetting.getValue()).getRGB();
                this.drawOutlineRect((float)(-width - 2), (float)(-(NametagsC.mc.fontRenderer.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, color);
            }
        }
        GlStateManager.enableAlpha();
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && !(renderMainHand.getItem() instanceof ItemTool)) {
            renderMainHand.getItem();
        }
        if (this.heldStackName.getValue() && !renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            final String stackName = renderMainHand.getDisplayName();
            final int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.0f);
            this.renderer.drawStringWithShadow(stackName, (float)(-stackNameWidth), -(this.getBiggestArmorTag(player) + 20.0f), -1);
            GL11.glScalef(1.5f, 1.5f, 1.0f);
            GL11.glPopMatrix();
        }
        final ArrayList armorInventory = new ArrayList((Collection<? extends E>)player.inventory.armorInventory);
        if (this.reverseArmor.getValue()) {
            Collections.reverse(armorInventory);
        }
        GlStateManager.pushMatrix();
        int xOffset = -8;
        for (final ItemStack stack : player.inventory.armorInventory) {
            if (stack == null) {
                continue;
            }
            xOffset -= 8;
        }
        xOffset -= 8;
        final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
        if (renderOffhand.hasEffect() && !(renderOffhand.getItem() instanceof ItemTool)) {
            renderOffhand.getItem();
        }
        this.renderItemStack(player, renderOffhand, xOffset, -26, this.armor.getValue());
        xOffset += 16;
        for (final ItemStack stack2 : player.inventory.armorInventory) {
            if (stack2 == null) {
                continue;
            }
            final ItemStack armourStack = stack2.copy();
            if (armourStack.hasEffect() && !(armourStack.getItem() instanceof ItemTool)) {
                armourStack.getItem();
            }
            this.renderItemStack(player, armourStack, xOffset, -26, this.armor.getValue());
            xOffset += 16;
        }
        this.renderItemStack(player, renderMainHand, xOffset, -26, this.armor.getValue());
        GlStateManager.popMatrix();
        this.renderer.drawStringWithShadow(displayTag, (float)(-width), (float)(-(this.renderer.getFontHeight() - 1)), this.getDisplayColour(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z, final float partialTicks) {
        final double tempY = y + (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = NametagsC.mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, partialTicks);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, partialTicks);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, partialTicks);
        final double distance = camera.getDistance(x + NametagsC.mc.getRenderManager().viewerPosX, y + NametagsC.mc.getRenderManager().viewerPosY, z + NametagsC.mc.getRenderManager().viewerPosZ);
        final int width = NametagsC.mc.fontRenderer.getStringWidth(this.getDisplayTag(player)) / 2;
        final double scale = (0.0018 + this.scaling.getValue() * distance) / 50.0;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.4f, (float)z);
        GlStateManager.rotate(-NametagsC.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        final float thirdPersonOffset = (NametagsC.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        GlStateManager.rotate(NametagsC.mc.getRenderManager().playerViewX, thirdPersonOffset, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        this.drawRect((float)(-width - 2), (float)(-(NametagsC.mc.fontRenderer.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, 1426063360);
        GlStateManager.enableAlpha();
        NametagsC.mc.fontRenderer.drawStringWithShadow(this.getDisplayTag(player), (float)(-width), (float)(-(NametagsC.mc.fontRenderer.FONT_HEIGHT - 1)), this.getNameColor((Entity)player).getRGB());
        if (this.armor.getValue()) {
            GlStateManager.pushMatrix();
            final double changeValue = 16.0;
            int xOffset = 0;
            xOffset -= (int)(changeValue / 2.0 * player.inventory.armorInventory.size());
            xOffset -= (int)(changeValue / 2.0);
            xOffset -= (int)(changeValue / 2.0);
            player.getHeldItemMainhand();
            xOffset += (int)changeValue;
            for (final ItemStack stack : player.inventory.armorInventory) {
                xOffset += (int)changeValue;
            }
            player.getHeldItemOffhand();
            GlStateManager.popMatrix();
        }
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
    
    public void drawRect(final float x, final float y, final float w, final float h, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.lineWidth.getValue());
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public void drawOutlineRect(final float x, final float y, final float w, final float h, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.lineWidth.getValue());
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    private Color getNameColor(final Entity entity) {
        return Color.WHITE;
    }
    
    private void renderItemStack(final EntityPlayer player, final ItemStack stack, final int x, final int y, final boolean item) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        NametagsC.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        if (item) {
            NametagsC.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            NametagsC.mc.getRenderItem().renderItemOverlays(NametagsC.mc.fontRenderer, stack, x, y);
        }
        NametagsC.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(player, stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private boolean shouldMoveArmor(final EntityPlayer player) {
        for (final ItemStack stack : player.inventory.armorInventory) {
            final NBTTagList enchants = stack.getEnchantmentTagList();
            if (enchants.tagCount() == 0) {
                continue;
            }
            return true;
        }
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            return true;
        }
        final ItemStack renderOffHand = player.getHeldItemOffhand().copy();
        return renderMainHand.hasEffect();
    }
    
    private void renderEnchantmentText(final EntityPlayer player, final ItemStack stack, final int x, final int y) {
        int enchantmentY = (int)(y - 8 - (this.topEnchant.getValue() ? (this.getBiggestArmorTag(player) - this.getEnchantHeight(stack)) : 0.0f));
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", (float)(x * 2), (float)enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            String encName = null;
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                final String string = enc.isCurse() ? (this.altEnchantNames.getValue() ? enc.getTranslatedName((int)level).substring(11).substring(0, this.altEnchantNames.getValue() ? 3 : 1) : (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, this.altEnchantNames.getValue() ? ((enc.getMaxLevel() == 1) ? 3 : 2) : 1))) : (encName = enc.getTranslatedName((int)level).substring(0, this.altEnchantNames.getValue() ? ((enc.getMaxLevel() == 1) ? 3 : 2) : 1));
                if (enc.getMaxLevel() != 1) {
                    encName += level;
                }
                if (!this.altEnchantNames.getValue()) {
                    encName = ((encName != null) ? encName.toLowerCase() : null);
                }
                enchantmentY -= 8;
            }
        }
        if (DamageUtil.hasDurability(stack)) {
            final int percent = DamageUtil.getRoundedDamage(stack);
            final String color = (percent >= 60) ? "§a" : ((percent >= 25) ? "§e" : "§c");
            this.renderer.drawStringWithShadow(color + percent + "%", (float)(x * 2), (float)enchantmentY, -1);
        }
    }
    
    private float getEnchantHeight(final ItemStack stack) {
        float enchantHeight = 0.0f;
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                enchantHeight += 8.0f;
            }
        }
        return enchantHeight;
    }
    
    private float getBiggestArmorTag(final EntityPlayer player) {
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (final ItemStack stack : player.inventory.armorInventory) {
            float encY = 0.0f;
            if (stack != null) {
                final NBTTagList enchants = stack.getEnchantmentTagList();
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    final short id = enchants.getCompoundTagAt(index).getShort("id");
                    final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc != null) {
                        encY += 8.0f;
                        arm = true;
                    }
                }
            }
            if (encY <= enchantmentY) {
                continue;
            }
            enchantmentY = encY;
        }
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants2.tagCount(); ++index2) {
                final short id = enchants2.getCompoundTagAt(index2).getShort("id");
                final Enchantment enc2 = Enchantment.getEnchantmentByID((int)id);
                if (enc2 != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        final ItemStack renderOffHand;
        if ((renderOffHand = player.getHeldItemOffhand().copy()).hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderOffHand.getEnchantmentTagList();
            for (int index = 0; index < enchants2.tagCount(); ++index) {
                final short id2 = enchants2.getCompoundTagAt(index).getShort("id");
                final Enchantment enc = Enchantment.getEnchantmentByID((int)id2);
                if (enc != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        return (arm ? 0 : 20) + enchantmentY;
    }
    
    private String getDisplayTag(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(NametagsC.mc.getSession().getUsername())) {
            name = "You";
        }
        if (!this.health.getValue()) {
            return name;
        }
        final float health = EntityUtil.getHealth((Entity)player);
        final String color = (health > 18.0f) ? "§a" : ((health > 16.0f) ? "§2" : ((health > 12.0f) ? "§e" : ((health > 8.0f) ? "§6" : ((health > 5.0f) ? "§c" : "§4"))));
        String pingStr = "";
        if (this.ping.getValue()) {
            try {
                final int responseTime = Objects.requireNonNull(NametagsC.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr = pingStr + responseTime + "ms ";
            }
            catch (Exception ex) {}
        }
        String popStr = " ";
        if (this.totemPops.getValue()) {
            popStr += OyVey.totemPopManager.getTotemPopString(player);
        }
        final String idString = "";
        final String gameModeStr = "";
        name = ((Math.floor(health) == health) ? (name + color + " " + ((health > 0.0f) ? Integer.valueOf((int)Math.floor(health)) : "dead")) : (name + color + " " + ((health > 0.0f) ? Integer.valueOf((int)health) : "dead")));
        return pingStr + idString + gameModeStr + name + popStr;
    }
    
    private int getDisplayColour(final EntityPlayer player) {
        int colour = -5592406;
        if (this.whiter.getValue()) {
            colour = -1;
        }
        if (OyVey.friendManager.isFriend(player)) {
            return -11157267;
        }
        if (player.isInvisible()) {
            colour = -1113785;
        }
        else if (player.isSneaking() && this.sneak.getValue()) {
            colour = -6481515;
        }
        return colour;
    }
    
    private double interpolate(final double previous, final double current, final float partialTicks) {
        return previous + (current - previous) * partialTicks;
    }
}
