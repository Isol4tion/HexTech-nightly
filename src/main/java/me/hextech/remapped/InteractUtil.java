package me.hextech.remapped;

import me.hextech.remapped.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class InteractUtil
implements Wrapper {
    public static HitResult getRtxTarget(float yaw, float pitch, double x, double y, double z) {
        Box box;
        HitResult result = InteractUtil.rayTrace(5.0, yaw, pitch, x, y, z);
        Vec3d vec3d = new Vec3d(x, y, z).add(0.0, (double)InteractUtil.mc.player.method_18381(InteractUtil.mc.player.method_18376()), 0.0);
        double distancePow2 = 25.0;
        if (result != null) {
            distancePow2 = result.getPos().squaredDistanceTo(vec3d);
        }
        Vec3d vec3d2 = InteractUtil.getRotationVector(yaw, pitch);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 5.0, vec3d2.y * 5.0, vec3d2.z * 5.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast((Entity)InteractUtil.mc.player, (Vec3d)vec3d, (Vec3d)vec3d3, (Box)(box = new Box(x - 0.3, y, z - 0.3, x + 0.3, y + (double)InteractUtil.mc.player.method_18381(InteractUtil.mc.player.method_18376()), z + 0.3).stretch(vec3d2.multiply(5.0)).expand(1.0, 1.0, 1.0)), entity -> !entity.isSpectator() && entity.canHit(), (double)distancePow2);
        if (entityHitResult != null) {
            Entity entity2 = entityHitResult.getEntity();
            Vec3d vec3d4 = entityHitResult.method_17784();
            double g = vec3d.squaredDistanceTo(vec3d4);
            if ((g < distancePow2 || result == null) && entity2 instanceof LivingEntity) {
                return entityHitResult;
            }
        }
        return result;
    }

    public static HitResult rayTrace(double dst, float yaw, float pitch, double x, double y, double z) {
        Vec3d vec3d = new Vec3d(x, y, z);
        Vec3d vec3d2 = InteractUtil.getRotationVector(pitch, yaw);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * dst, vec3d2.y * dst, vec3d2.z * dst);
        return InteractUtil.mc.world.method_17742(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)InteractUtil.mc.player));
    }

    private static Vec3d getRotationVector(float yaw, float pitch) {
        return new Vec3d((double)(MathHelper.sin((float)(-pitch * ((float)Math.PI / 180))) * MathHelper.cos((float)(yaw * ((float)Math.PI / 180)))), (double)(-MathHelper.sin((float)(yaw * ((float)Math.PI / 180)))), (double)(MathHelper.cos((float)(-pitch * ((float)Math.PI / 180))) * MathHelper.cos((float)(yaw * ((float)Math.PI / 180)))));
    }
}
