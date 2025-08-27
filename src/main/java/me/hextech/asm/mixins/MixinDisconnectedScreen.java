package me.hextech.asm.mixins;

import it.unimi.dsi.fastutil.Pair;
import me.hextech.remapped.AutoReconnect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({DisconnectedScreen.class})
public abstract class MixinDisconnectedScreen extends Screen {
   @Shadow
   @Final
   private DirectionalLayoutWidget field_44552;
   @Unique
   private ButtonWidget reconnectBtn;
   @Unique
   private double time = (double)(AutoReconnect.INSTANCE.delay.getValueInt() * 20);

   protected MixinDisconnectedScreen(Text title) {
      super(title);
   }

   @Inject(
      method = {"init"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;refreshPositions()V",
         shift = Shift.BEFORE
      )},
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void addButtons(CallbackInfo ci, ButtonWidget buttonWidget) {
      AutoReconnect autoReconnect = AutoReconnect.INSTANCE;
      if (autoReconnect.lastServerConnection != null) {
         this.reconnectBtn = new Builder(Text.method_43470(this.getText()), button -> this.tryConnecting()).method_46431();
         this.field_44552.method_52736(this.reconnectBtn);
         this.field_44552.method_52736(new Builder(Text.method_43470("Toggle Auto Reconnect"), button -> {
            autoReconnect.toggle();
            this.reconnectBtn.method_25355(Text.method_43470(this.getText()));
            this.time = (double)(autoReconnect.delay.getValueInt() * 20);
         }).method_46431());
      }
   }

   public void method_25393() {
      AutoReconnect autoReconnect = AutoReconnect.INSTANCE;
      if (autoReconnect.isOn() && autoReconnect.lastServerConnection != null) {
         if (this.time <= 0.0) {
            this.tryConnecting();
         } else {
            this.time--;
            if (this.reconnectBtn != null) {
               this.reconnectBtn.method_25355(Text.method_43470(this.getText()));
            }
         }
      }
   }

   @Unique
   private String getText() {
      String reconnectText = "Reconnect";
      if (AutoReconnect.INSTANCE.isOn()) {
         reconnectText = reconnectText + " " + String.format("(%.1f)", this.time / 20.0);
      }

      return reconnectText;
   }

   @Unique
   private void tryConnecting() {
      Pair<ServerAddress, ServerInfo> lastServer = AutoReconnect.INSTANCE.lastServerConnection;
      ConnectScreen.method_36877(new TitleScreen(), MinecraftClient.method_1551(), (ServerAddress)lastServer.left(), (ServerInfo)lastServer.right(), false);
   }
}
