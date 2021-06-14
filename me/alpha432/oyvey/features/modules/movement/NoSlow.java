//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class NoSlow extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Boolean> guiMove;
    boolean sneaking;
    private static final KeyBinding[] keys;
    
    public NoSlow() {
        super("NoSlow", "ns", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Normal));
        this.guiMove = (Setting<Boolean>)this.register(new Setting("GuiMove", (T)true));
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.guiMove.getValue()) {
            for (final KeyBinding bind : NoSlow.keys) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
            }
            if (NoSlow.mc.currentScreen == null) {
                for (final KeyBinding bind : NoSlow.keys) {
                    if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                        KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                    }
                }
            }
        }
        if (this.mode.getValue() == Mode.Strict) {
            final Item item = NoSlow.mc.player.getActiveItemStack().getItem();
            if (this.sneaking && ((!NoSlow.mc.player.isHandActive() && item instanceof ItemFood) || item instanceof ItemBow || item instanceof ItemPotion || !(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion))) {
                NoSlow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)NoSlow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.sneaking = false;
            }
        }
        if (NoSlow.mc.currentScreen != null && !(NoSlow.mc.currentScreen instanceof GuiChat) && this.guiMove.getValue()) {
            if (NoSlow.mc.currentScreen instanceof OyVeyGui && !this.guiMove.getValue()) {
                return;
            }
            if (Keyboard.isKeyDown(200)) {
                final EntityPlayerSP player = NoSlow.mc.player;
                player.rotationPitch -= 5.0f;
            }
            if (Keyboard.isKeyDown(208)) {
                final EntityPlayerSP player2 = NoSlow.mc.player;
                player2.rotationPitch += 5.0f;
            }
            if (Keyboard.isKeyDown(205)) {
                final EntityPlayerSP player3 = NoSlow.mc.player;
                player3.rotationYaw += 5.0f;
            }
            if (Keyboard.isKeyDown(203)) {
                final EntityPlayerSP player4 = NoSlow.mc.player;
                player4.rotationYaw -= 5.0f;
            }
            if (NoSlow.mc.player.rotationPitch > 90.0f) {
                NoSlow.mc.player.rotationPitch = 90.0f;
            }
            if (NoSlow.mc.player.rotationPitch < -90.0f) {
                NoSlow.mc.player.rotationPitch = -90.0f;
            }
        }
    }
    
    @SubscribeEvent
    public void onUseItem(final LivingEntityUseItemEvent event) {
        if (this.mode.getValue() == Mode.Strict && !this.sneaking) {
            NoSlow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)NoSlow.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.sneaking = true;
        }
    }
    
    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        if (this.mode.getValue() == Mode.Normal && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }
    
    static {
        keys = new KeyBinding[] { NoSlow.mc.gameSettings.keyBindForward, NoSlow.mc.gameSettings.keyBindBack, NoSlow.mc.gameSettings.keyBindLeft, NoSlow.mc.gameSettings.keyBindRight, NoSlow.mc.gameSettings.keyBindJump, NoSlow.mc.gameSettings.keyBindSprint };
    }
    
    public enum Mode
    {
        Normal, 
        Strict;
    }
}
