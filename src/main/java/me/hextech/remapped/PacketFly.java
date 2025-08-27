package me.hextech.remapped;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.MovementUtil;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.PacketEvent_YXFfxdDjQAfjBumqRbBu;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.UpdateWalkingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class PacketFly
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final BooleanSetting flight = this.add(new BooleanSetting("Flight", true).setParent());
    public final SliderSetting flightMode = this.add(new SliderSetting("FMode", 0, 0, 1, v -> this.flight.isOpen()));
    public final SliderSetting antiFactor = this.add(new SliderSetting("AntiFactor", 1.0, 0.1, 3.0));
    public final SliderSetting extraFactor = this.add(new SliderSetting("ExtraFactor", 1.0, 0.1, 3.0));
    public final BooleanSetting strafeFactor = this.add(new BooleanSetting("StrafeFactor", true));
    public final SliderSetting loops = this.add(new SliderSetting("Loops", 1, 1, 10));
    public final BooleanSetting antiRotation = this.add(new BooleanSetting("AntiRotation", false));
    public final BooleanSetting setID = this.add(new BooleanSetting("SetID", true));
    public final BooleanSetting setMove = this.add(new BooleanSetting("SetMove", false));
    public final BooleanSetting nocliperino = this.add(new BooleanSetting("NoClip", false));
    public final BooleanSetting sendTeleport = this.add(new BooleanSetting("Teleport", true));
    public final BooleanSetting setPos = this.add(new BooleanSetting("SetPos", false));
    public final BooleanSetting invalidPacket = this.add(new BooleanSetting("InvalidPacket", true));
    private final Set<PlayerMoveC2SPacket> packets = new ConcurrentSet();
    private int flightCounter = 0;
    private int teleportID = 0;

    public PacketFly() {
        super("PacketFly", "PacketFly", Module_JlagirAibYQgkHtbRnhw.Movement);
    }

    @EventHandler
    public void onUpdateWalkingPlayer(UpdateWalkingEvent event) {
        double speed;
        if (PacketFly.nullCheck()) {
            return;
        }
        if (event.isPost()) {
            return;
        }
        PacketFly.mc.player.method_18800(0.0, 0.0, 0.0);
        boolean checkCollisionBoxes = this.checkHitBoxes();
        double d = PacketFly.mc.player.input.field_3904 && (checkCollisionBoxes || !MovementUtil.isMoving()) ? (this.flight.getValue() && !checkCollisionBoxes ? (this.flightMode.getValue() == 0.0 ? (this.resetCounter(10) ? -0.032 : 0.062) : (this.resetCounter(20) ? -0.032 : 0.062)) : 0.062) : (PacketFly.mc.player.input.field_3903 ? -0.062 : (!checkCollisionBoxes ? (this.resetCounter(4) ? (this.flight.getValue() ? -0.04 : 0.0) : 0.0) : (speed = 0.0)));
        if (checkCollisionBoxes && MovementUtil.isMoving() && speed != 0.0) {
            speed /= this.antiFactor.getValue();
        }
        double[] strafing = this.getMotion(this.strafeFactor.getValue() && checkCollisionBoxes ? 0.031 : 0.26);
        int i = 1;
        while ((double)i < this.loops.getValue() + 1.0) {
            MovementUtil.setMotionX(strafing[0] * (double)i * this.extraFactor.getValue());
            MovementUtil.setMotionY(speed * (double)i);
            MovementUtil.setMotionZ(strafing[1] * (double)i * this.extraFactor.getValue());
            this.sendPackets(MovementUtil.getMotionX(), MovementUtil.getMotionY(), MovementUtil.getMotionZ(), this.sendTeleport.getValue());
            ++i;
        }
    }

    @EventHandler
    public void onMove(MoveEvent event) {
        if (PacketFly.nullCheck()) {
            return;
        }
        if (this.setMove.getValue() && this.flightCounter != 0) {
            event.setX(MovementUtil.getMotionX());
            event.setY(MovementUtil.getMotionY());
            event.setZ(MovementUtil.getMotionZ());
            if (this.nocliperino.getValue() && this.checkHitBoxes()) {
                PacketFly.mc.player.field_5960 = true;
            }
        }
    }

    @EventHandler
    public void onPacketSend(PacketEvent event) {
        PlayerMoveC2SPacket packet;
        if (PacketFly.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket && !this.packets.remove(packet = (PlayerMoveC2SPacket)event.getPacket())) {
            if (event.getPacket() instanceof PlayerMoveC2SPacket.LookAndOnGround && !this.antiRotation.getValue()) {
                return;
            }
            event.cancel();
        }
    }

    @EventHandler
    public void onReceivePacket(PacketEvent_YXFfxdDjQAfjBumqRbBu event) {
        if (PacketFly.nullCheck()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket)event.getPacket();
            if (this.setID.getValue()) {
                this.teleportID = packet.method_11737();
            }
        }
    }

    private boolean checkHitBoxes() {
        return PacketFly.mc.world.method_39454((Entity)PacketFly.mc.player, PacketFly.mc.player.method_5829().expand(-0.0625, -0.0625, -0.0625));
    }

    private boolean resetCounter(int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = MovementUtil.getMoveForward();
        float moveStrafe = MovementUtil.getMoveStrafe();
        float rotationYaw = PacketFly.mc.player.field_5982 + (PacketFly.mc.player.method_36454() - PacketFly.mc.player.field_5982) * mc.method_1488();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private void sendPackets(double x, double y, double z, boolean teleport) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = PacketFly.mc.player.method_19538().add(vec);
        Vec3d outOfBoundsVec = this.outOfBoundsVec(position);
        this.packetSender((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.PositionAndOnGround(position.x, position.y, position.z, PacketFly.mc.player.method_24828()));
        if (this.invalidPacket.getValue()) {
            this.packetSender((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.PositionAndOnGround(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, PacketFly.mc.player.method_24828()));
        }
        if (this.setPos.getValue()) {
            PacketFly.mc.player.method_5814(position.x, position.y, position.z);
        }
        this.teleportPacket(teleport);
    }

    private void teleportPacket(boolean shouldTeleport) {
        if (shouldTeleport) {
            PacketFly.mc.player.networkHandler.method_52787((Packet)new TeleportConfirmC2SPacket(++this.teleportID));
        }
    }

    private Vec3d outOfBoundsVec(Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }

    private void packetSender(PlayerMoveC2SPacket packet) {
        this.packets.add(packet);
        PacketFly.mc.player.networkHandler.method_52787((Packet)packet);
    }
}
