//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.block.state.IBlockState;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.block.BlockWorkbench;
import me.alpha432.oyvey.OyVey;
import java.util.Comparator;
import java.util.HashMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.DamageUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.math.RayTraceResult;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntity;
import me.alpha432.oyvey.util.RotationUtil;
import net.minecraft.item.ItemStack;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.block.BlockPlanks;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiCrafting;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.client.ServerModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class BedAura extends Module
{
    private final Setting<Boolean> server;
    private final Setting<Boolean> place;
    private final Setting<Integer> placeDelay;
    private final Setting<Float> placeRange;
    private final Setting<Boolean> extraPacket;
    private final Setting<Boolean> packet;
    private final Setting<Boolean> explode;
    private final Setting<BreakLogic> breakMode;
    private final Setting<Integer> breakDelay;
    private final Setting<Float> breakRange;
    private final Setting<Float> minDamage;
    private final Setting<Float> range;
    private final Setting<Boolean> suicide;
    private final Setting<Boolean> removeTiles;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> oneDot15;
    private final Setting<Logic> logic;
    private final Setting<Boolean> craft;
    private final Setting<Boolean> placeCraftingTable;
    private final Setting<Boolean> openCraftingTable;
    private final Setting<Boolean> craftTable;
    private final Setting<Float> tableRange;
    private final Setting<Integer> craftDelay;
    private final Setting<Integer> tableSlot;
    private final Setting<Boolean> sslot;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer craftTimer;
    private EntityPlayer target;
    private boolean sendRotationPacket;
    private final AtomicDouble yaw;
    private final AtomicDouble pitch;
    private final AtomicBoolean shouldRotate;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;
    private boolean five;
    private boolean six;
    private boolean seven;
    private boolean eight;
    private boolean nine;
    private boolean ten;
    private BlockPos maxPos;
    private boolean shouldCraft;
    private int craftStage;
    private int lastCraftStage;
    private int lastHotbarSlot;
    private int bedSlot;
    private BlockPos finalPos;
    private EnumFacing finalFacing;
    
    public BedAura() {
        super("BedAura", "AutoPlace and Break for beds", Category.COMBAT, true, false, false);
        this.server = (Setting<Boolean>)this.register(new Setting("Server", (T)false));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", (T)false));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("Placedelay", (T)50, (T)0, (T)500, v -> this.place.getValue()));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", (T)6.0f, (T)1.0f, (T)10.0f, v -> this.place.getValue()));
        this.extraPacket = (Setting<Boolean>)this.register(new Setting("InsanePacket", (T)false, v -> this.place.getValue()));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", (T)false, v -> this.place.getValue()));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", (T)true));
        this.breakMode = (Setting<BreakLogic>)this.register(new Setting("BreakMode", (T)BreakLogic.ALL, v -> this.explode.getValue()));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("Breakdelay", (T)50, (T)0, (T)500, v -> this.explode.getValue()));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", (T)6.0f, (T)1.0f, (T)10.0f, v -> this.explode.getValue()));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", (T)5.0f, (T)1.0f, (T)36.0f, v -> this.explode.getValue()));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)10.0f, (T)1.0f, (T)12.0f, v -> this.explode.getValue()));
        this.suicide = (Setting<Boolean>)this.register(new Setting("Suicide", (T)false, v -> this.explode.getValue()));
        this.removeTiles = (Setting<Boolean>)this.register(new Setting("RemoveTiles", (T)false));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false));
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("1.15", (T)false));
        this.logic = (Setting<Logic>)this.register(new Setting("Logic", (T)Logic.BREAKPLACE, v -> this.place.getValue() && this.explode.getValue()));
        this.craft = (Setting<Boolean>)this.register(new Setting("Craft", (T)false));
        this.placeCraftingTable = (Setting<Boolean>)this.register(new Setting("PlaceTable", (T)false, v -> this.craft.getValue()));
        this.openCraftingTable = (Setting<Boolean>)this.register(new Setting("OpenTable", (T)false, v -> this.craft.getValue()));
        this.craftTable = (Setting<Boolean>)this.register(new Setting("CraftTable", (T)false, v -> this.craft.getValue()));
        this.tableRange = (Setting<Float>)this.register(new Setting("TableRange", (T)6.0f, (T)1.0f, (T)10.0f, v -> this.craft.getValue()));
        this.craftDelay = (Setting<Integer>)this.register(new Setting("CraftDelay", (T)4, (T)1, (T)10, v -> this.craft.getValue()));
        this.tableSlot = (Setting<Integer>)this.register(new Setting("TableSlot", (T)8, (T)0, (T)8, v -> this.craft.getValue()));
        this.sslot = (Setting<Boolean>)this.register(new Setting("S-Slot", (T)false));
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.craftTimer = new Timer();
        this.target = null;
        this.sendRotationPacket = false;
        this.yaw = new AtomicDouble(-1.0);
        this.pitch = new AtomicDouble(-1.0);
        this.shouldRotate = new AtomicBoolean(false);
        this.maxPos = null;
        this.craftStage = 0;
        this.lastCraftStage = -1;
        this.lastHotbarSlot = -1;
        this.bedSlot = -1;
    }
    
    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck() && this.shouldServer()) {
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled true"));
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck() && this.shouldServer()) {
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled false"));
            if (this.sslot.getValue()) {
                BedAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(BedAura.mc.player.inventory.currentItem));
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Send event) {
        if (this.shouldRotate.get() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            packet.yaw = (float)this.yaw.get();
            packet.pitch = (float)this.pitch.get();
            this.shouldRotate.set(false);
        }
    }
    
    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (Feature.fullNullCheck() || (BedAura.mc.player.dimension != -1 && BedAura.mc.player.dimension != 1) || this.shouldServer()) {
            return;
        }
        if (event.getStage() == 0) {
            this.doBedBomb();
            if (this.shouldCraft && BedAura.mc.currentScreen instanceof GuiCrafting) {
                final int woolSlot = InventoryUtil.findInventoryWool(false);
                final int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
                if (woolSlot == -1 || woodSlot == -1) {
                    BedAura.mc.displayGuiScreen((GuiScreen)null);
                    BedAura.mc.currentScreen = null;
                    this.shouldCraft = false;
                    return;
                }
                if (this.craftStage > 1 && !this.one) {
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 1, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    this.one = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() && !this.two) {
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 2, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    this.two = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 2 && !this.three) {
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 3, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    this.three = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 3 && !this.four) {
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 4, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    this.four = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 4 && !this.five) {
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 5, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    this.five = true;
                }
                else if (this.craftStage > 1 + this.craftDelay.getValue() * 5 && !this.six) {
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 6, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                    this.recheckBedSlots(woolSlot, woodSlot);
                    BedAura.mc.playerController.windowClick(((GuiContainer)BedAura.mc.currentScreen).inventorySlots.windowId, 0, 0, ClickType.QUICK_MOVE, (EntityPlayer)BedAura.mc.player);
                    this.six = true;
                    this.one = false;
                    this.two = false;
                    this.three = false;
                    this.four = false;
                    this.five = false;
                    this.six = false;
                    this.craftStage = -2;
                    this.shouldCraft = false;
                }
                ++this.craftStage;
            }
        }
        else if (event.getStage() == 1 && this.finalPos != null) {
            final Vec3d hitVec = new Vec3d((Vec3i)this.finalPos.down()).add(0.5, 0.5, 0.5).add(new Vec3d(this.finalFacing.getOpposite().getDirectionVec()).scale(0.5));
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            InventoryUtil.switchToHotbarSlot(this.bedSlot, false);
            BlockUtil.rightClickBlock(this.finalPos.down(), hitVec, (this.bedSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.placeTimer.reset();
            this.finalPos = null;
        }
    }
    
    public void recheckBedSlots(final int woolSlot, final int woodSlot) {
        for (int i = 1; i <= 3; ++i) {
            if (BedAura.mc.player.openContainer.getInventory().get(i) == ItemStack.EMPTY) {
                BedAura.mc.playerController.windowClick(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                BedAura.mc.playerController.windowClick(1, i, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                BedAura.mc.playerController.windowClick(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            }
        }
        for (int i = 4; i <= 6; ++i) {
            if (BedAura.mc.player.openContainer.getInventory().get(i) == ItemStack.EMPTY) {
                BedAura.mc.playerController.windowClick(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                BedAura.mc.playerController.windowClick(1, i, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                BedAura.mc.playerController.windowClick(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            }
        }
    }
    
    public void incrementCraftStage() {
        if (this.craftTimer.passedMs(this.craftDelay.getValue())) {
            ++this.craftStage;
            if (this.craftStage > 9) {
                this.craftStage = 0;
            }
            this.craftTimer.reset();
        }
    }
    
    private void doBedBomb() {
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.mapBeds();
                this.breakBeds();
                this.placeBeds();
                break;
            }
            case PLACEBREAK: {
                this.mapBeds();
                this.placeBeds();
                this.breakBeds();
                break;
            }
        }
    }
    
    private void breakBeds() {
        if (this.explode.getValue() && this.breakTimer.passedMs(this.breakDelay.getValue())) {
            if (this.breakMode.getValue() == BreakLogic.CALC) {
                if (this.maxPos != null) {
                    final Vec3d hitVec = new Vec3d((Vec3i)this.maxPos).add(0.5, 0.5, 0.5);
                    final float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set((double)rotations[0]);
                    if (this.rotate.getValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set((double)rotations[1]);
                    }
                    final RayTraceResult result;
                    final EnumFacing facing = ((result = BedAura.mc.world.rayTraceBlocks(new Vec3d(BedAura.mc.player.posX, BedAura.mc.player.posY + BedAura.mc.player.getEyeHeight(), BedAura.mc.player.posZ), new Vec3d(this.maxPos.getX() + 0.5, this.maxPos.getY() - 0.5, this.maxPos.getZ() + 0.5))) == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                    BlockUtil.rightClickBlock(this.maxPos, hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            }
            else {
                for (final TileEntity entityBed : BedAura.mc.world.loadedTileEntityList) {
                    if (entityBed instanceof TileEntityBed) {
                        if (BedAura.mc.player.getDistanceSq(entityBed.getPos()) > MathUtil.square(this.breakRange.getValue())) {
                            continue;
                        }
                        final Vec3d hitVec2 = new Vec3d((Vec3i)entityBed.getPos()).add(0.5, 0.5, 0.5);
                        final float[] rotations2 = RotationUtil.getLegitRotations(hitVec2);
                        this.yaw.set((double)rotations2[0]);
                        if (this.rotate.getValue()) {
                            this.shouldRotate.set(true);
                            this.pitch.set((double)rotations2[1]);
                        }
                        final RayTraceResult result2;
                        final EnumFacing facing2 = ((result2 = BedAura.mc.world.rayTraceBlocks(new Vec3d(BedAura.mc.player.posX, BedAura.mc.player.posY + BedAura.mc.player.getEyeHeight(), BedAura.mc.player.posZ), new Vec3d(entityBed.getPos().getX() + 0.5, entityBed.getPos().getY() - 0.5, entityBed.getPos().getZ() + 0.5))) == null || result2.sideHit == null) ? EnumFacing.UP : result2.sideHit;
                        BlockUtil.rightClickBlock(entityBed.getPos(), hitVec2, EnumHand.MAIN_HAND, facing2, true);
                        this.breakTimer.reset();
                    }
                }
            }
        }
    }
    
    private void mapBeds() {
        this.maxPos = null;
        float maxDamage = 0.5f;
        if (this.removeTiles.getValue()) {
            final ArrayList<BedData> removedBlocks = new ArrayList<BedData>();
            for (final TileEntity tile : BedAura.mc.world.loadedTileEntityList) {
                if (!(tile instanceof TileEntityBed)) {
                    continue;
                }
                final TileEntityBed bed = (TileEntityBed)tile;
                final BedData data = new BedData(tile.getPos(), BedAura.mc.world.getBlockState(tile.getPos()), bed, bed.isHeadPiece());
                removedBlocks.add(data);
            }
            for (final BedData data2 : removedBlocks) {
                BedAura.mc.world.setBlockToAir(data2.getPos());
            }
            for (final BedData data2 : removedBlocks) {
                final BlockPos pos;
                if (data2.isHeadPiece() && BedAura.mc.player.getDistanceSq(pos = data2.getPos()) <= MathUtil.square(this.breakRange.getValue())) {
                    final float selfDamage;
                    if ((selfDamage = DamageUtil.calculateDamage(pos, (Entity)BedAura.mc.player)) + 1.0 >= EntityUtil.getHealth((Entity)BedAura.mc.player) && DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        continue;
                    }
                    for (final EntityPlayer player : BedAura.mc.world.playerEntities) {
                        final float damage;
                        if (player.getDistanceSq(pos) < MathUtil.square(this.range.getValue()) && EntityUtil.isValid((Entity)player, this.range.getValue() + this.breakRange.getValue()) && ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) > selfDamage || (damage > this.minDamage.getValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) || damage > EntityUtil.getHealth((Entity)player))) {
                            if (damage <= maxDamage) {
                                continue;
                            }
                            maxDamage = damage;
                            this.maxPos = pos;
                        }
                    }
                }
            }
            for (final BedData data2 : removedBlocks) {
                BedAura.mc.world.setBlockState(data2.getPos(), data2.getState());
            }
        }
        else {
            for (final TileEntity tile2 : BedAura.mc.world.loadedTileEntityList) {
                final TileEntityBed bed2;
                final BlockPos pos2;
                if (tile2 instanceof TileEntityBed && (bed2 = (TileEntityBed)tile2).isHeadPiece() && BedAura.mc.player.getDistanceSq(pos2 = bed2.getPos()) <= MathUtil.square(this.breakRange.getValue())) {
                    final float selfDamage2;
                    if ((selfDamage2 = DamageUtil.calculateDamage(pos2, (Entity)BedAura.mc.player)) + 1.0 >= EntityUtil.getHealth((Entity)BedAura.mc.player) && DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        continue;
                    }
                    for (final EntityPlayer player : BedAura.mc.world.playerEntities) {
                        final float damage;
                        if (player.getDistanceSq(pos2) < MathUtil.square(this.range.getValue()) && EntityUtil.isValid((Entity)player, this.range.getValue() + this.breakRange.getValue()) && ((damage = DamageUtil.calculateDamage(pos2, (Entity)player)) > selfDamage2 || (damage > this.minDamage.getValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) || damage > EntityUtil.getHealth((Entity)player))) {
                            if (damage <= maxDamage) {
                                continue;
                            }
                            maxDamage = damage;
                            this.maxPos = pos2;
                        }
                    }
                }
            }
        }
    }
    
    private void placeBeds() {
        if (this.place.getValue() && this.placeTimer.passedMs(this.placeDelay.getValue()) && this.maxPos == null) {
            this.bedSlot = this.findBedSlot();
            if (this.bedSlot == -1) {
                if (BedAura.mc.player.getHeldItemOffhand().getItem() != Items.BED) {
                    if (this.craft.getValue() && !this.shouldCraft && EntityUtil.getClosestEnemy(this.placeRange.getValue()) != null) {
                        this.doBedCraft();
                    }
                    return;
                }
                this.bedSlot = -2;
            }
            this.lastHotbarSlot = BedAura.mc.player.inventory.currentItem;
            this.target = EntityUtil.getClosestEnemy(this.placeRange.getValue());
            if (this.target != null) {
                final BlockPos targetPos = new BlockPos(this.target.getPositionVector());
                this.placeBed(targetPos, true);
                if (this.craft.getValue()) {
                    this.doBedCraft();
                }
            }
        }
    }
    
    private void placeBed(final BlockPos pos, final boolean firstCheck) {
        if (BedAura.mc.world.getBlockState(pos).getBlock() == Blocks.BED) {
            return;
        }
        final float damage = DamageUtil.calculateDamage(pos, (Entity)BedAura.mc.player);
        if (damage > EntityUtil.getHealth((Entity)BedAura.mc.player) + 0.5) {
            if (firstCheck && this.oneDot15.getValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        if (!BedAura.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            if (firstCheck && this.oneDot15.getValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        final ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        final HashMap<BlockPos, EnumFacing> facings = new HashMap<BlockPos, EnumFacing>();
        for (final EnumFacing facing : EnumFacing.values()) {
            final BlockPos position;
            if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && BedAura.mc.player.getDistanceSq(position = pos.offset(facing)) <= MathUtil.square(this.placeRange.getValue()) && BedAura.mc.world.getBlockState(position).getMaterial().isReplaceable()) {
                if (!BedAura.mc.world.getBlockState(position.down()).getMaterial().isReplaceable()) {
                    positions.add(position);
                    facings.put(position, facing.getOpposite());
                }
            }
        }
        if (positions.isEmpty()) {
            if (firstCheck && this.oneDot15.getValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        positions.sort(Comparator.comparingDouble(pos2 -> BedAura.mc.player.getDistanceSq(pos2)));
        this.finalPos = positions.get(0);
        this.finalFacing = facings.get(this.finalPos);
        final float[] rotation = RotationUtil.simpleFacing(this.finalFacing);
        if (!this.sendRotationPacket && this.extraPacket.getValue()) {
            RotationUtil.faceYawAndPitch(rotation[0], rotation[1]);
            this.sendRotationPacket = true;
        }
        this.yaw.set((double)rotation[0]);
        this.pitch.set((double)rotation[1]);
        this.shouldRotate.set(true);
        OyVey.rotationManager.setPlayerRotations(rotation[0], rotation[1]);
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    public void doBedCraft() {
        final int woolSlot = InventoryUtil.findInventoryWool(false);
        final int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
        if (woolSlot == -1 || woodSlot == -1) {
            if (BedAura.mc.currentScreen instanceof GuiCrafting) {
                BedAura.mc.displayGuiScreen((GuiScreen)null);
                BedAura.mc.currentScreen = null;
            }
            return;
        }
        final List targets;
        if (this.placeCraftingTable.getValue() && BlockUtil.getBlockSphere(this.tableRange.getValue() - 1.0f, BlockWorkbench.class).size() == 0 && !(targets = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)BedAura.mc.player), this.tableRange.getValue(), this.tableRange.getValue().intValue(), false, true, 0).stream().filter(pos -> BlockUtil.isPositionPlaceable(pos, false) == 3).sorted(Comparator.comparingInt(pos -> -this.safety(pos))).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList())).isEmpty()) {
            final BlockPos target = targets.get(0);
            int tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class);
            if (tableSlot != -1) {
                BedAura.mc.player.inventory.currentItem = tableSlot;
                BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            else {
                if (this.craftTable.getValue()) {
                    this.craftTable();
                }
                if ((tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class)) != -1) {
                    BedAura.mc.player.inventory.currentItem = tableSlot;
                    BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                }
            }
        }
        if (this.openCraftingTable.getValue()) {
            final List<BlockPos> tables = BlockUtil.getBlockSphere(this.tableRange.getValue(), BlockWorkbench.class);
            tables.sort(Comparator.comparingDouble(pos -> BedAura.mc.player.getDistanceSq(pos)));
            if (!tables.isEmpty() && !(BedAura.mc.currentScreen instanceof GuiCrafting)) {
                final BlockPos target = tables.get(0);
                BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                if (BedAura.mc.player.getDistanceSq(target) > MathUtil.square(this.breakRange.getValue())) {
                    return;
                }
                final Vec3d hitVec = new Vec3d((Vec3i)target);
                final float[] rotations = RotationUtil.getLegitRotations(hitVec);
                this.yaw.set((double)rotations[0]);
                if (this.rotate.getValue()) {
                    this.shouldRotate.set(true);
                    this.pitch.set((double)rotations[1]);
                }
                final RayTraceResult result;
                final EnumFacing facing = ((result = BedAura.mc.world.rayTraceBlocks(new Vec3d(BedAura.mc.player.posX, BedAura.mc.player.posY + BedAura.mc.player.getEyeHeight(), BedAura.mc.player.posZ), new Vec3d(target.getX() + 0.5, target.getY() - 0.5, target.getZ() + 0.5))) == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                BlockUtil.rightClickBlock(target, hitVec, EnumHand.MAIN_HAND, facing, true);
                this.breakTimer.reset();
                if (BedAura.mc.player.isSneaking()) {
                    BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
            this.shouldCraft = (BedAura.mc.currentScreen instanceof GuiCrafting);
            this.craftStage = 0;
            this.craftTimer.reset();
        }
    }
    
    public void craftTable() {
        final int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
        if (woodSlot != -1) {
            BedAura.mc.playerController.windowClick(0, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(0, 1, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(0, 2, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(0, 3, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(0, 4, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(0, 0, 0, ClickType.QUICK_MOVE, (EntityPlayer)BedAura.mc.player);
            final int table = InventoryUtil.findInventoryBlock(BlockWorkbench.class, true);
            if (table != -1) {
                BedAura.mc.playerController.windowClick(0, table, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                BedAura.mc.playerController.windowClick(0, (int)this.tableSlot.getValue(), 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
                BedAura.mc.playerController.windowClick(0, table, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            }
        }
    }
    
    @Override
    public void onToggle() {
        this.lastHotbarSlot = -1;
        this.bedSlot = -1;
        this.sendRotationPacket = false;
        this.target = null;
        this.yaw.set(-1.0);
        this.pitch.set(-1.0);
        this.shouldRotate.set(false);
        this.shouldCraft = false;
    }
    
    private int findBedSlot() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = BedAura.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() == Items.BED) {
                return i;
            }
        }
        return -1;
    }
    
    private int safety(final BlockPos pos) {
        int safety = 0;
        for (final EnumFacing facing : EnumFacing.values()) {
            if (!BedAura.mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) {
                ++safety;
            }
        }
        return safety;
    }
    
    public enum BreakLogic
    {
        ALL, 
        CALC;
    }
    
    public enum Logic
    {
        BREAKPLACE, 
        PLACEBREAK;
    }
    
    public static class BedData
    {
        private final BlockPos pos;
        private final IBlockState state;
        private final boolean isHeadPiece;
        private final TileEntityBed entity;
        
        public BedData(final BlockPos pos, final IBlockState state, final TileEntityBed bed, final boolean isHeadPiece) {
            this.pos = pos;
            this.state = state;
            this.entity = bed;
            this.isHeadPiece = isHeadPiece;
        }
        
        public BlockPos getPos() {
            return this.pos;
        }
        
        public IBlockState getState() {
            return this.state;
        }
        
        public boolean isHeadPiece() {
            return this.isHeadPiece;
        }
        
        public TileEntityBed getEntity() {
            return this.entity;
        }
    }
}
