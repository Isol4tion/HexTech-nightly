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
        if (WallCheck.mc.world != null && WallCheck.mc.player != null) {
            result = WallCheck.mc.world.method_17742(new RaycastContext(EntityUtil.getEyesPos(), WallVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)WallCheck.mc.player));
        }
        if (result == null || result.getType() == HitResult.Type.MISS) {
            return false;
        }
        return WallCheck.mc.player.method_33571().distanceTo(pos.toCenterPos().add(0.0, -0.5, 0.0)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue();
    }
}
