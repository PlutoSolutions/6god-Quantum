//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.block.Block;
import me.alpha432.oyvey.event.events.JesusEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.features.modules.player.Freecam;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.math.AxisAlignedBB;
import me.alpha432.oyvey.features.modules.Module;

public class Jesus extends Module
{
    public static AxisAlignedBB offset;
    private static Jesus INSTANCE;
    public Setting<Mode> mode;
    public Setting<Boolean> cancelVehicle;
    public Setting<EventMode> eventMode;
    public Setting<Boolean> fall;
    private boolean grounded;
    
    public Jesus() {
        super("Jesus", "Allows you to walk on water", Category.PLAYER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.NORMAL));
        this.cancelVehicle = (Setting<Boolean>)this.register(new Setting("NoVehicle", (T)false));
        this.eventMode = (Setting<EventMode>)this.register(new Setting("Jump", (T)EventMode.PRE, v -> this.mode.getValue() == Mode.TRAMPOLINE));
        this.fall = (Setting<Boolean>)this.register(new Setting("NoFallOldfag", (T)false, v -> this.mode.getValue() == Mode.TRAMPOLINE));
        this.grounded = false;
        Jesus.INSTANCE = this;
    }
    
    public static Jesus getInstance() {
        if (Jesus.INSTANCE == null) {
            Jesus.INSTANCE = new Jesus();
        }
        return Jesus.INSTANCE;
    }
    
    @SubscribeEvent
    public void updateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (Feature.fullNullCheck() || Freecam.getInstance().isOn()) {
            return;
        }
        if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && !Jesus.mc.player.isSneaking() && !Jesus.mc.player.noClip && !Jesus.mc.gameSettings.keyBindJump.isKeyDown() && EntityUtil.isInLiquid()) {
            Jesus.mc.player.motionY = 0.10000000149011612;
        }
        if (event.getStage() == 0 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.PRE)) {
            this.doTrampoline();
        }
        else if (event.getStage() == 1 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.POST)) {
            this.doTrampoline();
        }
    }
    
    @SubscribeEvent
    public void sendPacket(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && Freecam.getInstance().isOff() && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.NORMAL) && Jesus.mc.player.getRidingEntity() == null && !Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
            final CPacketPlayer packet = event.getPacket();
            if (!EntityUtil.isInLiquid() && EntityUtil.isOnLiquid(0.05000000074505806) && EntityUtil.checkCollide() && Jesus.mc.player.ticksExisted % 3 == 0) {
                final CPacketPlayer cPacketPlayer = packet;
                cPacketPlayer.y -= 0.05000000074505806;
            }
        }
    }
    
    @SubscribeEvent
    public void onLiquidCollision(final JesusEvent event) {
        if (Feature.fullNullCheck() || Freecam.getInstance().isOn()) {
            return;
        }
        if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && Jesus.mc.world != null && Jesus.mc.player != null && EntityUtil.checkCollide() && Jesus.mc.player.motionY < 0.10000000149011612 && event.getPos().getY() < Jesus.mc.player.posY - 0.05000000074505806) {
            if (Jesus.mc.player.getRidingEntity() != null) {
                event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.949999988079071, 1.0));
            }
            else {
                event.setBoundingBox(Block.FULL_BLOCK_AABB);
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (this.cancelVehicle.getValue() && event.getPacket() instanceof SPacketMoveVehicle) {
            event.setCanceled(true);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.NORMAL) {
            return null;
        }
        return this.mode.currentEnumName();
    }
    
    private void doTrampoline() {
        if (Jesus.mc.player.isSneaking()) {
            return;
        }
        if (EntityUtil.isAboveLiquid((Entity)Jesus.mc.player) && !Jesus.mc.player.isSneaking() && !Jesus.mc.gameSettings.keyBindJump.pressed) {
            Jesus.mc.player.motionY = 0.1;
            return;
        }
        if (Jesus.mc.player.onGround || Jesus.mc.player.isOnLadder()) {
            this.grounded = false;
        }
        if (Jesus.mc.player.motionY > 0.0) {
            if (Jesus.mc.player.motionY < 0.03 && this.grounded) {
                final EntityPlayerSP player = Jesus.mc.player;
                player.motionY += 0.06713;
            }
            else if (Jesus.mc.player.motionY <= 0.05 && this.grounded) {
                final EntityPlayerSP player2 = Jesus.mc.player;
                player2.motionY *= 1.20000000999;
                final EntityPlayerSP player3 = Jesus.mc.player;
                player3.motionY += 0.06;
            }
            else if (Jesus.mc.player.motionY <= 0.08 && this.grounded) {
                final EntityPlayerSP player4 = Jesus.mc.player;
                player4.motionY *= 1.20000003;
                final EntityPlayerSP player5 = Jesus.mc.player;
                player5.motionY += 0.055;
            }
            else if (Jesus.mc.player.motionY <= 0.112 && this.grounded) {
                final EntityPlayerSP player6 = Jesus.mc.player;
                player6.motionY += 0.0535;
            }
            else if (this.grounded) {
                final EntityPlayerSP player7 = Jesus.mc.player;
                player7.motionY *= 1.000000000002;
                final EntityPlayerSP player8 = Jesus.mc.player;
                player8.motionY += 0.0517;
            }
        }
        if (this.grounded && Jesus.mc.player.motionY < 0.0 && Jesus.mc.player.motionY > -0.3) {
            final EntityPlayerSP player9 = Jesus.mc.player;
            player9.motionY += 0.045835;
        }
        if (!this.fall.getValue()) {
            Jesus.mc.player.fallDistance = 0.0f;
        }
        if (!EntityUtil.checkForLiquid((Entity)Jesus.mc.player, true)) {
            return;
        }
        if (EntityUtil.checkForLiquid((Entity)Jesus.mc.player, true)) {
            Jesus.mc.player.motionY = 0.5;
        }
        this.grounded = true;
    }
    
    static {
        Jesus.offset = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9999, 1.0);
        Jesus.INSTANCE = new Jesus();
    }
    
    public enum Mode
    {
        TRAMPOLINE, 
        BOUNCE, 
        VANILLA, 
        NORMAL;
    }
    
    public enum EventMode
    {
        PRE, 
        POST, 
        ALL;
    }
}
