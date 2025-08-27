package me.hextech.asm.mixins.freelook;

import me.hextech.remapped.CameraState;
import me.hextech.remapped.FreeLook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GameRenderer.class})
public abstract class GameRendererMixin {
    @Unique
    private CameraState camera;
    @Unique
    private Entity cameraEntity;
    @Unique
    private float originalYaw;
    @Unique
    private float originalPitch;

    @Inject(method={"renderHand"}, at={@At(value="HEAD")})
    private void onRenderHandBegin(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        this.camera = FreeLook.INSTANCE.getCameraState();
        if (this.camera.doTransition || this.camera.doLock) {
            this.cameraEntity = MinecraftClient.method_1551().method_1560();
            this.originalYaw = this.cameraEntity.method_36454();
            this.originalPitch = this.cameraEntity.method_36455();
            float pitch = this.camera.lookPitch;
            this.cameraEntity.method_36456(this.camera.lookYaw);
            this.cameraEntity.method_36457(pitch -= MathHelper.method_15379((float)(this.camera.lookYaw - this.camera.originalYaw())));
        }
    }

    @Inject(method={"renderHand"}, at={@At(value="RETURN")})
    private void onRenderHandEnd(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (this.camera.doTransition || this.camera.doLock) {
            this.cameraEntity.method_36456(this.originalYaw);
            this.cameraEntity.method_36457(this.originalPitch);
        }
    }
}
