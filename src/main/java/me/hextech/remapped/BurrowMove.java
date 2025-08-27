package me.hextech.remapped;

public class BurrowMove extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static BurrowMove INSTANCE;
   public final SliderSetting wspeed = this.add(new SliderSetting("WebSpeed", 10.0, 0.0, 50.0, 0.01)).setSuffix("÷2");
   public final SliderSetting aSpeed = this.add(new SliderSetting("BlockSpeed", 10.02, 0.0, 30.0, 0.01));
   public final SliderSetting aForward = this.add(new SliderSetting("AnchorForward", 1.0, 0.0, 30.0, 0.25));
   public final SliderSetting bSpeed = this.add(new SliderSetting("currentPos", 10.0, 0.0, 30.0, 0.01));

   public BurrowMove() {
      super("BurrowMove", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @EventHandler
   public void onMove(MoveEvent event) {
      if (EntityUtil.isInsideBlock()) {
         double speed = AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos == null
            ? (HoleKickTest.isInWeb(mc.field_1724) ? this.wspeed.getValue() : this.aSpeed.getValue())
            : this.bSpeed.getValue();
         double moveSpeed = 0.002873 * speed;
         double n = 0.0;
         if (mc.field_1724 != null) {
            n = (double)mc.field_1724.field_3913.field_3905;
         }

         double n2 = 0.0;
         if (mc.field_1724 != null) {
            n2 = (double)mc.field_1724.field_3913.field_3907;
         }

         double n3 = 0.0;
         if (mc.field_1724 != null) {
            n3 = (double)mc.field_1724.method_36454();
         }

         if (n == 0.0 && n2 == 0.0) {
            if (AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos == null) {
               event.setX(0.0);
               event.setZ(0.0);
            } else {
               moveSpeed = 0.002873 * this.aForward.getValue();
               event.setX(1.0 * moveSpeed * -Math.sin(Math.toRadians(n3)));
               event.setZ(1.0 * moveSpeed * Math.cos(Math.toRadians(n3)));
            }
         } else {
            if (n != 0.0 && n2 != 0.0) {
               n *= Math.sin(Math.PI / 4);
               n2 *= Math.cos(Math.PI / 4);
            }

            event.setX(n * moveSpeed * -Math.sin(Math.toRadians(n3)) + n2 * moveSpeed * Math.cos(Math.toRadians(n3)));
            event.setZ(n * moveSpeed * Math.cos(Math.toRadians(n3)) - n2 * moveSpeed * -Math.sin(Math.toRadians(n3)));
         }
      }
   }
}
