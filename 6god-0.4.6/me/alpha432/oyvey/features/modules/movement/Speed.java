//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.util.MovementInput;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.event.events.ClientEvent;
import java.util.Random;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.EntityUtil;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Speed extends Module
{
    private static Speed INSTANCE;
    public Setting<Mode> mode;
    public Setting<Boolean> strafeJump;
    public Setting<Boolean> noShake;
    public Setting<Boolean> useTimer;
    public Setting<Double> zeroSpeed;
    public Setting<Double> speed;
    public Setting<Double> blocked;
    public Setting<Double> unblocked;
    public double startY;
    public boolean antiShake;
    public double minY;
    public boolean changeY;
    private double highChainVal;
    private double lowChainVal;
    private boolean oneTime;
    private double bounceHeight;
    private float move;
    private int vanillaCounter;
    
    public Speed() {
        super("Speed", "Makes you faster", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.INSTANT));
        this.strafeJump = (Setting<Boolean>)this.register(new Setting("Jump", (T)false, v -> this.mode.getValue() == Mode.INSTANT));
        this.noShake = (Setting<Boolean>)this.register(new Setting("NoShake", (T)true, v -> this.mode.getValue() != Mode.INSTANT));
        this.useTimer = (Setting<Boolean>)this.register(new Setting("UseTimer", (T)false, v -> this.mode.getValue() != Mode.INSTANT));
        this.zeroSpeed = (Setting<Double>)this.register(new Setting("0-Speed", (T)0.0, (T)0.0, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)10.0, (T)0.1, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.blocked = (Setting<Double>)this.register(new Setting("Blocked", (T)10.0, (T)0.0, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.unblocked = (Setting<Double>)this.register(new Setting("Unblocked", (T)10.0, (T)0.0, (T)100.0, v -> this.mode.getValue() == Mode.VANILLA));
        this.startY = 0.0;
        this.antiShake = false;
        this.minY = 0.0;
        this.changeY = false;
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.oneTime = false;
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        this.vanillaCounter = 0;
        this.setInstance();
    }
    
    public static Speed getInstance() {
        if (Speed.INSTANCE == null) {
            Speed.INSTANCE = new Speed();
        }
        return Speed.INSTANCE;
    }
    
    private void setInstance() {
        Speed.INSTANCE = this;
    }
    
    private boolean shouldReturn() {
        return OyVey.moduleManager.isModuleEnabled("Freecam") || OyVey.moduleManager.isModuleEnabled("ElytraFlight");
    }
    
    @Override
    public void onUpdate() {
        if (this.shouldReturn() || Speed.mc.player.isSneaking() || Speed.mc.player.isInWater() || Speed.mc.player.isInLava()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                this.doBoost();
                break;
            }
            case ACCEL: {
                this.doAccel();
                break;
            }
            case ONGROUND: {
                this.doOnground();
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() != Mode.VANILLA || Feature.nullCheck()) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                this.vanillaCounter = (this.vanilla() ? (++this.vanillaCounter) : 0);
                if (this.vanillaCounter != 4) {
                    break;
                }
                this.changeY = true;
                this.minY = Speed.mc.player.getEntityBoundingBox().minY + (Speed.mc.world.getBlockState(Speed.mc.player.getPosition()).getMaterial().blocksMovement() ? (-this.blocked.getValue() / 10.0) : (this.unblocked.getValue() / 10.0)) + this.getJumpBoostModifier();
            }
            case 1: {
                if (this.vanillaCounter == 3) {
                    final EntityPlayerSP player = Speed.mc.player;
                    player.motionX *= this.zeroSpeed.getValue() / 10.0;
                    final EntityPlayerSP player2 = Speed.mc.player;
                    player2.motionZ *= this.zeroSpeed.getValue() / 10.0;
                    break;
                }
                if (this.vanillaCounter != 4) {
                    break;
                }
                final EntityPlayerSP player3 = Speed.mc.player;
                player3.motionX /= this.speed.getValue() / 10.0;
                final EntityPlayerSP player4 = Speed.mc.player;
                player4.motionZ /= this.speed.getValue() / 10.0;
                this.vanillaCounter = 2;
                break;
            }
        }
    }
    
    private double getJumpBoostModifier() {
        double boost = 0.0;
        if (Speed.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            final int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier();
            boost *= 1.0 + 0.2 * amplifier;
        }
        return boost;
    }
    
    private boolean vanillaCheck() {
        if (Speed.mc.player.onGround) {}
        return false;
    }
    
    private boolean vanilla() {
        return Speed.mc.player.onGround;
    }
    
    private void doBoost() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.player.onGround) {
            this.startY = Speed.mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.player) && !Speed.mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.getValue() && Speed.mc.player.getRidingEntity() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.player.posY >= this.startY + this.bounceHeight) {
                Speed.mc.player.motionY = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.15f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.2f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.225f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.25f;
                }
                if (this.lowChainVal >= 7.0) {
                    this.move = 0.27895f;
                }
                if (this.useTimer.getValue()) {
                    OyVey.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.player.posY == this.startY) {
                Speed.mc.player.motionY = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.325f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.4f;
                }
                if (this.highChainVal >= 6.0) {
                    this.move = 0.43395f;
                }
                if (this.useTimer.getValue()) {
                    if (rnd) {
                        OyVey.timerManager.setTimer(1.3f);
                    }
                    else {
                        OyVey.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.player);
        }
        else {
            if (this.oneTime) {
                Speed.mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.antiShake = false;
            this.speedOff();
        }
    }
    
    private void doAccel() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.player.onGround) {
            this.startY = Speed.mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.player) && !Speed.mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.getValue() && Speed.mc.player.getRidingEntity() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.player.posY >= this.startY + this.bounceHeight) {
                Speed.mc.player.motionY = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.getValue()) {
                    OyVey.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.player.posY == this.startY) {
                Speed.mc.player.motionY = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.175f;
                }
                if (this.useTimer.getValue()) {
                    if (rnd) {
                        OyVey.timerManager.setTimer(1.3f);
                    }
                    else {
                        OyVey.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.player);
        }
        else {
            if (this.oneTime) {
                Speed.mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }
    
    private void doOnground() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.player.onGround) {
            this.startY = Speed.mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.player) && !Speed.mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.getValue() && Speed.mc.player.getRidingEntity() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.player.posY >= this.startY + this.bounceHeight) {
                Speed.mc.player.motionY = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.getValue()) {
                    OyVey.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.player.posY == this.startY) {
                Speed.mc.player.motionY = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.2f;
                }
                if (this.useTimer.getValue()) {
                    if (rnd) {
                        OyVey.timerManager.setTimer(1.3f);
                    }
                    else {
                        OyVey.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.player);
        }
        else {
            if (this.oneTime) {
                Speed.mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }
    
    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.ONGROUND || this.mode.getValue() == Mode.BOOST) {
            Speed.mc.player.motionY = -0.1;
        }
        this.changeY = false;
        OyVey.timerManager.setTimer(1.0f);
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.antiShake = false;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().equals(this.mode) && this.mode.getPlannedValue() == Mode.INSTANT) {
            Speed.mc.player.motionY = -0.1;
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    @SubscribeEvent
    public void onMode(final MoveEvent event) {
        if (!this.shouldReturn() && event.getStage() == 0 && this.mode.getValue() == Mode.INSTANT && !Feature.nullCheck() && !Speed.mc.player.isSneaking() && !Speed.mc.player.isInWater() && !Speed.mc.player.isInLava() && (Speed.mc.player.movementInput.moveForward != 0.0f || Speed.mc.player.movementInput.moveStrafe != 0.0f)) {
            if (Speed.mc.player.onGround && this.strafeJump.getValue()) {
                event.setY(Speed.mc.player.motionY = 0.4);
            }
            final MovementInput movementInput = Speed.mc.player.movementInput;
            float moveForward = movementInput.moveForward;
            float moveStrafe = movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (moveForward == 0.0 && moveStrafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            else {
                if (moveForward != 0.0) {
                    if (moveStrafe > 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? -45 : 45);
                    }
                    else if (moveStrafe < 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    if (moveForward != 0.0f) {
                        moveForward = ((moveForward > 0.0) ? 1.0f : -1.0f);
                    }
                }
                moveStrafe = ((moveStrafe == 0.0f) ? moveStrafe : ((moveStrafe > 0.0) ? 1.0f : -1.0f));
                event.setX(moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ(moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }
    
    private void speedOff() {
        final float yaw = (float)Math.toRadians(Speed.mc.player.rotationYaw);
        if (BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player)) {
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindSneak.isKeyDown() && Speed.mc.player.onGround) {
                final EntityPlayerSP player = Speed.mc.player;
                player.motionX -= MathUtil.sin(yaw) * 0.15;
                final EntityPlayerSP player2 = Speed.mc.player;
                player2.motionZ += MathUtil.cos(yaw) * 0.15;
            }
        }
        else if (Speed.mc.player.collidedHorizontally) {
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindSneak.isKeyDown() && Speed.mc.player.onGround) {
                final EntityPlayerSP player3 = Speed.mc.player;
                player3.motionX -= MathUtil.sin(yaw) * 0.03;
                final EntityPlayerSP player4 = Speed.mc.player;
                player4.motionZ += MathUtil.cos(yaw) * 0.03;
            }
        }
        else if (!BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindSneak.isKeyDown() && Speed.mc.player.onGround) {
                final EntityPlayerSP player5 = Speed.mc.player;
                player5.motionX -= MathUtil.sin(yaw) * 0.03;
                final EntityPlayerSP player6 = Speed.mc.player;
                player6.motionZ += MathUtil.cos(yaw) * 0.03;
            }
        }
        else {
            Speed.mc.player.motionX = 0.0;
            Speed.mc.player.motionZ = 0.0;
        }
    }
    
    static {
        Speed.INSTANCE = new Speed();
    }
    
    public enum Mode
    {
        INSTANT, 
        ONGROUND, 
        ACCEL, 
        BOOST, 
        VANILLA;
    }
}
