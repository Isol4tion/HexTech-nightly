package me.hextech.remapped;

import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class WallCheck implements Wrapper {
   public static boolean behindWall(BlockPos pos) {
      if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue()) {
         new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5);
      } else {
         new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
      }

      Vec3d WallVec = new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
      HitResult result = null;
      if (mc.field_1687 != null && mc.field_1724 != null) {
         result = mc.field_1687
            .method_17742(new RaycastContext(EntityUtil.getEyesPos(), WallVec, ShapeType.field_17558, FluidHandling.field_1348, mc.field_1724));
      }

      return result != null && result.method_17783() != Type.field_1333
         ? mc.field_1724.method_33571().method_1022(pos.method_46558().method_1031(0.0, -0.5, 0.0))
            > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue()
         : false;
   }
}
