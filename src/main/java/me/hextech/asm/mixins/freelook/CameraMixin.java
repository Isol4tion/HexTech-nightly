package me.hextech.asm.mixins.freelook;

import me.hextech.remapped.CameraState;
import me.hextech.remapped.FreeLook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Camera.class})
public abstract class CameraMixin {
   @Shadow
   private float field_18721;
   @Unique
   private float lastUpdate;

   @Inject(
      method = {"update"},
      at = {@At("HEAD")}
   )
   private void onCameraUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
      CameraState camera = FreeLook.INSTANCE.getCameraState();
      if (camera.doLock) {
         float limitNegativeYaw = camera.originalYaw() - 180.0F;
         float limitPositiveYaw = camera.originalYaw() + 180.0F;
         if (camera.lookYaw > limitPositiveYaw) {
            camera.lookYaw = limitPositiveYaw;
         }

         if (camera.lookYaw < limitNegativeYaw) {
            camera.lookYaw = limitNegativeYaw;
         }
      }
   }

   @ModifyArgs(
      method = {"update"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
      )
   )
   private void modifyRotationArgs(Args args) {
      CameraState camera = FreeLook.INSTANCE.getCameraState();
      if (camera.doLock) {
         float yaw = camera.lookYaw;
         float pitch = camera.lookPitch;
         if (MinecraftClient.method_1551().field_1690.method_31044().method_31035()) {
            yaw -= 180.0F;
            pitch = -pitch;
         }

         args.set(0, yaw);
         args.set(1, pitch);
      } else if (camera.doTransition) {
         float delta = this.getCurrentTime() - this.lastUpdate;
         float steps = 1.2F;
         float speed = 2.0F;
         float yawDiff = camera.lookYaw - camera.originalYaw();
         float pitchDiff = camera.lookPitch - camera.originalPitch();
         float yawStep = speed * yawDiff * steps;
         float pitchStep = speed * pitchDiff * steps;
         float yaw = MathHelper.method_15348(camera.lookYaw, camera.originalYaw(), yawStep * delta);
         float pitch = MathHelper.method_15348(camera.lookPitch, camera.originalPitch(), pitchStep * delta);
         camera.lookYaw = yaw;
         camera.lookPitch = pitch;
         args.set(0, yaw);
         args.set(1, pitch);
         camera.doTransition = (int)camera.originalYaw() != (int)camera.lookYaw || (int)camera.originalPitch() != (int)camera.lookPitch;
      }

      this.lastUpdate = this.getCurrentTime();
   }

   @Unique
   private float getCurrentTime() {
      return (float)((double)System.nanoTime() * 1.0E-8);
   }
}
