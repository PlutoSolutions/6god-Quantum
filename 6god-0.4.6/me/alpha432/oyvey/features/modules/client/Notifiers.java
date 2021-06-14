//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.manager.FileManager;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.init.SoundEvents;
import me.alpha432.oyvey.OyVey;
import java.util.Collection;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.PotionColorCalculationEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.alpha432.oyvey.features.command.Command;
import java.util.HashSet;
import java.util.ArrayList;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.Entity;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Notifiers extends Module
{
    public Setting<Boolean> totemPops;
    public Setting<Integer> PopDelay;
    public Setting<Boolean> visualRange;
    public Setting<Boolean> VisualRangeSound;
    public Setting<Boolean> visualRangeCoords;
    public Setting<Boolean> visualRangeLeaving;
    public Setting<Boolean> pearlNotify;
    public Setting<Boolean> ghastNotify;
    public Setting<Boolean> ghastSound;
    public Setting<Boolean> ghastChat;
    public Setting<Boolean> burrow;
    public Setting<Boolean> strength;
    public Setting<Boolean> crash;
    private static final String fileName = "phobos/util/ModuleMessage_List.txt";
    private final List<EntityPlayer> burrowedPlayers;
    public static HashMap<String, Integer> TotemPopContainer;
    private List<EntityPlayer> knownPlayers;
    private static final List<String> modules;
    public static Map<EntityPlayer, Integer> strMap;
    private static Notifiers INSTANCE;
    public static Set<EntityPlayer> strengthPlayers;
    private Set<Entity> ghasts;
    private final Timer timer;
    private Entity enderPearl;
    private boolean check;
    private boolean flag;
    
    public Notifiers() {
        super("Notifications", "n", Category.CLIENT, true, false, false);
        this.totemPops = (Setting<Boolean>)this.register(new Setting("PopNotify", (T)false));
        this.PopDelay = (Setting<Integer>)this.register(new Setting("NotifyDelay", (T)0, (T)0, (T)5000, v -> this.totemPops.getValue()));
        this.visualRange = (Setting<Boolean>)this.register(new Setting("VisualRange", (T)false));
        this.VisualRangeSound = (Setting<Boolean>)this.register(new Setting("VSound", (T)false, v -> this.visualRange.getValue()));
        this.visualRangeCoords = (Setting<Boolean>)this.register(new Setting("VCoords", (T)true, v -> this.visualRange.getValue()));
        this.visualRangeLeaving = (Setting<Boolean>)this.register(new Setting("LeavingRange", (T)false, v -> this.visualRange.getValue()));
        this.pearlNotify = (Setting<Boolean>)this.register(new Setting("PearlNotify", (T)false));
        this.ghastNotify = (Setting<Boolean>)this.register(new Setting("GhastNotify", (T)false));
        this.ghastSound = (Setting<Boolean>)this.register(new Setting("GSound", (T)true, v -> this.ghastNotify.getValue()));
        this.ghastChat = (Setting<Boolean>)this.register(new Setting("GCoords", (T)true, v -> this.ghastNotify.getValue()));
        this.burrow = (Setting<Boolean>)this.register(new Setting("Burrow", (T)false));
        this.strength = (Setting<Boolean>)this.register(new Setting("Strength", (T)false));
        this.crash = (Setting<Boolean>)this.register(new Setting("CrashInfo", (T)false));
        this.burrowedPlayers = new ArrayList<EntityPlayer>();
        this.knownPlayers = new ArrayList<EntityPlayer>();
        this.ghasts = new HashSet<Entity>();
        this.timer = new Timer();
        this.setInstance();
    }
    
    public static Notifiers getInstance() {
        if (Notifiers.INSTANCE == null) {
            Notifiers.INSTANCE = new Notifiers();
        }
        return Notifiers.INSTANCE;
    }
    
    public static void displayCrash(final Exception e) {
        Command.sendMessage("§cException caught: " + e.getMessage());
    }
    
    private void setInstance() {
        Notifiers.INSTANCE = this;
    }
    
    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }
    
    @Override
    public void onEnable() {
        this.ghasts.clear();
        this.flag = true;
        Notifiers.TotemPopContainer.clear();
        this.knownPlayers = new ArrayList<EntityPlayer>();
        if (!this.check) {
            this.loadFile();
        }
    }
    
    @Override
    public void onTick() {
        if (!this.burrow.getValue()) {
            return;
        }
        for (final EntityPlayer entityPlayer2 : (List)Notifiers.mc.world.playerEntities.stream().filter(entityPlayer -> entityPlayer != Notifiers.mc.player).collect(Collectors.toList())) {
            if (!this.burrowedPlayers.contains(entityPlayer2) && this.isInBurrow(entityPlayer2)) {
                Command.sendMessage(ChatFormatting.RED + entityPlayer2.getDisplayNameString() + ChatFormatting.GRAY + " has burrowed");
                this.burrowedPlayers.add(entityPlayer2);
            }
        }
    }
    
    private boolean isInBurrow(final EntityPlayer entityPlayer) {
        final BlockPos playerPos = new BlockPos(this.getMiddlePosition(entityPlayer.posX), entityPlayer.posY, this.getMiddlePosition(entityPlayer.posZ));
        return Notifiers.mc.world.getBlockState(playerPos).getBlock() == Blocks.OBSIDIAN || Notifiers.mc.world.getBlockState(playerPos).getBlock() == Blocks.ENDER_CHEST || Notifiers.mc.world.getBlockState(playerPos).getBlock() == Blocks.ANVIL;
    }
    
    private double getMiddlePosition(final double positionIn) {
        double positionFinal = (double)Math.round(positionIn);
        if (Math.round(positionIn) > positionIn) {
            positionFinal -= 0.5;
        }
        else if (Math.round(positionIn) <= positionIn) {
            positionFinal += 0.5;
        }
        return positionFinal;
    }
    
    @SubscribeEvent
    public void onPotionColor(final PotionColorCalculationEvent event) {
        if (!this.strength.getValue()) {
            return;
        }
        if (event.getEntityLiving() instanceof EntityPlayer) {
            boolean hasStrength = false;
            for (final PotionEffect potionEffect : event.getEffects()) {
                if (potionEffect.getPotion() == MobEffects.STRENGTH) {
                    Notifiers.strMap.put((EntityPlayer)event.getEntityLiving(), potionEffect.getAmplifier());
                    Command.sendMessage(ChatFormatting.RED + event.getEntityLiving().getName() + ChatFormatting.GRAY + " has strength");
                    hasStrength = true;
                    break;
                }
            }
            if (Notifiers.strMap.containsKey(event.getEntityLiving()) && !hasStrength) {
                Notifiers.strMap.remove(event.getEntityLiving());
                Command.sendMessage(ChatFormatting.RED + event.getEntityLiving().getName() + ChatFormatting.GRAY + " no longer has strength");
            }
        }
    }
    
    public void onDeath(final EntityPlayer player) {
        if (this.totemPops.getValue() && Notifiers.TotemPopContainer.containsKey(player.getName())) {
            final int l_Count = Notifiers.TotemPopContainer.get(player.getName());
            Notifiers.TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                Command.sendSilentMessage(ChatFormatting.RED + player.getName() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.GRAY + " totem");
            }
            else {
                Command.sendSilentMessage(ChatFormatting.RED + player.getName() + ChatFormatting.GRAY + " died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.GRAY + " totems");
            }
        }
    }
    
    public void onTotemPop(final EntityPlayer player) {
        if (this.totemPops.getValue()) {
            if (fullNullCheck()) {
                return;
            }
            if (Notifiers.mc.player.equals((Object)player)) {
                return;
            }
            int l_Count = 1;
            if (Notifiers.TotemPopContainer.containsKey(player.getName())) {
                l_Count = Notifiers.TotemPopContainer.get(player.getName());
                Notifiers.TotemPopContainer.put(player.getName(), ++l_Count);
            }
            else {
                Notifiers.TotemPopContainer.put(player.getName(), l_Count);
            }
            if (l_Count == 1) {
                Command.sendSilentMessage(ChatFormatting.RED + player.getName() + ChatFormatting.GRAY + " popped " + ChatFormatting.RED + l_Count + ChatFormatting.GRAY + " totem");
            }
            else {
                Command.sendSilentMessage(ChatFormatting.RED + player.getName() + ChatFormatting.GRAY + " popped " + ChatFormatting.RED + l_Count + ChatFormatting.GRAY + " totems");
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.check && this.timer.passedMs(750L)) {
            this.check = false;
        }
        if (this.visualRange.getValue()) {
            final ArrayList<EntityPlayer> tickPlayerList = new ArrayList<EntityPlayer>(Notifiers.mc.world.playerEntities);
            if (tickPlayerList.size() > 0) {
                for (final EntityPlayer player : tickPlayerList) {
                    if (!player.getName().equals(Notifiers.mc.player.getName())) {
                        if (this.knownPlayers.contains(player)) {
                            continue;
                        }
                        this.knownPlayers.add(player);
                        if (OyVey.friendManager.isFriend(player)) {
                            Command.sendMessage(ChatFormatting.GRAY + "Player " + ChatFormatting.RED + player.getName() + ChatFormatting.GRAY + " entered your visual range" + (this.visualRangeCoords.getValue() ? (" at (" + ChatFormatting.RED + (int)player.posX + ChatFormatting.GRAY + ", " + ChatFormatting.RED + (int)player.posY + ChatFormatting.GRAY + ", " + ChatFormatting.RED + (int)player.posZ + ChatFormatting.GRAY + ")!") : "!"));
                        }
                        else {
                            Command.sendMessage(ChatFormatting.GRAY + "Player " + ChatFormatting.RED + player.getName() + ChatFormatting.GRAY + " entered your visual range" + (this.visualRangeCoords.getValue() ? (" at (" + ChatFormatting.RED + (int)player.posX + ChatFormatting.GRAY + ", " + ChatFormatting.RED + (int)player.posY + ChatFormatting.GRAY + ", " + ChatFormatting.RED + (int)player.posZ + ChatFormatting.GRAY + ")!") : "!"));
                        }
                        if (this.VisualRangeSound.getValue()) {
                            Notifiers.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        }
                        return;
                    }
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (final EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains(player)) {
                        continue;
                    }
                    this.knownPlayers.remove(player);
                    if (this.visualRangeLeaving.getValue()) {
                        if (OyVey.friendManager.isFriend(player)) {
                            Command.sendMessage("Player §a" + player.getName() + "§r left your visual range" + (this.visualRangeCoords.getValue() ? (" at (" + (int)player.posX + ", " + (int)player.posY + ", " + (int)player.posZ + ")!") : "!"));
                        }
                        else {
                            Command.sendMessage("Player §c" + player.getName() + "§r left your visual range" + (this.visualRangeCoords.getValue() ? (" at (" + (int)player.posX + ", " + (int)player.posY + ", " + (int)player.posZ + ")!") : "!"));
                        }
                    }
                    return;
                }
            }
        }
        if (this.pearlNotify.getValue()) {
            if (Notifiers.mc.world == null || Notifiers.mc.player == null) {
                return;
            }
            this.enderPearl = null;
            for (final Entity e : Notifiers.mc.world.loadedEntityList) {
                if (e instanceof EntityEnderPearl) {
                    this.enderPearl = e;
                    break;
                }
            }
            if (this.enderPearl == null) {
                this.flag = true;
                return;
            }
            EntityPlayer closestPlayer = null;
            for (final EntityPlayer entity : Notifiers.mc.world.playerEntities) {
                if (closestPlayer == null) {
                    closestPlayer = entity;
                }
                else {
                    if (closestPlayer.getDistance(this.enderPearl) <= entity.getDistance(this.enderPearl)) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
            if (closestPlayer == Notifiers.mc.player) {
                this.flag = false;
            }
            if (closestPlayer != null && this.flag) {
                String faceing = this.enderPearl.getHorizontalFacing().toString();
                if (faceing.equals("west")) {
                    faceing = "east";
                }
                else if (faceing.equals("east")) {
                    faceing = "west";
                }
                Command.sendSilentMessage(OyVey.friendManager.isFriend(closestPlayer.getName()) ? (ChatFormatting.AQUA + closestPlayer.getName() + ChatFormatting.GRAY + " has just thrown a pearl heading " + ChatFormatting.RED + faceing + ChatFormatting.GRAY + "!") : (ChatFormatting.RED + closestPlayer.getName() + ChatFormatting.GRAY + " has just thrown a pearl heading " + ChatFormatting.RED + faceing + ChatFormatting.GRAY + "!"));
                this.flag = false;
            }
        }
        if (this.ghastNotify.getValue()) {
            for (final Entity entity2 : Notifiers.mc.world.getLoadedEntityList()) {
                if (entity2 instanceof EntityGhast) {
                    if (this.ghasts.contains(entity2)) {
                        continue;
                    }
                    if (this.ghastChat.getValue()) {
                        Command.sendMessage(ChatFormatting.GRAY + "Ghast Detected at: " + ChatFormatting.RED + entity2.getPosition().getX() + "x" + ChatFormatting.GRAY + ", " + ChatFormatting.RED + entity2.getPosition().getY() + "y" + ChatFormatting.GRAY + ", " + ChatFormatting.RED + entity2.getPosition().getZ() + "z" + ChatFormatting.GRAY + ".");
                    }
                    this.ghasts.add(entity2);
                    if (!this.ghastSound.getValue()) {
                        continue;
                    }
                    Notifiers.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
            }
        }
    }
    
    public void loadFile() {
        final List<String> fileInput = FileManager.readTextFileAllLines("phobos/util/ModuleMessage_List.txt");
        final Iterator<String> i = fileInput.iterator();
        Notifiers.modules.clear();
        while (i.hasNext()) {
            final String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) {
                continue;
            }
            Notifiers.modules.add(s);
        }
    }
    
    static {
        Notifiers.TotemPopContainer = new HashMap<String, Integer>();
        modules = new ArrayList<String>();
        Notifiers.INSTANCE = new Notifiers();
        Notifiers.strengthPlayers = new HashSet<EntityPlayer>();
        Notifiers.strMap = new HashMap<EntityPlayer, Integer>();
    }
}
