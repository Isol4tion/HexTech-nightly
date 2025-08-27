package me.hextech.remapped;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HitboxDesync extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final double MAGIC_OFFSET;

   public HitboxDesync() {
      super("HitboxDesync", Module_JlagirAibYQgkHtbRnhw.Player);
   }

   @Override
   public void onUpdate() {
      if (!nullCheck()) {
         Direction f = mc.field_1724.method_5735();
         Box bb = mc.field_1724.method_5829();
         Vec3d center = bb.method_1005();
         Vec3d offset = new Vec3d(f.method_23955());
         Vec3d fin = this.merge(
            Vec3d.method_24954(BlockPos.method_49638(center)).method_1031(0.5, 0.0, 0.5).method_1019(offset.method_1021(0.20000996883537)), f
         );
         mc.field_1724
            .method_5814(
               fin.field_1352 == 0.0 ? mc.field_1724.method_23317() : fin.field_1352,
               mc.field_1724.method_23318(),
               fin.field_1350 == 0.0 ? mc.field_1724.method_23321() : fin.field_1350
            );
         this.disable();
      }
   }

   private Vec3d merge(Vec3d a, Direction facing) {
      return new Vec3d(
         a.field_1352 * (double)Math.abs(facing.method_23955().x()),
         a.field_1351 * (double)Math.abs(facing.method_23955().y()),
         a.field_1350 * (double)Math.abs(facing.method_23955().z())
      );
   }
}
