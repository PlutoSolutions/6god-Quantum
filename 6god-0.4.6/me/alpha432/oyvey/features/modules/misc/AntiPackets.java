// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import java.util.Iterator;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketAnimation;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AntiPackets extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Integer> page;
    private final Setting<Integer> pages;
    private final Setting<Boolean> AdvancementInfo;
    private final Setting<Boolean> Animation;
    private final Setting<Boolean> BlockAction;
    private final Setting<Boolean> BlockBreakAnim;
    private final Setting<Boolean> BlockChange;
    private final Setting<Boolean> Camera;
    private final Setting<Boolean> ChangeGameState;
    private final Setting<Boolean> Chat;
    private final Setting<Boolean> ChunkData;
    private final Setting<Boolean> CloseWindow;
    private final Setting<Boolean> CollectItem;
    private final Setting<Boolean> CombatEvent;
    private final Setting<Boolean> ConfirmTransaction;
    private final Setting<Boolean> Cooldown;
    private final Setting<Boolean> CustomPayload;
    private final Setting<Boolean> CustomSound;
    private final Setting<Boolean> DestroyEntities;
    private final Setting<Boolean> Disconnect;
    private final Setting<Boolean> DisplayObjective;
    private final Setting<Boolean> Effect;
    private final Setting<Boolean> Entity;
    private final Setting<Boolean> EntityAttach;
    private final Setting<Boolean> EntityEffect;
    private final Setting<Boolean> EntityEquipment;
    private final Setting<Boolean> EntityHeadLook;
    private final Setting<Boolean> EntityMetadata;
    private final Setting<Boolean> EntityProperties;
    private final Setting<Boolean> EntityStatus;
    private final Setting<Boolean> EntityTeleport;
    private final Setting<Boolean> EntityVelocity;
    private final Setting<Boolean> Explosion;
    private final Setting<Boolean> HeldItemChange;
    private final Setting<Boolean> JoinGame;
    private final Setting<Boolean> KeepAlive;
    private final Setting<Boolean> Maps;
    private final Setting<Boolean> MoveVehicle;
    private final Setting<Boolean> MultiBlockChange;
    private final Setting<Boolean> OpenWindow;
    private final Setting<Boolean> Particles;
    private final Setting<Boolean> PlaceGhostRecipe;
    private final Setting<Boolean> PlayerAbilities;
    private final Setting<Boolean> PlayerListHeaderFooter;
    private final Setting<Boolean> PlayerListItem;
    private final Setting<Boolean> PlayerPosLook;
    private final Setting<Boolean> RecipeBook;
    private final Setting<Boolean> RemoveEntityEffect;
    private final Setting<Boolean> ResourcePackSend;
    private final Setting<Boolean> Respawn;
    private final Setting<Boolean> ScoreboardObjective;
    private final Setting<Boolean> SelectAdvancementsTab;
    private final Setting<Boolean> ServerDifficulty;
    private final Setting<Boolean> SetExperience;
    private final Setting<Boolean> SetPassengers;
    private final Setting<Boolean> SetSlot;
    private final Setting<Boolean> SignEditorOpen;
    private final Setting<Boolean> SoundEffect;
    private final Setting<Boolean> SpawnExperienceOrb;
    private final Setting<Boolean> SpawnGlobalEntity;
    private final Setting<Boolean> SpawnMob;
    private final Setting<Boolean> SpawnObject;
    private final Setting<Boolean> SpawnPainting;
    private final Setting<Boolean> SpawnPlayer;
    private final Setting<Boolean> SpawnPosition;
    private final Setting<Boolean> Statistics;
    private final Setting<Boolean> TabComplete;
    private final Setting<Boolean> Teams;
    private final Setting<Boolean> TimeUpdate;
    private final Setting<Boolean> Title;
    private final Setting<Boolean> UnloadChunk;
    private final Setting<Boolean> UpdateBossInfo;
    private final Setting<Boolean> UpdateHealth;
    private final Setting<Boolean> UpdateScore;
    private final Setting<Boolean> UpdateTileEntity;
    private final Setting<Boolean> UseBed;
    private final Setting<Boolean> WindowItems;
    private final Setting<Boolean> WindowProperty;
    private final Setting<Boolean> WorldBorder;
    private final Setting<Boolean> Animations;
    private final Setting<Boolean> ChatMessage;
    private final Setting<Boolean> ClickWindow;
    private final Setting<Boolean> ClientSettings;
    private final Setting<Boolean> ClientStatus;
    private final Setting<Boolean> CloseWindows;
    private final Setting<Boolean> ConfirmTeleport;
    private final Setting<Boolean> ConfirmTransactions;
    private final Setting<Boolean> CreativeInventoryAction;
    private final Setting<Boolean> CustomPayloads;
    private final Setting<Boolean> EnchantItem;
    private final Setting<Boolean> EntityAction;
    private final Setting<Boolean> HeldItemChanges;
    private final Setting<Boolean> Input;
    private final Setting<Boolean> KeepAlives;
    private final Setting<Boolean> PlaceRecipe;
    private final Setting<Boolean> Player;
    private final Setting<Boolean> PlayerAbility;
    private final Setting<Boolean> PlayerDigging;
    private final Setting<Boolean> PlayerTryUseItem;
    private final Setting<Boolean> PlayerTryUseItemOnBlock;
    private final Setting<Boolean> RecipeInfo;
    private final Setting<Boolean> ResourcePackStatus;
    private final Setting<Boolean> SeenAdvancements;
    private final Setting<Boolean> PlayerPackets;
    private final Setting<Boolean> Spectate;
    private final Setting<Boolean> SteerBoat;
    private final Setting<Boolean> TabCompletion;
    private final Setting<Boolean> UpdateSign;
    private final Setting<Boolean> UseEntity;
    private final Setting<Boolean> VehicleMove;
    private int hudAmount;
    
    public AntiPackets() {
        super("PacketCancel", "pc", Category.MISC, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Packets", (T)Mode.CLIENT));
        this.page = (Setting<Integer>)this.register(new Setting("SPackets", (T)1, (T)1, (T)10, v -> this.mode.getValue() == Mode.SERVER));
        this.pages = (Setting<Integer>)this.register(new Setting("CPackets", (T)1, (T)1, (T)4, v -> this.mode.getValue() == Mode.CLIENT));
        this.AdvancementInfo = (Setting<Boolean>)this.register(new Setting("AdvancementInfo", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.Animation = (Setting<Boolean>)this.register(new Setting("Animation", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.BlockAction = (Setting<Boolean>)this.register(new Setting("BlockAction", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.BlockBreakAnim = (Setting<Boolean>)this.register(new Setting("BlockBreakAnim", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.BlockChange = (Setting<Boolean>)this.register(new Setting("BlockChange", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.Camera = (Setting<Boolean>)this.register(new Setting("Camera", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.ChangeGameState = (Setting<Boolean>)this.register(new Setting("ChangeGameState", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.Chat = (Setting<Boolean>)this.register(new Setting("Chat", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1));
        this.ChunkData = (Setting<Boolean>)this.register(new Setting("ChunkData", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.CloseWindow = (Setting<Boolean>)this.register(new Setting("CloseWindow", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.CollectItem = (Setting<Boolean>)this.register(new Setting("CollectItem", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.CombatEvent = (Setting<Boolean>)this.register(new Setting("Combatevent", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.ConfirmTransaction = (Setting<Boolean>)this.register(new Setting("ConfirmTransaction", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.Cooldown = (Setting<Boolean>)this.register(new Setting("Cooldown", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.CustomPayload = (Setting<Boolean>)this.register(new Setting("CustomPayload", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.CustomSound = (Setting<Boolean>)this.register(new Setting("CustomSound", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2));
        this.DestroyEntities = (Setting<Boolean>)this.register(new Setting("DestroyEntities", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.Disconnect = (Setting<Boolean>)this.register(new Setting("Disconnect", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.DisplayObjective = (Setting<Boolean>)this.register(new Setting("DisplayObjective", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.Effect = (Setting<Boolean>)this.register(new Setting("Effect", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.Entity = (Setting<Boolean>)this.register(new Setting("Entity", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.EntityAttach = (Setting<Boolean>)this.register(new Setting("EntityAttach", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.EntityEffect = (Setting<Boolean>)this.register(new Setting("EntityEffect", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.EntityEquipment = (Setting<Boolean>)this.register(new Setting("EntityEquipment", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3));
        this.EntityHeadLook = (Setting<Boolean>)this.register(new Setting("EntityHeadLook", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.EntityMetadata = (Setting<Boolean>)this.register(new Setting("EntityMetadata", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.EntityProperties = (Setting<Boolean>)this.register(new Setting("EntityProperties", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.EntityStatus = (Setting<Boolean>)this.register(new Setting("EntityStatus", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.EntityTeleport = (Setting<Boolean>)this.register(new Setting("EntityTeleport", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.EntityVelocity = (Setting<Boolean>)this.register(new Setting("EntityVelocity", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.Explosion = (Setting<Boolean>)this.register(new Setting("Explosion", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.HeldItemChange = (Setting<Boolean>)this.register(new Setting("HeldItemChange", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4));
        this.JoinGame = (Setting<Boolean>)this.register(new Setting("JoinGame", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.KeepAlive = (Setting<Boolean>)this.register(new Setting("KeepAlive", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.Maps = (Setting<Boolean>)this.register(new Setting("Maps", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.MoveVehicle = (Setting<Boolean>)this.register(new Setting("MoveVehicle", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.MultiBlockChange = (Setting<Boolean>)this.register(new Setting("MultiBlockChange", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.OpenWindow = (Setting<Boolean>)this.register(new Setting("OpenWindow", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.Particles = (Setting<Boolean>)this.register(new Setting("Particles", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.PlaceGhostRecipe = (Setting<Boolean>)this.register(new Setting("PlaceGhostRecipe", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5));
        this.PlayerAbilities = (Setting<Boolean>)this.register(new Setting("PlayerAbilities", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.PlayerListHeaderFooter = (Setting<Boolean>)this.register(new Setting("PlayerListHeaderFooter", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.PlayerListItem = (Setting<Boolean>)this.register(new Setting("PlayerListItem", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.PlayerPosLook = (Setting<Boolean>)this.register(new Setting("PlayerPosLook", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.RecipeBook = (Setting<Boolean>)this.register(new Setting("RecipeBook", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.RemoveEntityEffect = (Setting<Boolean>)this.register(new Setting("RemoveEntityEffect", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.ResourcePackSend = (Setting<Boolean>)this.register(new Setting("ResourcePackSend", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.Respawn = (Setting<Boolean>)this.register(new Setting("Respawn", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6));
        this.ScoreboardObjective = (Setting<Boolean>)this.register(new Setting("ScoreboardObjective", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SelectAdvancementsTab = (Setting<Boolean>)this.register(new Setting("SelectAdvancementsTab", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.ServerDifficulty = (Setting<Boolean>)this.register(new Setting("ServerDifficulty", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SetExperience = (Setting<Boolean>)this.register(new Setting("SetExperience", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SetPassengers = (Setting<Boolean>)this.register(new Setting("SetPassengers", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SetSlot = (Setting<Boolean>)this.register(new Setting("SetSlot", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SignEditorOpen = (Setting<Boolean>)this.register(new Setting("SignEditorOpen", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SoundEffect = (Setting<Boolean>)this.register(new Setting("SoundEffect", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7));
        this.SpawnExperienceOrb = (Setting<Boolean>)this.register(new Setting("SpawnExperienceOrb", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.SpawnGlobalEntity = (Setting<Boolean>)this.register(new Setting("SpawnGlobalEntity", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.SpawnMob = (Setting<Boolean>)this.register(new Setting("SpawnMob", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.SpawnObject = (Setting<Boolean>)this.register(new Setting("SpawnObject", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.SpawnPainting = (Setting<Boolean>)this.register(new Setting("SpawnPainting", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.SpawnPlayer = (Setting<Boolean>)this.register(new Setting("SpawnPlayer", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.SpawnPosition = (Setting<Boolean>)this.register(new Setting("SpawnPosition", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.Statistics = (Setting<Boolean>)this.register(new Setting("Statistics", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8));
        this.TabComplete = (Setting<Boolean>)this.register(new Setting("TabComplete", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.Teams = (Setting<Boolean>)this.register(new Setting("Teams", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.TimeUpdate = (Setting<Boolean>)this.register(new Setting("TimeUpdate", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.Title = (Setting<Boolean>)this.register(new Setting("Title", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.UnloadChunk = (Setting<Boolean>)this.register(new Setting("UnloadChunk", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.UpdateBossInfo = (Setting<Boolean>)this.register(new Setting("UpdateBossInfo", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.UpdateHealth = (Setting<Boolean>)this.register(new Setting("UpdateHealth", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.UpdateScore = (Setting<Boolean>)this.register(new Setting("UpdateScore", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9));
        this.UpdateTileEntity = (Setting<Boolean>)this.register(new Setting("UpdateTileEntity", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
        this.UseBed = (Setting<Boolean>)this.register(new Setting("UseBed", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
        this.WindowItems = (Setting<Boolean>)this.register(new Setting("WindowItems", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
        this.WindowProperty = (Setting<Boolean>)this.register(new Setting("WindowProperty", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
        this.WorldBorder = (Setting<Boolean>)this.register(new Setting("WorldBorder", (T)false, v -> this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10));
        this.Animations = (Setting<Boolean>)this.register(new Setting("Animations", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.ChatMessage = (Setting<Boolean>)this.register(new Setting("ChatMessage", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.ClickWindow = (Setting<Boolean>)this.register(new Setting("ClickWindow", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.ClientSettings = (Setting<Boolean>)this.register(new Setting("ClientSettings", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.ClientStatus = (Setting<Boolean>)this.register(new Setting("ClientStatus", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.CloseWindows = (Setting<Boolean>)this.register(new Setting("CloseWindows", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.ConfirmTeleport = (Setting<Boolean>)this.register(new Setting("ConfirmTeleport", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.ConfirmTransactions = (Setting<Boolean>)this.register(new Setting("ConfirmTransactions", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1));
        this.CreativeInventoryAction = (Setting<Boolean>)this.register(new Setting("CreativeInventoryAction", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.CustomPayloads = (Setting<Boolean>)this.register(new Setting("CustomPayloads", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.EnchantItem = (Setting<Boolean>)this.register(new Setting("EnchantItem", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.EntityAction = (Setting<Boolean>)this.register(new Setting("EntityAction", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.HeldItemChanges = (Setting<Boolean>)this.register(new Setting("HeldItemChanges", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.Input = (Setting<Boolean>)this.register(new Setting("Input", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.KeepAlives = (Setting<Boolean>)this.register(new Setting("KeepAlives", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.PlaceRecipe = (Setting<Boolean>)this.register(new Setting("PlaceRecipe", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2));
        this.Player = (Setting<Boolean>)this.register(new Setting("Player", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.PlayerAbility = (Setting<Boolean>)this.register(new Setting("PlayerAbility", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.PlayerDigging = (Setting<Boolean>)this.register(new Setting("PlayerDigging", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.page.getValue() == 3));
        this.PlayerTryUseItem = (Setting<Boolean>)this.register(new Setting("PlayerTryUseItem", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.PlayerTryUseItemOnBlock = (Setting<Boolean>)this.register(new Setting("TryUseItemOnBlock", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.RecipeInfo = (Setting<Boolean>)this.register(new Setting("RecipeInfo", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.ResourcePackStatus = (Setting<Boolean>)this.register(new Setting("ResourcePackStatus", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.SeenAdvancements = (Setting<Boolean>)this.register(new Setting("SeenAdvancements", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3));
        this.PlayerPackets = (Setting<Boolean>)this.register(new Setting("PlayerPackets", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.Spectate = (Setting<Boolean>)this.register(new Setting("Spectate", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.SteerBoat = (Setting<Boolean>)this.register(new Setting("SteerBoat", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.TabCompletion = (Setting<Boolean>)this.register(new Setting("TabCompletion", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.UpdateSign = (Setting<Boolean>)this.register(new Setting("UpdateSign", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.UseEntity = (Setting<Boolean>)this.register(new Setting("UseEntity", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.VehicleMove = (Setting<Boolean>)this.register(new Setting("VehicleMove", (T)false, v -> this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4));
        this.hudAmount = 0;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (!this.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation && this.Animations.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketChatMessage && this.ChatMessage.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClickWindow && this.ClickWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings && this.ClientSettings.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketClientStatus && this.ClientStatus.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCloseWindow && this.CloseWindows.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport && this.ConfirmTeleport.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction && this.ConfirmTransactions.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCreativeInventoryAction && this.CreativeInventoryAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && this.CustomPayloads.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEnchantItem && this.EnchantItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction && this.EntityAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && this.HeldItemChanges.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketInput && this.Input.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketKeepAlive && this.KeepAlives.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlaceRecipe && this.PlaceRecipe.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && this.Player.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerAbilities && this.PlayerAbility.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && this.PlayerDigging.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && this.PlayerTryUseItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.PlayerTryUseItemOnBlock.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketRecipeInfo && this.RecipeInfo.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketResourcePackStatus && this.ResourcePackStatus.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSeenAdvancements && this.SeenAdvancements.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSpectate && this.Spectate.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketSteerBoat && this.SteerBoat.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketTabComplete && this.TabCompletion.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUpdateSign && this.UpdateSign.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketUseEntity && this.UseEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketVehicleMove && this.VehicleMove.getValue()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (!this.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof SPacketAdvancementInfo && this.AdvancementInfo.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketAnimation && this.Animation.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockAction && this.BlockAction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim && this.BlockBreakAnim.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketBlockChange && this.BlockChange.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCamera && this.Camera.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChangeGameState && this.ChangeGameState.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChat && this.Chat.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCombatEvent && this.CombatEvent.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketConfirmTransaction && this.ConfirmTransaction.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCooldown && this.Cooldown.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomPayload && this.CustomPayload.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCustomSound && this.CustomSound.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDestroyEntities && this.DestroyEntities.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisconnect && this.Disconnect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketDisplayObjective && this.DisplayObjective.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEffect && this.Effect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntity && this.Entity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityAttach && this.EntityAttach.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEffect && this.EntityEffect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityEquipment && this.EntityEquipment.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityHeadLook && this.EntityHeadLook.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityMetadata && this.EntityMetadata.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityProperties && this.EntityProperties.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityStatus && this.EntityStatus.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityTeleport && this.EntityTeleport.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && this.EntityVelocity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketExplosion && this.Explosion.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketHeldItemChange && this.HeldItemChange.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketJoinGame && this.JoinGame.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketKeepAlive && this.KeepAlive.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMaps && this.Maps.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMoveVehicle && this.MoveVehicle.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketMultiBlockChange && this.MultiBlockChange.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketOpenWindow && this.OpenWindow.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketParticles && this.Particles.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlaceGhostRecipe && this.PlaceGhostRecipe.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerAbilities && this.PlayerAbilities.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListHeaderFooter && this.PlayerListHeaderFooter.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && this.PlayerListItem.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.PlayerPosLook.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRecipeBook && this.RecipeBook.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRemoveEntityEffect && this.RemoveEntityEffect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketResourcePackSend && this.ResourcePackSend.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketRespawn && this.Respawn.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketScoreboardObjective && this.ScoreboardObjective.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSelectAdvancementsTab && this.SelectAdvancementsTab.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketServerDifficulty && this.ServerDifficulty.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetExperience && this.SetExperience.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetPassengers && this.SetPassengers.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSetSlot && this.SetSlot.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSignEditorOpen && this.SignEditorOpen.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSoundEffect && this.SoundEffect.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnExperienceOrb && this.SpawnExperienceOrb.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnGlobalEntity && this.SpawnGlobalEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnMob && this.SpawnMob.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnObject && this.SpawnObject.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPainting && this.SpawnPainting.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPlayer && this.SpawnPlayer.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketSpawnPosition && this.SpawnPosition.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketStatistics && this.Statistics.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTabComplete && this.TabComplete.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTeams && this.Teams.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTimeUpdate && this.TimeUpdate.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketTitle && this.Title.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUnloadChunk && this.UnloadChunk.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateBossInfo && this.UpdateBossInfo.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateHealth && this.UpdateHealth.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateScore && this.UpdateScore.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUpdateTileEntity && this.UpdateTileEntity.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketUseBed && this.UseBed.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowItems && this.WindowItems.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWindowProperty && this.WindowProperty.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketWorldBorder && this.WorldBorder.getValue()) {
            event.setCanceled(true);
        }
    }
    
    @Override
    public void onEnable() {
        final String standart = "§aAntiPackets On!§f Cancelled Packets: ";
        final StringBuilder text = new StringBuilder(standart);
        if (!this.settings.isEmpty()) {
            for (final Setting setting : this.settings) {
                if (setting.getValue() instanceof Boolean && setting.getValue() && !setting.getName().equalsIgnoreCase("Enabled")) {
                    if (setting.getName().equalsIgnoreCase("drawn")) {
                        continue;
                    }
                    final String name = setting.getName();
                    text.append(name).append(", ");
                }
            }
        }
        if (text.toString().equals(standart)) {
            Command.sendMessage("§aAntiPackets On!§f Currently not cancelling any Packets.");
        }
        else {
            final String output = this.removeLastChar(this.removeLastChar(text.toString()));
            Command.sendMessage(output);
        }
    }
    
    @Override
    public void onUpdate() {
        int amount = 0;
        if (!this.settings.isEmpty()) {
            for (final Setting setting : this.settings) {
                if (setting.getValue() instanceof Boolean && setting.getValue() && !setting.getName().equalsIgnoreCase("Enabled")) {
                    if (setting.getName().equalsIgnoreCase("drawn")) {
                        continue;
                    }
                    ++amount;
                }
            }
        }
        this.hudAmount = amount;
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.hudAmount == 0) {
            return "";
        }
        return this.hudAmount + "";
    }
    
    public String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    
    public enum Mode
    {
        CLIENT, 
        SERVER;
    }
}
