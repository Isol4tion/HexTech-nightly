package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.CommandManager;
import me.hextech.api.managers.ConfigManager;
import me.hextech.mod.commands.Command;

import java.util.List;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", "debug", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        CommandManager.sendChatMessage("\u00a7e[!] \u00a7fReloading..");
        HexTech.CONFIG = new ConfigManager();
        HexTech.PREFIX = HexTech.CONFIG.getString("prefix", HexTech.PREFIX);
        HexTech.CONFIG.loadSettings();
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
