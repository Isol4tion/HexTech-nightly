package me.hextech.asm.mixins;

import me.hextech.remapped.CommandManager;
import me.hextech.remapped.SilentDisconnect;
import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientCommonNetworkHandler.class})
public class MixinClientCommonNetworkHandler {
    @Inject(method={"onDisconnected"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDisconnected(Text reason, CallbackInfo ci) {
        if (Wrapper.mc.player != null && Wrapper.mc.world != null && SilentDisconnect.INSTANCE.isOn()) {
            CommandManager.sendChatMessage("\u00a74[!] \u00a7cDisconnect! reason: \u00a77" + reason.getString());
            ci.cancel();
        }
    }

    @Inject(method={"sendPacket"}, at={@At(value="HEAD")})
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {
    }
}
