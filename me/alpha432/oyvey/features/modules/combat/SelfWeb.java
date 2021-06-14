//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import me.alpha432.oyvey.util.BlockInteractionUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Iterator;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.util.WorldUtil;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.alpha432.oyvey.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class SelfWeb extends Module
{
    public Setting<Boolean> alwayson;
    public Setting<Boolean> rotate;
    public Setting<Integer> webRange;
    int new_slot;
    boolean sneak;
    
    public SelfWeb() {
        super("SelfWeb", "Places webs at your feet", Category.COMBAT, true, false, false);
        this.alwayson = (Setting<Boolean>)this.register(new Setting("AlwaysOn", (T)false));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.webRange = (Setting<Integer>)this.register(new Setting("EnemyRange", (T)4, (T)0, (T)8));
        this.new_slot = -1;
        this.sneak = false;
    }
    
    @Override
    public void enable() {
        if (SelfWeb.mc.player != null) {
            this.new_slot = this.find_in_hotbar();
            if (this.new_slot == -1) {
                Command.sendMessage(ChatFormatting.RED + "< " + ChatFormatting.GRAY + "SelfWeb" + ChatFormatting.RED + "> " + ChatFormatting.DARK_RED + "No webs in hotbar!");
            }
        }
    }
    
    @Override
    public void disable() {
        if (SelfWeb.mc.player != null && this.sneak) {
            SelfWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfWeb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.sneak = false;
        }
    }
    
    @Override
    public void onUpdate() {
        if (SelfWeb.mc.player == null) {
            return;
        }
        if (this.alwayson.getValue()) {
            final EntityPlayer target = this.find_closest_target();
            if (target == null) {
                return;
            }
            if (SelfWeb.mc.player.getDistance((Entity)target) < this.webRange.getValue() && this.is_surround()) {
                final int last_slot = SelfWeb.mc.player.inventory.currentItem;
                SelfWeb.mc.player.inventory.currentItem = this.new_slot;
                SelfWeb.mc.playerController.updateController();
                this.place_blocks(WorldUtil.GetLocalPlayerPosFloored());
                SelfWeb.mc.player.inventory.currentItem = last_slot;
            }
        }
        else {
            final int last_slot2 = SelfWeb.mc.player.inventory.currentItem;
            SelfWeb.mc.player.inventory.currentItem = this.new_slot;
            SelfWeb.mc.playerController.updateController();
            this.place_blocks(WorldUtil.GetLocalPlayerPosFloored());
            SelfWeb.mc.player.inventory.currentItem = last_slot2;
            this.disable();
        }
    }
    
    public EntityPlayer find_closest_target() {
        if (SelfWeb.mc.world.playerEntities.isEmpty()) {
            return null;
        }
        EntityPlayer closestTarget = null;
        for (final EntityPlayer target : SelfWeb.mc.world.playerEntities) {
            if (target == SelfWeb.mc.player) {
                continue;
            }
            if (EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (closestTarget != null && SelfWeb.mc.player.getDistance((Entity)target) > SelfWeb.mc.player.getDistance((Entity)closestTarget)) {
                continue;
            }
            closestTarget = target;
        }
        return closestTarget;
    }
    
    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = SelfWeb.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Item.getItemById(30)) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean is_surround() {
        final BlockPos player_block = WorldUtil.GetLocalPlayerPosFloored();
        return SelfWeb.mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR && SelfWeb.mc.world.getBlockState(player_block.west()).getBlock() != Blocks.AIR && SelfWeb.mc.world.getBlockState(player_block.north()).getBlock() != Blocks.AIR && SelfWeb.mc.world.getBlockState(player_block.south()).getBlock() != Blocks.AIR && SelfWeb.mc.world.getBlockState(player_block).getBlock() == Blocks.AIR;
    }
    
    private void place_blocks(final BlockPos pos) {
        if (!SelfWeb.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return;
        }
        if (!BlockInteractionUtil.checkForNeighbours(pos)) {
            return;
        }
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockInteractionUtil.canBeClicked(neighbor)) {
                if (BlockInteractionUtil.blackList.contains(SelfWeb.mc.world.getBlockState(neighbor).getBlock())) {
                    SelfWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfWeb.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    this.sneak = true;
                }
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (this.rotate.getValue()) {
                    BlockInteractionUtil.faceVectorPacketInstant(hitVec);
                }
                SelfWeb.mc.playerController.processRightClickBlock(SelfWeb.mc.player, SelfWeb.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                SelfWeb.mc.player.swingArm(EnumHand.MAIN_HAND);
                return;
            }
        }
    }
}
