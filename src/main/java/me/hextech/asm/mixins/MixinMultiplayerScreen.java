package me.hextech.asm.mixins;

import me.hextech.remapped.AltScreen;
import net.minecraft.client.gui.Element;
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
        this.method_37063((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Alt Manager"), b -> this.field_22787.method_1507((Screen)new AltScreen((Screen)((MultiplayerScreen)this)))).method_46434(this.field_22789 / 2 + 4 + 50, 7, 100, 20).method_46431());
    }
}
