package me.hextech.remapped;

public class FastWeb_dehcwwTxEbDSnkFtZvNl extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static FastWeb_dehcwwTxEbDSnkFtZvNl INSTANCE;
   public final EnumSetting<FastWeb> mode = this.add(new EnumSetting("Mode", FastWeb.Vanilla));
   public final SliderSetting xZSlow = this.add(new SliderSetting("XZSpeed", 25.0, 0.0, 100.0, 0.1, v -> this.mode.getValue() == FastWeb.Custom).setSuffix("%"));
   public final SliderSetting ySlow = this.add(new SliderSetting("YSpeed", 100.0, 0.0, 100.0, 0.1, v -> this.mode.getValue() == FastWeb.Custom).setSuffix("%"));
   public final BooleanSetting onlySneak = this.add(new BooleanSetting("OnlySneak", true));
   public final BooleanSetting groundcheck = this.add(new BooleanSetting("GroundCheck", true));
   private final SliderSetting fastSpeed = this.add(
      new SliderSetting("Speed", 3.0, 0.0, 8.0, v -> this.mode.getValue() == FastWeb.Vanilla || this.mode.getValue() == FastWeb.Strict)
   );
   private boolean work = false;

   public FastWeb_dehcwwTxEbDSnkFtZvNl() {
      super("FastWeb", "So you don't need to keep timer on keybind", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @Override
   public String getInfo() {
      return ((FastWeb)this.mode.getValue()).name();
   }

   public boolean isWorking() {
      return this.work;
   }

   @Override
   public void onUpdate() {
      if (!this.groundcheck.getValue() || !mc.field_1724.method_24828()) {
         this.work = !mc.field_1724.method_24828()
            && (mc.field_1690.field_1832.method_1434() || !this.onlySneak.getValue())
            && HoleKickTest.isInWeb(mc.field_1724);
         if (this.work && this.mode.getValue() == FastWeb.Vanilla) {
            MovementUtil.setMotionY(MovementUtil.getMotionY() - this.fastSpeed.getValue());
         }
      }
   }

   @EventHandler(
      priority = -100
   )
   public void onTimer(TimerEvent event) {
      if (this.work && this.mode.getValue() == FastWeb.Strict) {
         event.set(this.fastSpeed.getValueFloat());
      }
   }
}
