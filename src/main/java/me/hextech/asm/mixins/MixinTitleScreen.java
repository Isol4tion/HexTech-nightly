package me.hextech.asm.mixins;

import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.ClickGui_lDjmIzzmqoIBLakgOLww;
import me.hextech.remapped.HexTech;
import me.hextech.remapped.Menu;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TitleScreen.class})
public abstract class MixinTitleScreen
extends Screen {
    public MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void tick(CallbackInfo ci) {
        if (this.client != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.menu.getValue() == ClickGui_lDjmIzzmqoIBLakgOLww.Nullpoint) {
            this.client.setScreen((Screen)new Menu());
        }
        if (this.client != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.menu.getValue() == ClickGui_lDjmIzzmqoIBLakgOLww.HexTech) {
            this.client.setScreen((Screen)new HexTech());
        }
    }

    @Inject(method={"init"}, at={@At(value="HEAD")})
    private void init(CallbackInfo ci) {
        if (this.client != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.menu.getValue() == ClickGui_lDjmIzzmqoIBLakgOLww.Nullpoint) {
            this.client.setScreen((Screen)new Menu());
        }
        if (this.client != null && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.menu.getValue() == ClickGui_lDjmIzzmqoIBLakgOLww.HexTech) {
            this.client.setScreen((Screen)new HexTech());
        }
    }
}
