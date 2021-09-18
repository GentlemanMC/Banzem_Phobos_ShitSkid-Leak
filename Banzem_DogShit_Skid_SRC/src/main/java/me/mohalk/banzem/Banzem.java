// 
// Decompiled by Procyon v0.5.36
// 

package me.mohalk.banzem;
import org.apache.logging.log4j.LogManager;
import me.mohalk.banzem.features.modules.module.Display;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.IOException;
import me.mohalk.banzem.features.modules.client.IRC;
import me.mohalk.banzem.features.modules.client.RPC;
import me.mohalk.banzem.manager.WaypointManager;
import me.mohalk.banzem.manager.NoStopManager;
import me.mohalk.banzem.features.gui.custom.GuiCustomMainScreen;
import me.mohalk.banzem.manager.SafetyManager;
import me.mohalk.banzem.manager.NotificationManager;
import me.mohalk.banzem.manager.HoleManager;
import me.mohalk.banzem.manager.TotemPopManager;
import me.mohalk.banzem.manager.ReloadManager;
import me.mohalk.banzem.manager.PacketManager;
import me.mohalk.banzem.manager.TimerManager;
import me.mohalk.banzem.manager.InventoryManager;
import me.mohalk.banzem.manager.PotionManager;
import me.mohalk.banzem.manager.ServerManager;
import me.mohalk.banzem.manager.ColorManager;
import me.mohalk.banzem.manager.TextManager;
import me.mohalk.banzem.manager.FriendManager;
import me.mohalk.banzem.manager.FileManager;
import me.mohalk.banzem.manager.ConfigManager;
import me.mohalk.banzem.manager.EventManager;
import me.mohalk.banzem.manager.CommandManager;
import me.mohalk.banzem.manager.RotationManager;
import me.mohalk.banzem.manager.PositionManager;
import me.mohalk.banzem.manager.SpeedManager;
import me.mohalk.banzem.manager.ModuleManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "banzem", name = "Banzem", version = "1.0.0")
public class Banzem
{
    public static final String MODID = "banzem";
    public static final String MODNAME = "Banzem";
    public static final String MODVER = "1.0.0";
    public static final Logger LOGGER;
    public static ModuleManager moduleManager;
    public static SpeedManager speedManager;
    public static PositionManager positionManager;
    public static RotationManager rotationManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static ConfigManager configManager;
    public static FileManager fileManager;
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
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;
    public static GuiCustomMainScreen customMainScreen;
    public static NoStopManager baritoneManager;
    public static WaypointManager waypointManager;
    @Mod.Instance
    public static Banzem INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        Banzem.LOGGER.info("\n\nLoading Banzem");
        Banzem.unloaded = false;
        if (Banzem.reloadManager != null) {
            Banzem.reloadManager.unload();
            Banzem.reloadManager = null;
        }
        Banzem.baritoneManager = new NoStopManager();
        Banzem.totemPopManager = new TotemPopManager();
        Banzem.timerManager = new TimerManager();
        Banzem.packetManager = new PacketManager();
        Banzem.serverManager = new ServerManager();
        Banzem.colorManager = new ColorManager();
        Banzem.textManager = new TextManager();
        Banzem.moduleManager = new ModuleManager();
        Banzem.speedManager = new SpeedManager();
        Banzem.rotationManager = new RotationManager();
        Banzem.positionManager = new PositionManager();
        Banzem.commandManager = new CommandManager();
        Banzem.eventManager = new EventManager();
        Banzem.configManager = new ConfigManager();
        Banzem.fileManager = new FileManager();
        Banzem.friendManager = new FriendManager();
        Banzem.potionManager = new PotionManager();
        Banzem.inventoryManager = new InventoryManager();
        Banzem.holeManager = new HoleManager();
        Banzem.notificationManager = new NotificationManager();
        Banzem.safetyManager = new SafetyManager();
        Banzem.waypointManager = new WaypointManager();
        Banzem.LOGGER.info("Initialized Managers");
        Banzem.moduleManager.init();
        Banzem.LOGGER.info("Modules loaded.");
        Banzem.configManager.init();
        Banzem.eventManager.init();
        Banzem.LOGGER.info("EventManager loaded.");
        Banzem.textManager.init(true);
        Banzem.moduleManager.onLoad();
        Banzem.totemPopManager.init();
        Banzem.timerManager.init();
        if (Banzem.moduleManager.getModuleByClass(RPC.class).isEnabled()) {
            DiscordPresence.start();
        }
    }
    
    public static void unload(final boolean unload) {
        Banzem.LOGGER.info("\n\nUnloading banzem");
        if (unload) {
            (Banzem.reloadManager = new ReloadManager()).init((Banzem.commandManager != null) ? Banzem.commandManager.getPrefix() : ".");
        }
        if (Banzem.baritoneManager != null) {
            Banzem.baritoneManager.stop();
        }
        onUnload();
        Banzem.eventManager = null;
        Banzem.holeManager = null;
        Banzem.timerManager = null;
        Banzem.moduleManager = null;
        Banzem.totemPopManager = null;
        Banzem.serverManager = null;
        Banzem.colorManager = null;
        Banzem.textManager = null;
        Banzem.speedManager = null;
        Banzem.rotationManager = null;
        Banzem.positionManager = null;
        Banzem.commandManager = null;
        Banzem.configManager = null;
        Banzem.fileManager = null;
        Banzem.friendManager = null;
        Banzem.potionManager = null;
        Banzem.inventoryManager = null;
        Banzem.notificationManager = null;
        Banzem.safetyManager = null;
        Banzem.LOGGER.info("banzem unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!Banzem.unloaded) {
            try {
                IRC.INSTANCE.disconnect();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Banzem.eventManager.onUnload();
            Banzem.moduleManager.onUnload();
            Banzem.configManager.saveConfig(Banzem.configManager.config.replaceFirst("banzem/", ""));
            Banzem.moduleManager.onUnloadPost();
            Banzem.timerManager.unload();
            Banzem.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Banzem.customMainScreen = new GuiCustomMainScreen();
        org.lwjgl.opengl.Display.setTitle((String)Display.getInstance().gang.getDefaultValue());
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("banzem");
        Banzem.unloaded = false;
    }
}
