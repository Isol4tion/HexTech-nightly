package me.hextech.asm.mixins;

import me.hextech.remapped.api.alts.AltScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={MultiplayerScreen.class})
public class MixinMultiplayerScreen
extends Screen {
    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(at={@At(value="TAIL")}, method={"init()V"})
    private void onInit(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.of("Alt Manager"), b -> this.client.setScreen(new AltScreen(this))).dimensions(this.width / 2 + 4 + 50, 7, 100, 20).build());
    }
}
