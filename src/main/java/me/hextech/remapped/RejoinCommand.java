package me.hextech.remapped;

import java.util.List;
import me.hextech.remapped.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class RejoinCommand
extends Command {
    public RejoinCommand() {
        super("rejoin", "rejoin", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        mc.method_18859(() -> {
            if (RejoinCommand.mc.field_1687 != null && mc.method_1558() != null) {
                ServerInfo lastestServerEntry = mc.method_1558();
                new DisconnectS2CPacket(Text.method_30163((String)"Self kick")).method_11467((ClientCommonPacketListener)RejoinCommand.mc.field_1724.field_3944);
                ConnectScreen.method_36877((Screen)new TitleScreen(), (MinecraftClient)mc, (ServerAddress)ServerAddress.method_2950((String)lastestServerEntry.field_3761), (ServerInfo)lastestServerEntry, (boolean)false);
            }
        });
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
