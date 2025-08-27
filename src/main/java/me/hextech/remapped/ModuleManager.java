package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import me.hextech.HexTech;
import me.hextech.remapped.Ambience;
import me.hextech.remapped.AntiCev;
import me.hextech.remapped.AntiCheat;
import me.hextech.remapped.AntiCrawl;
import me.hextech.remapped.AntiHunger;
import me.hextech.remapped.AntiPiston;
import me.hextech.remapped.AntiRegear;
import me.hextech.remapped.AntiSpam;
import me.hextech.remapped.AntiVoid;
import me.hextech.remapped.AntiWeakness_SVNjAQUSXMmCfEPBellQ;
import me.hextech.remapped.AspectRatio;
import me.hextech.remapped.Aura;
import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.AutoArmor;
import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.AutoEXP;
import me.hextech.remapped.AutoEZ;
import me.hextech.remapped.AutoFuck;
import me.hextech.remapped.AutoMinePlus_frUaFZksknOJRqkizndn;
import me.hextech.remapped.AutoMine_TjXbWuTzfnbezzlShKiP;
import me.hextech.remapped.AutoPearl;
import me.hextech.remapped.AutoPot;
import me.hextech.remapped.AutoPotPlus;
import me.hextech.remapped.AutoQueue_kggvJAWJdfERirsncrmh;
import me.hextech.remapped.AutoReconnect;
import me.hextech.remapped.AutoRegear;
import me.hextech.remapped.AutoRespawn;
import me.hextech.remapped.AutoTrap;
import me.hextech.remapped.AutoWalk;
import me.hextech.remapped.BaseFinder;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.BedAura_BzCWaQEhnpenizjBqrRp;
import me.hextech.remapped.BedCrafter;
import me.hextech.remapped.BindSetting;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockHighLight;
import me.hextech.remapped.BlockerESP;
import me.hextech.remapped.Blocker_mEBqWazfEhCLEwVSYEFP;
import me.hextech.remapped.BowBomb;
import me.hextech.remapped.BreakESP;
import me.hextech.remapped.BugClip;
import me.hextech.remapped.BurrowAssist;
import me.hextech.remapped.BurrowMove;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.BypassSetting_RInKGmTQYgWFRhsUOiJP;
import me.hextech.remapped.CameraClip;
import me.hextech.remapped.ChatSetting_qVnAbgCzNciNTevKRovy;
import me.hextech.remapped.ChatSuffix;
import me.hextech.remapped.CleanInventory;
import me.hextech.remapped.Cleaner_iFwqnooxsJEmHoVteFeQ;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.ColorsSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.ComboBreaks;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.ConfigManager;
import me.hextech.remapped.Criticals;
import me.hextech.remapped.Crosshair;
import me.hextech.remapped.CrystalChams;
import me.hextech.remapped.CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI;
import me.hextech.remapped.CustomFov;
import me.hextech.remapped.Debug;
import me.hextech.remapped.DesyncESP_dCvptoNghaTFSegtZyHR;
import me.hextech.remapped.ESP;
import me.hextech.remapped.ElytraFly;
import me.hextech.remapped.EntityControl;
import me.hextech.remapped.FakeLag_pNelqtbEdFyayuoaPLch;
import me.hextech.remapped.FakePlayer;
import me.hextech.remapped.FastFall_mtLznGzMDzxhgBaLMnXD;
import me.hextech.remapped.FastUse;
import me.hextech.remapped.FastWeb_dehcwwTxEbDSnkFtZvNl;
import me.hextech.remapped.FinalHoleKick;
import me.hextech.remapped.Flight;
import me.hextech.remapped.ForceSync;
import me.hextech.remapped.FreeCam;
import me.hextech.remapped.FreeLook;
import me.hextech.remapped.HUD_ssNtBhEveKlCmIccBvAN;
import me.hextech.remapped.HitLog;
import me.hextech.remapped.HitMarker;
import me.hextech.remapped.HitboxDesync;
import me.hextech.remapped.HoleESP_uLDVZuHQKEvOMTkALgRO;
import me.hextech.remapped.HoleFiller;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.HolePush;
import me.hextech.remapped.HoleSnap;
import me.hextech.remapped.HotbarAnimation;
import me.hextech.remapped.IncreasesTime;
import me.hextech.remapped.Indicator_PdJeoIXjQQIjzfhnRXks;
import me.hextech.remapped.InventoryMove;
import me.hextech.remapped.ItemHUD;
import me.hextech.remapped.ItemTag;
import me.hextech.remapped.KillEffects;
import me.hextech.remapped.LogoutSpots;
import me.hextech.remapped.MCP;
import me.hextech.remapped.MainHand;
import me.hextech.remapped.MineTweak;
import me.hextech.remapped.Mod;
import me.hextech.remapped.ModuleList_ZBgBxeJhVhAvRjXaLZeK;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Moratorium;
import me.hextech.remapped.MoveUp;
import me.hextech.remapped.NameTags_NZLxiZHrtsQKbsfDngrN;
import me.hextech.remapped.NewBurrow_bHmPnkYIKEocVyqCgEHa;
import me.hextech.remapped.NoFall;
import me.hextech.remapped.NoInterp;
import me.hextech.remapped.NoRender;
import me.hextech.remapped.NoRotateSet;
import me.hextech.remapped.NoSlow_PaVUKKxFbWGbplzMaucl;
import me.hextech.remapped.NoSoundLag;
import me.hextech.remapped.Notification_lQoZqJolJVHgxQLLpwsm;
import me.hextech.remapped.Notify_EXlgYplaRzfgofOPOkyB;
import me.hextech.remapped.OffHand;
import me.hextech.remapped.PacketControl;
import me.hextech.remapped.PacketEat;
import me.hextech.remapped.PacketFly;
import me.hextech.remapped.PearlClip;
import me.hextech.remapped.PistonCrystal;
import me.hextech.remapped.PlaceRender;
import me.hextech.remapped.PopChams_WNWBvFQQYNjRmTHDKpkM;
import me.hextech.remapped.PopCounter;
import me.hextech.remapped.PortalGui;
import me.hextech.remapped.PredictionSetting;
import me.hextech.remapped.Quiver;
import me.hextech.remapped.Reach;
import me.hextech.remapped.Render3DEvent;
import me.hextech.remapped.Replenish;
import me.hextech.remapped.Rotation;
import me.hextech.remapped.SafeWalk;
import me.hextech.remapped.Scaffold;
import me.hextech.remapped.SelfFlatten;
import me.hextech.remapped.SelfTrap;
import me.hextech.remapped.SendNotification;
import me.hextech.remapped.ServerLagger_xbIbOIunYFUorlZcLJkD;
import me.hextech.remapped.Setting;
import me.hextech.remapped.Shader_CLqIXXaHSdAoBoxRSgjR;
import me.hextech.remapped.ShulkerViewer;
import me.hextech.remapped.SilentDisconnect;
import me.hextech.remapped.Skybox;
import me.hextech.remapped.Spammer;
import me.hextech.remapped.Speed;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.SpinBot;
import me.hextech.remapped.Sprint;
import me.hextech.remapped.Step_EShajbhvQeYkCdreEeNY;
import me.hextech.remapped.Strafe;
import me.hextech.remapped.Surround_BjIoVRziuWIfEWTJHPVz;
import me.hextech.remapped.TPAura_LycLkxHLQeGfgqfryvmV;
import me.hextech.remapped.TargetHud;
import me.hextech.remapped.TickShift;
import me.hextech.remapped.TotemAnimation;
import me.hextech.remapped.TotemParticle;
import me.hextech.remapped.Trajectories;
import me.hextech.remapped.TwoDESP_CLphFghCvliwVuLcyYHt;
import me.hextech.remapped.Velocity;
import me.hextech.remapped.ViewModel;
import me.hextech.remapped.WaterMark;
import me.hextech.remapped.Weather_BfaBZRqvqRbKrhUkvqny;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import me.hextech.remapped.Wrapper;
import me.hextech.remapped.XCarry;
import me.hextech.remapped.Zoom_qxASoURSmqLSKrnTPdNq;
import me.hextech.remapped.moveupV;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class ModuleManager
implements Wrapper {
    public static Mod lastLoadMod;
    public final HashMap<Module_JlagirAibYQgkHtbRnhw, Integer> categoryModules = new HashMap();
    public ArrayList<Module_eSdgMXWuzcxgQVaJFmKZ> modules = new ArrayList();

    public ModuleManager() {
        if (!ConfigManager.canSave) {
            return;
        }
        this.addModule(new WaterMark());
        this.addModule(new Spammer());
        this.addModule(new AutoFuck());
        this.addModule(new PredictionSetting());
        this.addModule(new HolePush());
        this.addModule(new AutoPot());
        this.addModule(new AutoMinePlus_frUaFZksknOJRqkizndn());
        this.addModule(new MainHand());
        this.addModule(new NewBurrow_bHmPnkYIKEocVyqCgEHa());
        this.addModule(new AutoPotPlus());
        this.addModule(new Flight());
        this.addModule(new MineTweak());
        this.addModule(new AutoRespawn());
        this.addModule(new AutoArmor());
        this.addModule(new HoleFiller());
        this.addModule(new FastWeb_dehcwwTxEbDSnkFtZvNl());
        this.addModule(new BugClip());
        this.addModule(new AutoPearl());
        this.addModule(new WebAuraTick_gaIdrzDzsbegzNTtPQoV());
        this.addModule(new XCarry());
        this.addModule(new HoleESP_uLDVZuHQKEvOMTkALgRO());
        this.addModule(new ElytraFly());
        this.addModule(new PlaceRender());
        this.addModule(new BowBomb());
        this.addModule(new AntiWeakness_SVNjAQUSXMmCfEPBellQ());
        this.addModule(new AutoEZ());
        this.addModule(new AntiSpam());
        this.addModule(new PacketFly());
        this.addModule(new EntityControl());
        this.addModule(new SilentDisconnect());
        this.addModule(new HoleSnap());
        this.addModule(new PacketEat());
        this.addModule(new Step_EShajbhvQeYkCdreEeNY());
        this.addModule(new Shader_CLqIXXaHSdAoBoxRSgjR());
        this.addModule(new ServerLagger_xbIbOIunYFUorlZcLJkD());
        this.addModule(new Strafe());
        this.addModule(new BurrowAssist());
        this.addModule(new Zoom_qxASoURSmqLSKrnTPdNq());
        this.addModule(new Scaffold());
        this.addModule(new BedAura_BzCWaQEhnpenizjBqrRp());
        this.addModule(new DesyncESP_dCvptoNghaTFSegtZyHR());
        this.addModule(new CrystalChams());
        this.addModule(new NoRender());
        this.addModule(new FastUse());
        this.addModule(new ModuleList_ZBgBxeJhVhAvRjXaLZeK());
        this.addModule(new Reach());
        this.addModule(new HotbarAnimation());
        this.addModule(new PopCounter());
        this.addModule(new SpeedMine());
        this.addModule(new ClickGui_ABoiivByuLsVqarYqfYv());
        this.addModule(new CameraClip());
        this.addModule(new CustomFov());
        this.addModule(new ChatSuffix());
        this.addModule(new FastFall_mtLznGzMDzxhgBaLMnXD());
        this.addModule(new AutoTrap());
        this.addModule(new AspectRatio());
        this.addModule(new PacketControl());
        this.addModule(new BlockHighLight());
        this.addModule(new BlockerESP());
        this.addModule(new Blocker_mEBqWazfEhCLEwVSYEFP());
        this.addModule(new HUD_ssNtBhEveKlCmIccBvAN());
        this.addModule(new PopChams_WNWBvFQQYNjRmTHDKpkM());
        this.addModule(new Burrow_eOaBGEoOSTDRbYIUAbXC());
        this.addModule(new AutoCrystal_QcRVYRsOqpKivetoXSJa());
        this.addModule(new Sprint());
        this.addModule(new TickShift());
        this.addModule(new AutoEXP());
        this.addModule(new AntiPiston());
        this.addModule(new AntiRegear());
        this.addModule(new Blink());
        this.addModule(new NameTags_NZLxiZHrtsQKbsfDngrN());
        this.addModule(new Velocity());
        this.addModule(new Trajectories());
        this.addModule(new AntiHunger());
        this.addModule(new LogoutSpots());
        this.addModule(new PortalGui());
        this.addModule(new CombatSetting_kxXrLvbWbduSuFoeBUsC());
        this.addModule(new SelfFlatten());
        this.addModule(new NoInterp());
        this.addModule(new AntiVoid());
        this.addModule(new HitMarker());
        this.addModule(new TotemAnimation());
        this.addModule(new Crosshair());
        this.addModule(new Indicator_PdJeoIXjQQIjzfhnRXks());
        this.addModule(new Speed());
        this.addModule(new ViewModel());
        this.addModule(new NoSoundLag());
        this.addModule(new CrystalPlaceESP_MvvdKnDNeuhVBnPUXMmI());
        this.addModule(new InventoryMove());
        this.addModule(new FakePlayer());
        this.addModule(new FreeCam());
        this.addModule(new AntiCheat());
        this.addModule(new Surround_BjIoVRziuWIfEWTJHPVz());
        this.addModule(new Aura());
        this.addModule(new TPAura_LycLkxHLQeGfgqfryvmV());
        this.addModule(new ESP());
        this.addModule(new Criticals());
        this.addModule(new ShulkerViewer());
        this.addModule(new AutoReconnect());
        this.addModule(new OffHand());
        this.addModule(new Quiver());
        this.addModule(new SpinBot());
        this.addModule(new AutoRegear());
        this.addModule(new PistonCrystal());
        this.addModule(new SafeWalk());
        this.addModule(new Ambience());
        this.addModule(new Skybox());
        this.addModule(new NoSlow_PaVUKKxFbWGbplzMaucl());
        this.addModule(new ForceSync());
        this.addModule(new NoFall());
        this.addModule(new NoRotateSet());
        this.addModule(new MCP());
        this.addModule(new ChatSetting_qVnAbgCzNciNTevKRovy());
        this.addModule(new BedCrafter());
        this.addModule(new BaseFinder());
        this.addModule(new TotemParticle());
        this.addModule(new TwoDESP_CLphFghCvliwVuLcyYHt());
        this.addModule(new BreakESP());
        this.addModule(new FakeLag_pNelqtbEdFyayuoaPLch());
        this.addModule(new FreeLook());
        this.addModule(new HitboxDesync());
        this.addModule(new Replenish());
        this.addModule(new Notify_EXlgYplaRzfgofOPOkyB());
        this.addModule(new PearlClip());
        this.addModule(new AutoWalk());
        this.addModule(new HoleKickTest());
        this.addModule(new MoveUp());
        this.addModule(new AntiCev());
        this.addModule(new HitLog());
        this.addModule(new BurrowMove());
        this.addModule(new ItemHUD());
        this.addModule(new AutoMine_TjXbWuTzfnbezzlShKiP());
        this.addModule(new Debug());
        this.addModule(new IncreasesTime());
        this.addModule(new FinalHoleKick());
        this.addModule(new BaseThreadSetting_TYdViPaJQVoRZLdgWIXF());
        this.addModule(new Weather_BfaBZRqvqRbKrhUkvqny());
        this.addModule(new Moratorium());
        this.addModule(new Rotation());
        this.addModule(new AutoQueue_kggvJAWJdfERirsncrmh());
        this.addModule(new ComboBreaks());
        this.addModule(new KillEffects());
        this.addModule(new ColorsSetting());
        this.addModule(new ItemTag());
        this.addModule(new Cleaner_iFwqnooxsJEmHoVteFeQ());
        this.addModule(new AutoAnchor_MDcwoWYRcPYheLZJWRZK());
        this.addModule(new moveupV());
        this.addModule(new AntiCrawl());
        this.addModule(new SelfTrap());
        this.addModule(new TargetHud());
        this.addModule(new CleanInventory());
        this.addModule(new Notification_lQoZqJolJVHgxQLLpwsm());
        this.addModule(new SendNotification());
        this.addModule(new BypassSetting_RInKGmTQYgWFRhsUOiJP());
        this.modules.sort(Comparator.comparing(Mod::getName));
    }

    public boolean setBind(int eventKey) {
        if (eventKey == -1 || eventKey == 0) {
            return false;
        }
        AtomicBoolean set = new AtomicBoolean(false);
        this.modules.forEach(module -> {
            for (Setting setting : module.getSettings()) {
                BindSetting bind;
                if (!(setting instanceof BindSetting) || !(bind = (BindSetting)setting).isListening()) continue;
                bind.setKey(eventKey);
                bind.setListening(false);
                if (bind.getBind().equals("DELETE")) {
                    bind.setKey(-1);
                }
                set.set(true);
            }
        });
        return set.get();
    }

    public void onKeyReleased(int eventKey) {
        if (eventKey == -1 || eventKey == 0) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey && module.getBind().isHoldEnable() && module.getBind().hold) {
                module.toggle();
                module.getBind().hold = false;
            }
            module.getSettings().stream().filter(setting -> setting instanceof BindSetting).map(setting -> (BindSetting)setting).filter(bindSetting -> bindSetting.getKey() == eventKey).forEach(bindSetting -> bindSetting.setPressed(false));
        });
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == -1 || eventKey == 0 || ModuleManager.mc.currentScreen instanceof ClickGuiScreen) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey && ModuleManager.mc.currentScreen == null) {
                module.toggle();
                module.getBind().hold = true;
            }
            module.getSettings().stream().filter(setting -> setting instanceof BindSetting).map(setting -> (BindSetting)setting).filter(bindSetting -> bindSetting.getKey() == eventKey).forEach(bindSetting -> bindSetting.setPressed(true));
        });
    }

    public void onThread() {
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
            try {
                module.onThread();
            }
            catch (Exception e) {
                e.printStackTrace();
                CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
                Notify_EXlgYplaRzfgofOPOkyB.sendNotify("\u00a74[!]\u7ebf\u7a0b\u8b66\u544a!");
            }
        });
    }

    public void onUpdate() {
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
            try {
                module.onUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
                CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
                Notify_EXlgYplaRzfgofOPOkyB.sendNotify("\u00a74[!]\u7a7a\u6307\u9488\u8b66\u544a\uff01!");
            }
        });
    }

    public void onLogin() {
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(Module_eSdgMXWuzcxgQVaJFmKZ::onLogin);
    }

    public void onLogout() {
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(Module_eSdgMXWuzcxgQVaJFmKZ::onLogout);
    }

    public void render2D(DrawContext drawContext) {
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> module.onRender2D(drawContext, MinecraftClient.getInstance().method_1488()));
    }

    public void render3D(MatrixStack matrixStack) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)2929);
        matrixStack.push();
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
            try {
                module.onRender3D(matrixStack, mc.method_1488());
            }
            catch (Exception e) {
                CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
            }
        });
        try {
            HexTech.EVENT_BUS.post(new Render3DEvent(matrixStack, mc.method_1488()));
        }
        catch (Exception e) {
            CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
        }
        matrixStack.pop();
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public void addModule(Module_eSdgMXWuzcxgQVaJFmKZ module) {
        module.add(module.getBind());
        this.modules.add(module);
        this.categoryModules.put(module.getCategory(), this.categoryModules.getOrDefault((Object)module.getCategory(), 0) + 1);
    }

    public void disableAll() {
        for (Module_eSdgMXWuzcxgQVaJFmKZ module : this.modules) {
            module.disable();
        }
    }

    public Module_eSdgMXWuzcxgQVaJFmKZ getModuleByName(String string) {
        for (Module_eSdgMXWuzcxgQVaJFmKZ module : this.modules) {
            if (!module.getName().equalsIgnoreCase(string)) continue;
            return module;
        }
        return null;
    }
}
