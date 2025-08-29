package me.hextech.asm.mixins.freelook;

import me.hextech.mod.modules.impl.player.freelook.CameraState;
import me.hextech.mod.modules.impl.player.freelook.FreeLook;
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

@Mixin(value = {GameRenderer.class})
public abstract class GameRendererMixin {
    @Unique
    private CameraState camera;
    @Unique
    private Entity cameraEntity;
    @Unique
    private float originalYaw;
    @Unique
    private float originalPitch;

    @Inject(method = {"renderHand"}, at = {@At(value = "HEAD")})
    private void onRenderHandBegin(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        this.camera = FreeLook.INSTANCE.getCameraState();
        if (this.camera.doTransition || this.camera.doLock) {
            this.cameraEntity = MinecraftClient.getInstance().getCameraEntity();
            this.originalYaw = this.cameraEntity.getYaw();
            this.originalPitch = this.cameraEntity.getPitch();
            float pitch = this.camera.lookPitch;
            this.cameraEntity.setYaw(this.camera.lookYaw);
            this.cameraEntity.setPitch(pitch -= MathHelper.abs(this.camera.lookYaw - this.camera.originalYaw()));
        }
    }

    @Inject(method = {"renderHand"}, at = {@At(value = "RETURN")})
    private void onRenderHandEnd(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (this.camera.doTransition || this.camera.doLock) {
            this.cameraEntity.setYaw(this.originalYaw);
            this.cameraEntity.setPitch(this.originalPitch);
        }
    }
}
