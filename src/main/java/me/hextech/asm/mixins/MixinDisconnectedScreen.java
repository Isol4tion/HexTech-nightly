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
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={DisconnectedScreen.class})
public abstract class MixinDisconnectedScreen
extends Screen {
    @Shadow
    @Final
    private DirectionalLayoutWidget field_44552;
    @Unique
    private ButtonWidget reconnectBtn;
    @Unique
    private double time;

    protected MixinDisconnectedScreen(Text title) {
        super(title);
        this.time = AutoReconnect.INSTANCE.delay.getValueInt() * 20;
    }

    @Inject(method={"init"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;refreshPositions()V", shift=At.Shift.BEFORE)}, locals=LocalCapture.CAPTURE_FAILHARD)
    private void addButtons(CallbackInfo ci, ButtonWidget buttonWidget) {
        AutoReconnect autoReconnect = AutoReconnect.INSTANCE;
        if (autoReconnect.lastServerConnection != null) {
            this.reconnectBtn = new ButtonWidget.Builder((Text)Text.literal((String)this.getText()), button -> this.tryConnecting()).build();
            this.field_44552.add((Widget)this.reconnectBtn);
            this.field_44552.add((Widget)new ButtonWidget.Builder((Text)Text.literal((String)"Toggle Auto Reconnect"), button -> {
                autoReconnect.toggle();
                this.reconnectBtn.method_25355((Text)Text.literal((String)this.getText()));
                this.time = autoReconnect.delay.getValueInt() * 20;
            }).build());
        }
    }

    public void method_25393() {
        AutoReconnect autoReconnect = AutoReconnect.INSTANCE;
        if (!autoReconnect.isOn() || autoReconnect.lastServerConnection == null) {
            return;
        }
        if (this.time <= 0.0) {
            this.tryConnecting();
        } else {
            this.time -= 1.0;
            if (this.reconnectBtn != null) {
                this.reconnectBtn.method_25355((Text)Text.literal((String)this.getText()));
            }
        }
    }

    @Unique
    private String getText() {
        Object reconnectText = "Reconnect";
        if (AutoReconnect.INSTANCE.isOn()) {
            reconnectText = (String)reconnectText + " " + String.format("(%.1f)", this.time / 20.0);
        }
        return reconnectText;
    }

    @Unique
    private void tryConnecting() {
        Pair<ServerAddress, ServerInfo> lastServer = AutoReconnect.INSTANCE.lastServerConnection;
        ConnectScreen.method_36877((Screen)new TitleScreen(), (MinecraftClient)MinecraftClient.getInstance(), (ServerAddress)((ServerAddress)lastServer.left()), (ServerInfo)((ServerInfo)lastServer.right()), (boolean)false);
    }
}
