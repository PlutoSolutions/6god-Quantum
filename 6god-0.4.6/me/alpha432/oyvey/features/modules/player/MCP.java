//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ItemEnderPearl;
import org.lwjgl.input.Mouse;
import me.alpha432.oyvey.features.modules.Module;

public class MCP extends Module
{
    private boolean clicked;
    
    public MCP() {
        super("MCP", "mcp", Category.PLAYER, false, false, false);
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked) {
                this.throwPearl();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    private void throwPearl() {
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean bl;
        final boolean offhand = bl = (MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL);
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.playerController.processRightClick((EntityPlayer)MCP.mc.player, (World)MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
}
