package me.hextech.asm.mixins.freelook;

import me.hextech.remapped.CameraState;
import me.hextech.remapped.FreeLook;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public abstract class EntityMixin {
   @Unique
   private CameraState camera;

   @Inject(
      method = {"changeLookDirection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo callback) {
      if ((Entity)this instanceof ClientPlayerEntity) {
         this.camera = FreeLook.INSTANCE.getCameraState();
         if (this.camera.doLock) {
            this.applyTransformedAngle(cursorDeltaX, cursorDeltaY);
            callback.cancel();
         } else if (this.camera.doTransition) {
            this.applyTransformedAngle(cursorDeltaX, cursorDeltaY);
         }
      }
   }

   @Unique
   private void applyTransformedAngle(double cursorDeltaX, double cursorDeltaY) {
      float cursorDeltaMultiplier = 0.15F;
      float transformedCursorDeltaX = (float)cursorDeltaX * cursorDeltaMultiplier;
      float transformedCursorDeltaY = (float)cursorDeltaY * cursorDeltaMultiplier;
      float yaw = this.camera.lookYaw;
      float pitch = this.camera.lookPitch;
      yaw += transformedCursorDeltaX;
      pitch += transformedCursorDeltaY;
      pitch = MathHelper.method_15363(pitch, -90.0F, 90.0F);
      this.camera.lookYaw = yaw;
      this.camera.lookPitch = pitch;
   }
}
