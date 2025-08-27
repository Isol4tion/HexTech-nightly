package me.hextech.remapped;

public class AntiCheat extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public final EnumSetting<AntiCheat_ylqyOfVuMcYIoHfjTCiI> page = this.add(new EnumSetting("Page", AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong));
   public final SliderSetting xiaosongstepHeight = this.add(
      new SliderSetting("StepHeight[CN]", 3, 0, 4, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong))
   );
   public final SliderSetting xinstepHeight = this.add(new SliderSetting("StepHeight[Xin]", 2, 0, 4, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin)));
   private final BooleanSetting CNconfig = this.add(new BooleanSetting("XiaoSong", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
   private final BooleanSetting xiaosonginvSwap = this.add(new BooleanSetting("InvSwap[CN]", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
   private final BooleanSetting xiaosongfastWeb = this.add(new BooleanSetting("FastWeb[CN]", true, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
   private final BooleanSetting xiaosongfastWebGround = this.add(
      new BooleanSetting("FastWebGround[CN]", true, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong))
   );
   private final BooleanSetting xiaosongStepWebPause = this.add(
      new BooleanSetting("StepWebPauseOn[CN]", true, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong))
   );
   private final BooleanSetting xiaosongburrowWeb = this.add(
      new BooleanSetting("WebBurrowCheck[CN]", true, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.XiaoSong))
   );
   private final BooleanSetting xin = this.add(new BooleanSetting("Xin", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin)));
   private final BooleanSetting xininswap = this.add(new BooleanSetting("InvSwap[Xin]", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin)));
   private final BooleanSetting xinfastweb = this.add(new BooleanSetting("FastWeb[Xin]", true, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin)));
   private final BooleanSetting xinfastWebGround = this.add(
      new BooleanSetting("FastWebGround[Xin]", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin))
   );
   private final BooleanSetting xinStepinWebPause = this.add(
      new BooleanSetting("StepWebPauseOn[Xin]", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin))
   );
   private final BooleanSetting xinburrowWeb = this.add(new BooleanSetting("WebBurrowCheck[Xin]", false, v -> this.page.is(AntiCheat_ylqyOfVuMcYIoHfjTCiI.Xin)));
   private boolean needUpdate = true;

   public AntiCheat() {
      super("AntiCheat", Module_JlagirAibYQgkHtbRnhw.Setting);
   }

   @Override
   public void onLogin() {
      this.needUpdate = true;
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         if (this.needUpdate) {
            if (!mc.method_1542()) {
               String server = mc.field_1724.field_3944.method_45734().field_3761.toLowerCase();
               if (this.xin.getValue()) {
                  if (server.equals("2b2t.xin")) {
                     CommandManager.sendChatMessage("§6配置同步：2B2T.Xin");
                     Notify_EXlgYplaRzfgofOPOkyB.sendNotify("§6配置同步：2B2T.Xin");
                     Step_EShajbhvQeYkCdreEeNY.INSTANCE.height.setValue((double)this.xinstepHeight.getValueFloat());
                     Step_EShajbhvQeYkCdreEeNY.INSTANCE.inWebPause.setValue(this.xinStepinWebPause.getValue());
                     if (this.xininswap.getValue()) {
                        CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.invSwapBypass.setValue(false);
                        BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.SwapSync.setValue(false);
                     }

                     if (this.xinfastweb.getValue()) {
                        FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.groundcheck.setValue(this.xinfastWebGround.getValue());
                     }

                     if (this.xinburrowWeb.getValue()) {
                        Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.webcheck.setValue(this.xinburrowWeb.getValue());
                     }
                  } else if (this.CNconfig.getValue()
                     && (
                        server.equals("2b2tpvp.cn")
                           || server.equals("CrystalPVP.cn")
                           || server.equals("markpvp.kozow.com")
                           || server.equals("play.simpfun.cn:16266")
                           || server.equals("play.simpfun.cn:32672")
                     )) {
                     CommandManager.sendChatMessage("§6配置同步：CrystalPVP.cn");
                     Notify_EXlgYplaRzfgofOPOkyB.sendNotify("§6配置同步：CrystalPVP.cn");
                     Step_EShajbhvQeYkCdreEeNY.INSTANCE.height.setValue((double)this.xiaosongstepHeight.getValueFloat());
                     Step_EShajbhvQeYkCdreEeNY.INSTANCE.inWebPause.setValue(this.xiaosongStepWebPause.getValue());
                     if (this.xiaosonginvSwap.getValue()) {
                        CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.invSwapBypass.setValue(true);
                        BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.SwapSync.setValue(true);
                     }

                     if (this.xiaosongfastWeb.getValue()) {
                        FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.groundcheck.setValue(this.xiaosongfastWebGround.getValue());
                     }

                     if (this.xiaosongfastWeb.getValue()) {
                        Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.webcheck.setValue(this.xiaosongburrowWeb.getValue());
                     }
                  }
               }

               this.needUpdate = false;
            }
         }
      }
   }
}
