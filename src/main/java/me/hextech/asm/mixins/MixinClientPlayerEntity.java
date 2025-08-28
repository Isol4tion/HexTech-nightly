package me.hextech.asm.mixins;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.Random;
import me.hextech.HexTech;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.Event;
import me.hextech.remapped.ForceSync;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.NoSlow_PaVUKKxFbWGbplzMaucl;
import me.hextech.remapped.PacketControl;
import me.hextech.remapped.PortalGui;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.UpdateWalkingEvent;
import me.hextech.remapped.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerEntity.class})
public abstract class MixinClientPlayerEntity
extends AbstractClientPlayerEntity {
    @Shadow
    public Input input;
    @Final
    @Shadow
    public ClientPlayNetworkHandler networkHandler;
    @Final
    @Shadow
    protected MinecraftClient client;
    @Shadow
    @Final
    private List<ClientPlayerTickable> tickables;
    @Shadow
    private boolean autoJumpEnabled;
    @Shadow
    private double lastX;
    @Shadow
    private double lastBaseY;
    @Shadow
    private double lastZ;
    @Shadow
    private float lastYaw;
    @Shadow
    private float lastPitch;
    @Shadow
    private boolean lastOnGround;
    @Shadow
    private boolean lastSneaking;
    @Shadow
    private int ticksSinceLastPositionPacketSent;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPushOutOfBlocksHook(double x, double d, CallbackInfo info) {
        if (Velocity.INSTANCE.isOn() && Velocity.INSTANCE.blockPush.getValue()) {
            info.cancel();
        }
    }

    @Redirect(method={"tickMovement"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require=0)
    private boolean tickMovementHook(ClientPlayerEntity player) {
        if (NoSlow_PaVUKKxFbWGbplzMaucl.INSTANCE.isOn()) {
            return false;
        }
        return player.isUsingItem();
    }

    @Redirect(method={"updateNausea"}, at=@At(value="FIELD", target="Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
    private Screen updateNauseaGetCurrentScreenProxy(MinecraftClient client) {
        if (PortalGui.INSTANCE.isOn()) {
            return null;
        }
        return client.currentScreen;
    }

    @Inject(method={"move"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")}, cancellable=true)
    public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.movehook.getValue()) {
            MoveEvent event = new MoveEvent(movement.x, movement.y, movement.z);
            HexTech.EVENT_BUS.post(event);
            ci.cancel();
            if (!event.isCancelled()) {
                super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
            }
        }
    }

    @Shadow
    private void sendSprintingPacket() {
    }

    @Shadow
    private void sendMovementPackets() {
    }

    @Shadow
    protected boolean isCamera() {
        return false;
    }


    @Shadow public abstract float getYaw(float tickDelta);

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    private void sendMovementPacketsHook(CallbackInfo ci) {
        ci.cancel();
        if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.packethook.getValue()) {
            try {
                UpdateWalkingEvent updateEvent;
                RotateManager.lastEvent = updateEvent = new UpdateWalkingEvent(Event.Pre);
                HexTech.EVENT_BUS.post(updateEvent);
                this.sendSprintingPacket();
                boolean bl = this.isSneaking();
                if (bl != this.lastSneaking) {
                    ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
                    this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
                    this.lastSneaking = bl;
                }
                if (this.isCamera()) {
                    boolean bl3;
                    double d = this.getX() - this.lastX;
                    double e = this.getY() - this.lastBaseY;
                    double f = this.getZ() - this.lastZ;
                    float yaw = this.getYaw();
                    float pitch = this.getPitch();
                    RotateEvent rotateEvent = new RotateEvent(yaw, pitch);
                    HexTech.EVENT_BUS.post(rotateEvent);
                    if (rotateEvent.isModified() && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.random.getValue() && new Random().nextBoolean() && new Random().nextBoolean()) {
                        rotateEvent.setPitch(Math.min(new Random().nextFloat() * 2.0f + rotateEvent.getPitch(), 90.0f));
                    }
                    yaw = rotateEvent.getYaw();
                    pitch = rotateEvent.getPitch();
                    HexTech.ROTATE.rotateYaw = yaw;
                    HexTech.ROTATE.rotatePitch = pitch;
                    double g = yaw - HexTech.ROTATE.lastYaw;
                    double h = pitch - RotateManager.lastPitch;
                    ++this.ticksSinceLastPositionPacketSent;
                    boolean bl2 = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20 || ForceSync.INSTANCE.isOn() && ForceSync.INSTANCE.position.getValue();
                    boolean bl4 = bl3 = g != 0.0 || h != 0.0 || ForceSync.INSTANCE.isOn() && ForceSync.INSTANCE.rotate.getValue();
                    if (PacketControl.INSTANCE.isOn()) {
                        bl3 = PacketControl.INSTANCE.full;
                    }
                    if (this.hasVehicle()) {
                        Vec3d vec3d = this.getVelocity();
                        this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, yaw, pitch, this.isOnGround()));
                        bl2 = false;
                    } else if (bl2 && bl3) {
                        this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(this.getX(), this.getY(), this.getZ(), yaw, pitch, this.isOnGround()));
                    } else if (bl2) {
                        this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(this.getX(), this.getY(), this.getZ(), this.isOnGround()));
                    } else if (bl3) {
                        this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, this.isOnGround()));
                    } else if (this.lastOnGround != this.isOnGround()) {
                        this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(this.isOnGround()));
                    }
                    if (bl2) {
                        this.lastX = this.getX();
                        this.lastBaseY = this.getY();
                        this.lastZ = this.getZ();
                        this.ticksSinceLastPositionPacketSent = 0;
                    }
                    if (bl3) {
                        this.lastYaw = yaw;
                        this.lastPitch = pitch;
                    }
                    this.lastOnGround = this.isOnGround();
                    this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
                }
                HexTech.EVENT_BUS.post(new UpdateWalkingEvent(Event.Post));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void tickHook(CallbackInfo ci) {
        ci.cancel();
        if (!this.getWorld().isPosLoaded(this.getBlockX(), this.getBlockZ())) {
            return;
        }
        super.tick();
        if (this.hasVehicle()) {
            UpdateWalkingEvent pre;
            RotateManager.lastEvent = pre = new UpdateWalkingEvent(Event.Pre);
            HexTech.EVENT_BUS.post(pre);
            if (!pre.isCancelRotate()) {
                float yaw = this.getYaw();
                float pitch = this.getPitch();
                RotateEvent rot = new RotateEvent(yaw, pitch);
                HexTech.EVENT_BUS.post(rot);
                if (rot.isModified() && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.random.getValue() && new Random().nextBoolean() && new Random().nextBoolean()) {
                    rot.setPitch(Math.min(new Random().nextFloat() * 2.0f + rot.getPitch(), 90.0f));
                }
                yaw = rot.getYaw();
                pitch = rot.getPitch();
                HexTech.ROTATE.rotateYaw = yaw;
                HexTech.ROTATE.rotatePitch = pitch;
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, this.isOnGround()));
            }
            HexTech.EVENT_BUS.post(new UpdateWalkingEvent(Event.Post));
            this.networkHandler.sendPacket(new PlayerInputC2SPacket(this.sidewaysSpeed, this.forwardSpeed, this.input.jumping, this.input.sneaking));
            Entity root = this.getRootVehicle();
            if (root != this && root.isLogicalSideForUpdatingMovement()) {
                this.networkHandler.sendPacket(new VehicleMoveC2SPacket(root));
                this.sendSprintingPacket();
            }
        } else {
            this.sendMovementPackets();
        }
        for (ClientPlayerTickable tickable : this.tickables) {
            tickable.tick();
        }
    }
}
