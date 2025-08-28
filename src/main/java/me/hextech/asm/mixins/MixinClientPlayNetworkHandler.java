package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.mod.modules.impl.setting.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.GameLeftEvent;
import me.hextech.remapped.SendMessageEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayNetworkHandler.class})
public abstract class MixinClientPlayNetworkHandler
extends ClientCommonNetworkHandler {
    @Shadow
    private ClientWorld world;
    @Unique
    private boolean $worldNotNull;
    @Unique
    private boolean nullpoint_ignoreChatMessage;

    protected MixinClientPlayNetworkHandler(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method={"onGameJoin"}, at={@At(value="HEAD")})
    private void onGameJoinHead(GameJoinS2CPacket packet, CallbackInfo info) {
        if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.nullfix.getValue()) {
            this.$worldNotNull = this.world != null;
        }
    }

    @Inject(method={"onGameJoin"}, at={@At(value="TAIL")})
    private void onGameJoinTail(GameJoinS2CPacket packet, CallbackInfo info) {
        if (this.$worldNotNull) {
            HexTech.EVENT_BUS.post(new GameLeftEvent());
        }
    }

    @Shadow
    public abstract void sendChatMessage(String var1);

    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendChatMessage(String message, CallbackInfo ci) throws Throwable {
        if (this.nullpoint_ignoreChatMessage) {
            return;
        }
        if (message.startsWith(HexTech.PREFIX)) {
            HexTech.COMMAND.command(message.split(" "));
            ci.cancel();
        } else {
            SendMessageEvent event = new SendMessageEvent(message);
            HexTech.EVENT_BUS.post(event);
            if (event.isCancelled()) {
                ci.cancel();
            } else if (!event.message.equals(event.defaultMessage)) {
                this.nullpoint_ignoreChatMessage = true;
                this.sendChatMessage(event.message);
                this.nullpoint_ignoreChatMessage = false;
                ci.cancel();
            }
        }
    }
}
