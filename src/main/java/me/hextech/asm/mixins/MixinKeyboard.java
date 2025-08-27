package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.StringSetting;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Keyboard.class})
public class MixinKeyboard implements Wrapper {
   @Shadow
   @Final
   private MinecraftClient field_1678;

   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")}
   )
   private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
      if (!(mc.field_1755 instanceof ClickGuiScreen) || action != 1 || !HexTech.MODULE.setBind(key)) {
         if (action == 1) {
            HexTech.MODULE.onKeyPressed(key);
         }

         if (action == 0) {
            HexTech.MODULE.onKeyReleased(key);
         }
      }
   }

   @Inject(
      method = {"onChar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
      if (window == this.field_1678.method_22683().method_4490()) {
         Element element = this.field_1678.field_1755;
         if (element != null && this.field_1678.method_18506() == null) {
            if (Character.charCount(codePoint) == 1) {
               if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck() && HexTech.GUI != null && HexTech.GUI.isClickGuiOpen()) {
                  HexTech.MODULE
                     .modules
                     .forEach(
                        module -> module.getSettings()
                              .stream()
                              .filter(setting -> setting instanceof StringSetting)
                              .map(setting -> (StringSetting)setting)
                              .filter(StringSetting::isListening)
                              .forEach(setting -> setting.charType((char)codePoint))
                     );
                  HexTech.MODULE
                     .modules
                     .forEach(
                        module -> module.getSettings()
                              .stream()
                              .filter(setting -> setting instanceof SliderSetting)
                              .map(setting -> (SliderSetting)setting)
                              .filter(SliderSetting::isListening)
                              .forEach(setting -> setting.charType((char)codePoint))
                     );
               }

               Screen.method_25412(() -> element.method_25400((char)codePoint, modifiers), "charTyped event handler", element.getClass().getCanonicalName());
            } else {
               char[] var6 = Character.toChars(codePoint);

               for (char c : var6) {
                  if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck() && HexTech.GUI != null && HexTech.GUI.isClickGuiOpen()) {
                     HexTech.MODULE
                        .modules
                        .forEach(
                           module -> module.getSettings()
                                 .stream()
                                 .filter(setting -> setting instanceof StringSetting)
                                 .map(setting -> (StringSetting)setting)
                                 .filter(StringSetting::isListening)
                                 .forEach(setting -> setting.charType(c))
                        );
                     HexTech.MODULE
                        .modules
                        .forEach(
                           module -> module.getSettings()
                                 .stream()
                                 .filter(setting -> setting instanceof SliderSetting)
                                 .map(setting -> (SliderSetting)setting)
                                 .filter(SliderSetting::isListening)
                                 .forEach(setting -> setting.charType((char)codePoint))
                        );
                  }

                  Screen.method_25412(() -> element.method_25400(c, modifiers), "charTyped event handler", element.getClass().getCanonicalName());
               }
            }
         }
      }

      ci.cancel();
   }
}
