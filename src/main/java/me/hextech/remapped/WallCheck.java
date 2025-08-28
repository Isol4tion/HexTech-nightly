package me.hextech.remapped;

import me.hextech.remapped.api.utils.Wrapper;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.mod.modules.impl.combat.AutoCrystal_QcRVYRsOqpKivetoXSJa;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class WallCheck
implements Wrapper {
    public static boolean behindWall(BlockPos pos) {
        Vec3d WallVec;
        WallVec = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.7, (double)pos.getZ() + 0.5);
        BlockHitResult result = null;
        if (WallCheck.mc.world != null && WallCheck.mc.player != null) {
            result = WallCheck.mc.world.raycast(new RaycastContext(EntityUtil.getEyesPos(), WallVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, WallCheck.mc.player));
        }
        if (result == null || result.getType() == HitResult.Type.MISS) {
            return false;
        }
        return WallCheck.mc.player.getEyePos().distanceTo(pos.toCenterPos().add(0.0, -0.5, 0.0)) > AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.wallRange.getValue();
    }
}
