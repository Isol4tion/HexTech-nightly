package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.CommandManager;
import me.hextech.mod.commands.Command;

import java.util.List;

public class PrefixCommand
        extends Command {
    public PrefixCommand() {
        super("prefix", "Set prefix", "[prefix]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        if (parameters[0].startsWith("/")) {
            CommandManager.sendChatMessage("\u00a76[!] \u00a7fPlease specify a valid \u00a7bprefix.");
            return;
        }
        HexTech.PREFIX = parameters[0];
        CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7bPrefix \u00a7fset to \u00a7e" + parameters[0]);
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
