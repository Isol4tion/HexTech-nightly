package me.hextech.mod.modules.impl.movement;

import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.MoveEvent;
import me.hextech.api.events.impl.PacketEvent_gBzdMCvQxlHfSrulemGS;
import me.hextech.api.events.impl.UpdateWalkingEvent;
import me.hextech.api.utils.entity.EntityUtil;
import me.hextech.api.utils.entity.MovementUtil;
import me.hextech.api.utils.math.Timer;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.combat.HoleKickTest;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import java.util.Objects;

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
        super("Speed", Category.Movement);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onEnable() {
        if (Speed.mc.player != null) {
            this.speed = MovementUtil.getSpeed(false);
            this.distance = MovementUtil.getDistance2D();
        }
        this.stage = 4;
    }

    @EventHandler
    public void onStrafe(MoveEvent event) {
        if (this.stopGround.getValue() && Speed.mc.player.isOnGround()) {
            return;
        }
        if (HoleKickTest.isInWeb(Speed.mc.player) || Speed.mc.player.isSneaking() || HoleSnap.INSTANCE.isOn() || INSTANCE.isOn() || Speed.mc.player.isFallFlying() || EntityUtil.isInsideBlock() || Speed.mc.player.isInLava() || Speed.mc.player.isTouchingWater() || Speed.mc.player.getAbilities().flying) {
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
    public void invoke(PacketEvent_gBzdMCvQxlHfSrulemGS.Receive event) {
        if (this.mode.is(_hIXwTMQyjavijZllSIBF.Instant)) {
            Packet<?> packet3 = event.getPacket();
            if (packet3 instanceof EntityVelocityUpdateS2CPacket packet) {
                if (Speed.mc.player != null && packet.getId() == Speed.mc.player.getId() && this.velocity.getValue()) {
                    final double speed = Math.sqrt(packet.getVelocityX() * packet.getVelocityX() + packet.getVelocityZ() * packet.getVelocityZ()) / 8000.0;
                    this.lastExp = (this.expTimer.passedMs(this.coolDown.getValueInt()) ? speed : (speed - this.lastExp));
                    if (this.lastExp > 0.0) {
                        this.expTimer.reset();
                        Speed.mc.executeTask(() -> {
                            this.speed += this.lastExp * this.multiplier.getValue();
                            this.distance += this.lastExp * this.multiplier.getValue();
                            if (MovementUtil.getMotionY() > 0.0 && this.vertical.getValue() != 0.0) {
                                MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                            }
                        });
                    }
                }
            }
            else if (packet3 instanceof PlayerPositionLookS2CPacket) {
                this.lagTimer.reset();
                if (Speed.mc.player != null) {
                    this.distance = 0.0;
                }
                this.speed = 0.0;
                this.stage = 4;
            }
            else {
                if (packet3 instanceof ExplosionS2CPacket packet2) {
                    if (this.explosions.getValue() && MovementUtil.isMoving() && Speed.mc.player.squaredDistanceTo(packet2.getX(), packet2.getY(), packet2.getZ()) < 200.0) {
                        final double speed = Math.sqrt(Math.abs(packet2.getPlayerVelocityX() * packet2.getPlayerVelocityX()) + Math.abs(packet2.getPlayerVelocityZ() * packet2.getPlayerVelocityZ()));
                        this.lastExp = (this.expTimer.passedMs(this.coolDown.getValueInt()) ? speed : (speed - this.lastExp));
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
        if (!(!Speed.mc.player.hasStatusEffect(StatusEffects.SPEED) || this.slowCheck.getValue() && Speed.mc.player.hasStatusEffect(StatusEffects.SLOWNESS))) {
            n *= 1.0 + 0.2 * (double)(Objects.requireNonNull(Speed.mc.player.getStatusEffect(StatusEffects.SPEED)).getAmplifier() + 1);
        }
        return n;
    }

    @EventHandler
    public void invoke(MoveEvent event) {
        if (this.mode.is(_hIXwTMQyjavijZllSIBF.Strafe)) {
            if (Speed.mc.player.isSneaking() || HoleSnap.INSTANCE.isOn() || INSTANCE.isOn() || Speed.mc.player.isFallFlying() || EntityUtil.isInsideBlock() || Speed.mc.player.isInLava() || Speed.mc.player.isTouchingWater() || Speed.mc.player.getAbilities().flying) {
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
            if (!this.inWater.getValue() && (Speed.mc.player.isSubmergedInWater() || Speed.mc.player.isTouchingWater() || Speed.mc.player.isInLava()) || Speed.mc.player.isHoldingOntoLadder() || !this.inBlock.getValue() && EntityUtil.isInsideBlock() || Speed.mc.player.getAbilities().flying) {
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
            if (this.stopGround.getValue() && Speed.mc.player.isOnGround()) {
                return;
            }
            if (Speed.mc.player.isFallFlying()) {
                return;
            }
            if (!this.lagTimer.passedMs(this.lagTime.getValueInt())) {
                return;
            }
            if (this.stage == 1 && MovementUtil.isMoving()) {
                this.speed = 1.35 * MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue() / 1000.0) - 0.01;
            } else if (this.stage == 2 && Speed.mc.player.isOnGround() && MovementUtil.isMoving() && (Speed.mc.options.jumpKey.isPressed() || this.jump.getValue())) {
                double yMotion = 0.3999 + MovementUtil.getJumpSpeed();
                MovementUtil.setMotionY(yMotion);
                event.setY(yMotion);
                this.speed *= this.boost ? 1.6835 : 1.395;
            } else if (this.stage == 3) {
                this.speed = this.distance - 0.66 * (this.distance - MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue() / 1000.0));
                this.boost = !this.boost;
            } else {
                if ((Speed.mc.world.canCollide(null, Speed.mc.player.getBoundingBox().offset(0.0, MovementUtil.getMotionY(), 0.0)) || Speed.mc.player.collidedSoftly) && this.stage > 0) {
                    this.stage = MovementUtil.isMoving() ? 1 : 0;
                }
                this.speed = this.distance - this.distance / 159.0;
            }
            this.speed = Math.min(this.speed, 10.0);
            this.speed = Math.max(this.speed, MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue() / 1000.0));
            double n = MovementUtil.getMoveForward();
            double n2 = MovementUtil.getMoveStrafe();
            double n3 = Speed.mc.player.getYaw();
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

    public enum _hIXwTMQyjavijZllSIBF {
        Instant,
        Strafe

    }
}
