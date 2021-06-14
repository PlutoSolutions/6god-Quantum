//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import java.util.HashSet;
import io.netty.util.internal.ConcurrentSet;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.item.ItemPickaxe;
import org.lwjgl.input.Mouse;
import net.minecraft.init.Items;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.event.events.Render3DEvent;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import java.util.Iterator;
import me.alpha432.oyvey.features.modules.misc.NoSoundLag;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketExplosion;
import me.alpha432.oyvey.util.DamageUtil;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.network.play.server.SPacketSpawnObject;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.util.EntityUtil;
import java.util.Objects;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.setting.Bind;
import net.minecraft.network.play.client.CPacketUseEntity;
import java.util.Queue;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.features.modules.Module;

public class AutoCrystal extends Module
{
    public static EntityPlayer target;
    public static Set<BlockPos> lowDmgPos;
    public static Set<BlockPos> placedPos;
    public static Set<BlockPos> brokenPos;
    private static AutoCrystal instance;
    public final Timer threadTimer;
    private final Setting<Settings> setting;
    public final Setting<Boolean> attackOppositeHand;
    public final Setting<Boolean> removeAfterAttack;
    public final Setting<Boolean> antiBlock;
    private final Setting<Integer> switchCooldown;
    private final Setting<Integer> eventMode;
    private final Timer switchTimer;
    private final Timer manualTimer;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer syncTimer;
    private final Timer predictTimer;
    private final Timer renderTimer;
    private final AtomicBoolean shouldInterrupt;
    private final Timer syncroTimer;
    private final Map<EntityPlayer, Timer> totemPops;
    private final Queue<CPacketUseEntity> packetUseEntities;
    private final AtomicBoolean threadOngoing;
    public Setting<Raytrace> raytrace;
    public Setting<Boolean> place;
    public Setting<Integer> placeDelay;
    public Setting<Float> placeRange;
    public Setting<Float> minDamage;
    public Setting<Float> maxSelfPlace;
    public Setting<Integer> wasteAmount;
    public Setting<Boolean> wasteMinDmgCount;
    public Setting<Float> facePlace;
    public Setting<Float> placetrace;
    public Setting<Boolean> antiSurround;
    public Setting<Boolean> limitFacePlace;
    public Setting<Boolean> oneDot15;
    public Setting<Boolean> doublePop;
    public Setting<Double> popHealth;
    public Setting<Float> popDamage;
    public Setting<Integer> popTime;
    public Setting<Boolean> explode;
    public Setting<Switch> switchMode;
    public Setting<Integer> breakDelay;
    public Setting<Float> breakRange;
    public Setting<Integer> packets;
    public Setting<Float> maxSelfBreak;
    public Setting<Float> breaktrace;
    public Setting<Boolean> manual;
    public Setting<Boolean> manualMinDmg;
    public Setting<Integer> manualBreak;
    public Setting<Boolean> sync;
    public Setting<Boolean> instant;
    public Setting<PredictTimer> instantTimer;
    public Setting<Boolean> resetBreakTimer;
    public Setting<Integer> predictDelay;
    public Setting<Boolean> predictCalc;
    public Setting<Boolean> superSafe;
    public Setting<Boolean> antiCommit;
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
    public Setting<Boolean> holdFacePlace;
    public Setting<Boolean> holdFaceBreak;
    public Setting<Boolean> slowFaceBreak;
    public Setting<Boolean> actualSlowBreak;
    public Setting<Integer> facePlaceSpeed;
    public Setting<Boolean> antiNaked;
    public Setting<Float> range;
    public Setting<Target> targetMode;
    public Setting<Integer> minArmor;
    public Setting<AutoSwitch> autoSwitch;
    public Setting<Bind> switchBind;
    public Setting<Boolean> offhandSwitch;
    public Setting<Boolean> switchBack;
    public Setting<Boolean> lethalSwitch;
    public Setting<Boolean> mineSwitch;
    public Setting<Rotate> rotate;
    public Setting<Boolean> suicide;
    public Setting<Boolean> webAttack;
    public Setting<Boolean> ignoreTerrain;
    public Setting<Boolean> fullCalc;
    public Setting<Boolean> sound;
    public Setting<Float> soundRange;
    public Setting<Float> soundPlayer;
    public Setting<Boolean> soundConfirm;
    public Setting<Boolean> extraSelfCalc;
    public Setting<AntiFriendPop> antiFriendPop;
    public Setting<Boolean> noCount;
    public Setting<Boolean> calcEvenIfNoDamage;
    public Setting<Boolean> predictFriendDmg;
    public Setting<Float> minMinDmg;
    public Setting<Boolean> breakSwing;
    public Setting<Boolean> placeSwing;
    public Setting<Boolean> exactHand;
    public Setting<Boolean> justRender;
    public Setting<Boolean> fakeSwing;
    public Setting<Logic> logic;
    public Setting<DamageSync> damageSync;
    public Setting<Integer> damageSyncTime;
    public Setting<Float> dropOff;
    public Setting<Integer> confirm;
    public Setting<Boolean> syncedFeetPlace;
    public Setting<Boolean> fullSync;
    public Setting<Boolean> syncCount;
    public Setting<Boolean> hyperSync;
    public Setting<Boolean> gigaSync;
    public Setting<Boolean> syncySync;
    public Setting<Boolean> enormousSync;
    public Setting<Boolean> holySync;
    public Setting<Boolean> rotateFirst;
    public Setting<ThreadMode> threadMode;
    public Setting<Integer> threadDelay;
    public Setting<Boolean> syncThreadBool;
    public Setting<Integer> syncThreads;
    public Setting<Boolean> predictPos;
    public Setting<Boolean> renderExtrapolation;
    public Setting<Integer> predictTicks;
    public Setting<Integer> rotations;
    public Setting<Boolean> predictRotate;
    public Setting<Float> predictOffset;
    public Setting<Boolean> doublePopOnDamage;
    public boolean rotating;
    private Queue<Entity> attackList;
    private Map<Entity, Float> crystalMap;
    private Entity efficientTarget;
    private double currentDamage;
    private double renderDamage;
    private double lastDamage;
    private boolean didRotation;
    private boolean switching;
    private BlockPos placePos;
    private BlockPos renderPos;
    private boolean mainHand;
    private boolean offHand;
    private int crystalCount;
    private int minDmgCount;
    private int lastSlot;
    private float yaw;
    private float pitch;
    private BlockPos webPos;
    private BlockPos lastPos;
    private boolean posConfirmed;
    private boolean foundDoublePop;
    private int rotationPacketsSpoofed;
    private ScheduledExecutorService executor;
    private Thread thread;
    private EntityPlayer currentSyncTarget;
    private BlockPos syncedPlayerPos;
    private BlockPos syncedCrystalPos;
    private PlaceInfo placeInfo;
    private boolean addTolowDmg;
    private Object BlockPos;
    
