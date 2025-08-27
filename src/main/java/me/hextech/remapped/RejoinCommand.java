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
            if (RejoinCommand.mc.world != null && mc.getCurrentServerEntry() != null) {
                ServerInfo lastestServerEntry = mc.getCurrentServerEntry();
                new DisconnectS2CPacket(Text.of((String)"Self kick")).apply((ClientCommonPacketListener)RejoinCommand.mc.player.networkHandler);
                ConnectScreen.method_36877((Screen)new TitleScreen(), (MinecraftClient)mc, (ServerAddress)ServerAddress.parse((String)lastestServerEntry.address), (ServerInfo)lastestServerEntry, (boolean)false);
            }
        });
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
