package me.hextech;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.Queue;
import me.hextech.remapped.AltManager;
import me.hextech.remapped.BlurManager;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.ConfigManager;
import me.hextech.remapped.EventBus_bcCTmtyubWDfbrlTrSdl;
import me.hextech.remapped.FPSManager;
import me.hextech.remapped.FriendManager;
import me.hextech.remapped.GuiManager;
import me.hextech.remapped.MineManager_aMxFbgVZCMGgbqNPBFpw;
import me.hextech.remapped.ModuleManager;
import me.hextech.remapped.PlayerManager_fDCPIpFeekihQpSyxOff;
import me.hextech.remapped.PopManager;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.ServerManager;
import me.hextech.remapped.ShaderManager;
import me.hextech.remapped.SpeedManager;
import me.hextech.remapped.ThreadManager_BMAJbvVqHsNRgpJZHNrD;
import me.hextech.remapped.TimerManager;
import net.fabricmc.api.ModInitializer;

public final class HexTech
implements ModInitializer {
    public static final String NAME;
    public static final String LOG_NAME;
    public static final String CHAT_NAME;
    public static final String VERSION;
    public static final EventBus_bcCTmtyubWDfbrlTrSdl EVENT_BUS;
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
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        continue;
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
        EVENT_BUS.registerLambdaFactory((lookupInMethod, klass) -> (MethodHandles.Lookup)lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
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
        System.out.println("[HexTech-nightly Cracked By NoWhisper] Initialized and ready to play!");
        isLoaded = true;
        Runtime.getRuntime().addShutdownHook(new Thread(HexTech::save));
    }

    public static void unload() {
        System.out.println("[HexTech-nightly Cracked By NoWhisper] Unloading..");
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
        System.out.println("[HexTech-nightly Cracked By NoWhisper] Unloading success!");
    }

    public static void save() {
        System.out.println("[HexTech-nightly Cracked By NoWhisper] Saving...");
        CONFIG.saveSettings();
        FRIEND.saveFriends();
        ALT.saveAlts();
        System.out.println("[HexTech-nightly Cracked By NoWhisper] Saving success!");
        System.out.println(HexTech.MODULE.modules.size() + " modules loaded.");
    }

    public void onInitialize() {
        try {
            isLoaded = false;
        }
        catch (Throwable e) {
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
                }
                catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        });
        thread.start();
    }

    static {
        VERSION = "8";
        CHAT_NAME = "HexTech-nightly Cracked By NoWhisper";
        LOG_NAME = "HexTech-nightly Cracked By NoWhisper";
        NAME = "HexTech";
        EVENT_BUS = new EventBus_bcCTmtyubWDfbrlTrSdl();
        MESSAGE_QUEUE = new LinkedList<String>();
        COMMAND_QUEUE = new LinkedList<String>();
        PREFIX = ".";
        isLoaded = false;
    }
}
