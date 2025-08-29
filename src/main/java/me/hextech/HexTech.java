package me.hextech;

import me.hextech.api.alts.AltManager;
import me.hextech.api.events.eventbus.EventBus;
import me.hextech.api.managers.*;
import me.hextech.api.utils.render.BlurManager;
import net.fabricmc.api.ModInitializer;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.Queue;

public final class HexTech
        implements ModInitializer {
    public static final String NAME;
    public static final String LOG_NAME;
    public static final String CHAT_NAME;
    public static final String VERSION;
    public static final EventBus EVENT_BUS;
    public static final Queue<String> MESSAGE_QUEUE;
    public static final Queue<String> COMMAND_QUEUE;
    public static Thread thread;
    public static String PREFIX;
    public static PlayerManager_fDCPIpFeekihQpSyxOff PLAYER;
    public static BlurManager BLUR;
    public static ModuleManager MODULE;
    public static CommandManager COMMAND;
    public static AltManager ALT;
    public static GuiManager GUI;
    public static ConfigManager CONFIG;
    public static RotateManager ROTATE;
    public static MineManager_aMxFbgVZCMGgbqNPBFpw BREAK;
    public static PopManager POP;
    public static SpeedManager SPEED;
    public static FriendManager FRIEND;
    public static TimerManager TIMER;
    public static ShaderManager SHADER;
    public static FPSManager FPS;
    public static ServerManager SERVER;
    public static ThreadManager_BMAJbvVqHsNRgpJZHNrD THREAD;
    public static boolean isLoaded;

    static {
        VERSION = "8";
        CHAT_NAME = "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c";
        LOG_NAME = "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c";
        NAME = "HexTech";
        EVENT_BUS = new EventBus();
        MESSAGE_QUEUE = new LinkedList<String>();
        COMMAND_QUEUE = new LinkedList<String>();
        PREFIX = ".";
        isLoaded = false;
    }

    public static void update() {
        if (thread == null || !thread.isAlive() || thread.isInterrupted()) {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        while (true) {
                            if (MODULE == null) {
                                continue;
                            }
                            MODULE.onThread();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        MODULE.onUpdate();
        GUI.update();
        POP.update();
    }

    public static void load() throws Throwable {
        EVENT_BUS.registerLambdaFactory((lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        CONFIG = new ConfigManager();
        PREFIX = CONFIG.getString("prefix", ".");
        MODULE = new ModuleManager();
        COMMAND = new CommandManager();
        GUI = new GuiManager();
        THREAD = new ThreadManager_BMAJbvVqHsNRgpJZHNrD();
        ALT = new AltManager();
        BLUR = new BlurManager();
        FRIEND = new FriendManager();
        ROTATE = new RotateManager();
        BREAK = new MineManager_aMxFbgVZCMGgbqNPBFpw();
        POP = new PopManager();
        TIMER = new TimerManager();
        SHADER = new ShaderManager();
        FPS = new FPSManager();
        SERVER = new ServerManager();
        SPEED = new SpeedManager();
        PLAYER = new PlayerManager_fDCPIpFeekihQpSyxOff();
        CONFIG.loadSettings();
        System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Initialized and ready to play!");
        isLoaded = true;
        Runtime.getRuntime().addShutdownHook(new Thread(HexTech::save));
    }

    public static void unload() {
        System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Unloading..");
        HexTech.EVENT_BUS.listenerMap.clear();
        ConfigManager.resetModule();
        CONFIG = null;
        MODULE = null;
        COMMAND = null;
        GUI = null;
        ALT = null;
        FRIEND = null;
        ROTATE = null;
        POP = null;
        BLUR = null;
        TIMER = null;
        System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Unloading success!");
    }

    public static void save() {
        System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Saving...");
        CONFIG.saveSettings();
        FRIEND.saveFriends();
        ALT.saveAlts();
        System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Saving success!");
        System.out.println(HexTech.MODULE.modules.size() + " modules loaded.");
    }

    public void onInitialize() {
        try {
            isLoaded = false;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        thread = new Thread(() -> {
            while (true) {
                try {
                    while (true) {
                        if (MODULE == null) {
                            continue;
                        }
                        MODULE.onThread();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
