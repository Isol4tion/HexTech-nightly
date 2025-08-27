package me.hextech.remapped;

import me.hextech.asm.accessors.IVec3d;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.Vec3d;

public class EntityControl extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static EntityControl INSTANCE;
   public final BooleanSetting fly = this.add(new BooleanSetting("Fly", true));
   public final SliderSetting speed = this.add(new SliderSetting("Speed", 5.0, 0.1, 50.0));
   public final SliderSetting fallSpeed = this.add(new SliderSetting("FallSpeed", 0.1, 0.0, 50.0));
   private final SliderSetting verticalSpeed = this.add(new SliderSetting("VerticalSpeed", 6.0, 0.0, 20.0));
   private final BooleanSetting noSync = this.add(new BooleanSetting("NoSync", false));

   public EntityControl() {
      super("EntityControl", Module_JlagirAibYQgkHtbRnhw.Movement);
      INSTANCE = this;
   }

   @EventHandler
   public void onBoat(BoatMoveEvent event) {
      if (!nullCheck() && this.fly.getValue()) {
         Entity boat = event.getBoat();
         if (boat != null) {
            if (boat.method_5642() == mc.field_1724) {
               boat.method_36456(mc.field_1724.method_36454());
               Vec3d vel = MovementUtil.getHorizontalVelocity(this.speed.getValue());
               double velX = vel.method_10216();
               double velZ = vel.method_10215();
               double velY;
               if (!(mc.field_1755 instanceof ChatScreen) && (mc.field_1755 == null || !ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.isOff())) {
                  boolean sprint = InputUtil.method_15987(
                     mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1867.method_1428()).method_1444()
                  );
                  boolean jump = InputUtil.method_15987(
                     mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1903.method_1428()).method_1444()
                  );
                  if (jump) {
                     if (sprint) {
                        velY = -this.fallSpeed.getValue() / 20.0;
                     } else {
                        velY = this.verticalSpeed.getValue() / 20.0;
                     }
                  } else if (sprint) {
                     velY = -this.verticalSpeed.getValue() / 20.0;
                  } else {
                     velY = -this.fallSpeed.getValue() / 20.0;
                  }
               } else {
                  velY = -this.fallSpeed.getValue() / 20.0;
               }

               ((IVec3d)boat.method_18798()).setX(velX);
               ((IVec3d)boat.method_18798()).setY(velY);
               ((IVec3d)boat.method_18798()).setZ(velZ);
            }
         }
      }
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      if (!nullCheck() && this.fly.getValue()) {
         Entity boat = mc.field_1724.method_5854();
         if (boat != null) {
            boat.method_36456(mc.field_1724.method_36454());
            Vec3d vel = MovementUtil.getHorizontalVelocity(this.speed.getValue());
            double velX = vel.method_10216();
            double velZ = vel.method_10215();
            double velY;
            if (!(mc.field_1755 instanceof ChatScreen) && (mc.field_1755 == null || !ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.isOff())) {
               boolean sprint = InputUtil.method_15987(
                  mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1867.method_1428()).method_1444()
               );
               boolean jump = InputUtil.method_15987(
                  mc.method_22683().method_4490(), InputUtil.method_15981(mc.field_1690.field_1903.method_1428()).method_1444()
               );
               if (jump) {
                  if (sprint) {
                     velY = -this.fallSpeed.getValue() / 20.0;
                  } else {
                     velY = this.verticalSpeed.getValue() / 20.0;
                  }
               } else if (sprint) {
                  velY = -this.verticalSpeed.getValue() / 20.0;
               } else {
                  velY = -this.fallSpeed.getValue() / 20.0;
               }
            } else {
               velY = -this.fallSpeed.getValue() / 20.0;
            }

            ((IVec3d)boat.method_18798()).setX(velX);
            ((IVec3d)boat.method_18798()).setY(velY);
            ((IVec3d)boat.method_18798()).setZ(velZ);
         }
      }
   }

   @EventHandler
   private void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (event.getPacket() instanceof VehicleMoveS2CPacket && this.noSync.getValue()) {
         event.cancel();
      }
   }
}
