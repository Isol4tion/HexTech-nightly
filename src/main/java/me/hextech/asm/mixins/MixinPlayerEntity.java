package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import me.hextech.api.events.impl.TravelEvent;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.modules.impl.setting.AntiCrawl;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {PlayerEntity.class})
public class MixinPlayerEntity
        implements Wrapper {
    @Inject(method = {"canChangeIntoPose"}, at = {@At(value = "RETURN")}, cancellable = true)
    private void poseNotCollide(EntityPose pose, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == MixinPlayerEntity.mc.player && !AntiCrawl.INSTANCE.crawl.getValue() && pose == EntityPose.SWIMMING) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = {"travel"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void onTravelPre(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player != MixinPlayerEntity.mc.player) {
            return;
        }
        TravelEvent event = new TravelEvent(Event_auduwKaxKOWXRtyJkCPb.Stage.Pre, player);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = {"travel"}, at = {@At(value = "RETURN")}, cancellable = true)
    private void onTravelPost(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player != MixinPlayerEntity.mc.player) {
            return;
        }
        TravelEvent event = new TravelEvent(Event_auduwKaxKOWXRtyJkCPb.Stage.Post, player);
        HexTech.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
