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
        if (boat.method_5642() != EntityControl.mc.field_1724) {
            return;
        }
        boat.method_36456(EntityControl.mc.field_1724.method_36454());
        Vec3d vel = MovementUtil.getHorizontalVelocity(this.speed.getValue());
        double velX = vel.method_10216();
        double velZ = vel.method_10215();
        if (EntityControl.mc.field_1755 instanceof ChatScreen || EntityControl.mc.field_1755 != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.isOff()) {
            velY = -this.fallSpeed.getValue() / 20.0;
        } else {
            boolean sprint = InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)EntityControl.mc.field_1690.field_1867.method_1428()).method_1444());
            boolean jump = InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)EntityControl.mc.field_1690.field_1903.method_1428()).method_1444());
            velY = jump ? (sprint ? -this.fallSpeed.getValue() / 20.0 : this.verticalSpeed.getValue() / 20.0) : (sprint ? -this.verticalSpeed.getValue() / 20.0 : -this.fallSpeed.getValue() / 20.0);
        }
        ((IVec3d)boat.method_18798()).setX(velX);
        ((IVec3d)boat.method_18798()).setY(velY);
        ((IVec3d)boat.method_18798()).setZ(velZ);
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        double velY;
        if (EntityControl.nullCheck() || !this.fly.getValue()) {
            return;
        }
        Entity boat = EntityControl.mc.field_1724.method_5854();
        if (boat == null) {
            return;
        }
        boat.method_36456(EntityControl.mc.field_1724.method_36454());
        Vec3d vel = MovementUtil.getHorizontalVelocity(this.speed.getValue());
        double velX = vel.method_10216();
        double velZ = vel.method_10215();
        if (EntityControl.mc.field_1755 instanceof ChatScreen || EntityControl.mc.field_1755 != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.isOff()) {
            velY = -this.fallSpeed.getValue() / 20.0;
        } else {
            boolean sprint = InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)EntityControl.mc.field_1690.field_1867.method_1428()).method_1444());
            boolean jump = InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)InputUtil.method_15981((String)EntityControl.mc.field_1690.field_1903.method_1428()).method_1444());
            velY = jump ? (sprint ? -this.fallSpeed.getValue() / 20.0 : this.verticalSpeed.getValue() / 20.0) : (sprint ? -this.verticalSpeed.getValue() / 20.0 : -this.fallSpeed.getValue() / 20.0);
        }
        ((IVec3d)boat.method_18798()).setX(velX);
        ((IVec3d)boat.method_18798()).setY(velY);
        ((IVec3d)boat.method_18798()).setZ(velZ);
    }

    @EventHandler
    private void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (event.getPacket() instanceof VehicleMoveS2CPacket && this.noSync.getValue()) {
            event.cancel();
        }
    }
}
