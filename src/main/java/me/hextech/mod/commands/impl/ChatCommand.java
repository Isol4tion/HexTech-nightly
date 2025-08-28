package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.mod.commands.Command;

import java.util.Arrays;
import java.util.List;

public class ChatCommand
extends Command {
    public ChatCommand() {
        super("c", "send message to origin-network", "[text]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        StringBuilder text = new StringBuilder();
        for (String s : Arrays.stream(parameters).toList()) {
            text.append(" ").append(s);
        }
        HexTech.MESSAGE_QUEUE.add(text.toString());
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
