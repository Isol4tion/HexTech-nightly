package me.hextech.asm.mixins;

import me.hextech.remapped.CommandManager;
import me.hextech.remapped.SilentDisconnect;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientCommonNetworkHandler.class})
public class MixinClientCommonNetworkHandler {
   @Inject(
      method = {"onDisconnected"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDisconnected(Text reason, CallbackInfo ci) {
      if (Wrapper.mc.field_1724 != null && Wrapper.mc.field_1687 != null && SilentDisconnect.INSTANCE.isOn()) {
         CommandManager.sendChatMessage("§4[!] §cDisconnect! reason: §7" + reason.getString());
         ci.cancel();
      }
   }

   @Inject(
      method = {"sendPacket"},
      at = {@At("HEAD")}
   )
   public void sendPacket(Packet<?> packet, CallbackInfo ci) {
   }
}
