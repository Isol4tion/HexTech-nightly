package me.hextech.remapped;

public class SafeWalk extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public SafeWalk() {
      super("SafeWalk", "stop at the edge", Module_JlagirAibYQgkHtbRnhw.Movement);
   }

   @EventHandler(
      priority = -100
   )
   public void onMove(MoveEvent event) {
      double x = event.getX();
      double y = event.getY();
      double z = event.getZ();
      if (mc.field_1724.method_24828()) {
         double increment = 0.05;

         while (x != 0.0 && this.isOffsetBBEmpty(x, -1.0, 0.0)) {
            if (x < increment && x >= -increment) {
               x = 0.0;
            } else if (x > 0.0) {
               x -= increment;
            } else {
               x += increment;
            }
         }

         while (z != 0.0 && this.isOffsetBBEmpty(0.0, -1.0, z)) {
            if (z < increment && z >= -increment) {
               z = 0.0;
            } else if (z > 0.0) {
               z -= increment;
            } else {
               z += increment;
            }
         }

         while (x != 0.0 && z != 0.0 && this.isOffsetBBEmpty(x, -1.0, z)) {
            x = x < increment && x >= -increment ? 0.0 : (x > 0.0 ? x - increment : x + increment);
            if (z < increment && z >= -increment) {
               z = 0.0;
            } else if (z > 0.0) {
               z -= increment;
            } else {
               z += increment;
            }
         }
      }

      event.setX(x);
      event.setY(y);
      event.setZ(z);
   }

   public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
      return !mc.field_1687.method_39454(mc.field_1724, mc.field_1724.method_5829().method_989(offsetX, offsetY, offsetZ));
   }
}
