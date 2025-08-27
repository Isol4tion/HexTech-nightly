package me.hextech.asm.mixins.freelook;

import me.hextech.remapped.CameraState;
import me.hextech.remapped.Crosshair;
import me.hextech.remapped.FreeLook;
import me.hextech.remapped.ProjectionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={InGameHud.class})
public class InGameHudMixin {
    @Unique
    private CameraState camera;
    @Unique
    private double offsetCrosshairX;
    @Unique
    private double offsetCrosshairY;

    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderCrosshairBegin(DrawContext context, CallbackInfo ci) {
        if (Crosshair.INSTANCE.isOn()) {
            Crosshair.INSTANCE.draw(context);
            ci.cancel();
            return;
        }
        this.camera = FreeLook.INSTANCE.getCameraState();
        boolean shouldDrawCrosshair = false;
        if (this.camera.doTransition || this.camera.doLock) {
            Vec3d rotation;
            Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
            int distance = Integer.MAX_VALUE;
            Vec3d position = cameraEntity.getPos();
            Vec3d point = position.add((rotation = Vec3d.fromPolar((float)this.camera.originalPitch(), (float)this.camera.originalYaw())).method_10216() * (double)distance, rotation.method_10214() * (double)distance, rotation.method_10215() * (double)distance);
            Vec3d projected = ProjectionUtils.worldToScreen(point);
            if (projected.method_10215() < 0.0) {
                this.offsetCrosshairX = -projected.method_10216();
                this.offsetCrosshairY = -projected.method_10214();
                shouldDrawCrosshair = true;
            }
            if (!(shouldDrawCrosshair |= MinecraftClient.getInstance().inGameHud.getDebugHud().shouldShowDebugHud())) {
                ci.cancel();
            }
        }
    }

    @ModifyArgs(method={"renderCrosshair"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private void modifyDrawTextureArgs(Args args) {
        if (this.camera.doTransition || this.camera.doLock) {
            args.set(1, (Object)((Integer)args.get(1) + (int)this.offsetCrosshairX));
            args.set(2, (Object)((Integer)args.get(2) + (int)this.offsetCrosshairY));
        }
    }
}
