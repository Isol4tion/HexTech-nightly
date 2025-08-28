package me.hextech.remapped;

import java.io.File;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.mod.commands.Command;

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
