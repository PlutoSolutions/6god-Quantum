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
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import java.util.Iterator;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.BlockUtil;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AutoWeb extends Module
{
    private final Setting<Settings> setting;
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerPlace;
    private final Setting<Boolean> packet;
    private final Setting<Boolean> antiSelf;
    private final Setting<Boolean> lowerbody;
    private final Setting<Boolean> upperBody;
    private final Setting<Boolean> ylower;
    private final Setting<TargetMode> targetMode;
    private final Setting<Boolean> disable;
    private final Setting<Double> targetRange;
    private final Setting<Double> range;
    private final Setting<Integer> eventMode;
    private final Setting<Boolean> freecam;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> raytrace;
    private final Setting<Boolean> info;
    private final Setting<Boolean> render;
    public Setting<Boolean> box;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    public Setting<Boolean> Rainbow;
    private final Setting<Boolean> rainbow;
    private final Setting<Integer> rainbowhue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    public Setting<Boolean> cRainbow;
    private final Setting<Integer> cAlpha;
    private final Setting<Float> lineWidth;
    public static boolean isPlacing;
    private final Timer timer;
    public EntityPlayer target;
    private boolean didPlace;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements;
    private boolean smartRotate;
    private BlockPos startPos;
    private BlockPos renderPos;
    
    public AutoWeb() {
        super("AutoWeb", "Traps other players in webs", Category.COMBAT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.PLACE));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)250, v -> this.setting.getValue() == Settings.PLACE));
        this.blocksPerPlace = (Setting<Integer>)this.register(new Setting("BlocksPerTick", (T)30, (T)1, (T)30, v -> this.setting.getValue() == Settings.PLACE));
        this.packet = (Setting<Boolean>)this.register(new Setting("PacketPlace", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.antiSelf = (Setting<Boolean>)this.register(new Setting("AntiSelf", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.lowerbody = (Setting<Boolean>)this.register(new Setting("Feet", (T)true, v -> this.setting.getValue() == Settings.PLACE));
        this.upperBody = (Setting<Boolean>)this.register(new Setting("Face", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.ylower = (Setting<Boolean>)this.register(new Setting("Y-1", (T)false, v -> this.setting.getValue() == Settings.PLACE));
        this.targetMode = (Setting<TargetMode>)this.register(new Setting("Target", (T)TargetMode.UNTRAPPED, v -> this.setting.getValue() == Settings.MISC));
        this.disable = (Setting<Boolean>)this.register(new Setting("AutoDisable", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.targetRange = (Setting<Double>)this.register(new Setting("TargetRange", (T)10.0, (T)0.0, (T)20.0, v -> this.setting.getValue() == Settings.MISC));
        this.range = (Setting<Double>)this.register(new Setting("PlaceRange", (T)6.0, (T)0.0, (T)6.0, v -> this.setting.getValue() == Settings.MISC));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", (T)3, (T)1, (T)3, v -> this.setting.getValue() == Settings.MISC));
        this.freecam = (Setting<Boolean>)this.register(new Setting("Freecam", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true, v -> this.setting.getValue() == Settings.MISC));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("Raytrace", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.info = (Setting<Boolean>)this.register(new Setting("Info", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)false, v -> this.setting.getValue() == Settings.RENDER));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)false, v -> this.render.getValue() && this.setting.getValue() == Settings.RENDER));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255, v -> this.box.getValue() && this.setting.getValue() == Settings.RENDER));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> this.box.getValue() && this.setting.getValue() == Settings.RENDER));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> this.box.getValue() && this.setting.getValue() == Settings.RENDER));
        this.Rainbow = (Setting<Boolean>)this.register(new Setting("CSync", (T)false, v -> this.render.getValue() && this.setting.getValue() == Settings.RENDER));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (T)false, v -> this.render.getValue() && this.setting.getValue() == Settings.RENDER));
        this.rainbowhue = (Setting<Integer>)this.register(new Setting("Brightness", (T)255, (T)0, (T)255, v -> this.rainbow.getValue() && this.render.getValue() && this.setting.getValue() == Settings.RENDER && this.rainbow.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255, v -> this.box.getValue() && this.setting.getValue() == Settings.RENDER));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.box.getValue() && this.setting.getValue() == Settings.RENDER));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)false, v -> this.render.getValue() && this.setting.getValue() == Settings.RENDER));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", (T)0, (T)0, (T)255, v -> this.outline.getValue() && this.setting.getValue() == Settings.RENDER));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", (T)0, (T)0, (T)255, v -> this.outline.getValue() && this.setting.getValue() == Settings.RENDER));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", (T)255, (T)0, (T)255, v -> this.outline.getValue() && this.setting.getValue() == Settings.RENDER));
        this.cRainbow = (Setting<Boolean>)this.register(new Setting("OL-Rainbow", (T)false, v -> this.outline.getValue() && this.setting.getValue() == Settings.RENDER));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", (T)255, (T)0, (T)255, v -> this.outline.getValue() && this.setting.getValue() == Settings.RENDER));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.outline.getValue()));
        this.timer = new Timer();
        this.didPlace = false;
        this.placements = 0;
        this.smartRotate = false;
        this.startPos = null;
        this.renderPos = null;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            return;
        }
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.player);
        this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
    }
    
    @Override
    public void onTick() {
        if (this.eventMode.getValue() == 3) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.eventMode.getValue() == 2) {
            this.smartRotate = (this.rotate.getValue() && this.blocksPerPlace.getValue() == 1);
            this.doTrap();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.eventMode.getValue() == 1) {
            this.smartRotate = false;
            this.doTrap();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.info.getValue() && this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    @Override
    public void onDisable() {
        AutoWeb.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    
    private void doTrap() {
        if (this.check()) {
            return;
        }
        this.doWebTrap();
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private void doWebTrap() {
        final List<Vec3d> placeTargets = this.getPlacements();
        this.placeList(placeTargets);
    }
    
    private List<Vec3d> getPlacements() {
        final ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        final Vec3d baseVec = this.target.getPositionVector();
        if (this.ylower.getValue()) {
            list.add(baseVec.add(0.0, -1.0, 0.0));
        }
        if (this.lowerbody.getValue()) {
            list.add(baseVec);
        }
        if (this.upperBody.getValue()) {
            list.add(baseVec.add(0.0, 1.0, 0.0));
        }
        return list;
    }
    
    private void placeList(final List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoWeb.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoWeb.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (final Vec3d vec3d3 : list) {
            final BlockPos position = new BlockPos(vec3d3);
            final int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue());
            if (placeability == 3 || placeability == 1) {
                if (this.antiSelf.getValue() && MathUtil.areVec3dsAligned(AutoWeb.mc.player.getPositionVector(), vec3d3)) {
                    continue;
                }
                this.placeBlock(position);
            }
        }
    }
    
    private boolean check() {
        AutoWeb.isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (this.isOff()) {
            return true;
        }
        if (this.disable.getValue() && !this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.player))) {
            this.disable();
            return true;
        }
        if (obbySlot == -1) {
            if (this.info.getValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> §cYou are out of Webs.");
            }
            this.disable();
            return true;
        }
        if (AutoWeb.mc.player.inventory.currentItem != this.lastHotbarSlot && AutoWeb.mc.player.inventory.currentItem != obbySlot) {
            this.lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(this.targetRange.getValue(), this.targetMode.getValue() == TargetMode.UNTRAPPED);
        return this.target == null || (OyVey.moduleManager.isModuleEnabled("Freecam") && !this.freecam.getValue()) || !this.timer.passedMs(this.delay.getValue());
    }
    
    private EntityPlayer getTarget(final double range, final boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && (!trapped || !player.isInWeb) && (!EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.player).equals((Object)EntityUtil.getRoundedBlockPos((Entity)player)) || !this.antiSelf.getValue())) {
                if (OyVey.speedManager.getPlayerSpeed(player) > this.blocksPerPlace.getValue()) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AutoWeb.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (AutoWeb.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AutoWeb.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && AutoWeb.mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue())) {
            AutoWeb.isPlacing = true;
            final int originalSlot = AutoWeb.mc.player.inventory.currentItem;
            final int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
            if (webSlot == -1) {
                this.toggle();
            }
            if (this.smartRotate) {
                AutoWeb.mc.player.inventory.currentItem = ((webSlot == -1) ? webSlot : webSlot);
                AutoWeb.mc.playerController.updateController();
                this.isSneaking = BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking);
                AutoWeb.mc.player.inventory.currentItem = originalSlot;
                AutoWeb.mc.playerController.updateController();
            }
            else {
                AutoWeb.mc.player.inventory.currentItem = ((webSlot == -1) ? webSlot : webSlot);
                AutoWeb.mc.playerController.updateController();
                this.isSneaking = BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking);
                AutoWeb.mc.player.inventory.currentItem = originalSlot;
                AutoWeb.mc.playerController.updateController();
            }
            this.didPlace = true;
            ++this.placements;
        }
    }
    
    @Override
    public void onLogout() {
        this.disable();
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render.getValue()) {
            RenderUtil.drawBoxESP(this.renderPos, ((boolean)this.Rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.outline.getValue(), ((boolean)this.cRainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
        }
    }
    
    static {
        AutoWeb.isPlacing = false;
    }
    
    public enum TargetMode
    {
        CLOSEST, 
        UNTRAPPED;
    }
    
    public enum Settings
    {
        PLACE, 
        MISC, 
        RENDER;
    }
}
