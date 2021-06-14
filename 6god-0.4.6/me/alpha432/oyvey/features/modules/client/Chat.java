//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.Packet;
import me.alpha432.oyvey.util.MathUtil;
import java.util.Random;
import me.alpha432.oyvey.manager.FileManager;
import net.minecraft.util.math.Vec3i;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.OyVey;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.alpha432.oyvey.event.events.DeathEvent;
import java.util.Iterator;
import me.alpha432.oyvey.features.modules.combat.CrystalAura;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.concurrent.ThreadLocalRandom;
import me.alpha432.oyvey.event.events.UpdateEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.Timer;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Chat extends Module
{
    private final Setting<Settings> setting;
    private final Setting<String> suffix;
    public Setting<Boolean> killmsg;
    private final Setting<Integer> targetResetTimer;
    private final Setting<Integer> delay;
    public Map<EntityPlayer, Integer> targets;
    private static final String path = "Quantum/killsmg.txt";
    public List<String> messages;
    private final Timer cooldownTimer;
    public EntityPlayer cauraTarget;
    private boolean cooldown;
    private final Timer delayTimer;
    public Setting<TextUtil.Color> timeStamps;
    public Setting<TextUtil.Color> bracket;
    public Setting<Boolean> space;
    public Setting<Boolean> all;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    private final Timer timer;
    public boolean check;
    private static Chat INSTANCE;
    
    public Chat() {
        super("Chat", "c", Category.CLIENT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", (T)Settings.Chat));
        this.suffix = (Setting<String>)this.register(new Setting("Mode", (T)"Quantum", v -> this.setting.getValue() == Settings.Chat));
        this.killmsg = (Setting<Boolean>)this.register(new Setting("AutoGG", (T)false, v -> this.setting.getValue() == Settings.Chat));
        this.targetResetTimer = (Setting<Integer>)this.register(new Setting("Reset", (T)30, (T)0, (T)90, v -> this.killmsg.getValue()));
        this.delay = (Setting<Integer>)this.register(new Setting("KillDelay", (T)10, (T)0, (T)30, v -> this.killmsg.getValue()));
        this.targets = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.messages = new ArrayList<String>();
        this.cooldownTimer = new Timer();
        this.delayTimer = new Timer();
        this.timeStamps = (Setting<TextUtil.Color>)this.register(new Setting("Time", (T)TextUtil.Color.NONE, v -> this.setting.getValue() == Settings.Visual));
        this.bracket = (Setting<TextUtil.Color>)this.register(new Setting("Bracket", (T)TextUtil.Color.WHITE, v -> this.setting.getValue() == Settings.Visual && this.timeStamps.getValue() != TextUtil.Color.NONE && this.setting.getValue() == Settings.Visual));
        this.space = (Setting<Boolean>)this.register(new Setting("Space", (T)true, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.all = (Setting<Boolean>)this.register(new Setting("All", (T)false, v -> this.timeStamps.getValue() != TextUtil.Color.NONE && this.setting.getValue() == Settings.Visual));
        this.clean = (Setting<Boolean>)this.register(new Setting("CleanChat", (T)false, v -> this.setting.getValue() == Settings.Visual));
        this.infinite = (Setting<Boolean>)this.register(new Setting("Infinite", (T)false, v -> this.setting.getValue() == Settings.Visual));
        this.timer = new Timer();
        this.setInstance();
        final File file = new File("Quantum/killsmg.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Chat getInstance() {
        if (Chat.INSTANCE == null) {
            Chat.INSTANCE = new Chat();
        }
        return Chat.INSTANCE;
    }
    
    private void setInstance() {
        Chat.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onUpdate(final UpdateEvent event) {
        if (Chat.mc.player == null) {
            return;
        }
        if (this.delayTimer.passedMs(this.delay.getValue())) {
            Chat.mc.player.sendChatMessage("I just walked " + ThreadLocalRandom.current().nextInt(1, 31) + "!");
            this.delayTimer.reset();
        }
    }
    
    @Override
    public void onEnable() {
        this.loadMessages();
        this.timer.reset();
        this.cooldownTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (AutoCrystal.target != null && this.cauraTarget != AutoCrystal.target) {
            this.cauraTarget = AutoCrystal.target;
        }
        if (CrystalAura.currentTarget != null && this.cauraTarget != CrystalAura.currentTarget) {
            this.cauraTarget = CrystalAura.currentTarget;
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passedS(this.delay.getValue()) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (AutoCrystal.target != null) {
            this.targets.put(AutoCrystal.target, (int)(this.timer.getPassedTimeMs() / 1000L));
        }
        this.targets.replaceAll((p, v) -> Integer.valueOf((int)(this.timer.getPassedTimeMs() / 1000L)));
        for (final EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.targetResetTimer.getValue()) {
                continue;
            }
            this.targets.remove(player);
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onEntityDeath(final DeathEvent event) {
        if (this.killmsg.getValue()) {
            if (this.targets.containsKey(event.player) && !this.cooldown) {
                this.announceDeath(event.player);
                this.cooldown = true;
                this.targets.remove(event.player);
            }
            if (event.player == this.cauraTarget && !this.cooldown) {
                this.announceDeath(event.player);
                this.cooldown = true;
            }
        }
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !OyVey.friendManager.isFriend(event.getEntityPlayer())) {
            this.targets.put((EntityPlayer)event.getTarget(), 0);
        }
    }
    
    @SubscribeEvent
    public void onSendAttackPacket(final PacketEvent.Send event) {
        final CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)Chat.mc.world) instanceof EntityPlayer && !OyVey.friendManager.isFriend((EntityPlayer)packet.getEntityFromWorld((World)Chat.mc.world))) {
            this.targets.put((EntityPlayer)packet.getEntityFromWorld((World)Chat.mc.world), 0);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if ((this.clean.getValue() || this.infinite.getValue()) && event.getPacket() instanceof CPacketChatMessage) {
            final String s = event.getPacket().getMessage();
            this.check = !s.startsWith(OyVey.commandManager.getPrefix());
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = event.getPacket();
            String s2 = packet.getMessage();
            if (s2.startsWith("/") || s2.startsWith(".") || s2.startsWith("#") || s2.startsWith(",") || s2.startsWith("-") || s2.startsWith("+") || s2.startsWith("$") || s2.startsWith(";")) {
                return;
            }
            final String string = this.suffix.getValue();
            event.getPacket().message = event.getPacket().getMessage() + " \u23d0 " + TextUtil.toUnicode(string);
            if (s2.length() >= 256) {
                s2 = s2.substring(0, 256);
            }
            packet.message = s2;
        }
    }
    
    @SubscribeEvent
    public void onChatPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() != 0 || event.getPacket() instanceof SPacketChat) {}
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!event.getPacket().isSystem()) {
                return;
            }
            final String originalMessage = event.getPacket().chatComponent.getFormattedText();
            final String message = this.getTimeString(originalMessage) + originalMessage;
            event.getPacket().chatComponent = (ITextComponent)new TextComponentString(message);
        }
    }
    
    public String getTimeString(final String message) {
        final String date = new SimpleDateFormat("k:mm").format(new Date());
        return ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<", this.bracket.getValue())) + TextUtil.coloredString(date, this.timeStamps.getValue()) + ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">", this.bracket.getValue())) + (this.space.getValue() ? " " : "") + "§r";
    }
    
    private boolean shouldSendMessage(final EntityPlayer player) {
        return player.dimension == 1 && player.getPosition().equals((Object)new Vec3i(0, 240, 0));
    }
    
    public void loadMessages() {
        this.messages = FileManager.readTextFileAllLines("Quantum/killsmg.txt");
    }
    
    public String getRandomMessage() {
        this.loadMessages();
        final Random rand = new Random();
        if (this.messages.size() == 0) {
            return "<player> just fucking died!";
        }
        if (this.messages.size() == 1) {
            return this.messages.get(0);
        }
        return this.messages.get(MathUtil.clamp(rand.nextInt(this.messages.size()), 0, this.messages.size() - 1));
    }
    
    public void announceDeath(final EntityPlayer target) {
        Chat.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(this.getRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
    }
    
    static {
        Chat.INSTANCE = new Chat();
    }
    
    public enum Settings
    {
        Chat, 
        Visual;
    }
    
    public enum SuffixMode
    {
        None, 
        Custom;
    }
    
    public enum AnnouncerMode
    {
        DotGod, 
        Simple;
    }
}
