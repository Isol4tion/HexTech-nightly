package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class ModuleManager implements Wrapper {
   public static Mod lastLoadMod;
   public final HashMap<Module_JlagirAibYQgkHtbRnhw, Integer> categoryModules = new HashMap();
   public ArrayList<Module_eSdgMXWuzcxgQVaJFmKZ> modules = new ArrayList();

   public ModuleManager() {
      if (ConfigManager.canSave) {
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
   }

   public boolean setBind(int eventKey) {
      if (eventKey != -1 && eventKey != 0) {
         AtomicBoolean set = new AtomicBoolean(false);
         this.modules.forEach(module -> {
            for (Setting setting : module.getSettings()) {
               if (setting instanceof BindSetting) {
                  BindSetting bind = (BindSetting)setting;
                  if (bind.isListening()) {
                     bind.setKey(eventKey);
                     bind.setListening(false);
                     if (bind.getBind().equals("DELETE")) {
                        bind.setKey(-1);
                     }

                     set.set(true);
                  }
               }
            }
         });
         return set.get();
      } else {
         return false;
      }
   }

   public void onKeyReleased(int eventKey) {
      if (eventKey != -1 && eventKey != 0) {
         this.modules
            .forEach(
               module -> {
                  if (module.getBind().getKey() == eventKey && module.getBind().isHoldEnable() && module.getBind().hold) {
                     module.toggle();
                     module.getBind().hold = false;
                  }

                  module.getSettings()
                     .stream()
                     .filter(setting -> setting instanceof BindSetting)
                     .map(setting -> (BindSetting)setting)
                     .filter(bindSetting -> bindSetting.getKey() == eventKey)
                     .forEach(bindSetting -> bindSetting.setPressed(false));
               }
            );
      }
   }

   public void onKeyPressed(int eventKey) {
      if (eventKey != -1 && eventKey != 0 && !(mc.field_1755 instanceof ClickGuiScreen)) {
         this.modules
            .forEach(
               module -> {
                  if (module.getBind().getKey() == eventKey && mc.field_1755 == null) {
                     module.toggle();
                     module.getBind().hold = true;
                  }

                  module.getSettings()
                     .stream()
                     .filter(setting -> setting instanceof BindSetting)
                     .map(setting -> (BindSetting)setting)
                     .filter(bindSetting -> bindSetting.getKey() == eventKey)
                     .forEach(bindSetting -> bindSetting.setPressed(true));
               }
            );
      }
   }

   public void onThread() {
      this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
         try {
            module.onThread();
         } catch (Exception var2) {
            var2.printStackTrace();
            CommandManager.sendChatMessage("§4[!] " + var2.getMessage());
            Notify_EXlgYplaRzfgofOPOkyB.sendNotify("§4[!]线程警告!");
         }
      });
   }

   public void onUpdate() {
      this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
         try {
            module.onUpdate();
         } catch (Exception var2) {
            var2.printStackTrace();
            CommandManager.sendChatMessage("§4[!] " + var2.getMessage());
            Notify_EXlgYplaRzfgofOPOkyB.sendNotify("§4[!]空指针警告！!");
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
      this.modules
         .stream()
         .filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn)
         .forEach(module -> module.onRender2D(drawContext, MinecraftClient.method_1551().method_1488()));
   }

   public void render3D(MatrixStack matrixStack) {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glEnable(2884);
      GL11.glDisable(2929);
      matrixStack.method_22903();
      this.modules.stream().filter(Module_eSdgMXWuzcxgQVaJFmKZ::isOn).forEach(module -> {
         try {
            module.onRender3D(matrixStack, mc.method_1488());
         } catch (Exception var3x) {
            CommandManager.sendChatMessage("§4[!] " + var3x.getMessage());
         }
      });

      try {
         me.hextech.HexTech.EVENT_BUS.post(new Render3DEvent(matrixStack, mc.method_1488()));
      } catch (Exception var3) {
         CommandManager.sendChatMessage("§4[!] " + var3.getMessage());
      }

      matrixStack.method_22909();
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }

   public void addModule(Module_eSdgMXWuzcxgQVaJFmKZ module) {
      module.add(module.getBind());
      this.modules.add(module);
      this.categoryModules.put(module.getCategory(), (Integer)this.categoryModules.getOrDefault(module.getCategory(), 0) + 1);
   }

   public void disableAll() {
      for (Module_eSdgMXWuzcxgQVaJFmKZ module : this.modules) {
         module.disable();
      }
   }

   public Module_eSdgMXWuzcxgQVaJFmKZ getModuleByName(String string) {
      for (Module_eSdgMXWuzcxgQVaJFmKZ module : this.modules) {
         if (module.getName().equalsIgnoreCase(string)) {
            return module;
         }
      }

      return null;
   }
}
