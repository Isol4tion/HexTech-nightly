package me.hextech.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.remapped.Ambience;
import me.hextech.remapped.NoRender;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BackgroundRenderer.class})
public class MixinBackgroundRenderer {
   @Inject(
      method = {"applyFog"},
      at = {@At("TAIL")}
   )
   private static void onApplyFog(Camera camera, FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
      if (Ambience.INSTANCE.isOn()) {
         if (Ambience.INSTANCE.fog.booleanValue) {
            RenderSystem.setShaderFogColor(
               (float)Ambience.INSTANCE.fog.getValue().getRed() / 255.0F,
               (float)Ambience.INSTANCE.fog.getValue().getGreen() / 255.0F,
               (float)Ambience.INSTANCE.fog.getValue().getBlue() / 255.0F,
               (float)Ambience.INSTANCE.fog.getValue().getAlpha() / 255.0F
            );
         }

         if (Ambience.INSTANCE.fogDistance.getValue()) {
            RenderSystem.setShaderFogStart(Ambience.INSTANCE.fogStart.getValueFloat());
            RenderSystem.setShaderFogEnd(Ambience.INSTANCE.fogEnd.getValueFloat());
         }
      }

      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fog.getValue() && fogType == FogType.field_20946) {
         RenderSystem.setShaderFogStart(viewDistance * 4.0F);
         RenderSystem.setShaderFogEnd(viewDistance * 4.25F);
      }
   }

   @Inject(
      method = {"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.blindness.getValue()) {
         info.setReturnValue(null);
      }
   }
}
