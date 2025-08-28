package me.hextech.asm.mixins.freelook;

import me.hextech.remapped.mod.modules.impl.player.freelook.CameraState;
import me.hextech.remapped.FreeLook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Camera.class})
public abstract class CameraMixin {
    @Shadow
    private float cameraY;
    @Unique
    private float lastUpdate;

    @Inject(method={"update"}, at={@At(value="HEAD")})
    private void onCameraUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        CameraState camera = FreeLook.INSTANCE.getCameraState();
        if (camera.doLock) {
            float limitNegativeYaw = camera.originalYaw() - 180.0f;
            float limitPositiveYaw = camera.originalYaw() + 180.0f;
            if (camera.lookYaw > limitPositiveYaw) {
                camera.lookYaw = limitPositiveYaw;
            }
            if (camera.lookYaw < limitNegativeYaw) {
                camera.lookYaw = limitNegativeYaw;
            }
        }
    }

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void modifyRotationArgs(Args args) {
        CameraState camera = FreeLook.INSTANCE.getCameraState();
        if (camera.doLock) {
            float yaw = camera.lookYaw;
            float pitch = camera.lookPitch;
            if (MinecraftClient.getInstance().options.getPerspective().isFrontView()) {
                yaw -= 180.0f;
                pitch = -pitch;
            }
            args.set(0, (Object)Float.valueOf(yaw));
            args.set(1, (Object)Float.valueOf(pitch));
        } else if (camera.doTransition) {
            float delta = this.getCurrentTime() - this.lastUpdate;
            float steps = 1.2f;
            float speed = 2.0f;
            float yawDiff = camera.lookYaw - camera.originalYaw();
            float pitchDiff = camera.lookPitch - camera.originalPitch();
            float yawStep = speed * (yawDiff * steps);
            float pitchStep = speed * (pitchDiff * steps);
            float yaw = MathHelper.stepTowards(camera.lookYaw, camera.originalYaw(), yawStep * delta);
            float pitch = MathHelper.stepTowards(camera.lookPitch, camera.originalPitch(), pitchStep * delta);
            camera.lookYaw = yaw;
            camera.lookPitch = pitch;
            args.set(0, (Object)Float.valueOf(yaw));
            args.set(1, (Object)Float.valueOf(pitch));
            camera.doTransition = (int)camera.originalYaw() != (int)camera.lookYaw || (int)camera.originalPitch() != (int)camera.lookPitch;
        }
        this.lastUpdate = this.getCurrentTime();
    }

    @Unique
    private float getCurrentTime() {
        return (float)((double)System.nanoTime() * 1.0E-8);
    }
}
