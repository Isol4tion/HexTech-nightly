package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.BoatMoveEvent;
import me.hextech.remapped.EntityControl;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BoatEntity.class})
public class MixinBoatEntity {
    @Shadow
    private boolean field_7710;
    @Shadow
    private boolean field_7695;

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/vehicle/BoatEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")}, cancellable=true)
    private void onTickInvokeMove(CallbackInfo info) {
        BoatMoveEvent event = new BoatMoveEvent((BoatEntity)this);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Redirect(method={"updatePaddles"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/vehicle/BoatEntity;pressingLeft:Z"))
    private boolean onUpdatePaddlesPressingLeft(BoatEntity boat) {
        if (EntityControl.INSTANCE.isOn() && EntityControl.INSTANCE.fly.getValue()) {
            return false;
        }
        return this.field_7710;
    }

    @Redirect(method={"updatePaddles"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/vehicle/BoatEntity;pressingRight:Z"))
    private boolean onUpdatePaddlesPressingRight(BoatEntity boat) {
        if (EntityControl.INSTANCE.isOn() && EntityControl.INSTANCE.fly.getValue()) {
            return false;
        }
        return this.field_7695;
    }
}
