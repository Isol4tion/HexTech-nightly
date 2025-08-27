package me.hextech.asm.mixins;

import me.hextech.remapped.NoSlow_PaVUKKxFbWGbplzMaucl;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Block.class})
public abstract class MixinBlock implements ItemConvertible {
   @Inject(
      at = {@At("HEAD")},
      method = {"getVelocityMultiplier()F"},
      cancellable = true
   )
   private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
      if (NoSlow_PaVUKKxFbWGbplzMaucl.INSTANCE.isOn()) {
         if (cir.getReturnValueF() < 1.0F) {
            cir.setReturnValue(1.0F);
         }
      }
   }
}
