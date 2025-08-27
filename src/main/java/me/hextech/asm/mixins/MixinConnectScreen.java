package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.ServerConnectBeginEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ConnectScreen.class})
public class MixinConnectScreen {
    @Inject(method={"connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V"}, at={@At(value="HEAD")})
    private void tryConnectEvent(MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci) {
        ServerConnectBeginEvent event = new ServerConnectBeginEvent(address, info);
        HexTech.EVENT_BUS.post(event);
    }
}
