package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.FreeCam;
import me.hextech.remapped.HoleSnap;
import me.hextech.remapped.KeyboardInputEvent;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.inJumpEvent;
import me.hextech.remapped.inVelocityEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Rotation
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Rotation INSTANCE;
    public static float fixRotation;
    public final BooleanSetting fixrotate = this.add(new BooleanSetting("RotateFix", true));
    private final BooleanSetting movement = this.add(new BooleanSetting("MovementFix", true));
    private float prevYaw;

    public Rotation() {
        super("Rotation", Module_JlagirAibYQgkHtbRnhw.Setting);
        INSTANCE = this;
    }

    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply((double)speed);
        float f = MathHelper.sin((float)(yaw * ((float)Math.PI / 180)));
        float g = MathHelper.cos((float)(yaw * ((float)Math.PI / 180)));
        return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
    }

    @EventHandler
    public void onJump(inJumpEvent e) {
        if (!this.fixrotate.getValue()) {
            return;
        }
        if (HoleSnap.INSTANCE.isOn()) {
            return;
        }
        if (Rotation.mc.player.isRiding()) {
            return;
        }
        if (e.isPre()) {
            this.prevYaw = Rotation.mc.player.getYaw();
            Rotation.mc.player.setYaw(fixRotation);
        } else {
            Rotation.mc.player.setYaw(this.prevYaw);
        }
    }

    @EventHandler
    public void onPlayerMove(inVelocityEvent event) {
        if (!this.fixrotate.getValue()) {
            return;
        }
        if (HoleSnap.INSTANCE.isOn()) {
            return;
        }
        if (Rotation.mc.player.isRiding()) {
            return;
        }
        event.setVelocity(Rotation.movementInputToVelocity(event.getMovementInput(), event.getSpeed(), fixRotation));
    }

    @EventHandler
    public void onKeyInput(KeyboardInputEvent e) {
        if (!this.movement.getValue()) {
            return;
        }
        if (HoleSnap.INSTANCE.isOn()) {
            return;
        }
        if (Rotation.mc.player.isRiding() || FreeCam.INSTANCE.isOn()) {
            return;
        }
        float mF = Rotation.mc.player.input.movementForward;
        float mS = Rotation.mc.player.input.movementSideways;
        float delta = (Rotation.mc.player.getYaw() - fixRotation) * ((float)Math.PI / 180);
        float cos = MathHelper.cos((float)delta);
        float sin = MathHelper.sin((float)delta);
        Rotation.mc.player.input.movementSideways = Math.round(mS * cos - mF * sin);
        Rotation.mc.player.input.movementForward = Math.round(mF * cos + mS * sin);
    }
}
