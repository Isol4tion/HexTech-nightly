package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.CommandManager;
import me.hextech.api.managers.ConfigManager;
import me.hextech.mod.commands.Command;

import java.io.File;
import java.util.List;

public class LoadCommand
extends Command {
    public LoadCommand() {
        super("load", "debug", "[config]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        CommandManager.sendChatMessage("\u00a7e[!] \u00a7fLoading..");
        ConfigManager.options = new File(LoadCommand.mc.runDirectory, parameters[0] + ".cfg");
        HexTech.CONFIG = new ConfigManager();
        HexTech.PREFIX = HexTech.CONFIG.getString("prefix", HexTech.PREFIX);
        HexTech.CONFIG.loadSettings();
        ConfigManager.options = new File(LoadCommand.mc.runDirectory, "hextech-OS.txt");
        HexTech.save();
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
