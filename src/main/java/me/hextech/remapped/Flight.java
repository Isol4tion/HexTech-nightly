package me.hextech.remapped;

public class Flight extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Flight INSTANCE;
   public final SliderSetting speed = this.add(new SliderSetting("Speed", 1.0, 0.1F, 10.0));
   public final SliderSetting downFactor = this.add(new SliderSetting("DownFactor", 0.0, 0.0, 1.0, 1.0E-6F));
   private final SliderSetting sneakDownSpeed = this.add(new SliderSetting("DownSpeed", 1.0, 0.1F, 10.0));
   private final SliderSetting upSpeed = this.add(new SliderSetting("UpSpeed", 1.0, 0.1F, 10.0));
   private MoveEvent event;

   public Flight() {
      super("Flight", "me", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @EventHandler
   public void onMove(MoveEvent event) {
      if (!nullCheck()) {
         this.event = event;
         if (mc.field_1690.field_1832.method_1434() && mc.field_1724.field_3913.field_3904) {
            this.setY(0.0);
         } else if (mc.field_1690.field_1832.method_1434()) {
            this.setY(-this.sneakDownSpeed.getValue());
         } else if (mc.field_1724.field_3913.field_3904) {
            this.setY(this.upSpeed.getValue());
         } else {
            this.setY(-this.downFactor.getValue());
         }

         double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
         this.setX(dir[0]);
         this.setZ(dir[1]);
      }
   }

   private void setX(double f) {
      this.event.setX(f);
      MovementUtil.setMotionX(f);
   }

   private void setY(double f) {
      this.event.setY(f);
      MovementUtil.setMotionY(f);
   }

   private void setZ(double f) {
      this.event.setZ(f);
      MovementUtil.setMotionZ(f);
   }
}
