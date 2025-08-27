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
        Vec3d vec3d = new Vec3d(x, y, z).method_1031(0.0, (double)InteractUtil.mc.field_1724.method_18381(InteractUtil.mc.field_1724.method_18376()), 0.0);
        double distancePow2 = 25.0;
        if (result != null) {
            distancePow2 = result.method_17784().method_1025(vec3d);
        }
        Vec3d vec3d2 = InteractUtil.getRotationVector(yaw, pitch);
        Vec3d vec3d3 = vec3d.method_1031(vec3d2.field_1352 * 5.0, vec3d2.field_1351 * 5.0, vec3d2.field_1350 * 5.0);
        EntityHitResult entityHitResult = ProjectileUtil.method_18075((Entity)InteractUtil.mc.field_1724, (Vec3d)vec3d, (Vec3d)vec3d3, (Box)(box = new Box(x - 0.3, y, z - 0.3, x + 0.3, y + (double)InteractUtil.mc.field_1724.method_18381(InteractUtil.mc.field_1724.method_18376()), z + 0.3).method_18804(vec3d2.method_1021(5.0)).method_1009(1.0, 1.0, 1.0)), entity -> !entity.method_7325() && entity.method_5863(), (double)distancePow2);
        if (entityHitResult != null) {
            Entity entity2 = entityHitResult.method_17782();
            Vec3d vec3d4 = entityHitResult.method_17784();
            double g = vec3d.method_1025(vec3d4);
            if ((g < distancePow2 || result == null) && entity2 instanceof LivingEntity) {
                return entityHitResult;
            }
        }
        return result;
    }

    public static HitResult rayTrace(double dst, float yaw, float pitch, double x, double y, double z) {
        Vec3d vec3d = new Vec3d(x, y, z);
        Vec3d vec3d2 = InteractUtil.getRotationVector(pitch, yaw);
        Vec3d vec3d3 = vec3d.method_1031(vec3d2.field_1352 * dst, vec3d2.field_1351 * dst, vec3d2.field_1350 * dst);
        return InteractUtil.mc.field_1687.method_17742(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.field_17559, RaycastContext.FluidHandling.field_1348, (Entity)InteractUtil.mc.field_1724));
    }

    private static Vec3d getRotationVector(float yaw, float pitch) {
        return new Vec3d((double)(MathHelper.method_15374((float)(-pitch * ((float)Math.PI / 180))) * MathHelper.method_15362((float)(yaw * ((float)Math.PI / 180)))), (double)(-MathHelper.method_15374((float)(yaw * ((float)Math.PI / 180)))), (double)(MathHelper.method_15362((float)(-pitch * ((float)Math.PI / 180))) * MathHelper.method_15362((float)(yaw * ((float)Math.PI / 180)))));
    }
}
