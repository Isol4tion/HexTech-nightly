package me.hextech.asm.mixins;

import me.hextech.mod.modules.impl.player.FreeCam;
import me.hextech.mod.modules.impl.render.CameraClip;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Camera.class})
public abstract class MixinCamera {
    @Shadow
    private boolean thirdPerson;

    @Shadow
    protected abstract double clipToSpace(double var1);

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;moveBy(DDD)V", ordinal=0))
    private void modifyCameraDistance(Args args) {
        if (CameraClip.INSTANCE.isOn()) {
            args.set(0, (Object)(-this.clipToSpace(CameraClip.INSTANCE.getDistance())));
        }
    }

    @Inject(method={"clipToSpace"}, at={@At(value="HEAD")}, cancellable=true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
        if (CameraClip.INSTANCE.isOn()) {
            info.setReturnValue(CameraClip.INSTANCE.getDistance());
        }
    }

    @Inject(method={"update"}, at={@At(value="TAIL")})
    private void updateHook(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (FreeCam.INSTANCE.isOn()) {
            this.thirdPerson = true;
        }
    }

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void setRotationHook(Args args) {
        if (FreeCam.INSTANCE.isOn()) {
            args.setAll(Float.valueOf(FreeCam.INSTANCE.getFakeYaw()), Float.valueOf(FreeCam.INSTANCE.getFakePitch()));
        }
    }

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void setPosHook(Args args) {
        if (FreeCam.INSTANCE.isOn()) {
            args.setAll(FreeCam.INSTANCE.getFakeX(), FreeCam.INSTANCE.getFakeY(), FreeCam.INSTANCE.getFakeZ());
        }
    }
}
