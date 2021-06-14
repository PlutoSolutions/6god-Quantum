//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.world.World;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Freecam extends Module
{
    public Setting<Double> speed;
    public Setting<Boolean> view;
    public Setting<Boolean> packet;
    public Setting<Boolean> disable;
    private static Freecam INSTANCE;
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;
    
    public Freecam() {
        super("Freecam", "Look around freely.", Category.PLAYER, true, false, false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (T)0.5, (T)0.1, (T)5.0));
        this.view = (Setting<Boolean>)this.register(new Setting("3D", (T)false));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", (T)true));
        this.disable = (Setting<Boolean>)this.register(new Setting("Logout/Off", (T)true));
        this.setInstance();
    }
    
    private void setInstance() {
        Freecam.INSTANCE = this;
    }
    
    public static Freecam getInstance() {
        if (Freecam.INSTANCE == null) {
            Freecam.INSTANCE = new Freecam();
        }
        return Freecam.INSTANCE;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck()) {
            this.oldBoundingBox = Freecam.mc.player.getEntityBoundingBox();
            Freecam.mc.player.setEntityBoundingBox(new AxisAlignedBB(Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ, Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ));
            if (Freecam.mc.player.getRidingEntity() != null) {
                this.riding = Freecam.mc.player.getRidingEntity();
                Freecam.mc.player.dismountRidingEntity();
            }
            (this.entity = new EntityOtherPlayerMP((World)Freecam.mc.world, Freecam.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Freecam.mc.player);
            this.entity.rotationYaw = Freecam.mc.player.rotationYaw;
            this.entity.rotationYawHead = Freecam.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Freecam.mc.player.inventory);
            Freecam.mc.world.addEntityToWorld(69420, (Entity)this.entity);
            this.position = Freecam.mc.player.getPositionVector();
            this.yaw = Freecam.mc.player.rotationYaw;
            this.pitch = Freecam.mc.player.rotationPitch;
            Freecam.mc.player.noClip = true;
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            Freecam.mc.player.setEntityBoundingBox(this.oldBoundingBox);
            if (this.riding != null) {
                Freecam.mc.player.startRiding(this.riding, true);
            }
            if (this.entity != null) {
                Freecam.mc.world.removeEntity((Entity)this.entity);
            }
            if (this.position != null) {
                Freecam.mc.player.setPosition(this.position.x, this.position.y, this.position.z);
            }
            Freecam.mc.player.rotationYaw = this.yaw;
            Freecam.mc.player.rotationPitch = this.pitch;
            Freecam.mc.player.noClip = false;
        }
    }
    
    @Override
    public void onUpdate() {
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.setVelocity(0.0, 0.0, 0.0);
        Freecam.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        final double[] dir = MathUtil.directionSpeed(this.speed.getValue());
        if (Freecam.mc.player.movementInput.moveStrafe != 0.0f || Freecam.mc.player.movementInput.moveForward != 0.0f) {
            Freecam.mc.player.motionX = dir[0];
            Freecam.mc.player.motionZ = dir[1];
        }
        else {
            Freecam.mc.player.motionX = 0.0;
            Freecam.mc.player.motionZ = 0.0;
        }
        Freecam.mc.player.setSprinting(false);
        if (this.view.getValue() && !Freecam.mc.gameSettings.keyBindSneak.isKeyDown() && !Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY = this.speed.getValue() * -MathUtil.degToRad(Freecam.mc.player.rotationPitch) * Freecam.mc.player.movementInput.moveForward;
        }
        if (Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player;
            final EntityPlayerSP player = player = Freecam.mc.player;
            player.motionY += this.speed.getValue();
        }
        if (Freecam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2;
            final EntityPlayerSP player2 = player2 = Freecam.mc.player;
            player2.motionY -= this.speed.getValue();
        }
    }
    
    @Override
    public void onLogout() {
        if (this.disable.getValue()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput)) {
            event.setCanceled(true);
        }
    }
    
    static {
        Freecam.INSTANCE = new Freecam();
    }
}