    public AutoCrystal() {
        super("AutoCrystal", "Best CA on the market", Category.COMBAT, true, false, false);
        this.threadTimer = new Timer();
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.PLACE));
        this.attackOppositeHand = (Setting<Boolean>)this.register(new Setting("OppositeHand", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.removeAfterAttack = (Setting<Boolean>)this.register(new Setting("AttackRemove", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.antiBlock = (Setting<Boolean>)this.register(new Setting("AntiFeetPlace", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.switchCooldown = (Setting<Integer>)this.register(new Setting("Cooldown", (T)0, (T)0, (T)1000, v -> this.setting.getValue() == Settings.MISC));
        this.eventMode = (Setting<Integer>)this.register(new Setting("Updates", (T)3, (T)1, (T)3, v -> this.setting.getValue() == Settings.DEV));
        this.switchTimer = new Timer();
        this.manualTimer = new Timer();
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.syncTimer = new Timer();
        this.predictTimer = new Timer();
        this.renderTimer = new Timer();
        this.shouldInterrupt = new AtomicBoolean(false);
        this.syncroTimer = new Timer();
        this.totemPops = new ConcurrentHashMap<EntityPlayer, Timer>();
        this.packetUseEntities = new LinkedList<CPacketUseEntity>();
        this.threadOngoing = new AtomicBoolean(false);
        this.raytrace = (Setting<Raytrace>)this.register(new Setting("Raytrace", (T)Raytrace.NONE, v -> this.setting.getValue() == Settings.MISC));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", (T)true, v -> this.setting.getValue() == Settings.PLACE));
        this.placeDelay = (Setting<Integer>)this.register(new Setting("PlaceDelay", (T)0, (T)0, (T)500, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", (T)6.0f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", (T)1.0f, (T)0.1f, (T)36.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.maxSelfPlace = (Setting<Float>)this.register(new Setting("MaxSelfPlace", (T)10.0f, (T)0.1f, (T)36.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.wasteAmount = (Setting<Integer>)this.register(new Setting("WasteAmount", (T)5, (T)1, (T)5, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.wasteMinDmgCount = (Setting<Boolean>)this.register(new Setting("CountMinDmg", (T)true, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.facePlace = (Setting<Float>)this.register(new Setting("FacePlace", (T)8.0f, (T)0.1f, (T)36.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.placetrace = (Setting<Float>)this.register(new Setting("Placetrace", (T)5.5f, (T)0.0f, (T)10.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.BREAK));
        this.antiSurround = (Setting<Boolean>)this.register(new Setting("AntiSurround", (T)true, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.limitFacePlace = (Setting<Boolean>)this.register(new Setting("LimitFacePlace", (T)false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.oneDot15 = (Setting<Boolean>)this.register(new Setting("1.15", (T)false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.doublePop = (Setting<Boolean>)this.register(new Setting("AntiTotem", (T)false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue()));
        this.popHealth = (Setting<Double>)this.register(new Setting("PopHealth", (T)3.0, (T)0.0, (T)3.0, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue()));
        this.popDamage = (Setting<Float>)this.register(new Setting("PopDamage", (T)6.0f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue()));
        this.popTime = (Setting<Integer>)this.register(new Setting("PopTime", (T)0, (T)0, (T)1000, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue()));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", (T)true, v -> this.setting.getValue() == Settings.BREAK));
        this.switchMode = (Setting<Switch>)this.register(new Setting("Attack", (T)Switch.BREAKSLOT, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("BreakDelay", (T)0, (T)0, (T)500, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", (T)6.0f, (T)0.0f, (T)6.0f, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.packets = (Setting<Integer>)this.register(new Setting("Packets", (T)1, (T)1, (T)6, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.maxSelfBreak = (Setting<Float>)this.register(new Setting("MaxSelfBreak", (T)10.0f, (T)0.1f, (T)36.0f, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue()));
        this.breaktrace = (Setting<Float>)this.register(new Setting("Breaktrace", (T)5.5f, (T)0.0f, (T)10.0f, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.PLACE));
        this.manual = (Setting<Boolean>)this.register(new Setting("Manual", (T)true, v -> this.setting.getValue() == Settings.BREAK));
        this.manualMinDmg = (Setting<Boolean>)this.register(new Setting("ManMinDmg", (T)true, v -> this.setting.getValue() == Settings.BREAK && this.manual.getValue()));
        this.manualBreak = (Setting<Integer>)this.register(new Setting("ManualDelay", (T)500, (T)0, (T)500, v -> this.setting.getValue() == Settings.BREAK && this.manual.getValue()));
        this.sync = (Setting<Boolean>)this.register(new Setting("Sync", (T)true, v -> this.setting.getValue() == Settings.BREAK && (this.explode.getValue() || this.manual.getValue())));
        this.instant = (Setting<Boolean>)this.register(new Setting("Predict", (T)true, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue()));
        this.instantTimer = (Setting<PredictTimer>)this.register(new Setting("PredictTimer", (T)PredictTimer.NONE, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue() && this.instant.getValue()));
        this.resetBreakTimer = (Setting<Boolean>)this.register(new Setting("ResetBreakTimer", (T)true, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue() && this.instant.getValue()));
        this.predictDelay = (Setting<Integer>)this.register(new Setting("PredictDelay", (T)12, (T)0, (T)500, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue() && this.instant.getValue() && this.instantTimer.getValue() == PredictTimer.PREDICT));
        this.predictCalc = (Setting<Boolean>)this.register(new Setting("PredictCalc", (T)true, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue() && this.instant.getValue()));
        this.superSafe = (Setting<Boolean>)this.register(new Setting("SuperSafe", (T)true, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue() && this.instant.getValue()));
        this.antiCommit = (Setting<Boolean>)this.register(new Setting("AntiOverCommit", (T)true, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() && this.place.getValue() && this.instant.getValue()));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true, v -> this.setting.getValue() == Settings.RENDER));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)30, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)80, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)120, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("ColorSync", (T)false, v -> this.setting.getValue() == Settings.RENDER));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)30, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)0.7f, (T)0.1f, (T)5.0f, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue()));
        this.text = (Setting<Boolean>)this.register(new Setting("DamageText", (T)true, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", (T)false, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", (T)255, (T)0, (T)255, v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
        this.holdFacePlace = (Setting<Boolean>)this.register(new Setting("HoldFacePlace", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.holdFaceBreak = (Setting<Boolean>)this.register(new Setting("HoldSlowBreak", (T)false, v -> this.setting.getValue() == Settings.MISC && this.holdFacePlace.getValue()));
        this.slowFaceBreak = (Setting<Boolean>)this.register(new Setting("SlowFaceBreak", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.actualSlowBreak = (Setting<Boolean>)this.register(new Setting("ActuallySlow", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.facePlaceSpeed = (Setting<Integer>)this.register(new Setting("FaceSpeed", (T)500, (T)0, (T)500, v -> this.setting.getValue() == Settings.MISC));
        this.antiNaked = (Setting<Boolean>)this.register(new Setting("AntiNaked", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)12.0f, (T)0.1f, (T)20.0f, v -> this.setting.getValue() == Settings.MISC));
        this.targetMode = (Setting<Target>)this.register(new Setting("Target", (T)Target.CLOSEST, v -> this.setting.getValue() == Settings.MISC));
        this.minArmor = (Setting<Integer>)this.register(new Setting("MinArmor", (T)5, (T)0, (T)125, v -> this.setting.getValue() == Settings.MISC));
        this.autoSwitch = (Setting<AutoSwitch>)this.register(new Setting("Switch", (T)AutoSwitch.TOGGLE, v -> this.setting.getValue() == Settings.MISC));
        this.switchBind = (Setting<Bind>)this.register(new Setting("SwitchBind", (T)new Bind(-1), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() == AutoSwitch.TOGGLE));
        this.offhandSwitch = (Setting<Boolean>)this.register(new Setting("Offhand", (T)true, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
        this.switchBack = (Setting<Boolean>)this.register(new Setting("Switchback", (T)true, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE && this.offhandSwitch.getValue()));
        this.lethalSwitch = (Setting<Boolean>)this.register(new Setting("LethalSwitch", (T)false, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
        this.mineSwitch = (Setting<Boolean>)this.register(new Setting("MineSwitch", (T)true, v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
        this.rotate = (Setting<Rotate>)this.register(new Setting("Rotate", (T)Rotate.OFF, v -> this.setting.getValue() == Settings.MISC));
        this.suicide = (Setting<Boolean>)this.register(new Setting("Suicide", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.webAttack = (Setting<Boolean>)this.register(new Setting("WebAttack", (T)true, v -> this.setting.getValue() == Settings.MISC && this.targetMode.getValue() != Target.DAMAGE));
        this.ignoreTerrain = (Setting<Boolean>)this.register(new Setting("TerrainIgnore", (T)true, v -> this.setting.getValue() == Settings.MISC && this.targetMode.getValue() != Target.DAMAGE));
        this.fullCalc = (Setting<Boolean>)this.register(new Setting("ExtraCalc", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.sound = (Setting<Boolean>)this.register(new Setting("Sound", (T)true, v -> this.setting.getValue() == Settings.MISC));
        this.soundRange = (Setting<Float>)this.register(new Setting("SoundRange", (T)12.0f, (T)0.0f, (T)12.0f, v -> this.setting.getValue() == Settings.MISC));
        this.soundPlayer = (Setting<Float>)this.register(new Setting("SoundPlayer", (T)6.0f, (T)0.0f, (T)12.0f, v -> this.setting.getValue() == Settings.MISC));
        this.soundConfirm = (Setting<Boolean>)this.register(new Setting("SoundConfirm", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.extraSelfCalc = (Setting<Boolean>)this.register(new Setting("MinSelfDmg", (T)false, v -> this.setting.getValue() == Settings.MISC));
        this.antiFriendPop = (Setting<AntiFriendPop>)this.register(new Setting("FriendPop", (T)AntiFriendPop.NONE, v -> this.setting.getValue() == Settings.MISC));
        this.noCount = (Setting<Boolean>)this.register(new Setting("AntiCount", (T)false, v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK)));
        this.calcEvenIfNoDamage = (Setting<Boolean>)this.register(new Setting("BigFriendCalc", (T)false, v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.targetMode.getValue() != Target.DAMAGE));
        this.predictFriendDmg = (Setting<Boolean>)this.register(new Setting("PredictFriend", (T)false, v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.instant.getValue()));
        this.minMinDmg = (Setting<Float>)this.register(new Setting("MinMinDmg", (T)0.0f, (T)0.0f, (T)3.0f, v -> this.setting.getValue() == Settings.DEV && this.place.getValue()));
        this.breakSwing = (Setting<Boolean>)this.register(new Setting("BreakSwing", (T)true, v -> this.setting.getValue() == Settings.DEV));
        this.placeSwing = (Setting<Boolean>)this.register(new Setting("PlaceSwing", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.exactHand = (Setting<Boolean>)this.register(new Setting("ExactHand", (T)false, v -> this.setting.getValue() == Settings.DEV && this.placeSwing.getValue()));
        this.justRender = (Setting<Boolean>)this.register(new Setting("JustRender", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.fakeSwing = (Setting<Boolean>)this.register(new Setting("FakeSwing", (T)false, v -> this.setting.getValue() == Settings.DEV && this.justRender.getValue()));
        this.logic = (Setting<Logic>)this.register(new Setting("Logic", (T)Logic.BREAKPLACE, v -> this.setting.getValue() == Settings.DEV));
        this.damageSync = (Setting<DamageSync>)this.register(new Setting("DamageSync", (T)DamageSync.NONE, v -> this.setting.getValue() == Settings.DEV));
        this.damageSyncTime = (Setting<Integer>)this.register(new Setting("SyncDelay", (T)500, (T)0, (T)500, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
        this.dropOff = (Setting<Float>)this.register(new Setting("DropOff", (T)5.0f, (T)0.0f, (T)10.0f, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() == DamageSync.BREAK));
        this.confirm = (Setting<Integer>)this.register(new Setting("Confirm", (T)250, (T)0, (T)1000, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
        this.syncedFeetPlace = (Setting<Boolean>)this.register(new Setting("FeetSync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
        this.fullSync = (Setting<Boolean>)this.register(new Setting("FullSync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.syncCount = (Setting<Boolean>)this.register(new Setting("SyncCount", (T)true, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.hyperSync = (Setting<Boolean>)this.register(new Setting("HyperSync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.gigaSync = (Setting<Boolean>)this.register(new Setting("GigaSync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.syncySync = (Setting<Boolean>)this.register(new Setting("SyncySync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.enormousSync = (Setting<Boolean>)this.register(new Setting("EnormousSync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.holySync = (Setting<Boolean>)this.register(new Setting("UnbelievableSync", (T)false, v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue()));
        this.rotateFirst = (Setting<Boolean>)this.register(new Setting("FirstRotation", (T)false, v -> this.setting.getValue() == Settings.DEV && this.rotate.getValue() != Rotate.OFF && this.eventMode.getValue() == 2));
        this.threadMode = (Setting<ThreadMode>)this.register(new Setting("Thread", (T)ThreadMode.NONE, v -> this.setting.getValue() == Settings.DEV));
        this.threadDelay = (Setting<Integer>)this.register(new Setting("ThreadDelay", (T)50, (T)1, (T)1000, v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE));
        this.syncThreadBool = (Setting<Boolean>)this.register(new Setting("ThreadSync", (T)true, v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE));
        this.syncThreads = (Setting<Integer>)this.register(new Setting("SyncThreads", (T)1000, (T)1, (T)10000, v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE && this.syncThreadBool.getValue()));
        this.predictPos = (Setting<Boolean>)this.register(new Setting("PredictPos", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.renderExtrapolation = (Setting<Boolean>)this.register(new Setting("RenderExtrapolation", (T)false, v -> this.setting.getValue() == Settings.DEV && this.predictPos.getValue()));
        this.predictTicks = (Setting<Integer>)this.register(new Setting("ExtrapolationTicks", (T)2, (T)1, (T)20, v -> this.setting.getValue() == Settings.DEV && this.predictPos.getValue()));
        this.rotations = (Setting<Integer>)this.register(new Setting("Spoofs", (T)1, (T)1, (T)20, v -> this.setting.getValue() == Settings.DEV));
        this.predictRotate = (Setting<Boolean>)this.register(new Setting("PredictRotate", (T)false, v -> this.setting.getValue() == Settings.DEV));
        this.predictOffset = (Setting<Float>)this.register(new Setting("PredictOffset", (T)0.0f, (T)0.0f, (T)4.0f, v -> this.setting.getValue() == Settings.DEV));
        this.doublePopOnDamage = (Setting<Boolean>)this.register(new Setting("DamagePop", (T)false, v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() && this.doublePop.getValue() && this.targetMode.getValue() == Target.DAMAGE));
        this.rotating = false;
        this.attackList = new ConcurrentLinkedQueue<Entity>();
        this.crystalMap = new HashMap<Entity, Float>();
        this.efficientTarget = null;
        this.currentDamage = 0.0;
        this.renderDamage = 0.0;
        this.lastDamage = 0.0;
        this.didRotation = false;
        this.switching = false;
        this.placePos = null;
        this.renderPos = null;
        this.mainHand = false;
        this.offHand = false;
        this.crystalCount = 0;
        this.minDmgCount = 0;
        this.lastSlot = -1;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.webPos = null;
        this.lastPos = null;
        this.posConfirmed = false;
        this.foundDoublePop = false;
        this.rotationPacketsSpoofed = 0;
        AutoCrystal.instance = this;
    }
    
    public static AutoCrystal getInstance() {
        if (AutoCrystal.instance == null) {
            AutoCrystal.instance = new AutoCrystal();
        }
        return AutoCrystal.instance;
    }
    
    @Override
    public void onTick() {
        if (this.threadMode.getValue() == ThreadMode.NONE && this.eventMode.getValue() == 3) {
            this.doAutoCrystal();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            this.postProcessing();
        }
        if (event.getStage() != 0) {
            return;
        }
        if (this.eventMode.getValue() == 2) {
            this.doAutoCrystal();
        }
    }
    
    public void postTick() {
        if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.processMultiThreading();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.threadMode.getValue() == ThreadMode.NONE && this.eventMode.getValue() == 1) {
            this.doAutoCrystal();
        }
    }
    
    @Override
    public void onToggle() {
        AutoCrystal.brokenPos.clear();
        AutoCrystal.placedPos.clear();
        this.totemPops.clear();
        this.rotating = false;
    }
    
    @Override
    public void onDisable() {
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
        if (this.executor != null) {
            this.executor.shutdown();
        }
    }
    
    @Override
    public void onEnable() {
        if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.processMultiThreading();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.switching) {
            return "§aSwitch";
        }
        if (AutoCrystal.target != null) {
            return AutoCrystal.target.getName();
        }
        return null;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue() != Rotate.OFF && this.rotating && this.eventMode.getValue() != 2 && event.getPacket() instanceof CPacketPlayer) {
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
        CPacketUseEntity packet3;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            pos = packet3.getEntityFromWorld((World)AutoCrystal.mc.world).getPosition();
            if (this.removeAfterAttack.getValue()) {
                Objects.requireNonNull(packet3.getEntityFromWorld((World)AutoCrystal.mc.world)).setDead();
                AutoCrystal.mc.world.removeEntityFromWorld(packet3.entityId);
            }
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            final EntityEnderCrystal crystal = (EntityEnderCrystal)packet3.getEntityFromWorld((World)AutoCrystal.mc.world);
            if (this.antiBlock.getValue() && EntityUtil.isCrystalAtFeet(crystal, this.range.getValue()) && pos != null) {
                this.rotateToPos(pos);
                BlockUtil.placeCrystalOnBlock2(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing.getValue(), this.exactHand.getValue());
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (fullNullCheck()) {
            return;
        }
        if (!this.justRender.getValue() && this.switchTimer.passedMs(this.switchCooldown.getValue()) && this.explode.getValue() && this.instant.getValue() && event.getPacket() instanceof SPacketSpawnObject && (this.syncedCrystalPos == null || !this.syncedFeetPlace.getValue() || this.damageSync.getValue() == DamageSync.NONE)) {
            final SPacketSpawnObject packet2 = event.getPacket();
            final BlockPos pos;
            if (packet2.getType() == 51 && AutoCrystal.mc.player.getDistanceSq(pos = new BlockPos(packet2.getX(), packet2.getY(), packet2.getZ())) + this.predictOffset.getValue() <= MathUtil.square(this.breakRange.getValue()) && (this.instantTimer.getValue() == PredictTimer.NONE || (this.instantTimer.getValue() == PredictTimer.BREAK && this.breakTimer.passedMs(this.breakDelay.getValue())) || (this.instantTimer.getValue() == PredictTimer.PREDICT && this.predictTimer.passedMs(this.predictDelay.getValue())))) {
                if (this.predictSlowBreak(pos.down())) {
                    return;
                }
                if (this.predictFriendDmg.getValue() && (this.antiFriendPop.getValue() == AntiFriendPop.BREAK || this.antiFriendPop.getValue() == AntiFriendPop.ALL) && this.isRightThread()) {
                    for (final EntityPlayer friend : AutoCrystal.mc.world.playerEntities) {
                        if (friend != null && !AutoCrystal.mc.player.equals((Object)friend) && friend.getDistanceSq(pos) <= MathUtil.square(this.range.getValue() + this.placeRange.getValue()) && OyVey.friendManager.isFriend(friend)) {
                            if (DamageUtil.calculateDamage(pos, (Entity)friend) <= EntityUtil.getHealth((Entity)friend) + 0.5) {
                                continue;
                            }
                            return;
                        }
                    }
                }
                if (AutoCrystal.placedPos.contains(pos.down())) {
                    Label_0621: {
                        if (this.isRightThread() && this.superSafe.getValue()) {
                            if (!DamageUtil.canTakeDamage(this.suicide.getValue())) {
                                break Label_0621;
                            }
                            final float selfDamage;
                            if ((selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoCrystal.mc.player)) - 0.5 <= EntityUtil.getHealth((Entity)AutoCrystal.mc.player)) {
                                if (selfDamage <= this.maxSelfBreak.getValue()) {
                                    break Label_0621;
                                }
                            }
                        }
                        else if (!this.superSafe.getValue()) {
                            break Label_0621;
                        }
                        return;
                    }
                    this.attackCrystalPredict(packet2.getEntityID(), pos);
                }
                else if (this.predictCalc.getValue() && this.isRightThread()) {
                    float selfDamage = -1.0f;
                    if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoCrystal.mc.player);
                    }
                    if (selfDamage + 0.5 < EntityUtil.getHealth((Entity)AutoCrystal.mc.player) && selfDamage <= this.maxSelfBreak.getValue()) {
                        for (final EntityPlayer player : AutoCrystal.mc.world.playerEntities) {
                            if (player.getDistanceSq(pos) <= MathUtil.square(this.range.getValue()) && EntityUtil.isValid((Entity)player, this.range.getValue() + this.breakRange.getValue()) && (!this.antiNaked.getValue() || !DamageUtil.isNaked(player))) {
                                final float damage;
                                if ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) <= selfDamage && (damage <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && damage <= EntityUtil.getHealth((Entity)player)) {
                                    continue;
                                }
                                if (this.predictRotate.getValue() && this.eventMode.getValue() != 2 && (this.rotate.getValue() == Rotate.BREAK || this.rotate.getValue() == Rotate.ALL)) {
                                    this.rotateToPos(pos);
                                }
                                this.attackCrystalPredict(packet2.getEntityID(), pos);
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if (!this.soundConfirm.getValue() && event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion packet3 = event.getPacket();
            final BlockPos pos2 = new BlockPos(packet3.getX(), packet3.getY(), packet3.getZ()).down();
            this.removePos(pos2);
        }
        else if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities packet4 = event.getPacket();
            for (final int id : packet4.getEntityIDs()) {
                final Entity entity = AutoCrystal.mc.world.getEntityByID(id);
                if (entity instanceof EntityEnderCrystal) {
                    AutoCrystal.brokenPos.remove(new BlockPos(entity.getPositionVector()).down());
                    AutoCrystal.placedPos.remove(new BlockPos(entity.getPositionVector()).down());
                }
            }
        }
        else if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet5 = event.getPacket();
            if (packet5.getOpCode() == 35 && packet5.getEntity((World)AutoCrystal.mc.world) instanceof EntityPlayer) {
                this.totemPops.put((EntityPlayer)packet5.getEntity((World)AutoCrystal.mc.world), new Timer().reset());
            }
        }
        else {
            final SPacketSoundEffect packet6;
            if (event.getPacket() instanceof SPacketSoundEffect && (packet6 = event.getPacket()).getCategory() == SoundCategory.BLOCKS && packet6.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                final BlockPos pos = new BlockPos(packet6.getX(), packet6.getY(), packet6.getZ());
                if (this.sound.getValue() || this.threadMode.getValue() == ThreadMode.SOUND) {
                    NoSoundLag.removeEntities(packet6, this.soundRange.getValue());
                }
                if (this.soundConfirm.getValue()) {
                    this.removePos(pos);
                }
                if (this.threadMode.getValue() == ThreadMode.SOUND && this.isRightThread() && AutoCrystal.mc.player != null && AutoCrystal.mc.player.getDistanceSq(pos) < MathUtil.square(this.soundPlayer.getValue())) {
                    this.handlePool(true);
                }
            }
        }
    }
    
    private boolean predictSlowBreak(final BlockPos pos) {
        return this.antiCommit.getValue() && AutoCrystal.lowDmgPos.remove(pos) && this.shouldSlowBreak(false);
    }
    
    private boolean isRightThread() {
        return AutoCrystal.mc.isCallingFromMinecraftThread() || (!OyVey.eventManager.ticksOngoing() && !this.threadOngoing.get());
    }
    
    private void attackCrystalPredict(final int entityID, final BlockPos pos) {
        if (this.predictRotate.getValue() && (this.eventMode.getValue() != 2 || this.threadMode.getValue() != ThreadMode.NONE) && (this.rotate.getValue() == Rotate.BREAK || this.rotate.getValue() == Rotate.ALL)) {
            this.rotateToPos(pos);
        }
        final CPacketUseEntity attackPacket = new CPacketUseEntity();
        attackPacket.entityId = entityID;
        attackPacket.action = CPacketUseEntity.Action.ATTACK;
        AutoCrystal.mc.player.connection.sendPacket((Packet)attackPacket);
        if (this.breakSwing.getValue()) {
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (this.resetBreakTimer.getValue()) {
            this.breakTimer.reset();
        }
        this.predictTimer.reset();
    }
    
    private void removePos(final BlockPos pos) {
        if (this.damageSync.getValue() == DamageSync.PLACE) {
            if (AutoCrystal.placedPos.remove(pos)) {
                this.posConfirmed = true;
            }
        }
        else if (this.damageSync.getValue() == DamageSync.BREAK && AutoCrystal.brokenPos.remove(pos)) {
            this.posConfirmed = true;
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if ((this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC) && this.renderPos != null && this.render.getValue() && (this.box.getValue() || this.text.getValue() || this.outline.getValue())) {
            RenderUtil.drawBoxESP(this.renderPos, ((boolean)this.colorSync.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), ((boolean)this.colorSync.getValue()) ? this.getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            if (this.text.getValue()) {
                RenderUtil.drawText(this.renderPos, ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "");
            }
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && !(AutoCrystal.mc.currentScreen instanceof OyVeyGui) && this.switchBind.getValue().getKey() == Keyboard.getEventKey()) {
            if (this.switchBack.getValue() && this.offhandSwitch.getValue() && this.offHand) {
                final Offhand module = OyVey.moduleManager.getModuleByClass(Offhand.class);
                if (module.isOff()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> §cSwitch failed. Enable the Offhand module.");
                }
                else {
                    module.setMode(Offhand.Mode2.TOTEMS);
                    module.doSwitch();
                }
                return;
            }
            this.switching = !this.switching;
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && this.isEnabled() && (event.getSetting().equals(this.threadDelay) || event.getSetting().equals(this.threadMode))) {
            if (this.executor != null) {
                this.executor.shutdown();
            }
            if (this.thread != null) {
                this.shouldInterrupt.set(true);
            }
        }
    }
    
    private void postProcessing() {
        if (this.threadMode.getValue() != ThreadMode.NONE || this.eventMode.getValue() != 2 || this.rotate.getValue() == Rotate.OFF || !this.rotateFirst.getValue()) {
            return;
        }
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.postProcessBreak();
                this.postProcessPlace();
                break;
            }
            case PLACEBREAK: {
                this.postProcessPlace();
                this.postProcessBreak();
                break;
            }
        }
    }
    
    private void postProcessBreak() {
        while (!this.packetUseEntities.isEmpty()) {
            final CPacketUseEntity packet = this.packetUseEntities.poll();
            AutoCrystal.mc.player.connection.sendPacket((Packet)packet);
            if (this.breakSwing.getValue()) {
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            this.breakTimer.reset();
        }
    }
    
    private void postProcessPlace() {
        if (this.placeInfo != null) {
            this.placeInfo.runPlace();
            this.placeTimer.reset();
            this.placeInfo = null;
        }
    }
    
    private void processMultiThreading() {
        if (this.isOff()) {
            return;
        }
        if (this.threadMode.getValue() == ThreadMode.WHILE) {
            this.handleWhile();
        }
        else if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.handlePool(false);
        }
    }
    
    private void handlePool(final boolean justDoIt) {
        if (justDoIt || this.executor == null || this.executor.isTerminated() || this.executor.isShutdown() || (this.syncroTimer.passedMs(this.syncThreads.getValue()) && this.syncThreadBool.getValue())) {
            if (this.executor != null) {
                this.executor.shutdown();
            }
            this.executor = this.getExecutor();
            this.syncroTimer.reset();
        }
    }
    
    private void handleWhile() {
        if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || (this.syncroTimer.passedMs(this.syncThreads.getValue()) && this.syncThreadBool.getValue())) {
            if (this.thread == null) {
                this.thread = new Thread(RAutoCrystal.getInstance(this));
            }
            else if (this.syncroTimer.passedMs(this.syncThreads.getValue()) && !this.shouldInterrupt.get() && this.syncThreadBool.getValue()) {
                this.shouldInterrupt.set(true);
                this.syncroTimer.reset();
                return;
            }
            if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                this.thread = new Thread(RAutoCrystal.getInstance(this));
            }
            if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                try {
                    this.thread.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.syncroTimer.reset();
            }
        }
    }
    
    private ScheduledExecutorService getExecutor() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(RAutoCrystal.getInstance(this), 0L, this.threadDelay.getValue(), TimeUnit.MILLISECONDS);
        return service;
    }
    
    public void doAutoCrystal() {
        if (this.check()) {
            switch (this.logic.getValue()) {
                case PLACEBREAK: {
                    this.placeCrystal();
                    this.breakCrystal();
                    break;
                }
                case BREAKPLACE: {
                    this.breakCrystal();
                    this.placeCrystal();
                    break;
                }
            }
            this.manualBreaker();
        }
    }
    
    private boolean check() {
        if (fullNullCheck()) {
            return false;
        }
        if (this.syncTimer.passedMs(this.damageSyncTime.getValue())) {
            this.currentSyncTarget = null;
            this.syncedCrystalPos = null;
            this.syncedPlayerPos = null;
        }
        else if (this.syncySync.getValue() && this.syncedCrystalPos != null) {
            this.posConfirmed = true;
        }
        this.foundDoublePop = false;
        if (this.renderTimer.passedMs(500L)) {
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.mainHand = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL);
        this.offHand = (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
        this.currentDamage = 0.0;
        this.placePos = null;
        if (this.lastSlot != AutoCrystal.mc.player.inventory.currentItem || AutoTrap.isPlacing) {
            this.lastSlot = AutoCrystal.mc.player.inventory.currentItem;
            this.switchTimer.reset();
        }
        if (!this.offHand && !this.mainHand) {
            this.placeInfo = null;
            this.packetUseEntities.clear();
        }
        if (this.offHand || this.mainHand) {
            this.switching = false;
        }
        if ((!this.offHand && !this.mainHand && this.switchMode.getValue() == Switch.BREAKSLOT && !this.switching) || !DamageUtil.canBreakWeakness((EntityPlayer)AutoCrystal.mc.player) || !this.switchTimer.passedMs(this.switchCooldown.getValue())) {
            this.renderPos = null;
            AutoCrystal.target = null;
            return this.rotating = false;
        }
        if (this.mineSwitch.getValue() && Mouse.isButtonDown(0) && (this.switching || this.autoSwitch.getValue() == AutoSwitch.ALWAYS) && Mouse.isButtonDown(1) && AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            this.switchItem();
        }
        this.mapCrystals();
        if (!this.posConfirmed && this.damageSync.getValue() != DamageSync.NONE && this.syncTimer.passedMs(this.confirm.getValue())) {
            this.syncTimer.setMs(this.damageSyncTime.getValue() + 1);
        }
        return true;
    }
    
    private void mapCrystals() {
        this.efficientTarget = null;
        if (this.packets.getValue() != 1) {
            this.attackList = new ConcurrentLinkedQueue<Entity>();
            this.crystalMap = new HashMap<Entity, Float>();
        }
        this.crystalCount = 0;
        this.minDmgCount = 0;
        Entity maxCrystal = null;
        float maxDamage = 0.5f;
        for (final Entity entity : AutoCrystal.mc.world.loadedEntityList) {
            if (!entity.isDead && entity instanceof EntityEnderCrystal) {
                if (!this.isValid(entity)) {
                    continue;
                }
                if (this.syncedFeetPlace.getValue() && entity.getPosition().down().equals((Object)this.syncedCrystalPos) && this.damageSync.getValue() != DamageSync.NONE) {
                    ++this.minDmgCount;
                    ++this.crystalCount;
                    if (this.syncCount.getValue()) {
                        this.minDmgCount = this.wasteAmount.getValue() + 1;
                        this.crystalCount = this.wasteAmount.getValue() + 1;
                    }
                    if (!this.hyperSync.getValue()) {
                        continue;
                    }
                    maxCrystal = null;
                    break;
                }
                else {
                    boolean count = false;
                    boolean countMin = false;
                    float selfDamage = -1.0f;
                    if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                        selfDamage = DamageUtil.calculateDamage(entity, (Entity)AutoCrystal.mc.player);
                    }
                    if (selfDamage + 0.5 < EntityUtil.getHealth((Entity)AutoCrystal.mc.player) && selfDamage <= this.maxSelfBreak.getValue()) {
                        final Entity beforeCrystal = maxCrystal;
                        final float beforeDamage = maxDamage;
                        for (final EntityPlayer player : AutoCrystal.mc.world.playerEntities) {
                            if (player.getDistanceSq(entity) > MathUtil.square(this.range.getValue())) {
                                continue;
                            }
                            if (EntityUtil.isValid((Entity)player, this.range.getValue() + this.breakRange.getValue())) {
                                if (this.antiNaked.getValue() && DamageUtil.isNaked(player)) {
                                    continue;
                                }
                                final float damage;
                                if ((damage = DamageUtil.calculateDamage(entity, (Entity)player)) <= selfDamage && (damage <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && damage <= EntityUtil.getHealth((Entity)player)) {
                                    continue;
                                }
                                if (damage > maxDamage) {
                                    maxDamage = damage;
                                    maxCrystal = entity;
                                }
                                if (this.packets.getValue() == 1) {
                                    if (damage >= this.minDamage.getValue() || !this.wasteMinDmgCount.getValue()) {
                                        count = true;
                                    }
                                    countMin = true;
                                }
                                else {
                                    if (this.crystalMap.get(entity) != null && this.crystalMap.get(entity) >= damage) {
                                        continue;
                                    }
                                    this.crystalMap.put(entity, damage);
                                }
                            }
                            else {
                                if ((this.antiFriendPop.getValue() != AntiFriendPop.BREAK && this.antiFriendPop.getValue() != AntiFriendPop.ALL) || !OyVey.friendManager.isFriend(player.getName())) {
                                    continue;
                                }
                                final float damage;
                                if ((damage = DamageUtil.calculateDamage(entity, (Entity)player)) <= EntityUtil.getHealth((Entity)player) + 0.5) {
                                    continue;
                                }
                                maxCrystal = beforeCrystal;
                                maxDamage = beforeDamage;
                                this.crystalMap.remove(entity);
                                if (!this.noCount.getValue()) {
                                    break;
                                }
                                count = false;
                                countMin = false;
                                break;
                            }
                        }
                    }
                    if (!countMin) {
                        continue;
                    }
                    ++this.minDmgCount;
                    if (!count) {
                        continue;
                    }
                    ++this.crystalCount;
                }
            }
        }
        if (this.damageSync.getValue() == DamageSync.BREAK && (maxDamage > this.lastDamage || this.syncTimer.passedMs(this.damageSyncTime.getValue()) || this.damageSync.getValue() == DamageSync.NONE)) {
            this.lastDamage = maxDamage;
        }
        if (this.enormousSync.getValue() && this.syncedFeetPlace.getValue() && this.damageSync.getValue() != DamageSync.NONE && this.syncedCrystalPos != null) {
            if (this.syncCount.getValue()) {
                this.minDmgCount = this.wasteAmount.getValue() + 1;
                this.crystalCount = this.wasteAmount.getValue() + 1;
            }
            return;
        }
        if (this.webAttack.getValue() && this.webPos != null) {
            if (AutoCrystal.mc.player.getDistanceSq(this.webPos.up()) > MathUtil.square(this.breakRange.getValue())) {
                this.webPos = null;
            }
            else {
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(this.webPos.up()))) {
                    if (!(entity instanceof EntityEnderCrystal)) {
                        continue;
                    }
                    this.attackList.add(entity);
                    this.efficientTarget = entity;
                    this.webPos = null;
                    this.lastDamage = 0.5;
                    return;
                }
            }
        }
        if (this.shouldSlowBreak(true) && maxDamage < this.minDamage.getValue() && (AutoCrystal.target == null || EntityUtil.getHealth((Entity)AutoCrystal.target) > this.facePlace.getValue() || (!this.breakTimer.passedMs(this.facePlaceSpeed.getValue()) && this.slowFaceBreak.getValue() && Mouse.isButtonDown(0) && this.holdFacePlace.getValue() && this.holdFaceBreak.getValue()))) {
            this.efficientTarget = null;
            return;
        }
        if (this.packets.getValue() == 1) {
            this.efficientTarget = maxCrystal;
        }
        else {
            this.crystalMap = MathUtil.sortByValue(this.crystalMap, true);
            for (final Map.Entry entry : this.crystalMap.entrySet()) {
                final Entity crystal = entry.getKey();
                final float damage2 = entry.getValue();
                if (damage2 >= this.minDamage.getValue() || !this.wasteMinDmgCount.getValue()) {
                    ++this.crystalCount;
                }
                this.attackList.add(crystal);
                ++this.minDmgCount;
            }
        }
    }
    
    private boolean shouldSlowBreak(final boolean withManual) {
        return (withManual && this.manual.getValue() && this.manualMinDmg.getValue() && Mouse.isButtonDown(1) && (!Mouse.isButtonDown(0) || !this.holdFacePlace.getValue())) || (this.holdFacePlace.getValue() && this.holdFaceBreak.getValue() && Mouse.isButtonDown(0) && !this.breakTimer.passedMs(this.facePlaceSpeed.getValue())) || (this.slowFaceBreak.getValue() && !this.breakTimer.passedMs(this.facePlaceSpeed.getValue()));
    }
    
    private void placeCrystal() {
        int crystalLimit = this.wasteAmount.getValue();
        if (this.placeTimer.passedMs(this.placeDelay.getValue()) && this.place.getValue() && (this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC || (this.switchMode.getValue() == Switch.BREAKSLOT && this.switching))) {
            if ((this.offHand || this.mainHand || (this.switchMode.getValue() != Switch.ALWAYS && !this.switching)) && this.crystalCount >= crystalLimit && (!this.antiSurround.getValue() || this.lastPos == null || !this.lastPos.equals((Object)this.placePos))) {
                return;
            }
            this.calculateDamage(this.getTarget(this.targetMode.getValue() == Target.UNSAFE));
            if (AutoCrystal.target != null && this.placePos != null) {
                if (!this.offHand && !this.mainHand && this.autoSwitch.getValue() != AutoSwitch.NONE && (this.currentDamage > this.minDamage.getValue() || (this.lethalSwitch.getValue() && EntityUtil.getHealth((Entity)AutoCrystal.target) <= this.facePlace.getValue())) && !this.switchItem()) {
                    return;
                }
                if (this.currentDamage < this.minDamage.getValue() && this.limitFacePlace.getValue()) {
                    crystalLimit = 1;
                }
                if (this.currentDamage >= this.minMinDmg.getValue() && (this.offHand || this.mainHand || this.autoSwitch.getValue() != AutoSwitch.NONE) && (this.crystalCount < crystalLimit || (this.antiSurround.getValue() && this.lastPos != null && this.lastPos.equals((Object)this.placePos))) && (this.currentDamage > this.minDamage.getValue() || this.minDmgCount < crystalLimit) && this.currentDamage >= 1.0 && (DamageUtil.isArmorLow(AutoCrystal.target, this.minArmor.getValue()) || EntityUtil.getHealth((Entity)AutoCrystal.target) <= this.facePlace.getValue() || this.currentDamage > this.minDamage.getValue() || this.shouldHoldFacePlace())) {
                    final float damageOffset = (this.damageSync.getValue() == DamageSync.BREAK) ? (this.dropOff.getValue() - 5.0f) : 0.0f;
                    boolean syncflag = false;
                    if (this.syncedFeetPlace.getValue() && this.placePos.equals((Object)this.lastPos) && this.isEligableForFeetSync(AutoCrystal.target, this.placePos) && !this.syncTimer.passedMs(this.damageSyncTime.getValue()) && AutoCrystal.target.equals((Object)this.currentSyncTarget) && AutoCrystal.target.getPosition().equals((Object)this.syncedPlayerPos) && this.damageSync.getValue() != DamageSync.NONE) {
                        this.syncedCrystalPos = this.placePos;
                        this.lastDamage = this.currentDamage;
                        if (this.fullSync.getValue()) {
                            this.lastDamage = 100.0;
                        }
                        syncflag = true;
                    }
                    if (syncflag || this.currentDamage - damageOffset > this.lastDamage || this.syncTimer.passedMs(this.damageSyncTime.getValue()) || this.damageSync.getValue() == DamageSync.NONE) {
                        if (!syncflag && this.damageSync.getValue() != DamageSync.BREAK) {
                            this.lastDamage = this.currentDamage;
                        }
                        this.renderPos = this.placePos;
                        this.renderDamage = this.currentDamage;
                        if (this.switchItem()) {
                            this.currentSyncTarget = AutoCrystal.target;
                            this.syncedPlayerPos = AutoCrystal.target.getPosition();
                            if (this.foundDoublePop) {
                                this.totemPops.put(AutoCrystal.target, new Timer().reset());
                            }
                            this.rotateToPos(this.placePos);
                            if (this.addTolowDmg || (this.actualSlowBreak.getValue() && this.currentDamage < this.minDamage.getValue())) {
                                AutoCrystal.lowDmgPos.add(this.placePos);
                            }
                            AutoCrystal.placedPos.add(this.placePos);
                            if (!this.justRender.getValue()) {
                                if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE && this.rotateFirst.getValue() && this.rotate.getValue() != Rotate.OFF) {
                                    this.placeInfo = new PlaceInfo(this.placePos, this.offHand, this.placeSwing.getValue(), this.exactHand.getValue());
                                }
                                else {
                                    BlockUtil.placeCrystalOnBlock2(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing.getValue(), this.exactHand.getValue());
                                }
                            }
                            this.lastPos = this.placePos;
                            this.placeTimer.reset();
                            this.posConfirmed = false;
                            if (this.syncTimer.passedMs(this.damageSyncTime.getValue())) {
                                this.syncedCrystalPos = null;
                                this.syncTimer.reset();
                            }
                        }
                    }
                }
            }
            else {
                this.renderPos = null;
            }
        }
    }
    
    private boolean shouldHoldFacePlace() {
        this.addTolowDmg = false;
        return this.holdFacePlace.getValue() && Mouse.isButtonDown(0) && (this.addTolowDmg = true);
    }
    
    private boolean switchItem() {
        if (this.offHand || this.mainHand) {
            return true;
        }
        switch (this.autoSwitch.getValue()) {
            case NONE: {
                return false;
            }
            case TOGGLE: {
                if (!this.switching) {
                    return false;
                }
            }
            case ALWAYS: {
                if (!this.doSwitch()) {
                    break;
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean doSwitch() {
        if (!this.offhandSwitch.getValue()) {
            if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                this.mainHand = false;
            }
            else {
                InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class, false);
                this.mainHand = true;
            }
            this.switching = false;
            return true;
        }
        final Offhand module = OyVey.moduleManager.getModuleByClass(Offhand.class);
        if (module.isOff()) {
            Command.sendMessage("<" + this.getDisplayName() + "> §cSwitch failed. Enable the Offhand module.");
            return this.switching = false;
        }
        this.switching = false;
        return true;
    }
    
    private void calculateDamage(final EntityPlayer targettedPlayer) {
        if (targettedPlayer == null && this.targetMode.getValue() != Target.DAMAGE && !this.fullCalc.getValue()) {
            return;
        }
        float maxDamage = 0.5f;
        EntityPlayer currentTarget = null;
        BlockPos currentPos = null;
        float maxSelfDamage = 0.0f;
        this.foundDoublePop = false;
        BlockPos setToAir = null;
        IBlockState state = null;
        final BlockPos playerPos;
        final Block web;
        if (this.webAttack.getValue() && targettedPlayer != null && (web = AutoCrystal.mc.world.getBlockState(playerPos = new BlockPos(targettedPlayer.getPositionVector())).getBlock()) == Blocks.WEB) {
            setToAir = playerPos;
            state = AutoCrystal.mc.world.getBlockState(playerPos);
            AutoCrystal.mc.world.setBlockToAir(playerPos);
        }
        for (final BlockPos pos : BlockUtil.possiblePlacePositionsCa(this.placeRange.getValue(), this.antiSurround.getValue(), this.oneDot15.getValue())) {
            if (!BlockUtil.rayTracePlaceCheck(pos, (this.raytrace.getValue() == Raytrace.PLACE || this.raytrace.getValue() == Raytrace.FULL) && AutoCrystal.mc.player.getDistanceSq(pos) > MathUtil.square(this.placetrace.getValue()), 1.0f)) {
                continue;
            }
            float selfDamage = -1.0f;
            if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoCrystal.mc.player);
            }
            if (selfDamage + 0.5 >= EntityUtil.getHealth((Entity)AutoCrystal.mc.player)) {
                continue;
            }
            if (selfDamage > this.maxSelfPlace.getValue()) {
                continue;
            }
            if (targettedPlayer != null) {
                final float playerDamage = DamageUtil.calculateDamage(pos, (Entity)targettedPlayer);
                if (this.calcEvenIfNoDamage.getValue() && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.PLACE)) {
                    boolean friendPop = false;
                    for (final EntityPlayer friend : AutoCrystal.mc.world.playerEntities) {
                        if (friend != null && !AutoCrystal.mc.player.equals((Object)friend) && friend.getDistanceSq(pos) <= MathUtil.square(this.range.getValue() + this.placeRange.getValue()) && OyVey.friendManager.isFriend(friend)) {
                            final float friendDamage;
                            if ((friendDamage = DamageUtil.calculateDamage(pos, (Entity)friend)) <= EntityUtil.getHealth((Entity)friend) + 0.5) {
                                continue;
                            }
                            friendPop = true;
                            break;
                        }
                    }
                    if (friendPop) {
                        continue;
                    }
                }
                if (this.isDoublePoppable(targettedPlayer, playerDamage) && (currentPos == null || targettedPlayer.getDistanceSq(pos) < targettedPlayer.getDistanceSq(currentPos))) {
                    currentTarget = targettedPlayer;
                    maxDamage = playerDamage;
                    currentPos = pos;
                    this.foundDoublePop = true;
                }
                else {
                    if (this.foundDoublePop || (playerDamage <= maxDamage && (!this.extraSelfCalc.getValue() || playerDamage < maxDamage || selfDamage >= maxSelfDamage))) {
                        continue;
                    }
                    if (playerDamage <= selfDamage && (playerDamage <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && playerDamage <= EntityUtil.getHealth((Entity)targettedPlayer)) {
                        continue;
                    }
                    maxDamage = playerDamage;
                    currentTarget = targettedPlayer;
                    currentPos = pos;
                    maxSelfDamage = selfDamage;
                }
            }
            else {
                final float maxDamageBefore = maxDamage;
                final EntityPlayer currentTargetBefore = currentTarget;
                final BlockPos currentPosBefore = currentPos;
                final float maxSelfDamageBefore = maxSelfDamage;
                for (final EntityPlayer player : AutoCrystal.mc.world.playerEntities) {
                    if (EntityUtil.isValid((Entity)player, this.placeRange.getValue() + this.range.getValue())) {
                        if (this.antiNaked.getValue() && DamageUtil.isNaked(player)) {
                            continue;
                        }
                        final float playerDamage2 = DamageUtil.calculateDamage(pos, (Entity)player);
                        if (this.doublePopOnDamage.getValue() && this.isDoublePoppable(player, playerDamage2) && (currentPos == null || player.getDistanceSq(pos) < player.getDistanceSq(currentPos))) {
                            currentTarget = player;
                            maxDamage = playerDamage2;
                            currentPos = pos;
                            maxSelfDamage = selfDamage;
                            this.foundDoublePop = true;
                            if (this.antiFriendPop.getValue() != AntiFriendPop.BREAK && this.antiFriendPop.getValue() != AntiFriendPop.PLACE) {
                                continue;
                            }
                            break;
                        }
                        else {
                            if (this.foundDoublePop || (playerDamage2 <= maxDamage && (!this.extraSelfCalc.getValue() || playerDamage2 < maxDamage || selfDamage >= maxSelfDamage))) {
                                continue;
                            }
                            if (playerDamage2 <= selfDamage && (playerDamage2 <= this.minDamage.getValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && playerDamage2 <= EntityUtil.getHealth((Entity)player)) {
                                continue;
                            }
                            maxDamage = playerDamage2;
                            currentTarget = player;
                            currentPos = pos;
                            maxSelfDamage = selfDamage;
                        }
                    }
                    else {
                        if ((this.antiFriendPop.getValue() != AntiFriendPop.ALL && this.antiFriendPop.getValue() != AntiFriendPop.PLACE) || player == null || player.getDistanceSq(pos) > MathUtil.square(this.range.getValue() + this.placeRange.getValue()) || !OyVey.friendManager.isFriend(player)) {
                            continue;
                        }
                        final float friendDamage2;
                        if ((friendDamage2 = DamageUtil.calculateDamage(pos, (Entity)player)) <= EntityUtil.getHealth((Entity)player) + 0.5) {
                            continue;
                        }
                        maxDamage = maxDamageBefore;
                        currentTarget = currentTargetBefore;
                        currentPos = currentPosBefore;
                        maxSelfDamage = maxSelfDamageBefore;
                        break;
                    }
                }
            }
        }
        if (setToAir != null) {
            AutoCrystal.mc.world.setBlockState(setToAir, state);
            this.webPos = currentPos;
        }
        AutoCrystal.target = currentTarget;
        this.currentDamage = maxDamage;
        this.placePos = currentPos;
    }
    
    private EntityPlayer getTarget(final boolean unsafe) {
        if (this.targetMode.getValue() == Target.DAMAGE) {
            return null;
        }
        EntityPlayer currentTarget = null;
        for (final EntityPlayer player : AutoCrystal.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, this.placeRange.getValue() + this.range.getValue()) && (!this.antiNaked.getValue() || !DamageUtil.isNaked(player))) {
                if (unsafe && EntityUtil.isSafe((Entity)player)) {
                    continue;
                }
                if (this.minArmor.getValue() > 0 && DamageUtil.isArmorLow(player, this.minArmor.getValue())) {
                    currentTarget = player;
                    break;
                }
                if (currentTarget == null) {
                    currentTarget = player;
                }
                else {
                    if (AutoCrystal.mc.player.getDistanceSq((Entity)player) >= AutoCrystal.mc.player.getDistanceSq((Entity)currentTarget)) {
                        continue;
                    }
                    currentTarget = player;
                }
            }
        }
        if (unsafe && currentTarget == null) {
            return this.getTarget(false);
        }
        if (this.predictPos.getValue() && currentTarget != null) {
            final GameProfile profile = new GameProfile((currentTarget.getUniqueID() == null) ? UUID.fromString("8af022c8-b926-41a0-8b79-2b544ff00fcf") : currentTarget.getUniqueID(), currentTarget.getName());
            final EntityOtherPlayerMP newTarget = new EntityOtherPlayerMP((World)AutoCrystal.mc.world, profile);
            final Vec3d extrapolatePosition = MathUtil.extrapolatePlayerPosition(currentTarget, this.predictTicks.getValue());
            newTarget.copyLocationAndAnglesFrom((Entity)currentTarget);
            newTarget.posX = extrapolatePosition.x;
            newTarget.posY = extrapolatePosition.y;
            newTarget.posZ = extrapolatePosition.z;
            newTarget.setHealth(EntityUtil.getHealth((Entity)currentTarget));
            newTarget.inventory.copyInventory(currentTarget.inventory);
            currentTarget = (EntityPlayer)newTarget;
        }
        return currentTarget;
    }
    
    private void breakCrystal() {
        if (this.explode.getValue() && this.breakTimer.passedMs(this.breakDelay.getValue()) && (this.switchMode.getValue() == Switch.ALWAYS || this.mainHand || this.offHand)) {
            if (this.packets.getValue() == 1 && this.efficientTarget != null) {
                if (this.justRender.getValue()) {
                    this.doFakeSwing();
                    return;
                }
                if (this.syncedFeetPlace.getValue() && this.gigaSync.getValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
                    return;
                }
                this.rotateTo(this.efficientTarget);
                this.attackEntity(this.efficientTarget);
                this.breakTimer.reset();
            }
            else if (!this.attackList.isEmpty()) {
                if (this.justRender.getValue()) {
                    this.doFakeSwing();
                    return;
                }
                if (this.syncedFeetPlace.getValue() && this.gigaSync.getValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
                    return;
                }
                for (int i = 0; i < this.packets.getValue(); ++i) {
                    final Entity entity = this.attackList.poll();
                    if (entity != null) {
                        this.rotateTo(entity);
                        this.attackEntity(entity);
                    }
                }
                this.breakTimer.reset();
            }
        }
    }
    
    private void attackEntity(final Entity entity) {
        if (entity != null) {
            if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE && this.rotateFirst.getValue() && this.rotate.getValue() != Rotate.OFF) {
                this.packetUseEntities.add(new CPacketUseEntity(entity));
            }
            else {
                EntityUtil.attackEntity(entity, this.sync.getValue(), this.breakSwing.getValue());
                AutoCrystal.brokenPos.add(new BlockPos(entity.getPositionVector()).down());
            }
        }
    }
    
    private void doFakeSwing() {
        if (this.fakeSwing.getValue()) {
            EntityUtil.swingArmNoPacket(EnumHand.MAIN_HAND, (EntityLivingBase)AutoCrystal.mc.player);
        }
    }
    
    private void manualBreaker() {
        if (this.rotate.getValue() != Rotate.OFF && this.eventMode.getValue() != 2 && this.rotating) {
            if (this.didRotation) {
                AutoCrystal.mc.player.rotationPitch += (float)4.0E-4;
                this.didRotation = false;
            }
            else {
                AutoCrystal.mc.player.rotationPitch -= (float)4.0E-4;
                this.didRotation = true;
            }
        }
        final RayTraceResult result;
        if ((this.offHand || this.mainHand) && this.manual.getValue() && this.manualTimer.passedMs(this.manualBreak.getValue()) && Mouse.isButtonDown(1) && AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE && AutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.GOLDEN_APPLE && AutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.BOW && AutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.EXPERIENCE_BOTTLE && (result = AutoCrystal.mc.objectMouseOver) != null) {
            switch (result.typeOfHit) {
                case ENTITY: {
                    final Entity entity = result.entityHit;
                    if (!(entity instanceof EntityEnderCrystal)) {
                        break;
                    }
                    EntityUtil.attackEntity(entity, this.sync.getValue(), this.breakSwing.getValue());
                    this.manualTimer.reset();
                    break;
                }
                case BLOCK: {
                    final BlockPos mousePos = AutoCrystal.mc.objectMouseOver.getBlockPos().up();
                    for (final Entity target : AutoCrystal.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(mousePos))) {
                        if (!(target instanceof EntityEnderCrystal)) {
                            continue;
                        }
                        EntityUtil.attackEntity(target, this.sync.getValue(), this.breakSwing.getValue());
                        this.manualTimer.reset();
                    }
                    break;
                }
            }
        }
    }
    
    private void rotateTo(final Entity entity) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case BREAK:
            case ALL: {
                final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), entity.getPositionVector());
                if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE) {
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
    
    private void rotateToPos(final BlockPos pos) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case PLACE:
            case ALL: {
                final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
                if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE) {
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
    
    private boolean isDoublePoppable(final EntityPlayer player, final float damage) {
        final float health;
        if (this.doublePop.getValue() && (health = EntityUtil.getHealth((Entity)player)) <= this.popHealth.getValue() && damage > health + 0.5 && damage <= this.popDamage.getValue()) {
            final Timer timer = this.totemPops.get(player);
            return timer == null || timer.passedMs(this.popTime.getValue());
        }
        return false;
    }
    
    private boolean isValid(final Entity entity) {
        return entity != null && AutoCrystal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breakRange.getValue()) && (this.raytrace.getValue() == Raytrace.NONE || this.raytrace.getValue() == Raytrace.PLACE || AutoCrystal.mc.player.canEntityBeSeen(entity) || (!AutoCrystal.mc.player.canEntityBeSeen(entity) && AutoCrystal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breaktrace.getValue())));
    }
    
    private boolean isEligableForFeetSync(final EntityPlayer player, final BlockPos pos) {
        if (this.holySync.getValue()) {
            final BlockPos playerPos = new BlockPos(player.getPositionVector());
            for (final EnumFacing facing : EnumFacing.values()) {
                final BlockPos holyPos;
                if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && pos.equals((Object)(holyPos = playerPos.down().offset(facing)))) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public Color getCurrentColor() {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }
    
    public boolean rayTraceSolidCheck(Vec3d start, final Vec3d end) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z) && !Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z)) {
            int currX = MathHelper.floor(start.x);
            int currY = MathHelper.floor(start.y);
            int currZ = MathHelper.floor(start.z);
            final int endX = MathHelper.floor(end.x);
            final int endY = MathHelper.floor(end.y);
            final int endZ = MathHelper.floor(end.z);
            BlockPos blockPos = new BlockPos(currX, currY, currZ);
            IBlockState blockState = mc.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (blockState.getCollisionBoundingBox((IBlockAccess)mc.world, blockPos) != Block.NULL_AABB && block.canCollideCheck(blockState, false) && (this.getBlocks().contains(block) || !this.ignoreTerrain.getValue())) {
                return true;
            }
            final double seDeltaX = end.x - start.x;
            final double seDeltaY = end.y - start.y;
            final double seDeltaZ = end.z - start.z;
            int steps = 200;
            while (steps-- >= 0) {
                if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                    return false;
                }
                if (currX == endX && currY == endY && currZ == endZ) {
                    return false;
                }
                boolean unboundedX = true;
                boolean unboundedY = true;
                boolean unboundedZ = true;
                double stepX = 999.0;
                double stepY = 999.0;
                double stepZ = 999.0;
                double deltaX = 999.0;
                double deltaY = 999.0;
                double deltaZ = 999.0;
                if (endX > currX) {
                    stepX = currX + 1.0;
                }
                else if (endX < currX) {
                    stepX = currX;
                }
                else {
                    unboundedX = false;
                }
                if (endY > currY) {
                    stepY = currY + 1.0;
                }
                else if (endY < currY) {
                    stepY = currY;
                }
                else {
                    unboundedY = false;
                }
                if (endZ > currZ) {
                    stepZ = currZ + 1.0;
                }
                else if (endZ < currZ) {
                    stepZ = currZ;
                }
                else {
                    unboundedZ = false;
                }
                if (unboundedX) {
                    deltaX = (stepX - start.x) / seDeltaX;
                }
                if (unboundedY) {
                    deltaY = (stepY - start.y) / seDeltaY;
                }
                if (unboundedZ) {
                    deltaZ = (stepZ - start.z) / seDeltaZ;
                }
                if (deltaX == 0.0) {
                    deltaX = -1.0E-4;
                }
                if (deltaY == 0.0) {
                    deltaY = -1.0E-4;
                }
                if (deltaZ == 0.0) {
                    deltaZ = -1.0E-4;
                }
                EnumFacing facing;
                if (deltaX < deltaY && deltaX < deltaZ) {
                    facing = ((endX > currX) ? EnumFacing.WEST : EnumFacing.EAST);
                    start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
                }
                else if (deltaY < deltaZ) {
                    facing = ((endY > currY) ? EnumFacing.DOWN : EnumFacing.UP);
                    start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
                }
                else {
                    facing = ((endZ > currZ) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                    start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
                }
                currX = MathHelper.floor(start.x) - ((facing == EnumFacing.EAST) ? 1 : 0);
                currY = MathHelper.floor(start.y) - ((facing == EnumFacing.UP) ? 1 : 0);
                currZ = MathHelper.floor(start.z) - ((facing == EnumFacing.SOUTH) ? 1 : 0);
                blockPos = new BlockPos(currX, currY, currZ);
                blockState = mc.world.getBlockState(blockPos);
                block = blockState.getBlock();
                if (block.canCollideCheck(blockState, false) && (this.getBlocks().contains(block) || !this.ignoreTerrain.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<Block> getBlocks() {
        return Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.COMMAND_BLOCK, Blocks.BARRIER, Blocks.ENCHANTING_TABLE, Blocks.ENDER_CHEST, Blocks.END_PORTAL_FRAME, (Block)Blocks.BEACON);
    }
    
    static {
        AutoCrystal.target = null;
        AutoCrystal.lowDmgPos = (Set<BlockPos>)new ConcurrentSet();
        AutoCrystal.placedPos = new HashSet<BlockPos>();
        AutoCrystal.brokenPos = new HashSet<BlockPos>();
    }
    
    public enum PredictTimer
    {
        NONE, 
        BREAK, 
        PREDICT;
    }
    
    public enum AntiFriendPop
    {
        NONE, 
        PLACE, 
        BREAK, 
        ALL;
    }
    
    public enum ThreadMode
    {
        NONE, 
        POOL, 
        SOUND, 
        WHILE;
    }
    
    public enum AutoSwitch
    {
        NONE, 
        TOGGLE, 
        ALWAYS;
    }
    
    public enum Raytrace
    {
        NONE, 
        PLACE, 
        BREAK, 
        FULL;
    }
    
    public enum Switch
    {
        ALWAYS, 
        BREAKSLOT, 
        CALC;
    }
    
    public enum Logic
    {
        BREAKPLACE, 
        PLACEBREAK;
    }
    
    public enum Target
    {
        CLOSEST, 
        UNSAFE, 
        DAMAGE;
    }
    
    public enum Rotate
    {
        OFF, 
        PLACE, 
        BREAK, 
        ALL;
    }
    
    public enum DamageSync
    {
        NONE, 
        PLACE, 
        BREAK;
    }
    
    public enum Settings
    {
        PLACE, 
        BREAK, 
        RENDER, 
        MISC, 
        DEV;
    }
    
    public static class PlaceInfo
    {
        private final BlockPos pos;
        private final boolean offhand;
        private final boolean placeSwing;
        private final boolean exactHand;
        
        public PlaceInfo(final BlockPos pos, final boolean offhand, final boolean placeSwing, final boolean exactHand) {
            this.pos = pos;
            this.offhand = offhand;
            this.placeSwing = placeSwing;
            this.exactHand = exactHand;
        }
        
        public void runPlace() {
            BlockUtil.placeCrystalOnBlock2(this.pos, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing, this.exactHand);
        }
    }
    
    private static class RAutoCrystal implements Runnable
    {
        private static RAutoCrystal instance;
        private AutoCrystal autoCrystal;
        
        public static RAutoCrystal getInstance(final AutoCrystal autoCrystal) {
            if (RAutoCrystal.instance == null) {
                RAutoCrystal.instance = new RAutoCrystal();
                RAutoCrystal.instance.autoCrystal = autoCrystal;
            }
            return RAutoCrystal.instance;
        }
        
        @Override
        public void run() {
            if (this.autoCrystal.threadMode.getValue() == ThreadMode.WHILE) {
                while (this.autoCrystal.isOn() && this.autoCrystal.threadMode.getValue() == ThreadMode.WHILE) {
                    while (OyVey.eventManager.ticksOngoing()) {}
                    if (this.autoCrystal.shouldInterrupt.get()) {
                        this.autoCrystal.shouldInterrupt.set(false);
                        this.autoCrystal.syncroTimer.reset();
                        this.autoCrystal.thread.interrupt();
                        break;
                    }
                    this.autoCrystal.threadOngoing.set(true);
                    this.autoCrystal.doAutoCrystal();
                    this.autoCrystal.threadOngoing.set(false);
                    try {
                        Thread.sleep(this.autoCrystal.threadDelay.getValue());
                    }
                    catch (InterruptedException e) {
                        this.autoCrystal.thread.interrupt();
                        e.printStackTrace();
                    }
                }
            }
            else if (this.autoCrystal.threadMode.getValue() != ThreadMode.NONE && this.autoCrystal.isOn()) {
                while (OyVey.eventManager.ticksOngoing()) {}
                this.autoCrystal.threadOngoing.set(true);
                this.autoCrystal.doAutoCrystal();
                this.autoCrystal.threadOngoing.set(false);
            }
        }
    }
    
    public class switchTimer
    {
        private long time;
        
        public switchTimer() {
            this.time = -1L;
        }
        
        public boolean passedS(final double s) {
            return this.passedMs((long)s * 1000L);
        }
        
        public boolean passedDms(final double dms) {
            return this.passedMs((long)dms * 10L);
        }
        
        public boolean passedDs(final double ds) {
            return this.passedMs((long)ds * 100L);
        }
        
        public boolean passedMs(final long ms) {
            return this.passedNS(this.convertToNS(ms));
        }
        
        public void setMs(final long ms) {
            this.time = System.nanoTime() - this.convertToNS(ms);
        }
        
        public boolean passedNS(final long ns) {
            return System.nanoTime() - this.time >= ns;
        }
        
        public long getPassedTimeMs() {
            return this.getMs(System.nanoTime() - this.time);
        }
        
        public switchTimer reset() {
            this.time = System.nanoTime();
            return this;
        }
        
        public long getMs(final long time) {
            return time / 1000000L;
        }
        
        public long convertToNS(final long time) {
            return time * 1000000L;
        }
    }
}
