//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockEnderChest;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.util.math.Vec3d;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class BlockLag extends Module
{
    private static BlockLag INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> smartTp;
    private final Setting<Integer> tpMin;
    private final Setting<Integer> tpMax;
    private final Setting<Boolean> noVoid;
    private final Setting<Integer> tpHeight;
    private final Setting<Boolean> keepInside;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> sneaking;
    private final Setting<Boolean> offground;
    private final Setting<Boolean> chat;
    private final Setting<Boolean> tpdebug;
    private BlockPos burrowPos;
    private int lastBlock;
    private int blockSlot;
    private Class block;
    private String name;
    
    public BlockLag() {
        super("SelfFill", "sf", Category.COMBAT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.OBSIDIAN));
        this.smartTp = (Setting<Boolean>)this.register(new Setting("SmartTP", (T)true));
        this.tpMin = (Setting<Integer>)this.register(new Setting("TPMin", (T)3, (T)3, (T)10, v -> this.smartTp.getValue()));
        this.tpMax = (Setting<Integer>)this.register(new Setting("TPMax", (T)25, (T)10, (T)40, v -> this.smartTp.getValue()));
        this.noVoid = (Setting<Boolean>)this.register(new Setting("NoVoid", (T)true, v -> this.smartTp.getValue()));
        this.tpHeight = (Setting<Integer>)this.register(new Setting("TPHeight", (T)2, (T)(-40), (T)40, v -> !this.smartTp.getValue()));
        this.keepInside = (Setting<Boolean>)this.register(new Setting("Center", (T)true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false));
        this.sneaking = (Setting<Boolean>)this.register(new Setting("Sneak", (T)false));
        this.offground = (Setting<Boolean>)this.register(new Setting("Offground", (T)false));
        this.chat = (Setting<Boolean>)this.register(new Setting("Chat Msgs", (T)true));
        this.tpdebug = (Setting<Boolean>)this.register(new Setting("Debug", (T)false, v -> this.chat.getValue() && this.smartTp.getValue()));
        BlockLag.INSTANCE = this;
    }
    
    public static BlockLag getInstance() {
        if (BlockLag.INSTANCE == null) {
            BlockLag.INSTANCE = new BlockLag();
        }
        return BlockLag.INSTANCE;
    }
    
    @Override
    public void onEnable() {
        this.burrowPos = new BlockPos(BlockLag.mc.player.posX, Math.ceil(BlockLag.mc.player.posY), BlockLag.mc.player.posZ);
        this.blockSlot = this.findBlockSlot();
        this.lastBlock = BlockLag.mc.player.inventory.currentItem;
        if (!this.doChecks() || this.blockSlot == -1) {
            this.disable();
            return;
        }
        if (this.keepInside.getValue()) {
            double x = BlockLag.mc.player.posX - Math.floor(BlockLag.mc.player.posX);
            double z = BlockLag.mc.player.posZ - Math.floor(BlockLag.mc.player.posZ);
            if (x <= 0.3 || x >= 0.7) {
                x = ((x > 0.5) ? 0.69 : 0.31);
            }
            if (z < 0.3 || z > 0.7) {
                z = ((z > 0.5) ? 0.69 : 0.31);
            }
            BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Math.floor(BlockLag.mc.player.posX) + x, BlockLag.mc.player.posY, Math.floor(BlockLag.mc.player.posZ) + z, BlockLag.mc.player.onGround));
            BlockLag.mc.player.setPosition(Math.floor(BlockLag.mc.player.posX) + x, BlockLag.mc.player.posY, Math.floor(BlockLag.mc.player.posZ) + z);
        }
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 0.41999998688698, BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 0.7531999805211997, BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 1.00133597911214, BlockLag.mc.player.posZ, !this.offground.getValue()));
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, BlockLag.mc.player.posY + 1.16610926093821, BlockLag.mc.player.posZ, !this.offground.getValue()));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.rotate.getValue()) {
            final float[] angle = MathUtil.calcAngle(BlockLag.mc.player.getPositionEyes(BlockLag.mc.getRenderPartialTicks()), new Vec3d((double)(this.burrowPos.getX() + 0.5f), (double)(this.burrowPos.getY() + 0.5f), (double)(this.burrowPos.getZ() + 0.5f)));
            OyVey.rotationManager.setPlayerRotations(angle[0], angle[1]);
        }
        InventoryUtil.switchToHotbarSlot(this.blockSlot, false);
        BlockUtil.placeBlock(this.burrowPos, (this.blockSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, false, true, this.sneaking.getValue());
        InventoryUtil.switchToHotbarSlot(this.lastBlock, false);
        BlockLag.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BlockLag.mc.player.posX, ((boolean)this.smartTp.getValue()) ? ((double)this.adaptiveTpHeight()) : (this.tpHeight.getValue() + BlockLag.mc.player.posY), BlockLag.mc.player.posZ, !this.offground.getValue()));
        this.disable();
    }
    
    private int findBlockSlot() {
        switch (this.mode.getValue()) {
            case ECHEST: {
                this.block = BlockEnderChest.class;
                this.name = "Ender Chests";
                break;
            }
            case OBSIDIAN: {
                this.block = BlockObsidian.class;
                this.name = "Obsidian";
                break;
            }
            case SOULSAND: {
                this.block = BlockSoulSand.class;
                this.name = "Soul Sand";
                break;
            }
        }
        final int slot = InventoryUtil.findHotbarBlock(this.block);
        if (slot == -1) {
            if (InventoryUtil.isBlock(BlockLag.mc.player.getHeldItemOffhand().getItem(), this.block)) {
                return -2;
            }
            if (this.chat.getValue()) {
                Command.sendMessage("§7" + this.displayName.getValue() + ":§c No " + this.name + " to use");
            }
        }
        return slot;
    }
    
    private int adaptiveTpHeight() {
        for (int airblock = (this.noVoid.getValue() && this.tpMax.getValue() * -1 + this.burrowPos.getY() < 0) ? (this.burrowPos.getY() * -1) : (this.tpMax.getValue() * -1); airblock < this.tpMax.getValue(); ++airblock) {
            if (Math.abs(airblock) >= this.tpMin.getValue() && BlockLag.mc.world.isAirBlock(this.burrowPos.offset(EnumFacing.UP, airblock)) && BlockLag.mc.world.isAirBlock(this.burrowPos.offset(EnumFacing.UP, airblock + 1))) {
                if (this.tpdebug.getValue()) {
                    Command.sendMessage(Integer.toString(airblock));
                }
                return this.burrowPos.getY() + airblock;
            }
        }
        return 69420;
    }
    
    private boolean doChecks() {
        if (fullNullCheck()) {
            return false;
        }
        if (this.smartTp.getValue() && this.adaptiveTpHeight() == 69420) {
            if (this.chat.getValue()) {
                Command.sendMessage("§7" + this.displayName.getValue() + ":§c Not enough room");
            }
            return false;
        }
        if (BlockLag.mc.world.getBlockState(this.burrowPos).getBlock().equals(Blocks.OBSIDIAN)) {
            return false;
        }
        if (!BlockLag.mc.world.isAirBlock(this.burrowPos.offset(EnumFacing.UP, 2))) {
            if (this.chat.getValue()) {
                Command.sendMessage("§7" + this.displayName.getValue() + ":§c Not enough room");
            }
            return false;
        }
        for (final Entity entity : BlockLag.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityItem) && !entity.equals((Object)BlockLag.mc.player) && new AxisAlignedBB(this.burrowPos).intersects(entity.getEntityBoundingBox())) {
                if (this.chat.getValue()) {
                    Command.sendMessage("§7" + this.displayName.getValue() + ":§c Not enough room");
                }
                return false;
            }
        }
        return true;
    }
    
    public enum Mode
    {
        OBSIDIAN, 
        ECHEST, 
        SOULSAND;
    }
}
