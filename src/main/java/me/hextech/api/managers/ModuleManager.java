package me.hextech.api.managers;

import me.hextech.HexTech;
import me.hextech.api.events.impl.Render3DEvent;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.Mod;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.client.*;
import me.hextech.mod.modules.impl.combat.*;
import me.hextech.mod.modules.impl.misc.*;
import me.hextech.mod.modules.impl.movement.*;
import me.hextech.mod.modules.impl.player.*;
import me.hextech.mod.modules.impl.player.freelook.FreeLook;
import me.hextech.mod.modules.impl.render.*;
import me.hextech.mod.modules.impl.render.sky.Skybox;
import me.hextech.mod.modules.impl.setting.*;
import me.hextech.mod.modules.settings.Setting;
import me.hextech.mod.modules.settings.impl.BindSetting;
import me.hextech.mod.modules.settings.impl.ColorsSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleManager
implements Wrapper {
    public static Mod lastLoadMod;
    public final HashMap<Module_eSdgMXWuzcxgQVaJFmKZ.Category, Integer> categoryModules = new HashMap();
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
        this.addModule(new WebAuraTick());
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
        this.addModule(new Zoom());
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
        this.addModule(new TwoDESP());
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
        this.addModule(new Weather());
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
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> module.onRender2D(drawContext, MinecraftClient.getInstance().getTickDelta()));
    }

    public void render3D(MatrixStack matrixStack) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glEnable(2884);
        GL11.glDisable(2929);
        matrixStack.push();
        this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
            try {
                module.onRender3D(matrixStack, mc.getTickDelta());
            }
            catch (Exception e) {
                CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
            }
        });
        try {
            HexTech.EVENT_BUS.post(new Render3DEvent(matrixStack, mc.getTickDelta()));
        }
        catch (Exception e) {
            CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
        }
        matrixStack.pop();
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public void addModule(Module_eSdgMXWuzcxgQVaJFmKZ module) {
        module.add(module.getBind());
        this.modules.add(module);
        this.categoryModules.put(module.getCategory(), this.categoryModules.getOrDefault(module.getCategory(), 0) + 1);
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
