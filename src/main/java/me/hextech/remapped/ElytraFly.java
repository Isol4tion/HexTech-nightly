package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import me.hextech.remapped.TravelEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraFly
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ElytraFly INSTANCE;
    public final SliderSetting upPitch = this.add(new SliderSetting("UpPitch", 0.0, 0.0, 90.0));
    public final SliderSetting upFactor = this.add(new SliderSetting("UpFactor", 1.0, 0.0, 10.0));
    public final SliderSetting downFactor = this.add(new SliderSetting("DownFactor", 1.0, 0.0, 10.0));
    public final SliderSetting speed = this.add(new SliderSetting("Speed", 1.0, 0.1f, 10.0));
    public final BooleanSetting speedLimit = this.add(new BooleanSetting("SpeedLimit", true));
    public final SliderSetting maxSpeed = this.add(new SliderSetting("MaxSpeed", 2.5, (double)0.1f, 10.0, v -> this.speedLimit.getValue()));
    public final BooleanSetting noDrag = this.add(new BooleanSetting("NoDrag", false));
    private final BooleanSetting instantFly = this.add(new BooleanSetting("InstantFly", true));
    private final SliderSetting sneakDownSpeed = this.add(new SliderSetting("DownSpeed", 1.0, 0.1f, 10.0));
    private final BooleanSetting boostTimer = this.add(new BooleanSetting("Timer", true));
    private final SliderSetting timeout = this.add(new SliderSetting("Timeout", 0.5, 0.1f, 1.0));
    private final Timer instantFlyTimer = new Timer();
    private final Timer strictTimer = new Timer();
    private boolean hasElytra = false;
    private boolean hasTouchedGround = false;

    public ElytraFly() {
        super("ElytraFly", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return "Control";
    }

    @Override
    public void onEnable() {
        if (ElytraFly.mc.player != null) {
            if (!ElytraFly.mc.player.isCreative()) {
                ElytraFly.mc.player.getAbilities().allowFlying = false;
            }
            ElytraFly.mc.player.getAbilities().flying = false;
        }
        this.hasElytra = false;
    }

    @Override
    public void onDisable() {
        HexTech.TIMER.reset();
        this.hasElytra = false;
        if (ElytraFly.mc.player != null) {
            if (!ElytraFly.mc.player.isCreative()) {
                ElytraFly.mc.player.getAbilities().allowFlying = false;
            }
            ElytraFly.mc.player.getAbilities().flying = false;
        }
    }

    @Override
    public void onUpdate() {
        if (ElytraFly.nullCheck()) {
            return;
        }
        if (ElytraFly.mc.player.isOnGround()) {
            this.hasTouchedGround = true;
        }
        for (ItemStack is : ElytraFly.mc.player.getArmorItems()) {
            if (is.getItem() instanceof ElytraItem) {
                this.hasElytra = true;
                break;
            }
            this.hasElytra = false;
        }
        if (this.strictTimer.passedMs(1500L) && !this.strictTimer.passedMs(2000L) || EntityUtil.isElytraFlying() && (double)HexTech.TIMER.get() == 0.3) {
            HexTech.TIMER.reset();
        }
        if (!ElytraFly.mc.player.isFallFlying()) {
            if (this.hasTouchedGround && this.boostTimer.getValue() && !ElytraFly.mc.player.isOnGround()) {
                HexTech.TIMER.set(0.3f);
            }
            if (!ElytraFly.mc.player.isOnGround() && this.instantFly.getValue() && ElytraFly.mc.player.getVelocity().getY() < 0.0) {
                if (!this.instantFlyTimer.passedMs((long)(1000.0 * this.timeout.getValue()))) {
                    return;
                }
                this.instantFlyTimer.reset();
                ElytraFly.mc.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)ElytraFly.mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                this.hasTouchedGround = false;
                this.strictTimer.reset();
            }
        }
    }

    protected final Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = MathHelper.cos((float)g);
        float i = MathHelper.sin((float)g);
        float j = MathHelper.cos((float)f);
        float k = MathHelper.sin((float)f);
        return new Vec3d((double)(i * j), (double)(-k), (double)(h * j));
    }

    public final Vec3d getRotationVec(float tickDelta) {
        return this.getRotationVector(-this.upPitch.getValueFloat(), ElytraFly.mc.player.getYaw(tickDelta));
    }

    @EventHandler
    public void onMove(TravelEvent event) {
        double[] dir;
        if (ElytraFly.nullCheck() || !this.hasElytra || !ElytraFly.mc.player.isFallFlying()) {
            return;
        }
        Vec3d lookVec = this.getRotationVec(mc.getTickDelta());
        double lookDist = Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z);
        double motionDist = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
        if (ElytraFly.mc.options.sneakKey.isPressed()) {
            this.setY(-this.sneakDownSpeed.getValue());
        } else if (!ElytraFly.mc.player.input.jumping) {
            this.setY(-3.0E-14 * this.downFactor.getValue());
        }
        if (ElytraFly.mc.player.input.jumping) {
            if (motionDist > this.upFactor.getValue() / this.upFactor.getMaximum()) {
                double rawUpSpeed = motionDist * 0.01325;
                this.setY(this.getY() + rawUpSpeed * 3.2);
                this.setX(this.getX() - lookVec.x * rawUpSpeed / lookDist);
                this.setZ(this.getZ() - lookVec.z * rawUpSpeed / lookDist);
            } else {
                dir = MovementUtil.directionSpeed(this.speed.getValue());
                this.setX(dir[0]);
                this.setZ(dir[1]);
            }
        }
        if (lookDist > 0.0) {
            this.setX(this.getX() + (lookVec.x / lookDist * motionDist - this.getX()) * 0.1);
            this.setZ(this.getZ() + (lookVec.z / lookDist * motionDist - this.getZ()) * 0.1);
        }
        if (!ElytraFly.mc.player.input.jumping) {
            dir = MovementUtil.directionSpeed(this.speed.getValue());
            this.setX(dir[0]);
            this.setZ(dir[1]);
        }
        if (!this.noDrag.getValue()) {
            this.setY(this.getY() * (double)0.99f);
            this.setX(this.getX() * (double)0.98f);
            this.setZ(this.getZ() * (double)0.99f);
        }
        double finalDist = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
        if (this.speedLimit.getValue() && finalDist > this.maxSpeed.getValue()) {
            this.setX(this.getX() * this.maxSpeed.getValue() / finalDist);
            this.setZ(this.getZ() * this.maxSpeed.getValue() / finalDist);
        }
        event.cancel();
        ElytraFly.mc.player.move(MovementType.SELF, ElytraFly.mc.player.getVelocity());
    }

    private double getX() {
        return MovementUtil.getMotionX();
    }

    private void setX(double f) {
        MovementUtil.setMotionX(f);
    }

    private double getY() {
        return MovementUtil.getMotionY();
    }

    private void setY(double f) {
        MovementUtil.setMotionY(f);
    }

    private double getZ() {
        return MovementUtil.getMotionZ();
    }

    private void setZ(double f) {
        MovementUtil.setMotionZ(f);
    }
}
