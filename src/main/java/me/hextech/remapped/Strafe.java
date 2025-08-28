package me.hextech.remapped;

import java.util.Objects;

import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.utils.entity.EntityUtil;
import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.entity.effect.StatusEffects;

public class Strafe
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Strafe INSTANCE;
    private final BooleanSetting airStop = this.add(new BooleanSetting("AirStop", true));
    private final BooleanSetting slowCheck = this.add(new BooleanSetting("SlowCheck", true));

    public Strafe() {
        super("Strafe", "Modifies sprinting", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @EventHandler
    public void onStrafe(MoveEvent event) {
        if (Strafe.mc.player.isSneaking() || HoleSnap.INSTANCE.isOn() || Speed.INSTANCE.isOn() || Strafe.mc.player.isFallFlying() || EntityUtil.isInsideBlock() || Strafe.mc.player.isInLava() || Strafe.mc.player.isTouchingWater() || Strafe.mc.player.getAbilities().flying) {
            return;
        }
        if (!MovementUtil.isMoving()) {
            if (this.airStop.getValue()) {
                MovementUtil.setMotionX(0.0);
                MovementUtil.setMotionZ(0.0);
            }
            return;
        }
        double[] dir = MovementUtil.directionSpeed(this.getBaseMoveSpeed());
        event.setX(dir[0]);
        event.setZ(dir[1]);
    }

    public double getBaseMoveSpeed() {
        double n = 0.2873;
        if (!(!Strafe.mc.player.hasStatusEffect(StatusEffects.SPEED) || this.slowCheck.getValue() && Strafe.mc.player.hasStatusEffect(StatusEffects.SLOWNESS))) {
            n *= 1.0 + 0.2 * (double)(Objects.requireNonNull(Strafe.mc.player.getStatusEffect(StatusEffects.SPEED)).getAmplifier() + 1);
        }
        return n;
    }
}
