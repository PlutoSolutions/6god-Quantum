//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.MoveEvent;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import java.util.concurrent.ConcurrentHashMap;
import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.Set;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Phase extends Module
{
    public Setting<Mode> mode;
    public Setting<Integer> xMove;
    public Setting<Integer> yMove;
    public Setting<Boolean> sneakpackets;
    public Setting<Boolean> extra;
    public Setting<Integer> offset;
    public Setting<Boolean> fallPacket;
    public Setting<Boolean> teleporter;
    public Setting<Boolean> boundingBox;
    public Setting<Integer> teleportConfirm;
    public Setting<Boolean> ultraPacket;
    public Setting<Boolean> updates;
    public Setting<Boolean> setOnMove;
    public Setting<Boolean> cliperino;
    public Setting<Boolean> scanPackets;
    public Setting<Boolean> resetConfirm;
    public Setting<Boolean> posLook;
    public Setting<Boolean> cancel;
    public Setting<Boolean> cancelType;
    public Setting<Boolean> onlyY;
    public Setting<Integer> cancelPacket;
    private final Set<CPacketPlayer> packets;
    private static Phase INSTANCE;
    private boolean teleport;
    private int teleportIds;
    private int posLookPackets;
    public Setting<Boolean> flight;
    public Setting<Integer> flightMode;
    public Setting<Boolean> doAntiFactor;
    public Setting<Double> antiFactor;
    public Setting<Double> extraFactor;
    public Setting<Boolean> strafeFactor;
    public Setting<Integer> loops;
    public Setting<Boolean> clearTeleMap;
    public Setting<Integer> mapTime;
    public Setting<Boolean> clearIDs;
    public Setting<Boolean> setYaw;
    public Setting<Boolean> setID;
    public Setting<Boolean> setMove;
    public Setting<Boolean> nocliperino;
    public Setting<Boolean> sendTeleport;
    public Setting<Boolean> resetID;
    public Setting<Boolean> setPos;
    public Setting<Boolean> invalidPacket;
    private final Map<Integer, IDtime> teleportmap;
    private int flightCounter;
    private int teleportID;
    
    public Phase() {
        super("Packetfly", "phase && pfly", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Phase));
        this.xMove = (Setting<Integer>)this.register(new Setting("MoveX", (T)1, (T)1, (T)1000, v -> this.mode.getValue() == Mode.Phase));
        this.yMove = (Setting<Integer>)this.register(new Setting("MoveY", (T)1, (T)1, (T)1000, v -> this.mode.getValue() == Mode.Phase));
        this.sneakpackets = (Setting<Boolean>)this.register(new Setting("SneakPacket", (T)true, v -> this.mode.getValue() == Mode.Phase));
        this.extra = (Setting<Boolean>)this.register(new Setting("ExtraPacket", (T)true, v -> this.mode.getValue() == Mode.Phase));
        this.offset = (Setting<Integer>)this.register(new Setting("ExtraOffset", (T)1337, (T)(-1337), (T)1337, v -> this.mode.getValue() == Mode.Phase && this.extra.getValue()));
        this.fallPacket = (Setting<Boolean>)this.register(new Setting("FallPacket", (T)true, v -> this.mode.getValue() == Mode.Phase));
        this.teleporter = (Setting<Boolean>)this.register(new Setting("Teleport", (T)true, v -> this.mode.getValue() == Mode.Phase));
        this.boundingBox = (Setting<Boolean>)this.register(new Setting("BoundingBox", (T)true, v -> this.mode.getValue() == Mode.Phase));
        this.teleportConfirm = (Setting<Integer>)this.register(new Setting("TPConfirm", (T)2, (T)0, (T)4, v -> this.mode.getValue() == Mode.Phase));
        this.ultraPacket = (Setting<Boolean>)this.register(new Setting("DoublePacket", (T)false, v -> this.mode.getValue() == Mode.Phase));
        this.updates = (Setting<Boolean>)this.register(new Setting("Update", (T)false, v -> this.mode.getValue() == Mode.Phase));
        this.setOnMove = (Setting<Boolean>)this.register(new Setting("SetMove", (T)false, v -> this.mode.getValue() == Mode.Phase));
        this.cliperino = (Setting<Boolean>)this.register(new Setting("NoClip", (T)false, v -> this.mode.getValue() == Mode.Phase && this.setOnMove.getValue()));
        this.scanPackets = (Setting<Boolean>)this.register(new Setting("ScanPackets", (T)false, v -> this.mode.getValue() == Mode.Phase));
        this.resetConfirm = (Setting<Boolean>)this.register(new Setting("Reset", (T)false, v -> this.mode.getValue() == Mode.Phase));
        this.posLook = (Setting<Boolean>)this.register(new Setting("PosLook", (T)false, v -> this.mode.getValue() == Mode.Phase));
        this.cancel = (Setting<Boolean>)this.register(new Setting("Cancel", (T)false, v -> this.mode.getValue() == Mode.Phase && this.posLook.getValue()));
        this.cancelType = (Setting<Boolean>)this.register(new Setting("SetYaw", (T)false, v -> this.mode.getValue() == Mode.Phase && this.posLook.getValue() && this.cancel.getValue()));
        this.onlyY = (Setting<Boolean>)this.register(new Setting("OnlyY", (T)false, v -> this.mode.getValue() == Mode.Phase && this.posLook.getValue()));
        this.cancelPacket = (Setting<Integer>)this.register(new Setting("Packets", (T)20, (T)0, (T)20, v -> this.mode.getValue() == Mode.Phase && this.posLook.getValue()));
        this.packets = (Set<CPacketPlayer>)new ConcurrentSet();
        this.teleport = true;
        this.teleportIds = 0;
        this.flight = (Setting<Boolean>)this.register(new Setting("Flight", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.flightMode = (Setting<Integer>)this.register(new Setting("FMode", (T)0, (T)0, (T)1, v -> this.mode.getValue() == Mode.Packetfly));
        this.doAntiFactor = (Setting<Boolean>)this.register(new Setting("Factorize", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.antiFactor = (Setting<Double>)this.register(new Setting("AntiFactor", (T)1.0, (T)0.1, (T)3.0, v -> this.mode.getValue() == Mode.Packetfly));
        this.extraFactor = (Setting<Double>)this.register(new Setting("ExtraFactor", (T)1.0, (T)0.1, (T)3.0, v -> this.mode.getValue() == Mode.Packetfly));
        this.strafeFactor = (Setting<Boolean>)this.register(new Setting("StrafeFactor", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.loops = (Setting<Integer>)this.register(new Setting("Loops", (T)1, (T)1, (T)10, v -> this.mode.getValue() == Mode.Packetfly));
        this.clearTeleMap = (Setting<Boolean>)this.register(new Setting("ClearMap", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.mapTime = (Setting<Integer>)this.register(new Setting("ClearTime", (T)30, (T)1, (T)500, v -> this.mode.getValue() == Mode.Packetfly));
        this.clearIDs = (Setting<Boolean>)this.register(new Setting("ClearIDs", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.setYaw = (Setting<Boolean>)this.register(new Setting("SetYaw", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.setID = (Setting<Boolean>)this.register(new Setting("SetID", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.setMove = (Setting<Boolean>)this.register(new Setting("SetMove", (T)false, v -> this.mode.getValue() == Mode.Packetfly));
        this.nocliperino = (Setting<Boolean>)this.register(new Setting("NoClip", (T)false, v -> this.mode.getValue() == Mode.Packetfly));
        this.sendTeleport = (Setting<Boolean>)this.register(new Setting("Teleport", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.resetID = (Setting<Boolean>)this.register(new Setting("ResetID", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.setPos = (Setting<Boolean>)this.register(new Setting("SetPos", (T)false, v -> this.mode.getValue() == Mode.Packetfly));
        this.invalidPacket = (Setting<Boolean>)this.register(new Setting("InvalidPacket", (T)true, v -> this.mode.getValue() == Mode.Packetfly));
        this.teleportmap = new ConcurrentHashMap<Integer, IDtime>();
        this.flightCounter = 0;
        this.teleportID = 0;
        this.setInstance();
    }
    
    public static Phase getInstance() {
        if (Phase.INSTANCE == null) {
            Phase.INSTANCE = new Phase();
        }
        return Phase.INSTANCE;
    }
    
    private void setInstance() {
        Phase.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (this.mode.getValue() == Mode.Packetfly) {
            this.teleportmap.entrySet().removeIf(idTime -> this.clearTeleMap.getValue() && idTime.getValue().getTimer().passedS(this.mapTime.getValue()));
        }
    }
    
    @Subscribe
    @Override
    public void onUpdate() {
        if (this.sneakpackets.getValue()) {
            Phase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Phase.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            Phase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Phase.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.Phase) {
            this.packets.clear();
            this.posLookPackets = 0;
            if (Phase.mc.player != null) {
                if (this.resetConfirm.getValue()) {
                    this.teleportIds = 0;
                }
                Phase.mc.player.noClip = false;
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (this.mode.getValue() == Mode.Phase) {
            if (this.setOnMove.getValue() && event.getStage() == 0 && !Phase.mc.isSingleplayer() && this.mode.getValue() == Mode.Phase) {
                event.setX(Phase.mc.player.motionX);
                event.setY(Phase.mc.player.motionY);
                event.setZ(Phase.mc.player.motionZ);
                if (this.cliperino.getValue()) {
                    Phase.mc.player.noClip = true;
                }
            }
            if (event.getStage() != 0 || Phase.mc.isSingleplayer() || this.mode.getValue() != Mode.Phase) {
                return;
            }
            if (!this.boundingBox.getValue() && !this.updates.getValue()) {
                this.doPhase(event);
            }
        }
        if (this.mode.getValue() == Mode.Packetfly && this.setMove.getValue() && this.flightCounter != 0) {
            event.setX(Phase.mc.player.motionX);
            event.setY(Phase.mc.player.motionY);
            event.setZ(Phase.mc.player.motionZ);
            if (this.nocliperino.getValue() && this.checkHitBoxes()) {
                Phase.mc.player.noClip = true;
            }
        }
    }
    
    @SubscribeEvent
    public void onPush(final PushEvent event) {
        if (this.mode.getValue() == Mode.Phase && event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPushOutOfBlocks(final PushEvent event) {
        if (this.mode.getValue() == Mode.Packetfly && event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onMove(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Mode.Phase) {
            if (Feature.fullNullCheck() || event.getStage() != 0 || this.mode.getValue() != Mode.Phase) {
                return;
            }
            if (this.boundingBox.getValue()) {
                this.doBoundingBox();
            }
            else if (this.updates.getValue()) {
                this.doPhase(null);
            }
        }
        if (this.mode.getValue() == Mode.Packetfly) {
            if (event.getStage() == 1) {
                return;
            }
            Phase.mc.player.setVelocity(0.0, 0.0, 0.0);
            double speed = 0.0;
            final boolean checkCollisionBoxes = this.checkHitBoxes();
            speed = ((Phase.mc.player.movementInput.jump && (checkCollisionBoxes || !EntityUtil.isMoving())) ? ((this.flight.getValue() && !checkCollisionBoxes) ? ((this.flightMode.getValue() == 0) ? (this.resetCounter(10) ? -0.032 : 0.062) : (this.resetCounter(20) ? -0.032 : 0.062)) : 0.062) : (Phase.mc.player.movementInput.sneak ? -0.062 : (checkCollisionBoxes ? 0.0 : (this.resetCounter(4) ? (this.flight.getValue() ? -0.04 : 0.0) : 0.0))));
            if (this.doAntiFactor.getValue() && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0) {
                speed /= this.antiFactor.getValue();
            }
            final double[] strafing = this.getMotion((this.strafeFactor.getValue() && checkCollisionBoxes) ? 0.031 : 0.26);
            for (int i = 1; i < this.loops.getValue() + 1; ++i) {
                Phase.mc.player.motionX = strafing[0] * i * this.extraFactor.getValue();
                Phase.mc.player.motionY = speed * i;
                Phase.mc.player.motionZ = strafing[1] * i * this.extraFactor.getValue();
                this.sendPackets(Phase.mc.player.motionX, Phase.mc.player.motionY, Phase.mc.player.motionZ, this.sendTeleport.getValue());
            }
        }
    }
    
    private void doPhase(final MoveEvent event) {
        if (this.mode.getValue() == Mode.Phase) {
            if (!this.boundingBox.getValue()) {
                final double[] dirSpeed = this.getMotion(this.teleport ? (this.yMove.getValue() / 10000.0) : ((this.yMove.getValue() - 1) / 10000.0));
                final double posX = Phase.mc.player.posX + dirSpeed[0];
                final double posY = Phase.mc.player.posY + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? (this.teleport ? (this.yMove.getValue() / 10000.0) : ((this.yMove.getValue() - 1) / 10000.0)) : 1.0E-8) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? (this.teleport ? (this.yMove.getValue() / 10000.0) : ((this.yMove.getValue() - 1) / 10000.0)) : 2.0E-8);
                final double posZ = Phase.mc.player.posZ + dirSpeed[1];
                final CPacketPlayer.PositionRotation packetPlayer = new CPacketPlayer.PositionRotation(posX, posY, posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, false);
                this.packets.add((CPacketPlayer)packetPlayer);
                Phase.mc.player.connection.sendPacket((Packet)packetPlayer);
                if (this.teleportConfirm.getValue() != 3) {
                    Phase.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportIds - 1));
                    ++this.teleportIds;
                }
                if (this.extra.getValue()) {
                    final CPacketPlayer.PositionRotation packet = new CPacketPlayer.PositionRotation(Phase.mc.player.posX, this.offset.getValue() + Phase.mc.player.posY, Phase.mc.player.posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, true);
                    this.packets.add((CPacketPlayer)packet);
                    Phase.mc.player.connection.sendPacket((Packet)packet);
                }
                if (this.teleportConfirm.getValue() != 1) {
                    Phase.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportIds + 1));
                    ++this.teleportIds;
                }
                if (this.ultraPacket.getValue()) {
                    final CPacketPlayer.PositionRotation packet2 = new CPacketPlayer.PositionRotation(posX, posY, posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, false);
                    this.packets.add((CPacketPlayer)packet2);
                    Phase.mc.player.connection.sendPacket((Packet)packet2);
                }
                if (this.teleportConfirm.getValue() == 4) {
                    Phase.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportIds));
                    ++this.teleportIds;
                }
                if (this.fallPacket.getValue()) {
                    Phase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Phase.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
                Phase.mc.player.setPosition(posX, posY, posZ);
                final boolean teleport = !this.teleporter.getValue() || !this.teleport;
                this.teleport = teleport;
                final boolean bl = teleport;
                if (event != null) {
                    event.setX(0.0);
                    event.setY(0.0);
                    event.setX(0.0);
                }
                else {
                    Phase.mc.player.motionX = 0.0;
                    Phase.mc.player.motionY = 0.0;
                    Phase.mc.player.motionZ = 0.0;
                }
            }
            Phase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Phase.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            Phase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Phase.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    private void doBoundingBox() {
        if (this.mode.getValue() == Mode.Phase) {
            final double[] dirSpeed = this.getMotion(this.teleport ? 0.02250000089406967 : 0.02239999920129776);
            Phase.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(Phase.mc.player.posX + dirSpeed[0], Phase.mc.player.posY + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 1.0E-8) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 2.0E-8), Phase.mc.player.posZ + dirSpeed[1], Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, false));
            Phase.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(Phase.mc.player.posX, -1337.0, Phase.mc.player.posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, true));
            Phase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Phase.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            Phase.mc.player.setPosition(Phase.mc.player.posX + dirSpeed[0], Phase.mc.player.posY + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 1.0E-8) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 2.0E-8), Phase.mc.player.posZ + dirSpeed[1]);
            this.teleport = !this.teleport;
            Phase.mc.player.motionZ = 0.0;
            Phase.mc.player.motionY = 0.0;
            Phase.mc.player.motionX = 0.0;
            Phase.mc.player.noClip = this.teleport;
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketPlayer packet;
        if (this.mode.getValue() == Mode.Packetfly && event.getPacket() instanceof CPacketPlayer && !this.packets.remove(packet = event.getPacket())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (this.mode.getValue() == Mode.Phase && this.posLook.getValue() && event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = event.getPacket();
            if (Phase.mc.player.isEntityAlive() && Phase.mc.world.isBlockLoaded(new BlockPos(Phase.mc.player.posX, Phase.mc.player.posY, Phase.mc.player.posZ)) && !(Phase.mc.currentScreen instanceof GuiDownloadTerrain)) {
                if (this.teleportIds <= 0) {
                    this.teleportIds = packet.getTeleportId();
                }
                if (this.cancel.getValue() && this.cancelType.getValue()) {
                    packet.yaw = Phase.mc.player.rotationYaw;
                    packet.pitch = Phase.mc.player.rotationPitch;
                    return;
                }
                if (this.cancel.getValue() && this.posLookPackets >= this.cancelPacket.getValue() && (!this.onlyY.getValue() || (!Phase.mc.gameSettings.keyBindForward.isKeyDown() && !Phase.mc.gameSettings.keyBindRight.isKeyDown() && !Phase.mc.gameSettings.keyBindLeft.isKeyDown() && !Phase.mc.gameSettings.keyBindBack.isKeyDown()))) {
                    this.posLookPackets = 0;
                    event.setCanceled(true);
                }
                ++this.posLookPackets;
            }
        }
        if (this.mode.getValue() == Mode.Packetfly && event.getPacket() instanceof SPacketPlayerPosLook && !Feature.fullNullCheck()) {
            final SPacketPlayerPosLook packet2 = event.getPacket();
            final BlockPos pos;
            if (Phase.mc.player.isEntityAlive() && Phase.mc.world.isBlockLoaded(pos = new BlockPos(Phase.mc.player.posX, Phase.mc.player.posY, Phase.mc.player.posZ), false) && !(Phase.mc.currentScreen instanceof GuiDownloadTerrain) && this.clearIDs.getValue()) {
                this.teleportmap.remove(packet2.getTeleportId());
            }
            if (this.setYaw.getValue()) {
                packet2.yaw = Phase.mc.player.rotationYaw;
                packet2.pitch = Phase.mc.player.rotationPitch;
            }
            if (this.setID.getValue()) {
                this.teleportID = packet2.getTeleportId();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Send event) {
        if (this.mode.getValue() == Mode.Phase && this.scanPackets.getValue() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packetPlayer = event.getPacket();
            if (this.packets.contains(packetPlayer)) {
                this.packets.remove(packetPlayer);
            }
            else {
                event.setCanceled(true);
            }
        }
    }
    
    private double[] getMotion(final double speed) {
        float moveForward = Phase.mc.player.movementInput.moveForward;
        float moveStrafe = Phase.mc.player.movementInput.moveStrafe;
        float rotationYaw = Phase.mc.player.prevRotationYaw + (Phase.mc.player.rotationYaw - Phase.mc.player.prevRotationYaw) * Phase.mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        final double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[] { posX, posZ };
    }
    
    private boolean checkHitBoxes() {
        return !Phase.mc.world.getCollisionBoxes((Entity)Phase.mc.player, Phase.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }
    
    private boolean resetCounter(final int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }
    
    private void sendPackets(final double x, final double y, final double z, final boolean teleport) {
        final Vec3d vec = new Vec3d(x, y, z);
        final Vec3d position = Phase.mc.player.getPositionVector().add(vec);
        final Vec3d outOfBoundsVec = this.outOfBoundsVec(vec, position);
        this.packetSender((CPacketPlayer)new CPacketPlayer.Position(position.x, position.y, position.z, Phase.mc.player.onGround));
        if (this.invalidPacket.getValue()) {
            this.packetSender((CPacketPlayer)new CPacketPlayer.Position(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, Phase.mc.player.onGround));
        }
        if (this.setPos.getValue()) {
            Phase.mc.player.setPosition(position.x, position.y, position.z);
        }
        this.teleportPacket(position, teleport);
    }
    
    private void teleportPacket(final Vec3d pos, final boolean shouldTeleport) {
        if (shouldTeleport) {
            Phase.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(++this.teleportID));
            this.teleportmap.put(this.teleportID, new IDtime(pos, new Timer()));
        }
    }
    
    private Vec3d outOfBoundsVec(final Vec3d offset, final Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }
    
    private void packetSender(final CPacketPlayer packet) {
        this.packets.add(packet);
        Phase.mc.player.connection.sendPacket((Packet)packet);
    }
    
    private void clean() {
        this.teleportmap.clear();
        this.flightCounter = 0;
        if (this.resetID.getValue()) {
            this.teleportID = 0;
        }
        this.packets.clear();
    }
    
    static {
        Phase.INSTANCE = new Phase();
    }
    
    public static class IDtime
    {
        private final Vec3d pos;
        private final Timer timer;
        
        public IDtime(final Vec3d pos, final Timer timer) {
            this.pos = pos;
            (this.timer = timer).reset();
        }
        
        public Vec3d getPos() {
            return this.pos;
        }
        
        public Timer getTimer() {
            return this.timer;
        }
    }
    
    public enum Mode
    {
        Phase, 
        Packetfly, 
        Alternative;
    }
}
