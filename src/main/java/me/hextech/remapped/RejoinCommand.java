package me.hextech.remapped;

import java.util.List;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class RejoinCommand extends Command {
   public RejoinCommand() {
      super("rejoin", "rejoin", "");
   }

   @Override
   public void runCommand(String[] parameters) {
      mc.method_18859(() -> {
         if (mc.field_1687 != null && mc.method_1558() != null) {
            ServerInfo lastestServerEntry = mc.method_1558();
            new DisconnectS2CPacket(Text.method_30163("Self kick")).method_11467(mc.field_1724.field_3944);
            ConnectScreen.method_36877(new TitleScreen(), mc, ServerAddress.method_2950(lastestServerEntry.field_3761), lastestServerEntry, false);
         }
      });
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
