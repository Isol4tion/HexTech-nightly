package me.hextech.remapped;

import me.hextech.remapped.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class WallCheck
implements Wrapper {
    public static boolean behindWall(BlockPos pos) {
        Vec3d WallVec = CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.lowVersion.getValue() ? new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264(), (double)pos.method_10260() + 0.5) : new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
        WallVec = new Vec3d((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + 0.5);
        BlockHitResult result = null;
        if (WallCheck.mc.field_1687 != null && WallCheck.mc.field_1724 != null) {
            result = WallCheck.mc.field_1687.method_17742(new RaycastContext(EntityUtil.getEyesPos(), WallVec, RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)WallCheck.mc.field_1724));
        }
        if (result == null || result.method_17783() == HitResult.Type.field_1333) {
            return false;
        }
        return WallCheck.mc.field_1724.method_33571().method_1022(pos.method_46558().method_1031(0.0, -0.5, 0.0)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue();
    }
}
