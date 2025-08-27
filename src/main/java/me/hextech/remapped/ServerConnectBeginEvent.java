package me.hextech.remapped;

import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class ServerConnectBeginEvent extends Event_auduwKaxKOWXRtyJkCPb {
   private final ServerAddress address;
   private final ServerInfo info;

   public ServerConnectBeginEvent(ServerAddress address, ServerInfo info) {
      super(Event.Pre);
      this.address = address;
      this.info = info;
   }

   public ServerAddress getAddress() {
      return this.address;
   }

   public ServerInfo getInfo() {
      return this.info;
   }
}
