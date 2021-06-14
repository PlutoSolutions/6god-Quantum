//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.OyVey;
import net.minecraft.util.math.Vec3d;
import java.util.Objects;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.Collection;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.alpha432.oyvey.mixin.mixins.accessors.AccessorCPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import me.alpha432.oyvey.event.events.PacketEvent;
import java.util.Iterator;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.alpha432.oyvey.util.ItemUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class CrystalAura extends Module
{
    private final Setting<Settings> setting;
    private final Setting<Integer> placeDelay;
    private final Setting<Float> placeRange;
    private final Setting<Integer> breakDelay;
    public Setting<Float> breakRange;
    private final Setting<Boolean> cancelcrystal;
    private final Setting<InfoMode> infomode;
    private final Setting<Boolean> offhandS;
    public Setting<Boolean> render;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> text;
    public Setting<Boolean> customOutline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private final Setting<Float> range;
    public Setting<Rotate> rotate;
    public Setting<Integer> rotations;
    public Setting<Boolean> rotateFirst;
    public Setting<Raytrace> raytrace;
    public Setting<Float> placetrace;
    public Setting<Float> breaktrace;
    private final Setting<Float> breakWallRange;
    private final Setting<Float> minDamage;
    private final Setting<Float> maxSelf;
    private final Setting<Float> lethalMult;
    private final Setting<Float> armorScale;
    private final Setting<Boolean> second;
    private final Setting<Boolean> autoSwitch;
    private final List<BlockPos> placedList;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer renderTimer;
    private int rotationPacketsSpoofed;
    public static EntityPlayer currentTarget;
    private BlockPos renderPos;
    private double renderDamage;
    private BlockPos placePos;
    private boolean offHand;
    public boolean rotating;
    private float pitch;
    private float yaw;
    private boolean offhand;
    
    public CrystalAura() {
        super("CrystalAura", "ca", Category.COMBAT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.Place));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)200, v -> this.setting.getValue() == Settings.Place));
        this.placeRange = (Setting<Float>)this.register(new Setting("Range", (T)6.0f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.Place));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("BDelay", (T)0, (T)0, (T)200, v -> this.setting.getValue() == Settings.Break));
        this.breakRange = (Setting<Float>)this.register(new Setting("BRange", (T)6.0f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.Break));
        this.cancelcrystal = (Setting<Boolean>)this.register(new Setting("SetDead", (T)false, v -> this.setting.getValue() == Settings.Break));
        this.infomode = (Setting<InfoMode>)this.register(new Setting("Info", (T)InfoMode.Target, v -> this.setting.getValue() == Settings.Render));
        this.offhandS = (Setting<Boolean>)this.register(new Setting("OffhandSwing", (T)true, v -> this.setting.getValue() == Settings.Render));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true, v -> this.setting.getValue() == Settings.Render));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)80, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)120, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)120, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("ColorSync", (T)false, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)30, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)0.1f, (T)0.1f, (T)5.0f, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.outline.getValue()));
        this.text = (Setting<Boolean>)this.register(new Setting("DamageText", (T)true, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", (T)false, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.range = (Setting<Float>)this.register(new Setting("TargetRange", (T)9.5f, (T)0.0f, (T)16.0f, v -> this.setting.getValue() == Settings.Misc));
        this.rotate = (Setting<Rotate>)this.register(new Setting("Rotate", (T)Rotate.OFF, v -> this.setting.getValue() == Settings.Misc));
        this.rotations = (Setting<Integer>)this.register(new Setting("Spoofs", (T)1, (T)1, (T)20, v -> this.setting.getValue() == Settings.Misc && this.rotate.getValue() != Rotate.OFF));
        this.rotateFirst = (Setting<Boolean>)this.register(new Setting("FirstRotation", (T)false, v -> this.setting.getValue() == Settings.Misc && this.rotate.getValue() != Rotate.OFF));
        this.raytrace = (Setting<Raytrace>)this.register(new Setting("Raytrace", (T)Raytrace.None, v -> this.setting.getValue() == Settings.Misc));
        this.placetrace = (Setting<Float>)this.register(new Setting("Placetrace", (T)5.5f, (T)0.0f, (T)10.0f, v -> this.setting.getValue() == Settings.Misc && this.raytrace.getValue() != Raytrace.None && this.raytrace.getValue() != Raytrace.Break));
        this.breaktrace = (Setting<Float>)this.register(new Setting("Breaktrace", (T)5.5f, (T)0.0f, (T)10.0f, v -> this.setting.getValue() == Settings.Misc && this.raytrace.getValue() != Raytrace.None && this.raytrace.getValue() != Raytrace.Place));
        this.breakWallRange = (Setting<Float>)this.register(new Setting("WallRange", (T)4.5f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.Misc));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", (T)0.7f, (T)0.0f, (T)30.0f, v -> this.setting.getValue() == Settings.Misc));
        this.maxSelf = (Setting<Float>)this.register(new Setting("MaxSelf", (T)18.5f, (T)0.0f, (T)36.0f, v -> this.setting.getValue() == Settings.Misc));
        this.lethalMult = (Setting<Float>)this.register(new Setting("LethalMult", (T)0.0f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.Misc));
        this.armorScale = (Setting<Float>)this.register(new Setting("ArmorBreak", (T)100.0f, (T)0.0f, (T)100.0f, v -> this.setting.getValue() == Settings.Misc));
        this.second = (Setting<Boolean>)this.register(new Setting("Second", (T)false, v -> this.setting.getValue() == Settings.Misc));
        this.autoSwitch = (Setting<Boolean>)this.register(new Setting("AutoSwitch", (T)false, v -> this.setting.getValue() == Settings.Misc));
        this.placedList = new ArrayList<BlockPos>();
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.renderTimer = new Timer();
        this.rotationPacketsSpoofed = 0;
        this.renderPos = null;
        this.renderDamage = 0.0;
        this.placePos = null;
        this.offHand = false;
        this.rotating = false;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }
    
    @Override
    public void onToggle() {
        this.placedList.clear();
        this.breakTimer.reset();
        this.placeTimer.reset();
        this.renderTimer.reset();
        CrystalAura.currentTarget = null;
        this.renderPos = null;
        this.offhand = false;
        this.rotating = false;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.isNull()) {
            return;
        }
        if (this.renderTimer.passedMs(500L)) {
            this.placedList.clear();
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.offhand = (((ItemStack)CrystalAura.mc.player.inventory.offHandInventory.get(0)).getItem() == Items.END_CRYSTAL);
        CrystalAura.currentTarget = EntityUtil.getClosestPlayer(this.range.getValue());
        if (CrystalAura.currentTarget == null) {
            return;
        }
        this.doPlace();
        if (event.phase == TickEvent.Phase.START) {
            this.doBreak();
        }
    }
    
    private void doBreak() {
        Entity crystal = null;
        double maxDamage = 0.5;
        for (int size = CrystalAura.mc.world.loadedEntityList.size(), i = 0; i < size; ++i) {
            final Entity entity = CrystalAura.mc.world.loadedEntityList.get(i);
            if (entity instanceof EntityEnderCrystal && CrystalAura.mc.player.getDistance(entity) < (CrystalAura.mc.player.canEntityBeSeen(entity) ? this.breakRange.getValue() : this.breakWallRange.getValue())) {
                final float targetDamage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase)CrystalAura.currentTarget);
                if (targetDamage > this.minDamage.getValue() || targetDamage * this.lethalMult.getValue() > CrystalAura.currentTarget.getHealth() + CrystalAura.currentTarget.getAbsorptionAmount() || ItemUtil.isArmorUnderPercent(CrystalAura.currentTarget, this.armorScale.getValue())) {
                    final float selfDamage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase)CrystalAura.mc.player);
                    if (selfDamage <= this.maxSelf.getValue() && selfDamage + 2.0f <= CrystalAura.mc.player.getHealth() + CrystalAura.mc.player.getAbsorptionAmount() && selfDamage < targetDamage) {
                        if (maxDamage <= targetDamage) {
                            maxDamage = targetDamage;
                            crystal = entity;
                        }
                    }
                }
            }
        }
        if (crystal != null && this.breakTimer.passedMs(this.breakDelay.getValue())) {
            CrystalAura.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(crystal));
            CrystalAura.mc.player.swingArm(((boolean)this.offhandS.getValue()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            this.breakTimer.reset();
        }
    }
    
    private void doPlace() {
        BlockPos placePos = null;
        double maxDamage = 0.5;
        final List<BlockPos> sphere = BlockUtil.getSphereRealth(this.placeRange.getValue(), true);
        for (int size = sphere.size(), i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (BlockUtil.canPlaceCrystalRealth(pos, this.second.getValue())) {
                final float targetDamage = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase)CrystalAura.currentTarget);
                if (targetDamage > this.minDamage.getValue() || targetDamage * this.lethalMult.getValue() > CrystalAura.currentTarget.getHealth() + CrystalAura.currentTarget.getAbsorptionAmount() || ItemUtil.isArmorUnderPercent(CrystalAura.currentTarget, this.armorScale.getValue())) {
                    final float selfDamage = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase)CrystalAura.mc.player);
                    if (selfDamage <= this.maxSelf.getValue() && selfDamage + 2.0f <= CrystalAura.mc.player.getHealth() + CrystalAura.mc.player.getAbsorptionAmount() && selfDamage < targetDamage) {
                        if (maxDamage <= targetDamage) {
                            maxDamage = targetDamage;
                            placePos = pos;
                            this.renderPos = pos;
                            this.renderDamage = targetDamage;
                        }
                    }
                }
            }
        }
        boolean flag = false;
        if (!this.offhand && CrystalAura.mc.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
            flag = true;
            if (!this.autoSwitch.getValue() || (CrystalAura.mc.player.inventory.getCurrentItem().getItem() == Items.GOLDEN_APPLE && CrystalAura.mc.player.isHandActive())) {
                return;
            }
        }
        if (placePos != null) {
            if (this.placeTimer.passedMs(this.placeDelay.getValue())) {
                if (flag) {
                    final int slot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
                    if (slot == -1) {
                        return;
                    }
                    CrystalAura.mc.player.inventory.currentItem = slot;
                }
                this.placedList.add(placePos);
                CrystalAura.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                this.placeTimer.reset();
            }
            this.renderPos = placePos;
        }
        for (final BlockPos pos2 : BlockUtil.possiblePlacePositionsCa(this.placeRange.getValue())) {
            if (!BlockUtil.rayTracePlaceCheck(pos2, (this.raytrace.getValue() == Raytrace.Place || this.raytrace.getValue() == Raytrace.Both) && AutoCrystal.mc.player.getDistanceSq(pos2) > MathUtil.square(this.placetrace.getValue()), 1.0f)) {
                continue;
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = event.getPacket();
            if (packet.getType() == 51 && this.placedList.contains(new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ()))) {
                final AccessorCPacketUseEntity use = (AccessorCPacketUseEntity)new CPacketUseEntity();
                use.setEntityId(packet.getEntityID());
                use.setAction(CPacketUseEntity.Action.ATTACK);
                CrystalAura.mc.getConnection().sendPacket((Packet)use);
                CrystalAura.mc.player.swingArm(((boolean)this.offhandS.getValue()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                this.breakTimer.reset();
                return;
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet2 = event.getPacket();
            if (packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                final SPacketSoundEffect sPacketSoundEffect;
                new ArrayList(CrystalAura.mc.world.loadedEntityList).forEach(e -> {
                    if (e instanceof EntityEnderCrystal && e.getDistanceSq(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) < 36.0) {
                        e.setDead();
                    }
                });
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.rotate.getValue() != Rotate.OFF && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet2 = event.getPacket();
            packet2.yaw = this.yaw;
            packet2.pitch = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
        BlockPos pos = null;
        CPacketUseEntity packet3 = null;
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            pos = packet3.getEntityFromWorld((World)AutoCrystal.mc.world).getPosition();
        }
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            final EntityEnderCrystal crystal = (EntityEnderCrystal)packet3.getEntityFromWorld((World)AutoCrystal.mc.world);
            if (EntityUtil.isCrystalAtFeet(crystal, this.range.getValue()) && pos != null) {
                this.rotateToPos(pos);
                BlockUtil.placeCrystalOnBlock2(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, false);
            }
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal && this.cancelcrystal.getValue()) {
            Objects.requireNonNull(packet3.getEntityFromWorld((World)AutoCrystal.mc.world)).setDead();
            AutoCrystal.mc.world.removeEntityFromWorld(packet3.entityId);
        }
    }
    
    private void rotateToPos(final BlockPos pos) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case Place:
            case All: {
                final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(CrystalAura.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
                if (this.rotate.getValue() != Rotate.OFF) {
                    OyVey.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
                break;
            }
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.renderPos != null && this.render.getValue() && (this.box.getValue() || this.text.getValue() || this.outline.getValue())) {
            RenderUtil.drawBoxESP(this.renderPos, ((boolean)this.colorSync.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), ((boolean)this.colorSync.getValue()) ? this.getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            if (this.text.getValue()) {
                RenderUtil.drawText(this.renderPos, ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "");
            }
        }
    }
    
    public Color getCurrentColor() {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }
    
    @Override
    public String getDisplayInfo() {
        if (CrystalAura.currentTarget != null) {
            if (this.infomode.getValue() == InfoMode.Target) {
                return CrystalAura.currentTarget.getName();
            }
            if (this.infomode.getValue() == InfoMode.Damage) {
                return ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "";
            }
            if (this.infomode.getValue() == InfoMode.Both) {
                return CrystalAura.currentTarget.getName() + ", " + ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "";
            }
        }
        return null;
    }
    
    private boolean isValid(final Entity entity) {
        return entity != null && AutoCrystal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breakRange.getValue()) && (this.raytrace.getValue() == Raytrace.None || this.raytrace.getValue() == Raytrace.Place || AutoCrystal.mc.player.canEntityBeSeen(entity) || (!AutoCrystal.mc.player.canEntityBeSeen(entity) && AutoCrystal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breaktrace.getValue())));
    }
    
    public enum Settings
    {
        Place, 
        Break, 
        Render, 
        Misc;
    }
    
    public enum InfoMode
    {
        Target, 
        Damage, 
        Both;
    }
    
    public enum Rotate
    {
        OFF, 
        Place, 
        Break, 
        All;
    }
    
    public enum Raytrace
    {
        None, 
        Place, 
        Break, 
        Both;
    }
}
