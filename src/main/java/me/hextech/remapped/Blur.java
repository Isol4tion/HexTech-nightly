package me.hextech.remapped;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.uniform.Uniform1f;

public class Blur {
   private static final ManagedShaderEffect blur;
   private static final Uniform1f blurProgress;
   private static final Uniform1f blurRadius;

   public static void blur(float deltaTick, float radius) {
      blurProgress.set(1.0F);
      blurRadius.set(radius);
      blur.render(deltaTick);
   }
}
