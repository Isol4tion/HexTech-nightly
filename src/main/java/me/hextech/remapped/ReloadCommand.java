package me.hextech.remapped;

import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.mod.commands.Command;

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
