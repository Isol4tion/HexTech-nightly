package me.hextech.asm.mixins;

import me.hextech.remapped.AltScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MultiplayerScreen.class})
public class MixinMultiplayerScreen extends Screen {
   protected MixinMultiplayerScreen(Text title) {
      super(title);
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"init()V"}
   )
   private void onInit(CallbackInfo ci) {
      this.method_37063(
         ButtonWidget.method_46430(Text.method_30163("Alt Manager"), b -> this.field_22787.method_1507(new AltScreen((MultiplayerScreen)this)))
            .method_46434(this.field_22789 / 2 + 4 + 50, 7, 100, 20)
            .method_46431()
      );
   }
}
