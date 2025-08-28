package me.hextech.asm.mixins;

import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Screen.class})
public class MixinScreen {
    @Shadow
    public int width;
    @Shadow
    public int height;

    @Inject(method={"renderInGameBackground"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderInGameBackgroundHook(DrawContext context, CallbackInfo ci) {
        ci.cancel();
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.blackground.getValue()) {
            context.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.customBackground.booleanValue) {
            context.fillGradient(0, 0, this.width, this.height, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.customBackground.getValue().getRGB(), ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.endColor.getValue().getRGB());
        }
    }
}
