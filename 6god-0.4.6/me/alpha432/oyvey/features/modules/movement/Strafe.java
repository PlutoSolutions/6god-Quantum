//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Strafe extends Module
{
    private static Strafe INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> limiter;
    private final Setting<Boolean> inwater;
    private final Setting<Boolean> reversestep;
    private final Setting<Boolean> bhop2;
    private final Setting<Boolean> limiter2;
    private final Setting<Boolean> noLag;
    private final Setting<Integer> specialMoveSpeed;
    private final Setting<Integer> potionSpeed;
    private final Setting<Integer> potionSpeed2;
    private final Setting<Integer> dFactor;
    private final Setting<Integer> acceleration;
    private final Setting<Float> speedLimit;
    private final Setting<Float> speedLimit2;
    private final Setting<Integer> yOffset;
    private final Setting<Boolean> potion;
    private final Setting<Boolean> wait;
    private final Setting<Boolean> hopWait;
    private final Setting<Integer> startStage;
    private final Setting<Boolean> setPos;
    private final Setting<Boolean> setNull;
    private final Setting<Integer> setGroundLimit;
    private final Setting<Integer> groundFactor;
    private final Setting<Integer> step;
    private final Setting<Boolean> setGroundNoLag;
    private int stage;
    private double moveSpeed;
    private double lastDist;
    private int cooldownHops;
    private boolean waitForGround;
    private final Timer timer;
    private int hops;
    
    public Strafe() {
        super("Strafe", "AirControl etc.", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.NCP));
        this.limiter = (Setting<Boolean>)this.register(new Setting("SetGround", (T)true));
        this.inwater = (Setting<Boolean>)this.register(new Setting("AntiWater", (T)true));
        this.reversestep = (Setting<Boolean>)this.register(new Setting("AntiReverseStep", (T)true));
        this.bhop2 = (Setting<Boolean>)this.register(new Setting("Hop", (T)true));
        this.limiter2 = (Setting<Boolean>)this.register(new Setting("Bhop", (T)false));
        this.noLag = (Setting<Boolean>)this.register(new Setting("NoLag", (T)false));
        this.specialMoveSpeed = (Setting<Integer>)this.register(new Setting("Speed", (T)100, (T)0, (T)150));
        this.potionSpeed = (Setting<Integer>)this.register(new Setting("Speed1", (T)130, (T)0, (T)150));
        this.potionSpeed2 = (Setting<Integer>)this.register(new Setting("Speed2", (T)125, (T)0, (T)150));
        this.dFactor = (Setting<Integer>)this.register(new Setting("DFactor", (T)159, (T)100, (T)200));
        this.acceleration = (Setting<Integer>)this.register(new Setting("Accel", (T)2149, (T)1000, (T)2500));
        this.speedLimit = (Setting<Float>)this.register(new Setting("SpeedLimit", (T)35.0f, (T)20.0f, (T)60.0f));
        this.speedLimit2 = (Setting<Float>)this.register(new Setting("SpeedLimit2", (T)60.0f, (T)20.0f, (T)60.0f));
        this.yOffset = (Setting<Integer>)this.register(new Setting("YOffset", (T)400, (T)350, (T)500));
        this.potion = (Setting<Boolean>)this.register(new Setting("Potion", (T)false));
        this.wait = (Setting<Boolean>)this.register(new Setting("Wait", (T)true));
        this.hopWait = (Setting<Boolean>)this.register(new Setting("HopWait", (T)true));
        this.startStage = (Setting<Integer>)this.register(new Setting("Stage", (T)2, (T)0, (T)4));
        this.setPos = (Setting<Boolean>)this.register(new Setting("SetPos", (T)true));
        this.setNull = (Setting<Boolean>)this.register(new Setting("SetNull", (T)false));
        this.setGroundLimit = (Setting<Integer>)this.register(new Setting("GroundLimit", (T)138, (T)0, (T)1000));
        this.groundFactor = (Setting<Integer>)this.register(new Setting("GroundFactor", (T)13, (T)0, (T)50));
        this.step = (Setting<Integer>)this.register(new Setting("SetStep", (T)1, (T)0, (T)2, v -> this.mode.getValue() == Mode.CWalk));
        this.setGroundNoLag = (Setting<Boolean>)this.register(new Setting("NoGroundLag", (T)true));
        this.stage = 1;
        this.cooldownHops = 0;
        this.waitForGround = false;
        this.timer = new Timer();
        this.hops = 0;
        Strafe.INSTANCE = this;
    }
    
    public static Strafe getInstance() {
        if (Strafe.INSTANCE == null) {
            Strafe.INSTANCE = new Strafe();
        }
        return Strafe.INSTANCE;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }
        return baseSpeed;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        final BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    @Override
    public void onEnable() {
        if (!Strafe.mc.player.onGround) {
            this.waitForGround = true;
        }
        this.hops = 0;
        this.timer.reset();
        this.moveSpeed = getBaseMoveSpeed();
    }
    
    @Override
    public void onDisable() {
        this.hops = 0;
        this.moveSpeed = 0.0;
        this.stage = this.startStage.getValue();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.lastDist = Math.sqrt((Strafe.mc.player.posX - Strafe.mc.player.prevPosX) * (Strafe.mc.player.posX - Strafe.mc.player.prevPosX) + (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ) * (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ));
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (event.getStage() != 0 || this.shouldReturn()) {
            return;
        }
        if (Strafe.mc.player.isSneaking()) {
            return;
        }
        if (Strafe.mc.player.isInWater() && this.inwater.getValue()) {
            return;
        }
        if (OyVey.moduleManager.isModuleEnabled("ReverseStep") && this.reversestep.getValue()) {
            return;
        }
        if (!Strafe.mc.player.onGround) {
            if (this.wait.getValue() && this.waitForGround) {
                return;
            }
        }
        else {
            this.waitForGround = false;
        }
        if (this.mode.getValue() == Mode.NCP) {
            this.doNCP(event);
        }
        else if (this.mode.getValue() == Mode.CWalk) {
            float moveForward = Strafe.mc.player.movementInput.moveForward;
            float moveStrafe = Strafe.mc.player.movementInput.moveStrafe;
            float rotationYaw = Strafe.mc.player.rotationYaw;
            if (this.step.getValue() == 1) {
                Strafe.mc.player.stepHeight = 0.6f;
            }
            if (this.limiter2.getValue() && Strafe.mc.player.onGround && OyVey.speedManager.getSpeedKpH() < this.speedLimit2.getValue()) {
                this.stage = 2;
            }
            if (this.limiter.getValue() && round(Strafe.mc.player.posY - (int)Strafe.mc.player.posY, 3) == round(this.setGroundLimit.getValue() / 1000.0, 3) && (!this.setGroundNoLag.getValue() || EntityUtil.isEntityMoving((Entity)Strafe.mc.player))) {
                if (this.setNull.getValue()) {
                    Strafe.mc.player.motionY = 0.0;
                }
                else {
                    final EntityPlayerSP player = Strafe.mc.player;
                    player.motionY -= this.groundFactor.getValue() / 100.0;
                    event.setY(event.getY() - this.groundFactor.getValue() / 100.0);
                    if (this.setPos.getValue()) {
                        final EntityPlayerSP player2 = Strafe.mc.player;
                        player2.posY -= this.groundFactor.getValue() / 100.0;
                    }
                }
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = this.getMultiplier() * getBaseMoveSpeed() - 0.01;
            }
            else if (this.stage == 2 && EntityUtil.isMoving()) {
                this.stage = 3;
                Strafe.mc.player.motionY = this.yOffset.getValue() / 1000.0;
                event.setY(this.yOffset.getValue() / 1000.0);
                if (this.cooldownHops > 0) {
                    --this.cooldownHops;
                }
                ++this.hops;
                final double accel = (this.acceleration.getValue() == 2149) ? 2.149802 : (this.acceleration.getValue() / 1000.0);
                this.moveSpeed *= accel;
            }
            else if (this.stage == 3) {
                this.stage = 4;
                final double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                if (Strafe.mc.world.getCollisionBoxes((Entity)Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || (Strafe.mc.player.collidedVertically && this.stage > 0)) {
                    this.stage = (((!this.bhop2.getValue() || OyVey.speedManager.getSpeedKpH() < this.speedLimit.getValue()) && (Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f)) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / this.dFactor.getValue();
            }
            this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
            if (this.hopWait.getValue() && this.limiter2.getValue() && this.hops < 2) {
                this.moveSpeed = EntityUtil.getMaxSpeed();
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            }
            else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                    moveStrafe = 0.0f;
                }
                else if (moveStrafe <= -1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
                event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
            }
            if (this.step.getValue() == 2) {
                Strafe.mc.player.stepHeight = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                this.timer.reset();
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
    }
    
    private void doNCP(final MoveEvent event) {
        if (!this.limiter.getValue() && Strafe.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if (Strafe.mc.player.moveForward == 0.0f && Strafe.mc.player.moveStrafing == 0.0f) {
                    break;
                }
                if (!Strafe.mc.player.onGround) {
                    break;
                }
                if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }
                event.setY(Strafe.mc.player.motionY = motionY);
                this.moveSpeed *= 2.149;
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - getBaseMoveSpeed());
                break;
            }
            default: {
                if (Strafe.mc.world.getCollisionBoxes((Entity)Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || (Strafe.mc.player.collidedVertically && this.stage > 0)) {
                    this.stage = (((!this.bhop2.getValue() || OyVey.speedManager.getSpeedKpH() < this.speedLimit.getValue()) && (Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f)) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                break;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        double forward = Strafe.mc.player.movementInput.moveForward;
        double strafe = Strafe.mc.player.movementInput.moveStrafe;
        final double yaw = Strafe.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++this.stage;
    }
    
    private float getMultiplier() {
        float baseSpeed = this.specialMoveSpeed.getValue();
        if (this.potion.getValue() && Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
            baseSpeed = (float)((amplifier >= 2) ? this.potionSpeed2.getValue() : ((float)(int)this.potionSpeed.getValue()));
        }
        return baseSpeed / 100.0f;
    }
    
    private boolean shouldReturn() {
        return OyVey.moduleManager.isModuleEnabled("Freecam") || OyVey.moduleManager.isModuleEnabled("Packetfly") || OyVey.moduleManager.isModuleEnabled("ElytraFlight") || OyVey.moduleManager.isModuleEnabled("Phase");
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.noLag.getValue()) {
            this.stage = ((this.mode.getValue() == Mode.CWalk && (this.limiter2.getValue() || this.bhop2.getValue())) ? 1 : 4);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.None) {
            return null;
        }
        if (this.mode.getValue() == Mode.NCP) {
            return this.mode.currentEnumName().toUpperCase();
        }
        return this.mode.currentEnumName();
    }
    
    public enum Mode
    {
        None, 
        NCP, 
        CWalk, 
        Simple;
    }
}
