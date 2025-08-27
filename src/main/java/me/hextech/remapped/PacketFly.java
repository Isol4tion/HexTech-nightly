package me.hextech.remapped;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class PacketFly extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public final BooleanSetting flight = this.add(new BooleanSetting("Flight", true).setParent());
   public final SliderSetting flightMode = this.add(new SliderSetting("FMode", 0, 0, 1, v -> this.flight.isOpen()));
   public final SliderSetting antiFactor = this.add(new SliderSetting("AntiFactor", 1.0, 0.1, 3.0));
   public final SliderSetting extraFactor = this.add(new SliderSetting("ExtraFactor", 1.0, 0.1, 3.0));
   public final BooleanSetting strafeFactor = this.add(new BooleanSetting("StrafeFactor", true));
   public final SliderSetting loops = this.add(new SliderSetting("Loops", 1, 1, 10));
   public final BooleanSetting antiRotation = this.add(new BooleanSetting("AntiRotation", false));
   public final BooleanSetting setID = this.add(new BooleanSetting("SetID", true));
   public final BooleanSetting setMove = this.add(new BooleanSetting("SetMove", false));
   public final BooleanSetting nocliperino = this.add(new BooleanSetting("NoClip", false));
   public final BooleanSetting sendTeleport = this.add(new BooleanSetting("Teleport", true));
   public final BooleanSetting setPos = this.add(new BooleanSetting("SetPos", false));
   public final BooleanSetting invalidPacket = this.add(new BooleanSetting("InvalidPacket", true));
   private final Set<PlayerMoveC2SPacket> packets = new ConcurrentSet();
   private int flightCounter = 0;
   private int teleportID = 0;

   public PacketFly() {
      super("PacketFly", "PacketFly", Module_JlagirAibYQgkHtbRnhw.Movement);
   }

   @EventHandler
   public void onUpdateWalkingPlayer(UpdateWalkingEvent event) {
      if (!nullCheck()) {
         if (!event.isPost()) {
            mc.field_1724.method_18800(0.0, 0.0, 0.0);
            boolean checkCollisionBoxes = this.checkHitBoxes();
            double speed = mc.field_1724.field_3913.field_3904 && (checkCollisionBoxes || !MovementUtil.isMoving())
               ? (
                  this.flight.getValue() && !checkCollisionBoxes
                     ? (this.flightMode.getValue() == 0.0 ? (this.resetCounter(10) ? -0.032 : 0.062) : (this.resetCounter(20) ? -0.032 : 0.062))
                     : 0.062
               )
               : (
                  mc.field_1724.field_3913.field_3903
                     ? -0.062
                     : (!checkCollisionBoxes ? (this.resetCounter(4) ? (this.flight.getValue() ? -0.04 : 0.0) : 0.0) : 0.0)
               );
            if (checkCollisionBoxes && MovementUtil.isMoving() && speed != 0.0) {
               speed /= this.antiFactor.getValue();
            }

            double[] strafing = this.getMotion(this.strafeFactor.getValue() && checkCollisionBoxes ? 0.031 : 0.26);

            for (int i = 1; (double)i < this.loops.getValue() + 1.0; i++) {
               MovementUtil.setMotionX(strafing[0] * (double)i * this.extraFactor.getValue());
               MovementUtil.setMotionY(speed * (double)i);
               MovementUtil.setMotionZ(strafing[1] * (double)i * this.extraFactor.getValue());
               this.sendPackets(MovementUtil.getMotionX(), MovementUtil.getMotionY(), MovementUtil.getMotionZ(), this.sendTeleport.getValue());
            }
         }
      }
   }

   @EventHandler
   public void onMove(MoveEvent event) {
      if (!nullCheck()) {
         if (this.setMove.getValue() && this.flightCounter != 0) {
            event.setX(MovementUtil.getMotionX());
            event.setY(MovementUtil.getMotionY());
            event.setZ(MovementUtil.getMotionZ());
            if (this.nocliperino.getValue() && this.checkHitBoxes()) {
               mc.field_1724.field_5960 = true;
            }
         }
      }
   }

   @EventHandler
   public void onPacketSend(PacketEvent event) {
      if (!nullCheck()) {
         if (event.getPacket() instanceof PlayerMoveC2SPacket && !this.packets.remove((PlayerMoveC2SPacket)event.getPacket())) {
            if (event.getPacket() instanceof LookAndOnGround && !this.antiRotation.getValue()) {
               return;
            }

            event.cancel();
         }
      }
   }

   @EventHandler
   public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (!nullCheck()) {
         if (!event.isCancelled()) {
            if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
               PlayerPositionLookS2CPacket packet = event.getPacket();
               if (this.setID.getValue()) {
                  this.teleportID = packet.method_11737();
               }
            }
         }
      }
   }

   private boolean checkHitBoxes() {
      return mc.field_1687.method_39454(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.0625, -0.0625, -0.0625));
   }

   private boolean resetCounter(int counter) {
      if (++this.flightCounter >= counter) {
         this.flightCounter = 0;
         return true;
      } else {
         return false;
      }
   }

   private double[] getMotion(double speed) {
      float moveForward = MovementUtil.getMoveForward();
      float moveStrafe = MovementUtil.getMoveStrafe();
      float rotationYaw = mc.field_1724.field_5982 + (mc.field_1724.method_36454() - mc.field_1724.field_5982) * mc.method_1488();
      if (moveForward != 0.0F) {
         if (moveStrafe > 0.0F) {
            rotationYaw += (float)(moveForward > 0.0F ? -45 : 45);
         } else if (moveStrafe < 0.0F) {
            rotationYaw += (float)(moveForward > 0.0F ? 45 : -45);
         }

         moveStrafe = 0.0F;
         if (moveForward > 0.0F) {
            moveForward = 1.0F;
         } else if (moveForward < 0.0F) {
            moveForward = -1.0F;
         }
      }

      double posX = (double)moveForward * speed * -Math.sin(Math.toRadians((double)rotationYaw))
         + (double)moveStrafe * speed * Math.cos(Math.toRadians((double)rotationYaw));
      double posZ = (double)moveForward * speed * Math.cos(Math.toRadians((double)rotationYaw))
         - (double)moveStrafe * speed * -Math.sin(Math.toRadians((double)rotationYaw));
      return new double[]{posX, posZ};
   }

   private void sendPackets(double x, double y, double z, boolean teleport) {
      Vec3d vec = new Vec3d(x, y, z);
      Vec3d position = mc.field_1724.method_19538().method_1019(vec);
      Vec3d outOfBoundsVec = this.outOfBoundsVec(position);
      this.packetSender(new PositionAndOnGround(position.field_1352, position.field_1351, position.field_1350, mc.field_1724.method_24828()));
      if (this.invalidPacket.getValue()) {
         this.packetSender(
            new PositionAndOnGround(outOfBoundsVec.field_1352, outOfBoundsVec.field_1351, outOfBoundsVec.field_1350, mc.field_1724.method_24828())
         );
      }

      if (this.setPos.getValue()) {
         mc.field_1724.method_5814(position.field_1352, position.field_1351, position.field_1350);
      }

      this.teleportPacket(teleport);
   }

   private void teleportPacket(boolean shouldTeleport) {
      if (shouldTeleport) {
         mc.field_1724.field_3944.method_52787(new TeleportConfirmC2SPacket(++this.teleportID));
      }
   }

   private Vec3d outOfBoundsVec(Vec3d position) {
      return position.method_1031(0.0, 1337.0, 0.0);
   }

   private void packetSender(PlayerMoveC2SPacket packet) {
      this.packets.add(packet);
      mc.field_1724.field_3944.method_52787(packet);
   }
}
