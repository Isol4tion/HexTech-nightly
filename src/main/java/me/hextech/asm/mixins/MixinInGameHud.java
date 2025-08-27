package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.AnimateUtil;
import me.hextech.remapped.AnimateUtil_AcLZzRdHWZkNeKEYTOwI;
import me.hextech.remapped.ChatSetting_qVnAbgCzNciNTevKRovy;
import me.hextech.remapped.HotbarAnimation;
import me.hextech.remapped.NoRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public class MixinInGameHud {
   @Unique
   private double hotbarX = 0.0;

   @Inject(
      method = {"renderPortalOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.portal.getValue()) {
         ci.cancel();
      }
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
         remap = false,
         ordinal = 3
      )},
      method = {"render(Lnet/minecraft/client/gui/DrawContext;F)V"}
   )
   private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
      HexTech.MODULE.render2D(context);
   }

   @Inject(
      method = {"clear"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/hud/ChatHud;clear(Z)V"
      )},
      cancellable = true
   )
   private void onClear(CallbackInfo info) {
      if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.isOn() && ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.keepHistory.getValue()) {
         info.cancel();
      }
   }

   @ModifyArg(
      method = {"renderHotbar"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
         ordinal = 1
      ),
      index = 1
   )
   private int selectedSlotX(int x) {
      if (HotbarAnimation.INSTANCE.isOn()) {
         this.hotbarX = AnimateUtil.animate(
            this.hotbarX,
            (double)x,
            HotbarAnimation.INSTANCE.hotbarSpeed.getValue(),
            (AnimateUtil_AcLZzRdHWZkNeKEYTOwI)HotbarAnimation.INSTANCE.animMode.getValue()
         );
         return (int)this.hotbarX;
      } else {
         return x;
      }
   }
}
