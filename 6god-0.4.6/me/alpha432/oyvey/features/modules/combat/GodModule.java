//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import me.alpha432.oyvey.util.Util;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.alpha432.oyvey.event.events.PacketEvent;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class GodModule extends Module
{
    public Setting<Integer> rotations;
    public Setting<Boolean> rotate;
    public Setting<Boolean> render;
    public Setting<Boolean> antiIllegal;
    public Setting<Boolean> checkPos;
    public Setting<Boolean> oneDot15;
    public Setting<Boolean> entitycheck;
    public Setting<Integer> attacks;
    public Setting<Integer> delay;
    private float yaw;
    private float pitch;
    private boolean rotating;
    private int rotationPacketsSpoofed;
    private int highestID;
    
    public GodModule() {
        super("Booster", "gm", Category.COMBAT, true, false, false);
        this.rotations = (Setting<Integer>)this.register(new Setting("Spoofs", (T)20, (T)1, (T)20));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)false));
        this.antiIllegal = (Setting<Boolean>)this.register(new Setting("AntiIllegal", (T)true));
        this.checkPos = (Setting<Boolean>)this.register(new Setting("CheckPos", (T)true));
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("1.15+", (T)false));
        this.entitycheck = (Setting<Boolean>)this.register(new Setting("EntityCheck", (T)false));
        this.attacks = (Setting<Integer>)this.register(new Setting("Attacks", (T)1, (T)1, (T)10));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)50));
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.highestID = -100000;
    }
    
    @Override
    public void onToggle() {
        this.resetFields();
        if (GodModule.mc.world != null) {
            this.updateEntityID();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.render.getValue()) {
            for (final Entity entity : GodModule.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }
                entity.setCustomNameTag(String.valueOf(entity.entityId));
                entity.setAlwaysRenderNameTag(true);
            }
        }
    }
    
    @Override
    public void onLogout() {
        this.resetFields();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (GodModule.mc.player.getHeldItem(packet.hand).getItem() instanceof ItemEndCrystal) {
                if ((this.checkPos.getValue() && !BlockUtil.canPlaceCrystal(packet.position, this.entitycheck.getValue(), this.oneDot15.getValue())) || this.checkPlayers()) {
                    return;
                }
                this.updateEntityID();
                for (int i = 1; i < this.attacks.getValue(); ++i) {
                    this.attackID(packet.position, this.highestID + i);
                }
            }
        }
        if (event.getStage() == 0 && this.rotating && this.rotate.getValue() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet2 = event.getPacket();
            packet2.yaw = this.yaw;
            packet2.pitch = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }
    
    private void attackID(final BlockPos pos, final int id) {
        final Entity entity = GodModule.mc.world.getEntityByID(id);
        if (entity == null || entity instanceof EntityEnderCrystal) {
            final AttackThread attackThread = new AttackThread(id, pos, this.delay.getValue(), this);
            attackThread.start();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            this.checkID(event.getPacket().getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
            this.checkID(event.getPacket().getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnPlayer) {
            this.checkID(event.getPacket().getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnGlobalEntity) {
            this.checkID(event.getPacket().getEntityId());
        }
        else if (event.getPacket() instanceof SPacketSpawnPainting) {
            this.checkID(event.getPacket().getEntityID());
        }
        else if (event.getPacket() instanceof SPacketSpawnMob) {
            this.checkID(event.getPacket().getEntityID());
        }
    }
    
    private void checkID(final int id) {
        if (id > this.highestID) {
            this.highestID = id;
        }
    }
    
    public void updateEntityID() {
        for (final Entity entity : GodModule.mc.world.loadedEntityList) {
            if (entity.getEntityId() <= this.highestID) {
                continue;
            }
            this.highestID = entity.getEntityId();
        }
    }
    
    private boolean checkPlayers() {
        if (this.antiIllegal.getValue()) {
            for (final EntityPlayer player : GodModule.mc.world.playerEntities) {
                if (!this.checkItem(player.getHeldItemMainhand()) && !this.checkItem(player.getHeldItemOffhand())) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean checkItem(final ItemStack stack) {
        return stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemExpBottle || stack.getItem() == Items.STRING;
    }
    
    public void rotateTo(final BlockPos pos) {
        final float[] angle = MathUtil.calcAngle(GodModule.mc.player.getPositionEyes(GodModule.mc.getRenderPartialTicks()), new Vec3d((Vec3i)pos));
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.rotating = true;
    }
    
    private void resetFields() {
        this.rotating = false;
        this.highestID = -1000000;
    }
    
    public static class AttackThread extends Thread
    {
        private final BlockPos pos;
        private final int id;
        private final int delay;
        private final GodModule godModule;
        
        public AttackThread(final int idIn, final BlockPos posIn, final int delayIn, final GodModule godModuleIn) {
            this.id = idIn;
            this.pos = posIn;
            this.delay = delayIn;
            this.godModule = godModuleIn;
        }
        
        @Override
        public void run() {
            try {
                this.wait(this.delay);
                final CPacketUseEntity attack = new CPacketUseEntity();
                attack.entityId = this.id;
                attack.action = CPacketUseEntity.Action.ATTACK;
                this.godModule.rotateTo(this.pos.up());
                Util.mc.player.connection.sendPacket((Packet)attack);
                Util.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
