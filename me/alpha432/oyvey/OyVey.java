// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.alpha432.oyvey.features.modules.misc.RPC;
import me.alpha432.oyvey.manager.Identify.IdentityManager;
import me.alpha432.oyvey.manager.SafetyManager;
import me.alpha432.oyvey.manager.HoleManager;
import me.alpha432.oyvey.manager.TotemPopManager;
import me.alpha432.oyvey.manager.ReloadManager;
import me.alpha432.oyvey.manager.PacketManager;
import me.alpha432.oyvey.manager.TimerManager;
import me.alpha432.oyvey.manager.InventoryManager;
import me.alpha432.oyvey.manager.PotionManager;
import me.alpha432.oyvey.manager.ServerManager;
import me.alpha432.oyvey.manager.ColorManager;
import me.alpha432.oyvey.manager.TextManager;
import me.alpha432.oyvey.manager.FriendManager;
import me.alpha432.oyvey.manager.ConfigManager;
import me.alpha432.oyvey.manager.FileManager;
import me.alpha432.oyvey.manager.EventManager;
import me.alpha432.oyvey.manager.CommandManager;
import me.alpha432.oyvey.manager.RotationManager;
import me.alpha432.oyvey.manager.PositionManager;
import me.alpha432.oyvey.manager.SpeedManager;
import me.alpha432.oyvey.manager.ModuleManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "quantum", name = "Quantum", version = "0.4.5")
public class OyVey
{
    public static final String MODID = "quantum";
    public static final String MODNAME = "Quantum";
    public static final String MODVER = "0.4.5";
    public static final Logger LOGGER;
    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static FriendManager friendManager;
    public static TextManager textManager;
    public static ColorManager colorManager;
    public static ServerManager serverManager;
    public static PotionManager potionManager;
    public static InventoryManager inventoryManager;
    public static TimerManager timerManager;
    public static PacketManager packetManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static HoleManager holeManager;
    public static SafetyManager safetyManager;
    public static IdentityManager identityManager;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        OyVey.LOGGER.info("\n\nLoading Quantum");
        OyVey.unloaded = false;
        if (OyVey.reloadManager != null) {
            OyVey.reloadManager.unload();
            OyVey.reloadManager = null;
        }
        OyVey.totemPopManager = new TotemPopManager();
        OyVey.timerManager = new TimerManager();
        OyVey.packetManager = new PacketManager();
        OyVey.serverManager = new ServerManager();
        OyVey.colorManager = new ColorManager();
        OyVey.textManager = new TextManager();
        OyVey.moduleManager = new ModuleManager();
        OyVey.speedManager = new SpeedManager();
        OyVey.rotationManager = new RotationManager();
        OyVey.positionManager = new PositionManager();
        OyVey.commandManager = new CommandManager();
        OyVey.eventManager = new EventManager();
        OyVey.fileManager = new FileManager();
        OyVey.friendManager = new FriendManager();
        OyVey.potionManager = new PotionManager();
        OyVey.inventoryManager = new InventoryManager();
        OyVey.configManager = new ConfigManager();
        OyVey.holeManager = new HoleManager();
        OyVey.safetyManager = new SafetyManager();
        OyVey.identityManager = new IdentityManager();
        OyVey.LOGGER.info("Initialized Managers");
        OyVey.moduleManager.init();
        OyVey.LOGGER.info("Modules loaded.");
        OyVey.configManager.init();
        OyVey.eventManager.init();
        OyVey.LOGGER.info("EventManager loaded.");
        OyVey.textManager.init(true);
        OyVey.moduleManager.onLoad();
        OyVey.totemPopManager.init();
        OyVey.timerManager.init();
        if (OyVey.moduleManager.getModuleByClass(RPC.class).isEnabled()) {
            DiscordPresence.start();
        }
        OyVey.LOGGER.info("\"Quantum initialized!\n");
    }
    
    public static void unload(final boolean unload) {
        OyVey.LOGGER.info("\n\nUnloading \"Quantum");
        if (unload) {
            (OyVey.reloadManager = new ReloadManager()).init((OyVey.commandManager != null) ? OyVey.commandManager.getPrefix() : ".");
        }
        onUnload();
        OyVey.eventManager = null;
        OyVey.holeManager = null;
        OyVey.timerManager = null;
        OyVey.moduleManager = null;
        OyVey.totemPopManager = null;
        OyVey.serverManager = null;
        OyVey.colorManager = null;
        OyVey.textManager = null;
        OyVey.speedManager = null;
        OyVey.rotationManager = null;
        OyVey.configManager = null;
        OyVey.positionManager = null;
        OyVey.commandManager = null;
        OyVey.fileManager = null;
        OyVey.friendManager = null;
        OyVey.potionManager = null;
        OyVey.inventoryManager = null;
        OyVey.safetyManager = null;
        OyVey.identityManager = null;
        OyVey.LOGGER.info("\"Quantum unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!OyVey.unloaded) {
            OyVey.eventManager.onUnload();
            OyVey.moduleManager.onUnload();
            OyVey.configManager.saveConfig(OyVey.configManager.config.replaceFirst("Quantum/", ""));
            OyVey.moduleManager.onUnloadPost();
            OyVey.timerManager.unload();
            OyVey.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        OyVey.LOGGER.info("Holy fuck!");
        OyVey.LOGGER.info("Big nigga");
        OyVey.LOGGER.info("i eat balls nigga i love kids i kidnap autistic kids");
        OyVey.LOGGER.info("my name is");
    }
    
    public final ColorManager getColorManager() {
        return OyVey.colorManager;
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Display.setTitle("Quantum v0.4.5");
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("Quantum");
        OyVey.unloaded = false;
    }
}
