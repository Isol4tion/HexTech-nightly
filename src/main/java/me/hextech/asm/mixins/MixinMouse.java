package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Mouse.class})
public class MixinMouse implements Wrapper {
   @Inject(
      method = {"onMouseButton"},
      at = {@At("HEAD")}
   )
   private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
      if (button == 3 || button == 4) {
         if (!(mc.field_1755 instanceof ClickGuiScreen) || action != 1 || !HexTech.MODULE.setBind(button)) {
            if (action == 1) {
               HexTech.MODULE.onKeyPressed(button);
            }

            if (action == 0) {
               HexTech.MODULE.onKeyReleased(button);
            }
         }
      }
   }
}
