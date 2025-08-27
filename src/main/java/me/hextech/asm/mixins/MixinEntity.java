package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.CameraState;
import me.hextech.remapped.FreeLook;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.NoRender;
import me.hextech.remapped.Shader_CLqIXXaHSdAoBoxRSgjR;
import me.hextech.remapped.Velocity;
import me.hextech.remapped.Wrapper;
import me.hextech.remapped.inVelocityEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Unique
    private CameraState camera;

    @Shadow
    private static Vec3d method_18795(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.method_1027();
        if (d < 1.0E-7) {
            return Vec3d.field_1353;
        }
        Vec3d vec3d = (d > 1.0 ? movementInput.method_1029() : movementInput).method_1021((double)speed);
        float f = MathHelper.method_15374((float)(yaw * ((float)Math.PI / 180)));
        float g = MathHelper.method_15362((float)(yaw * ((float)Math.PI / 180)));
        return new Vec3d(vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f);
    }

    @Inject(at={@At(value="HEAD")}, method={"isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z"}, cancellable=true)
    private void onIsInvisibleCheck(PlayerEntity message, CallbackInfoReturnable<Boolean> cir) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.invisible.getValue()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"updateVelocity"}, at={@At(value="HEAD")}, cancellable=true)
    public void updateVelocityHook(float speed, Vec3d movementInput, CallbackInfo ci) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        if (this == Wrapper.mc.field_1724) {
            ci.cancel();
            inVelocityEvent event = new inVelocityEvent(movementInput, speed, Wrapper.mc.field_1724.method_36454(), MixinEntity.method_18795(movementInput, speed, Wrapper.mc.field_1724.method_36454()));
            HexTech.EVENT_BUS.post(event);
            Wrapper.mc.field_1724.method_18799(Wrapper.mc.field_1724.method_18798().method_1019(event.getVelocity()));
        }
    }

    @Inject(method={"isGlowing"}, at={@At(value="HEAD")}, cancellable=true)
    void isGlowingHook(CallbackInfoReturnable<Boolean> cir) {
        if (Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.isOn()) {
            cir.setReturnValue((Object)Shader_CLqIXXaHSdAoBoxRSgjR.INSTANCE.shouldRender((Entity)this));
        }
    }

    @ModifyArgs(method={"pushAwayFrom"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void pushAwayFromHook(Args args) {
        if (this == MinecraftClient.method_1551().field_1724) {
            double value = 1.0;
            if (Velocity.INSTANCE.isOn() && Velocity.INSTANCE.entityPush.getValue()) {
                value = 0.0;
            }
            args.set(0, (Object)((Double)args.get(0) * value));
            args.set(1, (Object)((Double)args.get(1) * value));
            args.set(2, (Object)((Double)args.get(2) * value));
        }
    }

    @Inject(method={"isOnFire"}, at={@At(value="HEAD")}, cancellable=true)
    void isOnFireHook(CallbackInfoReturnable<Boolean> cir) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fireEntity.getValue()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"changeLookDirection"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo callback) {
        if ((Entity)this instanceof ClientPlayerEntity) {
            this.camera = FreeLook.INSTANCE.getCameraState();
            if (this.camera.doLock) {
                this.applyTransformedAngle(cursorDeltaX, cursorDeltaY);
                callback.cancel();
            } else if (this.camera.doTransition) {
                this.applyTransformedAngle(cursorDeltaX, cursorDeltaY);
            }
        }
    }

    @Unique
    private void applyTransformedAngle(double cursorDeltaX, double cursorDeltaY) {
        float cursorDeltaMultiplier = 0.15f;
        float transformedCursorDeltaX = (float)cursorDeltaX * cursorDeltaMultiplier;
        float transformedCursorDeltaY = (float)cursorDeltaY * cursorDeltaMultiplier;
        float yaw = this.camera.lookYaw;
        float pitch = this.camera.lookPitch;
        pitch += transformedCursorDeltaY;
        pitch = MathHelper.method_15363((float)pitch, (float)-90.0f, (float)90.0f);
        this.camera.lookYaw = yaw += transformedCursorDeltaX;
        this.camera.lookPitch = pitch;
    }
}
