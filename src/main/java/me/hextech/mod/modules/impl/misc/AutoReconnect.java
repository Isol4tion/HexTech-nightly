package me.hextech.mod.modules.impl.misc;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.ServerConnectBeginEvent;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class AutoReconnect
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoReconnect INSTANCE;
    public final SliderSetting delay = this.add(new SliderSetting("Delay", 3, 0, 20));
    public Pair<ServerAddress, ServerInfo> lastServerConnection;

    public AutoReconnect() {
        super("AutoReconnect", Category.Misc);
        INSTANCE = this;
    }

    @EventHandler
    private void onGameJoined(ServerConnectBeginEvent event) {
        this.lastServerConnection = new ObjectObjectImmutablePair(event.getAddress(), event.getInfo());
    }
}
