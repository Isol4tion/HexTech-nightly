package me.hextech.remapped;

import net.minecraft.client.render.LightmapTextureManager;

public class WeatherRenderEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private static final WeatherRenderEvent INSTANCE;
   public LightmapTextureManager lightmapTextureManager;
   public float tickDelta;
   public double cameraX;
   public double cameraY;
   public double cameraZ;

   public WeatherRenderEvent(Event stage) {
      super(stage);
   }

   public static WeatherRenderEvent get(LightmapTextureManager lightmapTextureManager, float tickDelta, double cameraX, double cameraY, double cameraZ) {
      INSTANCE.setCancelled(false);
      INSTANCE.lightmapTextureManager = lightmapTextureManager;
      INSTANCE.tickDelta = tickDelta;
      INSTANCE.cameraX = cameraX;
      INSTANCE.cameraY = cameraY;
      INSTANCE.cameraZ = cameraZ;
      return INSTANCE;
   }
}
