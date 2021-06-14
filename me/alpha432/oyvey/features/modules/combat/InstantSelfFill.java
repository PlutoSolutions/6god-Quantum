//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import java.lang.reflect.Field;
import net.minecraft.util.Timer;
import me.alpha432.oyvey.manager.Mapping;
import net.minecraft.client.Minecraft;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.util.ItemUtil;
import net.minecraft.item.Item;
import me.alpha432.oyvey.util.WorldUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.init.Blocks;
import me.alpha432.oyvey.features.modules.movement.ReverseStep;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.modules.Module;

public class InstantSelfFill extends Module
{
    private BlockPos playerPos;
    private final Setting<Mode> mode;
    public final Setting<Page> page;
    private final Setting<Boolean> timerfill;
    private final Setting<Boolean> autoCenter;
    private final Setting<Boolean> rotate;
    private final Setting<Float> height;
    public BlockPos startPos;
    
    public InstantSelfFill() {
        super("Burrow", "does the thing i guess", Category.COMBAT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Packet));
        this.page = (Setting<Page>)this.register(new Setting("Block", (T)Page.EChest, v -> this.mode.getValue() == Mode.Packet));
        this.timerfill = (Setting<Boolean>)this.register(new Setting("Timer", (T)true, v -> this.mode.getValue() == Mode.Vanilla));
        this.autoCenter = (Setting<Boolean>)this.register(new Setting("Center", (T)false));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)Boolean.FALSE));
        this.height = (Setting<Float>)this.register(new Setting("Height", (T)5.0f, (T)(-15.0f), (T)15.0f, v -> this.mode.getValue() == Mode.Packet));
        this.startPos = null;
    }
    
    @Override
    public void onEnable() {
        if (this.mode.getValue() == Mode.Vanilla) {
            if (this.timerfill.getValue()) {
                this.setTimer(50.0f);
            }
            OyVey.moduleManager.getModuleByName("ReverseStep").isEnabled();
            ReverseStep.getInstance().disable();
            this.playerPos = new BlockPos(InstantSelfFill.mc.player.posX, InstantSelfFill.mc.player.posY, InstantSelfFill.mc.player.posZ);
            if (InstantSelfFill.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.OBSIDIAN)) {
                this.disable();
                return;
            }
            InstantSelfFill.mc.player.jump();
        }
        if (this.mode.getValue() == Mode.Packet) {
            if (fullNullCheck()) {
                this.disable();
                return;
            }
            OyVey.moduleManager.getModuleByName("ReverseStep").isEnabled();
            ReverseStep.getInstance().disable();
            if (this.page.getValue() == Page.EChest || this.page.getValue() == Page.Obsdidian) {
                this.startPos = new BlockPos(InstantSelfFill.mc.player.getPositionVector());
            }
        }
    }
    
    @Subscribe
    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.Vanilla) {
            if (nullCheck()) {
                return;
            }
            if (InstantSelfFill.mc.player.posY > this.playerPos.getY() + 1.04) {
                WorldUtil.placeBlock(this.playerPos, InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN));
                InstantSelfFill.mc.player.jump();
                this.disable();
            }
        }
        if (this.autoCenter.getValue()) {
            OyVey.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
        }
        if (this.mode.getValue() == Mode.Packet) {
            if (fullNullCheck()) {
                return;
            }
            final int startSlot = InstantSelfFill.mc.player.inventory.currentItem;
            if (this.page.getValue() == Page.EChest) {
                final int enderSlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST));
                ItemUtil.switchToHotbarSlot(enderSlot, false);
                if (enderSlot == -1) {
                    Command.sendMessage("<" + this.getDisplayName() + "> out of echests.");
                    this.disable();
                    return;
                }
            }
            if (this.page.getValue() == Page.Obsdidian) {
                final int obbySlot = ItemUtil.getItemFromHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN));
                ItemUtil.switchToHotbarSlot(obbySlot, false);
                if (obbySlot == -1) {
                    Command.sendMessage("<" + this.getDisplayName() + "> out of obsidian.");
                    this.disable();
                    return;
                }
            }
            if (this.page.getValue() == Page.EChest || this.page.getValue() == Page.Obsdidian) {
                InstantSelfFill.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(InstantSelfFill.mc.player.posX, InstantSelfFill.mc.player.posY + 0.4199999, InstantSelfFill.mc.player.posZ, true));
                InstantSelfFill.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(InstantSelfFill.mc.player.posX, InstantSelfFill.mc.player.posY + 0.7531999, InstantSelfFill.mc.player.posZ, true));
                InstantSelfFill.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(InstantSelfFill.mc.player.posX, InstantSelfFill.mc.player.posY + 1.0013359, InstantSelfFill.mc.player.posZ, true));
                InstantSelfFill.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(InstantSelfFill.mc.player.posX, InstantSelfFill.mc.player.posY + 1.1661092, InstantSelfFill.mc.player.posZ, true));
                BlockUtil.placeBlock(this.startPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                InstantSelfFill.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(InstantSelfFill.mc.player.posX, InstantSelfFill.mc.player.posY + this.height.getValue(), InstantSelfFill.mc.player.posZ, false));
                InstantSelfFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)InstantSelfFill.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                InstantSelfFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)InstantSelfFill.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                if (startSlot != -1) {
                    ItemUtil.switchToHotbarSlot(startSlot, false);
                }
                this.disable();
            }
        }
    }
    
    @Override
    public void onDisable() {
        OyVey.moduleManager.getModuleByName("ReverseStep").isDisabled();
        ReverseStep.getInstance().enable();
        if (this.mode.getValue() == Mode.Vanilla) {
            this.setTimer(1.0f);
        }
    }
    
    private void setTimer(final float value) {
        try {
            final Field timer = Minecraft.class.getDeclaredField(Mapping.timer);
            timer.setAccessible(true);
            final Field tickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(InstantSelfFill.mc), 50.0f / value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : InstantSelfFill.mc.world.loadedEntityList) {
            if (entity.equals((Object)InstantSelfFill.mc.player)) {
                continue;
            }
            if (entity instanceof EntityItem) {
                continue;
            }
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.Vanilla) {
            return "Vanilla";
        }
        if (this.mode.getValue() == Mode.Packet) {}
        return "Packet";
    }
    
    public enum Page
    {
        EChest, 
        Obsdidian;
    }
    
    public enum Mode
    {
        Packet, 
        Vanilla;
    }
}
