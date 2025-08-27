package me.hextech.remapped;

import net.minecraft.client.MinecraftClient;

public class CameraState {
   public float lookYaw;
   public float lookPitch;
   public float transitionInitialYaw;
   public float transitionInitialPitch;
   public boolean doLock;
   public boolean doTransition;

   public float originalYaw() {
      return MinecraftClient.method_1551().method_1560().method_5791();
   }

   public float originalPitch() {
      return MinecraftClient.method_1551().method_1560().method_36455();
   }
}
