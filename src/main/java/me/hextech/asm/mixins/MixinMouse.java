package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Mouse.class})
public class MixinMouse
        implements Wrapper {
    @Inject(method = {"onMouseButton"}, at = {@At(value = "HEAD")})
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (button != 3 && button != 4) {
            return;
        }
        if (MixinMouse.mc.currentScreen instanceof ClickGuiScreen && action == 1 && HexTech.MODULE.setBind(button)) {
            return;
        }
        if (action == 1) {
            HexTech.MODULE.onKeyPressed(button);
        }
        if (action == 0) {
            HexTech.MODULE.onKeyReleased(button);
        }
    }
}
