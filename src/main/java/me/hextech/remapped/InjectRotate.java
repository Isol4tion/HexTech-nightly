package me.hextech.remapped;

import net.minecraft.util.math.MathHelper;

public class InjectRotate {
   public static float[] injectStep(float[] angle, float steps) {
      if (steps < 0.01F) {
         steps = 0.01F;
      }

      if (steps > 1.0F) {
         steps = 1.0F;
      }

      if (steps < 1.0F && angle != null) {
         float packetYaw = AutoCrystal_QcRVYRsOqpKivetoXSJa.lastYaw;
         float diff = MathHelper.method_15356(angle[0], packetYaw);
         if (Math.abs(diff) > 180.0F * steps) {
            angle[0] = packetYaw + diff * (180.0F * steps / Math.abs(diff));
         }

         float packetPitch = AutoCrystal_QcRVYRsOqpKivetoXSJa.lastPitch;
         diff = angle[1] - packetPitch;
         if (Math.abs(diff) > 90.0F * steps) {
            angle[1] = packetPitch + diff * (90.0F * steps / Math.abs(diff));
         }
      }

      return new float[]{angle[0], angle[1]};
   }
}
