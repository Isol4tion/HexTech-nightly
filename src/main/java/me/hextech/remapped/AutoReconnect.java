package me.hextech.remapped;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class AutoReconnect extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoReconnect INSTANCE;
   public final SliderSetting delay = this.add(new SliderSetting("Delay", 3, 0, 20));
   public Pair<ServerAddress, ServerInfo> lastServerConnection;

   public AutoReconnect() {
      super("AutoReconnect", Module_JlagirAibYQgkHtbRnhw.Misc);
      INSTANCE = this;
   }

   @EventHandler
   private void onGameJoined(ServerConnectBeginEvent event) {
      this.lastServerConnection = new ObjectObjectImmutablePair(event.getAddress(), event.getInfo());
   }
}
