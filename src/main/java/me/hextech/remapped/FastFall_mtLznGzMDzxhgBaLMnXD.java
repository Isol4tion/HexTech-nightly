package me.hextech.remapped;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class FastFall_mtLznGzMDzxhgBaLMnXD extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final EnumSetting<FastFall> mode = this.add(new EnumSetting("Mode", FastFall.Fast));
   private final BooleanSetting noLag = this.add(new BooleanSetting("NoLag", true, v -> this.mode.getValue() == FastFall.Fast));
   private final SliderSetting height = this.add(new SliderSetting("Height", 10.0, 1.0, 20.0, 0.5));
   private final SliderSetting STimer = this.add(new SliderSetting("Timer", 2.0, 1.0, 20.0, 0.5));
   private final Timer lagTimer = new Timer();
   boolean onGround = false;
   private boolean useTimer;

   public FastFall_mtLznGzMDzxhgBaLMnXD() {
      super("FastFall", "Miyagi son simulator", Module_JlagirAibYQgkHtbRnhw.Movement);
   }

   @Override
   public void onDisable() {
      this.useTimer = false;
   }

   @Override
   public String getInfo() {
      return ((FastFall)this.mode.getValue()).name();
   }

   @Override
   public void onUpdate() {
      if ((!(this.height.getValue() > 0.0) || !((double)this.traceDown() > this.height.getValue()))
         && !mc.field_1724.method_5757()
         && !mc.field_1724.method_5869()
         && !mc.field_1724.method_5771()
         && !mc.field_1724.method_21754()
         && this.lagTimer.passedMs(1000L)
         && !mc.field_1724.method_6128()
         && !Flight.INSTANCE.isOn()
         && !nullCheck()) {
         if (!HoleKickTest.isInWeb(mc.field_1724)) {
            if (mc.field_1724.method_24828()) {
               if (this.mode.getValue() == FastFall.Fast) {
                  MovementUtil.setMotionY(MovementUtil.getMotionY() - (double)(this.noLag.getValue() ? 0.62F : 1.0F));
               }

               if (this.traceDown() != 0 && (double)this.traceDown() <= this.height.getValue() && this.trace()) {
                  MovementUtil.setMotionX(MovementUtil.getMotionX() * 0.05);
                  MovementUtil.setMotionZ(MovementUtil.getMotionZ() * 0.05);
               }
            }

            if (this.mode.getValue() == FastFall.Strict) {
               if (!mc.field_1724.method_24828()) {
                  if (this.onGround) {
                     this.useTimer = true;
                  }

                  if (MovementUtil.getMotionY() >= 0.0) {
                     this.useTimer = false;
                  }

                  this.onGround = false;
               } else {
                  this.useTimer = false;
                  MovementUtil.setMotionY(-0.08);
                  this.onGround = true;
               }
            } else {
               this.useTimer = false;
            }
         }
      }
   }

   @EventHandler
   public void onTimer(TimerEvent event) {
      if (!nullCheck()) {
         if (!mc.field_1724.method_24828() && this.useTimer) {
            event.set((float)this.STimer.getValue());
         }
      }
   }

   @EventHandler
   public void onPacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
      if (!nullCheck() && event.getPacket() instanceof PlayerPositionLookS2CPacket) {
         this.lagTimer.reset();
      }
   }

   private int traceDown() {
      int retval = 0;
      int y = (int)Math.round(mc.field_1724.method_23318()) - 1;

      for (int tracey = y; tracey >= 0; tracey--) {
         HitResult trace = mc.field_1687
            .method_17742(
               new RaycastContext(
                  mc.field_1724.method_19538(),
                  new Vec3d(mc.field_1724.method_23317(), (double)tracey, mc.field_1724.method_23321()),
                  ShapeType.field_17558,
                  FluidHandling.field_1348,
                  mc.field_1724
               )
            );
         if (trace != null && trace.method_17783() == Type.field_1332) {
            return retval;
         }

         retval++;
      }

      return retval;
   }

   private boolean trace() {
      Box bbox = mc.field_1724.method_5829();
      Vec3d basepos = bbox.method_1005();
      double minX = bbox.field_1323;
      double minZ = bbox.field_1321;
      double maxX = bbox.field_1320;
      double maxZ = bbox.field_1324;
      Map<Vec3d, Vec3d> positions = new HashMap();
      positions.put(basepos, new Vec3d(basepos.field_1352, basepos.field_1351 - 1.0, basepos.field_1350));
      positions.put(new Vec3d(minX, basepos.field_1351, minZ), new Vec3d(minX, basepos.field_1351 - 1.0, minZ));
      positions.put(new Vec3d(maxX, basepos.field_1351, minZ), new Vec3d(maxX, basepos.field_1351 - 1.0, minZ));
      positions.put(new Vec3d(minX, basepos.field_1351, maxZ), new Vec3d(minX, basepos.field_1351 - 1.0, maxZ));
      positions.put(new Vec3d(maxX, basepos.field_1351, maxZ), new Vec3d(maxX, basepos.field_1351 - 1.0, maxZ));

      for (Vec3d key : positions.keySet()) {
         RaycastContext context = new RaycastContext(key, (Vec3d)positions.get(key), ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724);
         BlockHitResult result = mc.field_1687.method_17742(context);
         if (result != null && result.method_17783() == Type.field_1332) {
            return false;
         }
      }

      BlockState state = mc.field_1687
         .method_8320(new BlockPosX(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 1.0, mc.field_1724.method_23321()));
      return state.method_26215();
   }
}
