package me.hextech.remapped;

import me.hextech.asm.accessors.IVec3d;
import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class MovementUtil
implements Wrapper {
    private static final double diagonal = (double)1.0F / Math.sqrt(2.0F);
    private static final Vec3d horizontalVelocity = new Vec3d(0.0F, 0.0F, 0.0F);

    public static boolean isMoving() {
        return (double)MovementUtil.mc.player.input.movementForward != 0.0 || (double)MovementUtil.mc.player.input.movementSideways != 0.0 || HoleSnap.INSTANCE.isOn();
    }

    public static boolean isJumping() {
        return MovementUtil.mc.player.input.jumping;
    }

    public static double getDistance2D() {
        double xDist = MovementUtil.mc.player.getX() - MovementUtil.mc.player.prevX;
        double zDist = MovementUtil.mc.player.getZ() - MovementUtil.mc.player.prevZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;
        if (MovementUtil.mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            int amplifier = MovementUtil.mc.player.getActiveStatusEffects().get(StatusEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static float getMoveForward() {
        return MovementUtil.mc.player.input.movementForward;
    }

    public static float getMoveStrafe() {
        return MovementUtil.mc.player.input.movementSideways;
    }

    public static Vec3d getHorizontalVelocity(double bps) {
        float yaw = MovementUtil.mc.player.getYaw();
        Vec3d forward = Vec3d.fromPolar(0.0f, yaw);
        Vec3d right = Vec3d.fromPolar(0.0f, yaw + 90.0f);
        double velX = 0.0;
        double velZ = 0.0;
        boolean a = false;
        if (MovementUtil.mc.player.input.pressingForward) {
            velX += forward.x / 20.0 * bps;
            velZ += forward.z / 20.0 * bps;
            a = true;
        }
        if (MovementUtil.mc.player.input.pressingBack) {
            velX -= forward.x / 20.0 * bps;
            velZ -= forward.z / 20.0 * bps;
            a = true;
        }
        boolean b = false;
        if (MovementUtil.mc.player.input.pressingRight) {
            velX += right.x / 20.0 * bps;
            velZ += right.z / 20.0 * bps;
            b = true;
        }
        if (MovementUtil.mc.player.input.pressingLeft) {
            velX -= right.x / 20.0 * bps;
            velZ -= right.z / 20.0 * bps;
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
        float forward = MovementUtil.mc.player.input.movementForward;
        float side = MovementUtil.mc.player.input.movementSideways;
        float yaw = MovementUtil.mc.player.prevYaw + (MovementUtil.mc.player.getYaw() - MovementUtil.mc.player.prevYaw) * mc.getTickDelta();
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
        return MovementUtil.mc.player.getVelocity().x;
    }

    public static void setMotionX(double x) {
        ((IVec3d)MovementUtil.mc.player.getVelocity()).setX(x);
    }

    public static double getMotionY() {
        return MovementUtil.mc.player.getVelocity().y;
    }

    public static void setMotionY(double y) {
        ((IVec3d)MovementUtil.mc.player.getVelocity()).setY(y);
    }

    public static double getMotionZ() {
        return MovementUtil.mc.player.getVelocity().z;
    }

    public static void setMotionZ(double z) {
        ((IVec3d)MovementUtil.mc.player.getVelocity()).setZ(z);
    }

    public static double getSpeed(boolean slowness) {
        double defaultSpeed = 0.2873;
        return MovementUtil.getSpeed(slowness, defaultSpeed);
    }

    public static double getSpeed(boolean slowness, double defaultSpeed) {
        int amplifier;
        if (MovementUtil.mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            amplifier = MovementUtil.mc.player.getActiveStatusEffects().get(StatusEffects.SPEED).getAmplifier();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && MovementUtil.mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            amplifier = MovementUtil.mc.player.getActiveStatusEffects().get(StatusEffects.SLOWNESS).getAmplifier();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (MovementUtil.mc.player.isSneaking()) {
            defaultSpeed /= 5.0;
        }
        return defaultSpeed;
    }
}
