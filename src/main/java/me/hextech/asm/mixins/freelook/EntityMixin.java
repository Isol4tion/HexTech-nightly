package me.hextech.asm.mixins.freelook;

import me.hextech.remapped.mod.modules.impl.player.freelook.CameraState;
import me.hextech.remapped.FreeLook;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityMixin {
    @Unique
    private CameraState camera;

    @Inject(method={"changeLookDirection"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo callback) {
        if ((Object)this instanceof ClientPlayerEntity) {
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
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        this.camera.lookYaw = yaw += transformedCursorDeltaX;
        this.camera.lookPitch = pitch;
    }
}
