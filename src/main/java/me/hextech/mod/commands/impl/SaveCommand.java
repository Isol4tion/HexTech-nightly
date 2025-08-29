package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.CommandManager;
import me.hextech.api.managers.ConfigManager;
import me.hextech.mod.commands.Command;

import java.io.File;
import java.util.List;

public class SaveCommand
        extends Command {
    public SaveCommand() {
        super("save", "save", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        CommandManager.sendChatMessage("\u00a7e[!] \u00a7fSaving..");
        if (parameters.length == 1) {
            ConfigManager.options = new File(SaveCommand.mc.runDirectory, parameters[0] + ".cfg");
            HexTech.save();
            ConfigManager.options = new File(SaveCommand.mc.runDirectory, "nullpoint_options.txt");
        }
        HexTech.save();
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
