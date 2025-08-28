package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import me.hextech.mod.modules.settings.impl.StringSetting;
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

@Mixin(value={Keyboard.class})
public class MixinKeyboard
implements Wrapper {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method={"onKey"}, at={@At(value="HEAD")})
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (MixinKeyboard.mc.currentScreen instanceof ClickGuiScreen && action == 1 && HexTech.MODULE.setBind(key)) {
            return;
        }
        if (action == 1) {
            HexTech.MODULE.onKeyPressed(key);
        }
        if (action == 0) {
            HexTech.MODULE.onKeyReleased(key);
        }
    }

    @Inject(method={"onChar"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        Screen element;
        if (window == this.client.getWindow().getHandle() && (element = this.client.currentScreen) != null && this.client.getOverlay() == null) {
            if (Character.charCount(codePoint) == 1) {
                if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck() && HexTech.GUI != null && HexTech.GUI.isClickGuiOpen()) {
                    HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof StringSetting).map(setting -> (StringSetting)setting).filter(StringSetting::isListening).forEach(setting -> setting.charType((char)codePoint)));
                    HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof SliderSetting).map(setting -> (SliderSetting)setting).filter(SliderSetting::isListening).forEach(setting -> setting.charType((char)codePoint)));
                }
                Screen.wrapScreenError(() -> MixinKeyboard.lambda$onChar$8(element, codePoint, modifiers), "charTyped event handler", element.getClass().getCanonicalName());
            } else {
                char[] var6;
                for (char c : var6 = Character.toChars(codePoint)) {
                    if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck() && HexTech.GUI != null && HexTech.GUI.isClickGuiOpen()) {
                        HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof StringSetting).map(setting -> (StringSetting)setting).filter(StringSetting::isListening).forEach(setting -> setting.charType(c)));
                        HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof SliderSetting).map(setting -> (SliderSetting)setting).filter(SliderSetting::isListening).forEach(setting -> setting.charType((char)codePoint)));
                    }
                    Screen.wrapScreenError(() -> MixinKeyboard.lambda$onChar$17(element, c, modifiers), "charTyped event handler", element.getClass().getCanonicalName());
                }
            }
        }
        ci.cancel();
    }

    private static /* synthetic */ void lambda$onChar$17(Element element, char c, int modifiers) {
        element.charTyped(c, modifiers);
    }

    private static /* synthetic */ void lambda$onChar$8(Element element, int codePoint, int modifiers) {
        element.charTyped((char)codePoint, modifiers);
    }
}
