package me.hextech.remapped;

import java.util.Objects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Strafe extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Strafe INSTANCE;
   private final BooleanSetting airStop = this.add(new BooleanSetting("AirStop", true));
   private final BooleanSetting slowCheck = this.add(new BooleanSetting("SlowCheck", true));

   public Strafe() {
      super("Strafe", "Modifies sprinting", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @EventHandler
   public void onStrafe(MoveEvent event) {
      if (!mc.field_1724.method_5715()
         && !HoleSnap.INSTANCE.isOn()
         && !Speed.INSTANCE.isOn()
         && !mc.field_1724.method_6128()
         && !EntityUtil.isInsideBlock()
         && !mc.field_1724.method_5771()
         && !mc.field_1724.method_5799()
         && !mc.field_1724.method_31549().field_7479) {
         if (!MovementUtil.isMoving()) {
            if (this.airStop.getValue()) {
               MovementUtil.setMotionX(0.0);
               MovementUtil.setMotionZ(0.0);
            }
         } else {
            double[] dir = MovementUtil.directionSpeed(this.getBaseMoveSpeed());
            event.setX(dir[0]);
            event.setZ(dir[1]);
         }
      }
   }

   public double getBaseMoveSpeed() {
      double n = 0.2873;
      if (mc.field_1724.method_6059(StatusEffects.field_5904) && (!this.slowCheck.getValue() || !mc.field_1724.method_6059(StatusEffects.field_5909))) {
         n *= 1.0 + 0.2 * (double)(((StatusEffectInstance)Objects.requireNonNull(mc.field_1724.method_6112(StatusEffects.field_5904))).method_5578() + 1);
      }

      return n;
   }
}
