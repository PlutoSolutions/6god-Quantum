//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import java.util.Arrays;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.block.BlockEnderChest;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import me.alpha432.oyvey.OyVey;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import me.alpha432.oyvey.util.ColorUtil;
import net.minecraft.block.BlockAir;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.util.PlayerUtil;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.EntityUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import net.minecraft.util.math.Vec3d;
import java.util.Set;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Surround extends Module
{
    private final Setting<Settings> setting;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Boolean> noGhost;
    private final Setting<Boolean> floor;
    private final Setting<Boolean> alwaysHelp;
    private final Setting<Boolean> helpingBlocks;
    private final Setting<Integer> eventMode;
    private final Setting<Center> centerPlayer;
    private final Setting<Boolean> rotate;
    private final Setting<Integer> retryer;
    private final Setting<Boolean> render;
    private final Setting<Boolean> rainbow;
    private final Setting<Integer> rainbowhue;
    public final Setting<Boolean> box;
    public final Setting<Boolean> outline;
    public final Setting<Boolean> customOutline;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private final Timer timer;
    private final Timer retryTimer;
    private final Set<Vec3d> extendingBlocks;
    private final Map<BlockPos, Integer> retries;
    private List<BlockPos> placeVectors;
    private int isSafe;
    public static boolean isPlacing;
    private BlockPos startPos;
    private boolean didPlace;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements;
    private int extenders;
    private int obbySlot;
    private boolean offHand;
    Vec3d center;
    
    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Category.COMBAT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.PLACE));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)250, v -> this.setting.getValue() == Settings.PLACE));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("BlocksPerTick", (T)20, (T)1, (T)20, v -> this.setting.getValue() == Settings.PLACE));
        this.noGhost = (Setting<Boolean>)this.register(new Setting("PacketPlace", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.floor = (Setting<Boolean>)this.register(new Setting("Floor", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.alwaysHelp = (Setting<Boolean>)this.register(new Setting("AlwaysHelp", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.helpingBlocks = (Setting<Boolean>)this.register(new Setting("HelpingBlocks", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", (T)3, (T)1, (T)3, v -> this.setting.getValue() == Settings.MISC));
        this.centerPlayer = (Setting<Center>)this.register(new Setting("Center", (T)Center.None, v -> this.setting.getValue() == Settings.MISC));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true, v -> this.setting.getValue() == Settings.MISC));
        this.retryer = (Setting<Integer>)this.register(new Setting("Retries", (T)4, (T)1, (T)15, v -> this.setting.getValue() == Settings.MISC));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true, v -> this.setting.getValue() == Settings.RENDER));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false, v -> this.setting.getValue() == Settings.RENDER));
        this.rainbowhue = (Setting<Integer>)this.register(new Setting("RainbowHue", (T)255, (T)0, (T)255, v -> this.rainbow.getValue()));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)false, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", (T)false, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("cRed", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("cGreen", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("cBlue", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("cAlpha", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.timer = new Timer();
        this.retryTimer = new Timer();
        this.extendingBlocks = new HashSet<Vec3d>();
        this.retries = new HashMap<BlockPos, Integer>();
        this.placeVectors = new ArrayList<BlockPos>();
        this.didPlace = false;
        this.placements = 0;
        this.extenders = 1;
        this.obbySlot = -1;
        this.offHand = false;
        this.center = Vec3d.ZERO;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
        this.center = PlayerUtil.getCenter(Surround.mc.player.posX, Surround.mc.player.posY, Surround.mc.player.posZ);
        switch (this.centerPlayer.getValue()) {
            case TP: {
                Surround.mc.player.motionX = 0.0;
                Surround.mc.player.motionZ = 0.0;
                Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.center.x, this.center.y, this.center.z, true));
                Surround.mc.player.setPosition(this.center.x, this.center.y, this.center.z);
                break;
            }
            case NCP: {
                Surround.mc.player.motionX = (this.center.x - Surround.mc.player.posX) / 2.0;
                Surround.mc.player.motionZ = (this.center.z - Surround.mc.player.posZ) / 2.0;
                break;
            }
        }
        this.retries.clear();
        this.retryTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.doFeetPlace();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.doFeetPlace();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.doFeetPlace();
        }
        if (this.check()) {
            return;
        }
        final boolean onWeb = Surround.mc.world.getBlockState(new BlockPos(Surround.mc.player.getPositionVector())).getBlock() == Blocks.WEB;
        if (!BlockUtil.isSafe((Entity)Surround.mc.player, onWeb ? 1 : 0, this.floor.getValue())) {
            this.placeBlocks(Surround.mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray(Surround.mc.player.getPositionVector(), (int)(onWeb ? 1 : 0), this.floor.getValue()), this.helpingBlocks.getValue(), false, false);
        }
        else if (!BlockUtil.isSafe((Entity)Surround.mc.player, onWeb ? 0 : -1, false) && this.alwaysHelp.getValue()) {
            this.placeBlocks(Surround.mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray(Surround.mc.player.getPositionVector(), onWeb ? 0 : -1, false), false, false, true);
        }
        boolean inEChest = Surround.mc.world.getBlockState(new BlockPos(Surround.mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        if (Surround.mc.player.posY - (int)Surround.mc.player.posY < 0.7) {
            inEChest = false;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
        if (this.isSafe == 2) {
            this.placeVectors = new ArrayList<BlockPos>();
        }
    }
    
    @Override
    public void onDisable() {
        if (nullCheck()) {
            return;
        }
        Surround.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render.getValue() && (this.isSafe == 0 || this.isSafe == 1)) {
            this.placeVectors = this.rushehacj();
            for (final BlockPos pos : this.placeVectors) {
                if (!(Surround.mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) {
                    continue;
                }
                RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(this.rainbowhue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
            default: {
                return ChatFormatting.GREEN + "Safe";
            }
        }
    }
    
    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtil.isSafeOy((Entity)Surround.mc.player, 0, true)) {
            this.isSafe = 0;
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray2((Entity)Surround.mc.player, 0, true), true, false, false);
        }
        else if (!EntityUtil.isSafeOy((Entity)Surround.mc.player, -1, false)) {
            this.isSafe = 1;
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray2((Entity)Surround.mc.player, -1, false), false, false, true);
        }
        else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            final Vec3d[] array = new Vec3d[2];
            int i = 0;
            final Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                final Vec3d vec3d = array[i] = iterator.next();
                ++i;
            }
            final int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), BlockUtil.getUnsafeBlockArray(this.areClose(array), 0, this.floor.getValue()), this.helpingBlocks.getValue(), false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        }
        else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }
    
    private Vec3d areClose(final Vec3d[] vec3ds) {
        int matches = 0;
        for (final Vec3d vec3d : vec3ds) {
            for (final Vec3d pos : BlockUtil.getUnsafeBlockArray(Surround.mc.player.getPositionVector(), 0, this.floor.getValue())) {
                if (vec3d.equals((Object)pos)) {
                    ++matches;
                }
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }
    
    private boolean placeBlocks(final Vec3d pos, final Vec3d[] vec3ds, final boolean hasHelpingBlocks, final boolean isHelping, final boolean isExtending) {
        boolean gotHelp = true;
        for (final Vec3d vec3d : vec3ds) {
            gotHelp = true;
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get(position) == null || this.retries.get(position) < this.retryer.getValue()) {
                        this.placeBlock(position);
                        this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                        this.retryTimer.reset();
                        break;
                    }
                    if (OyVey.speedManager.getSpeedKpH() != 0.0 || isExtending) {
                        break;
                    }
                    if (this.extenders >= 1) {
                        break;
                    }
                    this.placeBlocks(Surround.mc.player.getPositionVector().add(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d2(Surround.mc.player.getPositionVector().add(vec3d), 0, true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    break;
                }
                case 2: {
                    if (!hasHelpingBlocks) {
                        break;
                    }
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) {
                        break;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean check() {
        if (nullCheck()) {
            return true;
        }
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        Surround.isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (this.obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != this.obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue());
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            final int originalSlot = Surround.mc.player.inventory.currentItem;
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            Surround.isPlacing = true;
            Surround.mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            Surround.mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            this.didPlace = true;
            ++this.placements;
        }
    }
    
    private List<BlockPos> rushehacj() {
        if (this.floor.getValue()) {
            return Arrays.asList(new BlockPos(Surround.mc.player.getPositionVector()).add(0, -1, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(-1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, -1), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, 1));
        }
        return Arrays.asList(new BlockPos(Surround.mc.player.getPositionVector()).add(1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(-1, 0, 0), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, -1), new BlockPos(Surround.mc.player.getPositionVector()).add(0, 0, 1));
    }
    
    static {
        Surround.isPlacing = false;
    }
    
    public enum Settings
    {
        PLACE, 
        MISC, 
        RENDER;
    }
    
    public enum Center
    {
        TP, 
        NCP, 
        None;
    }
}
