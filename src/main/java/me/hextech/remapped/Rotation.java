package me.hextech.remapped;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Rotation extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Rotation INSTANCE;
   public static float fixRotation;
   public final BooleanSetting fixrotate = this.add(new BooleanSetting("RotateFix", true));
   private final BooleanSetting movement = this.add(new BooleanSetting("MovementFix", true));
   private float prevYaw;

   public Rotation() {
      super("Rotation", Module_JlagirAibYQgkHtbRnhw.Setting);
      INSTANCE = this;
   }

   private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
      double d = movementInput.method_1027();
      if (d < 1.0E-7) {
         return Vec3d.field_1353;
      } else {
         Vec3d vec3d = (d > 1.0 ? movementInput.method_1029() : movementInput).method_1021((double)speed);
         float f = MathHelper.method_15374(yaw * (float) (Math.PI / 180.0));
         float g = MathHelper.method_15362(yaw * (float) (Math.PI / 180.0));
         return new Vec3d(
            vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f
         );
      }
   }

   @EventHandler
   public void onJump(inJumpEvent e) {
      if (this.fixrotate.getValue()) {
         if (!HoleSnap.INSTANCE.isOn()) {
            if (!mc.field_1724.method_3144()) {
               if (e.isPre()) {
                  this.prevYaw = mc.field_1724.method_36454();
                  mc.field_1724.method_36456(fixRotation);
               } else {
                  mc.field_1724.method_36456(this.prevYaw);
               }
            }
         }
      }
   }

   @EventHandler
   public void onPlayerMove(inVelocityEvent event) {
      if (this.fixrotate.getValue()) {
         if (!HoleSnap.INSTANCE.isOn()) {
            if (!mc.field_1724.method_3144()) {
               event.setVelocity(movementInputToVelocity(event.getMovementInput(), event.getSpeed(), fixRotation));
            }
         }
      }
   }

   @EventHandler
   public void onKeyInput(KeyboardInputEvent e) {
      if (this.movement.getValue()) {
         if (!HoleSnap.INSTANCE.isOn()) {
            if (!mc.field_1724.method_3144() && !FreeCam.INSTANCE.isOn()) {
               float mF = mc.field_1724.field_3913.field_3905;
               float mS = mc.field_1724.field_3913.field_3907;
               float delta = (mc.field_1724.method_36454() - fixRotation) * (float) (Math.PI / 180.0);
               float cos = MathHelper.method_15362(delta);
               float sin = MathHelper.method_15374(delta);
               mc.field_1724.field_3913.field_3907 = (float)Math.round(mS * cos - mF * sin);
               mc.field_1724.field_3913.field_3905 = (float)Math.round(mF * cos + mS * sin);
            }
         }
      }
   }
}
