//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.nbt.NBTTagList;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.DamageUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.enchantment.Enchantment;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.ColorHolder;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class NameTags extends Module
{
    private final Setting<Boolean> rect;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> reversed;
    private final Setting<Boolean> health;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> gamemode;
    private final Setting<Boolean> entityID;
    private final Setting<Boolean> heldStackName;
    private final Setting<Boolean> max;
    private final Setting<Boolean> maxText;
    private final Setting<Integer> Mred;
    private final Setting<Integer> Mgreen;
    private final Setting<Integer> Mblue;
    private final Setting<Float> size;
    private final Setting<Boolean> scaleing;
    private final Setting<Boolean> smartScale;
    private final Setting<Float> factor;
    private final Setting<Boolean> textcolor;
    private final Setting<Boolean> NCRainbow;
    private final Setting<Integer> NCred;
    private final Setting<Integer> NCgreen;
    private final Setting<Integer> NCblue;
    private final Setting<Boolean> outline;
    private final Setting<Boolean> ORainbow;
    private final Setting<Float> Owidth;
    private final Setting<Integer> Ored;
    private final Setting<Integer> Ogreen;
    private final Setting<Integer> Oblue;
    private final Setting<Boolean> friendcolor;
    private final Setting<Boolean> FCRainbow;
    private final Setting<Integer> FCred;
    private final Setting<Integer> FCgreen;
    private final Setting<Integer> FCblue;
    private final Setting<Boolean> FORainbow;
    private final Setting<Integer> FOred;
    private final Setting<Integer> FOgreen;
    private final Setting<Integer> FOblue;
    private final Setting<Boolean> sneakcolor;
    private final Setting<Boolean> sneak;
    private final Setting<Boolean> SCRainbow;
    private final Setting<Integer> SCred;
    private final Setting<Integer> SCgreen;
    private final Setting<Integer> SCblue;
    private final Setting<Boolean> SORainbow;
    private final Setting<Integer> SOred;
    private final Setting<Integer> SOgreen;
    private final Setting<Integer> SOblue;
    private final Setting<Boolean> invisiblescolor;
    private final Setting<Boolean> invisibles;
    private final Setting<Boolean> ICRainbow;
    private final Setting<Integer> ICred;
    private final Setting<Integer> ICgreen;
    private final Setting<Integer> ICblue;
    private final Setting<Boolean> IORainbow;
    private final Setting<Integer> IOred;
    private final Setting<Integer> IOgreen;
    private final Setting<Integer> IOblue;
    private static NameTags INSTANCE;
    
    public NameTags() {
        super("NameTags", "Renders info about the player on a NameTag", Category.RENDER, false, false, false);
        this.rect = (Setting<Boolean>)this.register(new Setting("Rectangle", (T)true));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", (T)true));
        this.reversed = (Setting<Boolean>)this.register(new Setting("ArmorReversed", (T)false, v -> this.armor.getValue()));
        this.health = (Setting<Boolean>)this.register(new Setting("Health", (T)true));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", (T)true));
        this.gamemode = (Setting<Boolean>)this.register(new Setting("Gamemode", (T)false));
        this.entityID = (Setting<Boolean>)this.register(new Setting("EntityID", (T)false));
        this.heldStackName = (Setting<Boolean>)this.register(new Setting("StackName", (T)true));
        this.max = (Setting<Boolean>)this.register(new Setting("Max", (T)true));
        this.maxText = (Setting<Boolean>)this.register(new Setting("NoMaxText", (T)false, v -> this.max.getValue()));
        this.Mred = (Setting<Integer>)this.register(new Setting("Max-Red", (T)178, (T)0, (T)255, v -> this.max.getValue()));
        this.Mgreen = (Setting<Integer>)this.register(new Setting("Max-Green", (T)52, (T)0, (T)255, v -> this.max.getValue()));
        this.Mblue = (Setting<Integer>)this.register(new Setting("Max-Blue", (T)57, (T)0, (T)255, v -> this.max.getValue()));
        this.size = (Setting<Float>)this.register(new Setting("Size", (T)0.3f, (T)0.1f, (T)20.0f));
        this.scaleing = (Setting<Boolean>)this.register(new Setting("Scale", (T)false));
        this.smartScale = (Setting<Boolean>)this.register(new Setting("SmartScale", (T)false, v -> this.scaleing.getValue()));
        this.factor = (Setting<Float>)this.register(new Setting("Factor", (T)0.3f, (T)0.1f, (T)1.0f, v -> this.scaleing.getValue()));
        this.textcolor = (Setting<Boolean>)this.register(new Setting("TextColor", (T)true));
        this.NCRainbow = (Setting<Boolean>)this.register(new Setting("Text-Rainbow", (T)false, v -> this.textcolor.getValue()));
        this.NCred = (Setting<Integer>)this.register(new Setting("Text-Red", (T)255, (T)0, (T)255, v -> this.textcolor.getValue()));
        this.NCgreen = (Setting<Integer>)this.register(new Setting("Text-Green", (T)255, (T)0, (T)255, v -> this.textcolor.getValue()));
        this.NCblue = (Setting<Integer>)this.register(new Setting("Text-Blue", (T)255, (T)0, (T)255, v -> this.textcolor.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.ORainbow = (Setting<Boolean>)this.register(new Setting("Outline-Rainbow", (T)false, v -> this.outline.getValue()));
        this.Owidth = (Setting<Float>)this.register(new Setting("Outline-Width", (T)1.3f, (T)0.0f, (T)5.0f, v -> this.outline.getValue()));
        this.Ored = (Setting<Integer>)this.register(new Setting("Outline-Red", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.Ogreen = (Setting<Integer>)this.register(new Setting("Outline-Green", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.Oblue = (Setting<Integer>)this.register(new Setting("Outline-Blue", (T)255, (T)0, (T)255, v -> this.outline.getValue()));
        this.friendcolor = (Setting<Boolean>)this.register(new Setting("FriendColor", (T)true));
        this.FCRainbow = (Setting<Boolean>)this.register(new Setting("Friend-Rainbow", (T)false, v -> this.friendcolor.getValue()));
        this.FCred = (Setting<Integer>)this.register(new Setting("Friend-Red", (T)0, (T)0, (T)255, v -> this.friendcolor.getValue()));
        this.FCgreen = (Setting<Integer>)this.register(new Setting("Friend-Green", (T)213, (T)0, (T)255, v -> this.friendcolor.getValue()));
        this.FCblue = (Setting<Integer>)this.register(new Setting("Friend-Blue", (T)255, (T)0, (T)255, v -> this.friendcolor.getValue()));
        this.FORainbow = (Setting<Boolean>)this.register(new Setting("FriendOutline-Rainbow", (T)false, v -> this.outline.getValue() && this.friendcolor.getValue()));
        this.FOred = (Setting<Integer>)this.register(new Setting("FriendOutline-Red", (T)0, (T)0, (T)255, v -> this.outline.getValue() && this.friendcolor.getValue()));
        this.FOgreen = (Setting<Integer>)this.register(new Setting("FriendOutline-Green", (T)213, (T)0, (T)255, v -> this.outline.getValue() && this.friendcolor.getValue()));
        this.FOblue = (Setting<Integer>)this.register(new Setting("FriendOutline-Blue", (T)255, (T)0, (T)255, v -> this.outline.getValue() && this.friendcolor.getValue()));
        this.sneakcolor = (Setting<Boolean>)this.register(new Setting("Sneak", (T)false));
        this.sneak = (Setting<Boolean>)this.register(new Setting("EnableSneak", (T)true, v -> this.sneakcolor.getValue()));
        this.SCRainbow = (Setting<Boolean>)this.register(new Setting("Sneak-Rainbow", (T)false, v -> this.sneakcolor.getValue()));
        this.SCred = (Setting<Integer>)this.register(new Setting("Sneak-Red", (T)245, (T)0, (T)255, v -> this.sneakcolor.getValue()));
        this.SCgreen = (Setting<Integer>)this.register(new Setting("Sneak-Green", (T)0, (T)0, (T)255, v -> this.sneakcolor.getValue()));
        this.SCblue = (Setting<Integer>)this.register(new Setting("Sneak-Blue", (T)122, (T)0, (T)255, v -> this.sneakcolor.getValue()));
        this.SORainbow = (Setting<Boolean>)this.register(new Setting("SneakOutline-Rainbow", (T)false, v -> this.outline.getValue() && this.sneakcolor.getValue()));
        this.SOred = (Setting<Integer>)this.register(new Setting("SneakOutline-Red", (T)245, (T)0, (T)255, v -> this.outline.getValue() && this.sneakcolor.getValue()));
        this.SOgreen = (Setting<Integer>)this.register(new Setting("SneakOutline-Green", (T)0, (T)0, (T)255, v -> this.outline.getValue() && this.sneakcolor.getValue()));
        this.SOblue = (Setting<Integer>)this.register(new Setting("SneakOutline-Blue", (T)122, (T)0, (T)255, v -> this.outline.getValue() && this.sneakcolor.getValue()));
        this.invisiblescolor = (Setting<Boolean>)this.register(new Setting("InvisiblesColor", (T)false));
        this.invisibles = (Setting<Boolean>)this.register(new Setting("EnableInvisibles", (T)true, v -> this.invisiblescolor.getValue()));
        this.ICRainbow = (Setting<Boolean>)this.register(new Setting("Invisible-Rainbow", (T)false, v -> this.invisiblescolor.getValue()));
        this.ICred = (Setting<Integer>)this.register(new Setting("Invisible-Red", (T)148, (T)0, (T)255, v -> this.invisiblescolor.getValue()));
        this.ICgreen = (Setting<Integer>)this.register(new Setting("Invisible-Green", (T)148, (T)0, (T)255, v -> this.invisiblescolor.getValue()));
        this.ICblue = (Setting<Integer>)this.register(new Setting("Invisible-Blue", (T)148, (T)0, (T)255, v -> this.invisiblescolor.getValue()));
        this.IORainbow = (Setting<Boolean>)this.register(new Setting("InvisibleOutline-Rainbow", (T)false, v -> this.outline.getValue() && this.invisiblescolor.getValue()));
        this.IOred = (Setting<Integer>)this.register(new Setting("InvisibleOutline-Red", (T)148, (T)0, (T)255, v -> this.outline.getValue() && this.invisiblescolor.getValue()));
        this.IOgreen = (Setting<Integer>)this.register(new Setting("InvisibleOutline-Green", (T)148, (T)0, (T)255, v -> this.outline.getValue() && this.invisiblescolor.getValue()));
        this.IOblue = (Setting<Integer>)this.register(new Setting("InvisibleOutline-Blue", (T)148, (T)0, (T)255, v -> this.outline.getValue() && this.invisiblescolor.getValue()));
    }
    
    public static NameTags getInstance() {
        if (NameTags.INSTANCE == null) {
            NameTags.INSTANCE = new NameTags();
        }
        return NameTags.INSTANCE;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        for (final EntityPlayer player : NameTags.mc.world.playerEntities) {
            if (player != null && !player.equals((Object)NameTags.mc.player) && player.isEntityAlive() && (!player.isInvisible() || this.invisibles.getValue())) {
                final double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - NameTags.mc.getRenderManager().renderPosX;
                final double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - NameTags.mc.getRenderManager().renderPosY;
                final double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - NameTags.mc.getRenderManager().renderPosZ;
                this.renderNameTag(player, x, y, z, event.getPartialTicks());
            }
        }
    }
    
    private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z, final float delta) {
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = NameTags.mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        final String displayTag = this.getDisplayTag(player);
        final double distance = camera.getDistance(x + NameTags.mc.getRenderManager().viewerPosX, y + NameTags.mc.getRenderManager().viewerPosY, z + NameTags.mc.getRenderManager().viewerPosZ);
        final int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + this.size.getValue() * (distance * this.factor.getValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue()) {
            scale = this.size.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.4f, (float)z);
        GlStateManager.rotate(-NameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(NameTags.mc.getRenderManager().playerViewX, (NameTags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue()) {
            this.drawRect((float)(-width - 2), (float)(-(NameTags.mc.fontRenderer.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, 1426063360);
        }
        if (this.outline.getValue()) {
            this.drawOutlineRect((float)(-width - 2), (float)(-(NameTags.mc.fontRenderer.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, this.getOutlineColor(player));
        }
        GlStateManager.disableBlend();
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
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
        if (this.armor.getValue()) {
            GlStateManager.pushMatrix();
            int xOffset = -6;
            int count = 0;
            for (final ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack != null) {
                    xOffset -= 8;
                    if (armourStack.getItem() == Items.AIR) {
                        continue;
                    }
                    ++count;
                }
            }
            xOffset -= 8;
            final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
                renderOffhand.stackSize = 1;
            }
            this.renderItemStack(renderOffhand, xOffset, -26);
            xOffset += 16;
            if (this.reversed.getValue()) {
                for (int index = 0; index <= 3; ++index) {
                    final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack2 != null && armourStack2.getItem() != Items.AIR) {
                        final ItemStack renderStack1 = armourStack2.copy();
                        this.renderItemStack(armourStack2, xOffset, -26);
                        xOffset += 16;
                    }
                }
            }
            else {
                for (int index = 3; index >= 0; --index) {
                    final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack2 != null && armourStack2.getItem() != Items.AIR) {
                        final ItemStack renderStack1 = armourStack2.copy();
                        this.renderItemStack(armourStack2, xOffset, -26);
                        xOffset += 16;
                    }
                }
            }
            this.renderItemStack(renderMainHand, xOffset, -26);
            GlStateManager.popMatrix();
        }
        this.renderer.drawStringWithShadow(displayTag, (float)(-width), (float)(-(this.renderer.getFontHeight() - 1)), this.getDisplayColor(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
    
    private int getDisplayColor(final EntityPlayer player) {
        int displaycolor = ColorHolder.toHex(this.NCred.getValue(), this.NCgreen.getValue(), this.NCblue.getValue());
        if (OyVey.friendManager.isFriend(player)) {
            return ColorHolder.toHex(this.FCred.getValue(), this.FCgreen.getValue(), this.FCblue.getValue());
        }
        if (player.isInvisible() && this.invisibles.getValue()) {
            displaycolor = ColorHolder.toHex(this.ICred.getValue(), this.ICgreen.getValue(), this.ICblue.getValue());
        }
        else if (player.isSneaking() && this.sneak.getValue()) {
            displaycolor = ColorHolder.toHex(this.SCred.getValue(), this.SCgreen.getValue(), this.SCblue.getValue());
        }
        return displaycolor;
    }
    
    private int getOutlineColor(final EntityPlayer player) {
        int outlinecolor = ColorHolder.toHex(this.Ored.getValue(), this.Ogreen.getValue(), this.Oblue.getValue());
        if (OyVey.friendManager.isFriend(player)) {
            outlinecolor = ColorHolder.toHex(this.FOred.getValue(), this.FOgreen.getValue(), this.FOblue.getValue());
        }
        else if (player.isInvisible() && this.invisibles.getValue()) {
            outlinecolor = ColorHolder.toHex(this.IOred.getValue(), this.IOgreen.getValue(), this.IOblue.getValue());
        }
        else if (player.isSneaking() && this.sneak.getValue()) {
            outlinecolor = ColorHolder.toHex(this.SOred.getValue(), this.SOgreen.getValue(), this.SOblue.getValue());
        }
        return outlinecolor;
    }
    
    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        NameTags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        NameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        NameTags.mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRenderer, stack, x, y);
        NameTags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderEnchantmentText(final ItemStack stack, final int x, final int y) {
        int enchantmentY = y - 8;
        final int yCount = y;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", (float)(x * 2), (float)enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        if (enchants.tagCount() > 2 && this.max.getValue()) {
            if (this.maxText.getValue()) {
                this.renderer.drawStringWithShadow("", (float)(x * 2), (float)enchantmentY, ColorHolder.toHex(this.Mred.getValue(), this.Mgreen.getValue(), this.Mblue.getValue()));
                enchantmentY -= 8;
            }
            else {
                this.renderer.drawStringWithShadow("max", (float)(x * 2), (float)enchantmentY, ColorHolder.toHex(this.Mred.getValue(), this.Mgreen.getValue(), this.Mblue.getValue()));
                enchantmentY -= 8;
            }
        }
        else {
            for (int index = 0; index < enchants.tagCount(); ++index) {
                final short id = enchants.getCompoundTagAt(index).getShort("id");
                final short level = enchants.getCompoundTagAt(index).getShort("lvl");
                final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                if (enc != null) {
                    String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, 1).toLowerCase()) : enc.getTranslatedName((int)level).substring(0, 1).toLowerCase();
                    encName += level;
                    this.renderer.drawStringWithShadow(encName, (float)(x * 2), (float)enchantmentY, -1);
                    enchantmentY -= 8;
                }
            }
        }
        if (DamageUtil.hasDurability(stack)) {
            final int percent = DamageUtil.getRoundedDamage(stack);
            String color;
            if (percent >= 60) {
                color = TextUtil.GREEN;
            }
            else if (percent >= 25) {
                color = TextUtil.YELLOW;
            }
            else {
                color = TextUtil.RED;
            }
            this.renderer.drawStringWithShadow(color + percent + "%", (float)(x * 2), (float)enchantmentY, -1);
        }
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
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants2.tagCount(); ++index2) {
                final short id2 = enchants2.getCompoundTagAt(index2).getShort("id");
                final Enchantment enc2 = Enchantment.getEnchantmentByID((int)id2);
                if (enc2 != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        final ItemStack renderOffHand = player.getHeldItemOffhand().copy();
        if (renderOffHand.hasEffect()) {
            float encY = 0.0f;
            final NBTTagList enchants = renderOffHand.getEnchantmentTagList();
            for (int index = 0; index < enchants.tagCount(); ++index) {
                final short id = enchants.getCompoundTagAt(index).getShort("id");
                final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                if (enc != null) {
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (arm ? 0 : 20) + enchantmentY;
    }
    
    private String getDisplayTag(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(NameTags.mc.getSession().getUsername())) {
            name = "You";
        }
        if (!this.health.getValue()) {
            return name;
        }
        final float health = EntityUtil.getHealth((Entity)player);
        String color;
        if (health > 18.0f) {
            color = TextUtil.GREEN;
        }
        else if (health > 16.0f) {
            color = TextUtil.DARK_GREEN;
        }
        else if (health > 12.0f) {
            color = TextUtil.YELLOW;
        }
        else if (health > 8.0f) {
            color = TextUtil.RED;
        }
        else if (health > 5.0f) {
            color = TextUtil.DARK_RED;
        }
        else {
            color = TextUtil.DARK_RED;
        }
        String pingStr = "";
        if (this.ping.getValue()) {
            try {
                final int responseTime = Objects.requireNonNull(NameTags.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr = pingStr + responseTime + "ms ";
            }
            catch (Exception ex) {}
        }
        String idString = "";
        if (this.entityID.getValue()) {
            idString = idString + "ID: " + player.getEntityId() + " ";
        }
        String gameModeStr = "";
        if (this.gamemode.getValue()) {
            if (player.isCreative()) {
                gameModeStr += "[C] ";
            }
            else if (player.isSpectator() || player.isInvisible()) {
                gameModeStr += "[I] ";
            }
            else {
                gameModeStr += "[S] ";
            }
        }
        if (Math.floor(health) == health) {
            name = name + color + " " + ((health > 0.0f) ? Integer.valueOf((int)Math.floor(health)) : "dead");
        }
        else {
            name = name + color + " " + ((health > 0.0f) ? Integer.valueOf((int)health) : "dead");
        }
        return " " + pingStr + idString + gameModeStr + name + " ";
    }
    
    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
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
        GlStateManager.glLineWidth((float)this.Owidth.getValue());
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
    
    public void drawRect(final float x, final float y, final float w, final float h, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.Owidth.getValue());
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
    
    @Override
    public void onUpdate() {
        if (this.outline.getValue().equals(false)) {
            this.rect.setValue(true);
        }
        else if (this.rect.getValue().equals(false)) {
            this.outline.setValue(true);
        }
        if (this.ORainbow.getValue()) {
            this.OutlineRainbow();
        }
        if (this.NCRainbow.getValue()) {
            this.TextRainbow();
        }
        if (this.FCRainbow.getValue()) {
            this.FriendRainbow();
        }
        if (this.SCRainbow.getValue()) {
            this.SneakColorRainbow();
        }
        if (this.ICRainbow.getValue()) {
            this.InvisibleRainbow();
        }
        if (this.FORainbow.getValue()) {
            this.FriendOutlineRainbow();
        }
        if (this.IORainbow.getValue()) {
            this.InvisibleOutlineRainbow();
        }
        if (this.SORainbow.getValue()) {
            this.SneakOutlineRainbow();
        }
    }
    
    public void OutlineRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.Ored.setValue(color_rgb_o >> 16 & 0xFF);
        this.Ogreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.Oblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void TextRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.NCred.setValue(color_rgb_o >> 16 & 0xFF);
        this.NCgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.NCblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void FriendRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.FCred.setValue(color_rgb_o >> 16 & 0xFF);
        this.FCgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.FCblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void SneakColorRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.SCred.setValue(color_rgb_o >> 16 & 0xFF);
        this.SCgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.SCblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void InvisibleRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.ICred.setValue(color_rgb_o >> 16 & 0xFF);
        this.ICgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.ICblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void InvisibleOutlineRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.IOred.setValue(color_rgb_o >> 16 & 0xFF);
        this.IOgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.IOblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void FriendOutlineRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.FOred.setValue(color_rgb_o >> 16 & 0xFF);
        this.FOgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.FOblue.setValue(color_rgb_o & 0xFF);
    }
    
    public void SneakOutlineRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.SOred.setValue(color_rgb_o >> 16 & 0xFF);
        this.SOgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.SOblue.setValue(color_rgb_o & 0xFF);
    }
    
    static {
        NameTags.INSTANCE = new NameTags();
    }
}
