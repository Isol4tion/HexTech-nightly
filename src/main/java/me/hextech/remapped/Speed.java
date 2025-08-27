package me.hextech.remapped;

import java.util.Objects;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.HoleKickTest;
import me.hextech.remapped.HoleSnap;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class Speed
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Speed INSTANCE;
    private final EnumSetting<_hIXwTMQyjavijZllSIBF> mode = this.add(new EnumSetting<_hIXwTMQyjavijZllSIBF>("Mode", _hIXwTMQyjavijZllSIBF.Instant));
    private final BooleanSetting strafeGround = this.add(new BooleanSetting("StrafeGround", true, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting stopGround = this.add(new BooleanSetting("StopGround", false, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting jump = this.add(new BooleanSetting("Jump", true, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting inWater = this.add(new BooleanSetting("InWater", false, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting inBlock = this.add(new BooleanSetting("InBlock", false, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting airStop = this.add(new BooleanSetting("AirStop", true, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Strafe)));
    private final BooleanSetting slowCheck = this.add(new BooleanSetting("SlowCheck", true, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Strafe)));
    private final SliderSetting strafeSpeed = this.add(new SliderSetting("StrafeSpeed", 287.3, 100.0, 1000.0, 0.1, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting explosions = this.add(new BooleanSetting("Explosions", false, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting velocity = this.add(new BooleanSetting("Velocity", true, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final SliderSetting multiplier = this.add(new SliderSetting("H-Factor", 1.0, 0.0, 5.0, 0.01, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final SliderSetting vertical = this.add(new SliderSetting("V-Factor", 1.0, 0.0, 5.0, 0.01, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final SliderSetting coolDown = this.add(new SliderSetting("CoolDown", 1000.0, 0.0, 5000.0, 1.0, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 500.0, 0.0, 1000.0, 1.0, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final BooleanSetting slow = this.add(new BooleanSetting("Slowness", false, v -> this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)));
    private final Timer expTimer = new Timer();
    private final Timer lagTimer = new Timer();
    private boolean stop;
    private double speed;
    private double distance;
    private int stage;
    private double lastExp;
    private boolean boost;

    public Speed() {
        super("Speed", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onEnable() {
        if (Speed.mc.field_1724 != null) {
            this.speed = MovementUtil.getSpeed(false);
            this.distance = MovementUtil.getDistance2D();
        }
        this.stage = 4;
    }

    @EventHandler
    public void onStrafe(MoveEvent event) {
        if (this.stopGround.getValue() && Speed.mc.field_1724.method_24828()) {
            return;
        }
        if (HoleKickTest.isInWeb((PlayerEntity)Speed.mc.field_1724) || Speed.mc.field_1724.method_5715() || HoleSnap.INSTANCE.isOn() || INSTANCE.isOn() || Speed.mc.field_1724.method_6128() || EntityUtil.isInsideBlock() || Speed.mc.field_1724.method_5771() || Speed.mc.field_1724.method_5799() || Speed.mc.field_1724.method_31549().field_7479) {
            return;
        }
        if (!MovementUtil.isMoving()) {
            if (this.strafeGround.getValue()) {
                MovementUtil.setMotionX(0.0);
                MovementUtil.setMotionZ(0.0);
            }
            return;
        }
        double[] dir = MovementUtil.directionSpeed(this.getBaseMoveSpeed());
        event.setX(dir[0]);
        event.setZ(dir[1]);
    }

    @EventHandler(priority=100)
    public void invoke(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)) {
            Object t = event.getPacket();
            if (t instanceof EntityVelocityUpdateS2CPacket) {
                EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket)t;
                if (Speed.mc.field_1724 != null && packet.method_11818() == Speed.mc.field_1724.method_5628() && this.velocity.getValue()) {
                    double speed = Math.sqrt(packet.method_11815() * packet.method_11815() + packet.method_11819() * packet.method_11819()) / 8000.0;
                    double d = this.lastExp = this.expTimer.passedMs(this.coolDown.getValueInt()) ? speed : speed - this.lastExp;
                    if (this.lastExp > 0.0) {
                        this.expTimer.reset();
                        mc.method_18859(() -> {
                            this.speed += this.lastExp * this.multiplier.getValue();
                            this.distance += this.lastExp * this.multiplier.getValue();
                            if (MovementUtil.getMotionY() > 0.0 && this.vertical.getValue() != 0.0) {
                                MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                            }
                        });
                    }
                }
            } else if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                this.lagTimer.reset();
                if (Speed.mc.field_1724 != null) {
                    this.distance = 0.0;
                }
                this.speed = 0.0;
                this.stage = 4;
            } else {
                t = event.getPacket();
                if (t instanceof ExplosionS2CPacket) {
                    ExplosionS2CPacket packet = (ExplosionS2CPacket)t;
                    if (this.explosions.getValue() && MovementUtil.isMoving() && Speed.mc.field_1724.method_5649(packet.method_11475(), packet.method_11477(), packet.method_11478()) < 200.0) {
                        double speed = Math.sqrt(Math.abs(packet.method_11472() * packet.method_11472()) + Math.abs(packet.method_11474() * packet.method_11474()));
                        double d = this.lastExp = this.expTimer.passedMs(this.coolDown.getValueInt()) ? speed : speed - this.lastExp;
                        if (this.lastExp > 0.0) {
                            this.expTimer.reset();
                            this.speed += this.lastExp * this.multiplier.getValue();
                            this.distance += this.lastExp * this.multiplier.getValue();
                            if (MovementUtil.getMotionY() > 0.0) {
                                MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        if (this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)) {
            if (!MovementUtil.isMoving()) {
                MovementUtil.setMotionX(0.0);
                MovementUtil.setMotionZ(0.0);
            }
            this.distance = MovementUtil.getDistance2D();
        }
    }

    public double getBaseMoveSpeed() {
        double n = 0.2873;
        if (!(!Speed.mc.field_1724.method_6059(StatusEffects.field_5904) || this.slowCheck.getValue() && Speed.mc.field_1724.method_6059(StatusEffects.field_5909))) {
            n *= 1.0 + 0.2 * (double)(Objects.requireNonNull(Speed.mc.field_1724.method_6112(StatusEffects.field_5904)).method_5578() + 1);
        }
        return n;
    }

    @EventHandler
    public void invoke(MoveEvent event) {
        if (this.mode.is(_hIXwTMQyjavijZllSIBF.Strafe)) {
            if (Speed.mc.field_1724.method_5715() || HoleSnap.INSTANCE.isOn() || INSTANCE.isOn() || Speed.mc.field_1724.method_6128() || EntityUtil.isInsideBlock() || Speed.mc.field_1724.method_5771() || Speed.mc.field_1724.method_5799() || Speed.mc.field_1724.method_31549().field_7479) {
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
        if (this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)) {
            if (!this.inWater.getValue() && (Speed.mc.field_1724.method_5869() || Speed.mc.field_1724.method_5799() || Speed.mc.field_1724.method_5771()) || Speed.mc.field_1724.method_21754() || !this.inBlock.getValue() && EntityUtil.isInsideBlock() || Speed.mc.field_1724.method_31549().field_7479) {
                this.stop = true;
                return;
            }
            if (this.stop) {
                this.stop = false;
                return;
            }
            if (!MovementUtil.isMoving() || HoleSnap.INSTANCE.isOn()) {
                return;
            }
            if (this.stopGround.getValue() && Speed.mc.field_1724.method_24828()) {
                return;
            }
            if (Speed.mc.field_1724.method_6128()) {
                return;
            }
            if (!this.lagTimer.passedMs(this.lagTime.getValueInt())) {
                return;
            }
            if (this.stage == 1 && MovementUtil.isMoving()) {
                this.speed = 1.35 * MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue() / 1000.0) - 0.01;
            } else if (this.stage == 2 && Speed.mc.field_1724.method_24828() && MovementUtil.isMoving() && (Speed.mc.field_1690.field_1903.method_1434() || this.jump.getValue())) {
                double yMotion = 0.3999 + MovementUtil.getJumpSpeed();
                MovementUtil.setMotionY(yMotion);
                event.setY(yMotion);
                this.speed *= this.boost ? 1.6835 : 1.395;
            } else if (this.stage == 3) {
                this.speed = this.distance - 0.66 * (this.distance - MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue() / 1000.0));
                this.boost = !this.boost;
            } else {
                if ((Speed.mc.field_1687.method_39454(null, Speed.mc.field_1724.method_5829().method_989(0.0, MovementUtil.getMotionY(), 0.0)) || Speed.mc.field_1724.field_34927) && this.stage > 0) {
                    this.stage = MovementUtil.isMoving() ? 1 : 0;
                }
                this.speed = this.distance - this.distance / 159.0;
            }
            this.speed = Math.min(this.speed, 10.0);
            this.speed = Math.max(this.speed, MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue() / 1000.0));
            double n = MovementUtil.getMoveForward();
            double n2 = MovementUtil.getMoveStrafe();
            double n3 = Speed.mc.field_1724.method_36454();
            if (n == 0.0 && n2 == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            } else if (n != 0.0 && n2 != 0.0) {
                n *= Math.sin(0.7853981633974483);
                n2 *= Math.cos(0.7853981633974483);
            }
            event.setX((n * this.speed * -Math.sin(Math.toRadians(n3)) + n2 * this.speed * Math.cos(Math.toRadians(n3))) * 0.99);
            event.setZ((n * this.speed * Math.cos(Math.toRadians(n3)) - n2 * this.speed * -Math.sin(Math.toRadians(n3))) * 0.99);
            if (MovementUtil.isMoving()) {
                ++this.stage;
            }
        }
    }

    public static final class _hIXwTMQyjavijZllSIBF
    extends Enum<_hIXwTMQyjavijZllSIBF> {
        public static final /* enum */ _hIXwTMQyjavijZllSIBF Instant;
        public static final /* enum */ _hIXwTMQyjavijZllSIBF Strafe;

        public static _hIXwTMQyjavijZllSIBF[] values() {
            return null;
        }

        public static _hIXwTMQyjavijZllSIBF valueOf(String string) {
            return null;
        }
    }
}
