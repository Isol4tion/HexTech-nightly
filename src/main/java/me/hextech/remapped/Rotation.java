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
        double d = movementInput.method_1027();
        if (d < 1.0E-7) {
            return Vec3d.field_1353;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.method_1029() : movementInput).method_1021((double)speed);
        float f = MathHelper.method_15374((float)(yaw * ((float)Math.PI / 180)));
        float g = MathHelper.method_15362((float)(yaw * ((float)Math.PI / 180)));
        return new Vec3d(vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f);
    }

    @EventHandler
    public void onJump(inJumpEvent e) {
        if (!this.fixrotate.getValue()) {
            return;
        }
        if (HoleSnap.INSTANCE.isOn()) {
            return;
        }
        if (Rotation.mc.player.method_3144()) {
            return;
        }
        if (e.isPre()) {
            this.prevYaw = Rotation.mc.player.method_36454();
            Rotation.mc.player.method_36456(fixRotation);
        } else {
            Rotation.mc.player.method_36456(this.prevYaw);
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
        if (Rotation.mc.player.method_3144()) {
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
        if (Rotation.mc.player.method_3144() || FreeCam.INSTANCE.isOn()) {
            return;
        }
        float mF = Rotation.mc.player.field_3913.field_3905;
        float mS = Rotation.mc.player.field_3913.field_3907;
        float delta = (Rotation.mc.player.method_36454() - fixRotation) * ((float)Math.PI / 180);
        float cos = MathHelper.method_15362((float)delta);
        float sin = MathHelper.method_15374((float)delta);
        Rotation.mc.player.field_3913.field_3907 = Math.round(mS * cos - mF * sin);
        Rotation.mc.player.field_3913.field_3905 = Math.round(mF * cos + mS * sin);
    }
}
