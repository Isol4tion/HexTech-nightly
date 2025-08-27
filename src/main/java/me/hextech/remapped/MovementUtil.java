package me.hextech.remapped;

import me.hextech.asm.accessors.IVec3d;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class MovementUtil implements Wrapper {
   private static final double diagonal;
   private static final Vec3d horizontalVelocity;

   public static boolean isMoving() {
      return (double)mc.field_1724.field_3913.field_3905 != 0.0 || (double)mc.field_1724.field_3913.field_3907 != 0.0 || HoleSnap.INSTANCE.isOn();
   }

   public static boolean isJumping() {
      return mc.field_1724.field_3913.field_3904;
   }

   public static double getDistance2D() {
      double xDist = mc.field_1724.method_23317() - mc.field_1724.field_6014;
      double zDist = mc.field_1724.method_23321() - mc.field_1724.field_5969;
      return Math.sqrt(xDist * xDist + zDist * zDist);
   }

   public static double getJumpSpeed() {
      double defaultSpeed = 0.0;
      if (mc.field_1724.method_6059(StatusEffects.field_5913)) {
         int amplifier = ((StatusEffectInstance)mc.field_1724.method_6088().get(StatusEffects.field_5913)).method_5578();
         defaultSpeed += (double)(amplifier + 1) * 0.1;
      }

      return defaultSpeed;
   }

   public static float getMoveForward() {
      return mc.field_1724.field_3913.field_3905;
   }

   public static float getMoveStrafe() {
      return mc.field_1724.field_3913.field_3907;
   }

   public static Vec3d getHorizontalVelocity(double bps) {
      float yaw = mc.field_1724.method_36454();
      Vec3d forward = Vec3d.method_1030(0.0F, yaw);
      Vec3d right = Vec3d.method_1030(0.0F, yaw + 90.0F);
      double velX = 0.0;
      double velZ = 0.0;
      boolean a = false;
      if (mc.field_1724.field_3913.field_3910) {
         velX += forward.field_1352 / 20.0 * bps;
         velZ += forward.field_1350 / 20.0 * bps;
         a = true;
      }

      if (mc.field_1724.field_3913.field_3909) {
         velX -= forward.field_1352 / 20.0 * bps;
         velZ -= forward.field_1350 / 20.0 * bps;
         a = true;
      }

      boolean b = false;
      if (mc.field_1724.field_3913.field_3906) {
         velX += right.field_1352 / 20.0 * bps;
         velZ += right.field_1350 / 20.0 * bps;
         b = true;
      }

      if (mc.field_1724.field_3913.field_3908) {
         velX -= right.field_1352 / 20.0 * bps;
         velZ -= right.field_1350 / 20.0 * bps;
         b = true;
      }

      if (a && b) {
         velX *= diagonal;
         velZ *= diagonal;
      }

      ((IVec3d)horizontalVelocity).setX(velX);
      ((IVec3d)horizontalVelocity).setZ(velZ);
      return horizontalVelocity;
   }

   public static double[] directionSpeed(double speed) {
      float forward = mc.field_1724.field_3913.field_3905;
      float side = mc.field_1724.field_3913.field_3907;
      float yaw = mc.field_1724.field_5982 + (mc.field_1724.method_36454() - mc.field_1724.field_5982) * mc.method_1488();
      if (forward != 0.0F) {
         if (side > 0.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
         } else if (side < 0.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
         }

         side = 0.0F;
         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      return new double[]{posX, posZ};
   }

   public static double getMotionX() {
      return mc.field_1724.method_18798().field_1352;
   }

   public static void setMotionX(double x) {
      ((IVec3d)mc.field_1724.method_18798()).setX(x);
   }

   public static double getMotionY() {
      return mc.field_1724.method_18798().field_1351;
   }

   public static void setMotionY(double y) {
      ((IVec3d)mc.field_1724.method_18798()).setY(y);
   }

   public static double getMotionZ() {
      return mc.field_1724.method_18798().field_1350;
   }

   public static void setMotionZ(double z) {
      ((IVec3d)mc.field_1724.method_18798()).setZ(z);
   }

   public static double getSpeed(boolean slowness) {
      double defaultSpeed = 0.2873;
      return getSpeed(slowness, defaultSpeed);
   }

   public static double getSpeed(boolean slowness, double defaultSpeed) {
      if (mc.field_1724.method_6059(StatusEffects.field_5904)) {
         int amplifier = ((StatusEffectInstance)mc.field_1724.method_6088().get(StatusEffects.field_5904)).method_5578();
         defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      if (slowness && mc.field_1724.method_6059(StatusEffects.field_5909)) {
         int amplifier = ((StatusEffectInstance)mc.field_1724.method_6088().get(StatusEffects.field_5909)).method_5578();
         defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      if (mc.field_1724.method_5715()) {
         defaultSpeed /= 5.0;
      }

      return defaultSpeed;
   }
}
