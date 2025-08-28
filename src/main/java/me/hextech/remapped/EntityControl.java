package me.hextech.remapped;

import me.hextech.asm.accessors.IVec3d;
import me.hextech.remapped.BoatMoveEvent;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.Vec3d;

public class EntityControl
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static EntityControl INSTANCE;
    public final BooleanSetting fly = this.add(new BooleanSetting("Fly", true));
    public final SliderSetting speed = this.add(new SliderSetting("Speed", 5.0, 0.1, 50.0));
    public final SliderSetting fallSpeed = this.add(new SliderSetting("FallSpeed", 0.1, 0.0, 50.0));
    private final SliderSetting verticalSpeed = this.add(new SliderSetting("VerticalSpeed", 6.0, 0.0, 20.0));
    private final BooleanSetting noSync = this.add(new BooleanSetting("NoSync", false));

    public EntityControl() {
        super("EntityControl", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @EventHandler
    public void onBoat(BoatMoveEvent event) {
        double velY;
        if (EntityControl.nullCheck() || !this.fly.getValue()) {
            return;
        }
        BoatEntity boat = event.getBoat();
        if (boat == null) {
            return;
        }
        if (boat.getControllingPassenger() != EntityControl.mc.player) {
            return;
        }
        boat.setYaw(EntityControl.mc.player.getYaw());
        Vec3d vel = MovementUtil.getHorizontalVelocity(this.speed.getValue());
        double velX = vel.getX();
        double velZ = vel.getZ();
        if (EntityControl.mc.currentScreen instanceof ChatScreen || EntityControl.mc.currentScreen != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.isOff()) {
            velY = -this.fallSpeed.getValue() / 20.0;
        } else {
            boolean sprint = InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(EntityControl.mc.options.sprintKey.getBoundKeyTranslationKey()).getCode());
            boolean jump = InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(EntityControl.mc.options.jumpKey.getBoundKeyTranslationKey()).getCode());
            velY = jump ? (sprint ? -this.fallSpeed.getValue() / 20.0 : this.verticalSpeed.getValue() / 20.0) : (sprint ? -this.verticalSpeed.getValue() / 20.0 : -this.fallSpeed.getValue() / 20.0);
        }
        ((IVec3d)boat.getVelocity()).setX(velX);
        ((IVec3d)boat.getVelocity()).setY(velY);
        ((IVec3d)boat.getVelocity()).setZ(velZ);
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        double velY;
        if (EntityControl.nullCheck() || !this.fly.getValue()) {
            return;
        }
        Entity boat = EntityControl.mc.player.getVehicle();
        if (boat == null) {
            return;
        }
        boat.setYaw(EntityControl.mc.player.getYaw());
        Vec3d vel = MovementUtil.getHorizontalVelocity(this.speed.getValue());
        double velX = vel.getX();
        double velZ = vel.getZ();
        if (EntityControl.mc.currentScreen instanceof ChatScreen || EntityControl.mc.currentScreen != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.isOff()) {
            velY = -this.fallSpeed.getValue() / 20.0;
        } else {
            boolean sprint = InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(EntityControl.mc.options.sprintKey.getBoundKeyTranslationKey()).getCode());
            boolean jump = InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(EntityControl.mc.options.jumpKey.getBoundKeyTranslationKey()).getCode());
            velY = jump ? (sprint ? -this.fallSpeed.getValue() / 20.0 : this.verticalSpeed.getValue() / 20.0) : (sprint ? -this.verticalSpeed.getValue() / 20.0 : -this.fallSpeed.getValue() / 20.0);
        }
        ((IVec3d)boat.getVelocity()).setX(velX);
        ((IVec3d)boat.getVelocity()).setY(velY);
        ((IVec3d)boat.getVelocity()).setZ(velZ);
    }

    @EventHandler
    private void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (event.getPacket() instanceof VehicleMoveS2CPacket && this.noSync.getValue()) {
            event.cancel();
        }
    }
}
