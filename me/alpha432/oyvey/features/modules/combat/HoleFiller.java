//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.TestUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.Consumer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Collection;
import java.util.Collections;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class HoleFiller extends Module
{
    private final Setting<Boolean> flatten;
    private final Setting<Settings> setting;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Boolean> packet;
    public Setting<PlaceMode> placeMode;
    private final Setting<Boolean> autoDisable;
    private final Setting<Double> smartRange;
    private final Setting<Boolean> onlySafe;
    private final Setting<Boolean> midSafeHoles;
    private final Setting<Double> range;
    private final Setting<Boolean> rotate;
    private final Vec3d[] offsetsDefault;
    private EntityPlayer target;
    private int offsetStep;
    private int oldSlot;
    private boolean placing;
    private static final BlockPos[] surroundOffset;
    private static HoleFiller INSTANCE;
    private final Timer offTimer;
    private final Timer timer;
    private boolean isSneaking;
    private boolean hasOffhand;
    private final Map<BlockPos, Integer> retries;
    private final Timer retryTimer;
    private int blocksThisTick;
    private ArrayList<BlockPos> holes;
    private int trie;
    
    public HoleFiller() {
        super("HoleFill", "hf", Category.COMBAT, true, false, true);
        this.flatten = (Setting<Boolean>)this.register(new Setting("FeetFloor", (T)false));
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.PLACE));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)250, v -> this.setting.getValue() == Settings.PLACE || this.flatten.getValue()));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("BlocksPerTick", (T)20, (T)1, (T)20, v -> this.setting.getValue() == Settings.PLACE || this.flatten.getValue()));
        this.packet = (Setting<Boolean>)this.register(new Setting("PacketPlace", (T)false, v -> this.setting.getValue() == Settings.PLACE || this.flatten.getValue()));
        this.placeMode = (Setting<PlaceMode>)this.register(new Setting("PlaceMode", (T)PlaceMode.ALL, v -> this.setting.getValue() == Settings.MISC && !this.flatten.getValue()));
        this.autoDisable = (Setting<Boolean>)this.register(new Setting("AutoDisable", (T)true, v -> this.setting.getValue() == Settings.MISC || this.flatten.getValue()));
        this.smartRange = (Setting<Double>)this.register(new Setting("SmartRange", (T)6.0, (T)0.0, (T)10.0, v -> this.placeMode.getValue() == PlaceMode.SMART && this.setting.getValue() == Settings.MISC && !this.flatten.getValue()));
        this.onlySafe = (Setting<Boolean>)this.register(new Setting("OnlySafe", (T)true, v -> this.setting.getValue() == Settings.MISC && !this.flatten.getValue()));
        this.midSafeHoles = (Setting<Boolean>)this.register(new Setting("SemiSafe", (T)false, v -> this.setting.getValue() == Settings.MISC && !this.flatten.getValue()));
        this.range = (Setting<Double>)this.register(new Setting("PlaceRange", (T)6.0, (T)0.0, (T)6.0, v -> this.setting.getValue() == Settings.MISC && !this.flatten.getValue()));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true, v -> this.setting.getValue() == Settings.MISC || this.flatten.getValue()));
        this.offsetsDefault = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0) };
        this.offsetStep = 0;
        this.oldSlot = -1;
        this.placing = false;
        this.hasOffhand = false;
        this.offTimer = new Timer();
        this.timer = new Timer();
        this.blocksThisTick = 0;
        this.retries = new HashMap<BlockPos, Integer>();
        this.retryTimer = new Timer();
        this.holes = new ArrayList<BlockPos>();
        this.setInstance();
    }
    
    public static HoleFiller getInstance() {
        if (HoleFiller.INSTANCE == null) {
            HoleFiller.INSTANCE = new HoleFiller();
        }
        return HoleFiller.INSTANCE;
    }
    
    private void setInstance() {
        HoleFiller.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
        this.oldSlot = HoleFiller.mc.player.inventory.currentItem;
        this.offTimer.reset();
        this.trie = 0;
    }
    
    @Override
    public void onTick() {
        if (this.isOn() && (this.blocksPerTick.getValue() != 1 || !this.rotate.getValue())) {
            this.doHoleFill();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.isOn() && event.getStage() == 0 && this.blocksPerTick.getValue() == 1 && this.rotate.getValue()) {
            this.doHoleFill();
        }
    }
    
    @Override
    public void onDisable() {
        this.retries.clear();
    }
    
    @Override
    public void onUpdate() {
        if (this.flatten.getValue()) {
            final EntityPlayer closest_target = this.findClosestTarget();
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            if (closest_target == null) {
                this.disable();
                return;
            }
            final List<Vec3d> place_targets = new ArrayList<Vec3d>();
            Collections.addAll(place_targets, this.offsetsDefault);
            int blocks_placed = 0;
            while (blocks_placed < this.blocksPerTick.getValue()) {
                if (this.offsetStep >= place_targets.size()) {
                    this.offsetStep = 0;
                    break;
                }
                this.placing = true;
                final BlockPos offset_pos = new BlockPos((Vec3d)place_targets.get(this.offsetStep));
                final BlockPos target_pos = new BlockPos(closest_target.getPositionVector()).down().add(offset_pos.getX(), offset_pos.getY(), offset_pos.getZ());
                boolean should_try_place = HoleFiller.mc.world.getBlockState(target_pos).getMaterial().isReplaceable();
                for (final Entity entity : HoleFiller.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(target_pos))) {
                    if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                        should_try_place = false;
                        break;
                    }
                }
                if (should_try_place) {
                    this.place(target_pos, obbySlot, this.oldSlot);
                    ++blocks_placed;
                }
                ++this.offsetStep;
                this.placing = false;
            }
            if (this.autoDisable.getValue()) {
                this.disable();
            }
        }
    }
    
    private void place(final BlockPos pos, final int slot, final int oldSlot) {
        HoleFiller.mc.player.inventory.currentItem = slot;
        HoleFiller.mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), HoleFiller.mc.player.isSneaking());
        HoleFiller.mc.player.inventory.currentItem = oldSlot;
        HoleFiller.mc.playerController.updateController();
    }
    
    private EntityPlayer findClosestTarget() {
        if (!this.flatten.getValue()) {
            return null;
        }
        if (HoleFiller.mc.world.playerEntities.isEmpty()) {
            return null;
        }
        EntityPlayer closestTarget = null;
        for (final EntityPlayer target : HoleFiller.mc.world.playerEntities) {
            if (target != HoleFiller.mc.player) {
                if (!target.isEntityAlive()) {
                    continue;
                }
                if (OyVey.friendManager.isFriend(target.getName())) {
                    continue;
                }
                if (target.getHealth() <= 0.0f) {
                    continue;
                }
                if (HoleFiller.mc.player.getDistance((Entity)target) > 5.0f) {
                    continue;
                }
                if (closestTarget != null && HoleFiller.mc.player.getDistance((Entity)target) > HoleFiller.mc.player.getDistance((Entity)closestTarget)) {
                    continue;
                }
                closestTarget = target;
            }
        }
        return closestTarget;
    }
    
    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        this.holes = new ArrayList<BlockPos>();
        final Iterable<BlockPos> blocks = (Iterable<BlockPos>)BlockPos.getAllInBox(HoleFiller.mc.player.getPosition().add(-this.range.getValue(), -this.range.getValue(), -this.range.getValue()), HoleFiller.mc.player.getPosition().add((double)this.range.getValue(), (double)this.range.getValue(), (double)this.range.getValue()));
        for (final BlockPos pos : blocks) {
            if (HoleFiller.mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue())) {
                if (this.placeMode.getValue() == PlaceMode.SMART && !this.isPlayerInRange(pos)) {
                    continue;
                }
                if (HoleFiller.mc.world.getBlockState(pos).getMaterial().blocksMovement() || HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) {
                    continue;
                }
                final boolean solidNeighbours = (HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) && (HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                if (!solidNeighbours) {
                    continue;
                }
                this.holes.add(pos);
            }
        }
        if (this.midSafeHoles.getValue()) {
            final Object object = OyVey.holeManager.getMidSafety();
            synchronized (object) {
                final ArrayList<BlockPos> targets = new ArrayList<BlockPos>(OyVey.holeManager.getMidSafety());
            }
        }
        final Object object = OyVey.holeManager.getHoles();
        synchronized (object) {
            final ArrayList<BlockPos> targets = new ArrayList<BlockPos>(OyVey.holeManager.getHoles());
        }
        this.holes.forEach(this::placeBlock);
        this.toggle();
    }
    
    private void placeBlock(final BlockPos pos) {
        for (final Entity entity : HoleFiller.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityLivingBase) {
                return;
            }
        }
        if (this.blocksThisTick < this.blocksPerTick.getValue()) {
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            final boolean smartRotate = this.blocksPerTick.getValue() == 1 && this.rotate.getValue();
            if (smartRotate) {
                this.isSneaking = BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking);
            }
            else {
                this.isSneaking = BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking);
            }
            final int originalSlot = HoleFiller.mc.player.inventory.currentItem;
            HoleFiller.mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            HoleFiller.mc.playerController.updateController();
            TestUtil.placeBlock(pos);
            if (HoleFiller.mc.player.inventory.currentItem != originalSlot) {
                HoleFiller.mc.player.inventory.currentItem = originalSlot;
                HoleFiller.mc.playerController.updateController();
            }
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }
    
    private boolean isPlayerInRange(final BlockPos pos) {
        for (final EntityPlayer player : HoleFiller.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, this.smartRange.getValue())) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    private boolean check() {
        this.blocksThisTick = 0;
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (this.onlySafe.getValue() && !EntityUtil.isSafe((Entity)HoleFiller.mc.player)) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue());
    }
    
    static {
        HoleFiller.INSTANCE = new HoleFiller();
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets2(0, true));
    }
    
    public enum PlaceMode
    {
        SMART, 
        ALL;
    }
    
    public enum Settings
    {
        PLACE, 
        MISC, 
        RENDER;
    }
}
