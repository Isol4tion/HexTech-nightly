package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.AntiCrawl;
import me.hextech.remapped.Event;
import me.hextech.remapped.TravelEvent;
import me.hextech.remapped.Wrapper;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerEntity.class})
public class MixinPlayerEntity implements Wrapper {
   @Inject(
      method = {"canChangeIntoPose"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void poseNotCollide(EntityPose pose, CallbackInfoReturnable<Boolean> cir) {
      if (this == mc.field_1724 && !AntiCrawl.INSTANCE.crawl.getValue() && pose == EntityPose.field_18079) {
         cir.setReturnValue(false);
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTravelPre(Vec3d movementInput, CallbackInfo ci) {
      PlayerEntity player = (PlayerEntity)this;
      if (player == mc.field_1724) {
         TravelEvent event = new TravelEvent(Event.Pre, player);
         HexTech.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onTravelPost(Vec3d movementInput, CallbackInfo ci) {
      PlayerEntity player = (PlayerEntity)this;
      if (player == mc.field_1724) {
         TravelEvent event = new TravelEvent(Event.Post, player);
         HexTech.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            ci.cancel();
         }
      }
   }
}
