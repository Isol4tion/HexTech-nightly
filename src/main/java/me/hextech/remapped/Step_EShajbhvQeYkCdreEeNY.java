package me.hextech.remapped;

public class Step_EShajbhvQeYkCdreEeNY extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Step_EShajbhvQeYkCdreEeNY INSTANCE;
   public final SliderSetting height = this.add(new SliderSetting("Height", 1.0, 0.0, 5.0, 0.5));
   public final BooleanSetting onlyMoving = this.add(new BooleanSetting("OnlyMoving", true));
   public final BooleanSetting inWebPause = this.add(new BooleanSetting("InWebPause", true));
   private final EnumSetting<Step> mode = this.add(new EnumSetting("Mode", Step.Vanilla));
   boolean timer;
   int packets = 0;

   public Step_EShajbhvQeYkCdreEeNY() {
      super("Step", "Steps up blocks.", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @Override
   public void onDisable() {
      if (!nullCheck()) {
         mc.field_1724.method_49477(0.6F);
         me.hextech.HexTech.TIMER.reset();
      }
   }

   @Override
   public String getInfo() {
      return ((Step)this.mode.getValue()).name();
   }

   @Override
   public void onUpdate() {
      if ((!this.inWebPause.getValue() || !HoleKickTest.isInWeb(mc.field_1724))
         && mc.field_1724.method_24828()
         && (!this.onlyMoving.getValue() || MovementUtil.isMoving())) {
         mc.field_1724.method_49477(this.height.getValueFloat());
      } else {
         mc.field_1724.method_49477(0.6F);
      }
   }

   @EventHandler
   public void onStep(UpdateWalkingEvent event) {
      if (event.isPost()) {
         this.packets--;
      } else {
         if (this.timer && this.packets <= 0) {
            me.hextech.HexTech.TIMER.reset();
            this.timer = false;
         }

         boolean strict = this.mode.getValue() == Step.NCP;
      }
   }

   public double getTimer(double height) {
      if (height > 0.6 && height <= 1.0) {
         return 0.5;
      } else {
         double[] offsets = this.getOffset(height);
         return offsets == null ? 1.0 : 1.0 / (double)offsets.length;
      }
   }

   public double[] getOffset(double height) {
      boolean strict = this.mode.getValue() == Step.NCP;
      if (height == 0.75) {
         return strict ? new double[]{0.42, 0.753, 0.75} : new double[]{0.42, 0.753};
      } else if (height == 0.8125) {
         return strict ? new double[]{0.39, 0.7, 0.8125} : new double[]{0.39, 0.7};
      } else if (height == 0.875) {
         return strict ? new double[]{0.39, 0.7, 0.875} : new double[]{0.39, 0.7};
      } else if (height == 1.0) {
         return strict ? new double[]{0.42, 0.753, 1.0} : new double[]{0.42, 0.753};
      } else if (height == 1.5) {
         return new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
      } else if (height == 2.0) {
         return new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
      } else {
         return height == 2.5 ? new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907} : null;
      }
   }
}
