//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.features.command.Command;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.modules.Module;

public class OldFagDupe extends Module
{
    Entity donkey;
    
    public OldFagDupe() {
        super("OldFagDupe", "", Category.MISC, true, false, false);
    }
    
    public boolean setup() {
        return false;
    }
    
    @Override
    public void onEnable() {
        if (this.findAirInHotbar() == -1) {
            this.disable();
            return;
        }
        if (this.findChestInHotbar() == -1) {
            this.disable();
            return;
        }
        this.donkey = (Entity)OldFagDupe.mc.world.loadedEntityList.stream().filter(this::isValidEntity).min(Comparator.comparing(p_Entity -> OldFagDupe.mc.player.getDistance(p_Entity))).orElse(null);
        if (this.donkey == null) {
            this.disable();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.findAirInHotbar() == -1) {
            this.disable();
            return;
        }
        if (this.findChestInHotbar() == -1) {
            this.disable();
            return;
        }
        this.donkey = (Entity)OldFagDupe.mc.world.loadedEntityList.stream().filter(this::isValidEntity).min(Comparator.comparing(p_Entity -> OldFagDupe.mc.player.getDistance(p_Entity))).orElse(null);
        if (this.donkey == null) {
            this.disable();
            return;
        }
        this.putChestOn();
        Command.sendMessage("put chest on the donkey");
        this.toggle();
    }
    
    public void putChestOn() {
        OldFagDupe.mc.player.inventory.currentItem = this.findAirInHotbar();
        OldFagDupe.mc.player.inventory.currentItem = this.findChestInHotbar();
        OldFagDupe.mc.playerController.interactWithEntity((EntityPlayer)OldFagDupe.mc.player, this.donkey, EnumHand.MAIN_HAND);
    }
    
    private int findChestInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = OldFagDupe.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockChest) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    private int findAirInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = OldFagDupe.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.AIR) {
                slot = i;
            }
        }
        return slot;
    }
    
    private boolean isValidEntity(final Entity entity) {
        if (entity instanceof AbstractChestHorse) {
            final AbstractChestHorse donkey = (AbstractChestHorse)entity;
            return !donkey.isChild() && donkey.isTame();
        }
        return false;
    }
}
