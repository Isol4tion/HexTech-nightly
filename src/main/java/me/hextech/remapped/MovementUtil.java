package me.hextech.remapped;

import me.hextech.asm.accessors.IVec3d;
import me.hextech.remapped.HoleSnap;
import me.hextech.remapped.Wrapper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class MovementUtil
implements Wrapper {
    private static final double diagonal;
    private static final Vec3d horizontalVelocity;

    public static boolean isMoving() {
        return (double)MovementUtil.mc.player.field_3913.field_3905 != 0.0 || (double)MovementUtil.mc.player.field_3913.field_3907 != 0.0 || HoleSnap.INSTANCE.isOn();
    }

    public static boolean isJumping() {
        return MovementUtil.mc.player.field_3913.field_3904;
    }

    public static double getDistance2D() {
        double xDist = MovementUtil.mc.player.getX() - MovementUtil.mc.player.field_6014;
        double zDist = MovementUtil.mc.player.getZ() - MovementUtil.mc.player.field_5969;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;
        if (MovementUtil.mc.player.method_6059(StatusEffects.field_5913)) {
            int amplifier = ((StatusEffectInstance)MovementUtil.mc.player.method_6088().get(StatusEffects.field_5913)).method_5578();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static float getMoveForward() {
        return MovementUtil.mc.player.field_3913.field_3905;
    }

    public static float getMoveStrafe() {
        return MovementUtil.mc.player.field_3913.field_3907;
    }

    public static Vec3d getHorizontalVelocity(double bps) {
        float yaw = MovementUtil.mc.player.method_36454();
        Vec3d forward = Vec3d.method_1030((float)0.0f, (float)yaw);
        Vec3d right = Vec3d.method_1030((float)0.0f, (float)(yaw + 90.0f));
        double velX = 0.0;
        double velZ = 0.0;
        boolean a = false;
        if (MovementUtil.mc.player.field_3913.field_3910) {
            velX += forward.field_1352 / 20.0 * bps;
            velZ += forward.field_1350 / 20.0 * bps;
            a = true;
        }
        if (MovementUtil.mc.player.field_3913.field_3909) {
            velX -= forward.field_1352 / 20.0 * bps;
            velZ -= forward.field_1350 / 20.0 * bps;
            a = true;
        }
        boolean b = false;
        if (MovementUtil.mc.player.field_3913.field_3906) {
            velX += right.field_1352 / 20.0 * bps;
            velZ += right.field_1350 / 20.0 * bps;
            b = true;
        }
        if (MovementUtil.mc.player.field_3913.field_3908) {
            velX -= right.field_1352 / 20.0 * bps;
            velZ -= right.field_1350 / 20.0 * bps;
            b = true;
        }
        if (a && b) {
            velX *= diagonal;
            velZ *= diagonal;
        }
        ((IVec3d)horizontalVelocity).setX(velX);
        ((IVec3d)horizontalVelocity).setZ(velZ);
        return horizontalVelocity;
    }

    public static double[] directionSpeed(double speed) {
        float forward = MovementUtil.mc.player.field_3913.field_3905;
        float side = MovementUtil.mc.player.field_3913.field_3907;
        float yaw = MovementUtil.mc.player.field_5982 + (MovementUtil.mc.player.method_36454() - MovementUtil.mc.player.field_5982) * mc.getTickDelta();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double getMotionX() {
        return MovementUtil.mc.player.method_18798().field_1352;
    }

    public static void setMotionX(double x) {
        ((IVec3d)MovementUtil.mc.player.method_18798()).setX(x);
    }

    public static double getMotionY() {
        return MovementUtil.mc.player.method_18798().field_1351;
    }

    public static void setMotionY(double y) {
        ((IVec3d)MovementUtil.mc.player.method_18798()).setY(y);
    }

    public static double getMotionZ() {
        return MovementUtil.mc.player.method_18798().field_1350;
    }

    public static void setMotionZ(double z) {
        ((IVec3d)MovementUtil.mc.player.method_18798()).setZ(z);
    }

    public static double getSpeed(boolean slowness) {
        double defaultSpeed = 0.2873;
        return MovementUtil.getSpeed(slowness, defaultSpeed);
    }

    public static double getSpeed(boolean slowness, double defaultSpeed) {
        int amplifier;
        if (MovementUtil.mc.player.method_6059(StatusEffects.field_5904)) {
            amplifier = ((StatusEffectInstance)MovementUtil.mc.player.method_6088().get(StatusEffects.field_5904)).method_5578();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && MovementUtil.mc.player.method_6059(StatusEffects.field_5909)) {
            amplifier = ((StatusEffectInstance)MovementUtil.mc.player.method_6088().get(StatusEffects.field_5909)).method_5578();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (MovementUtil.mc.player.method_5715()) {
            defaultSpeed /= 5.0;
        }
        return defaultSpeed;
    }
}
