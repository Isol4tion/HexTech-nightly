package me.hextech.remapped;

import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.mod.commands.Command;

public class ReloadAllCommand
extends Command {
    public ReloadAllCommand() {
        super("reloadall", "debug", "");
    }

    @Override
    public void runCommand(String[] parameters) throws Throwable {
        CommandManager.sendChatMessage("\u00a7e[!] \u00a7fReloading..");
        HexTech.unload();
        HexTech.load();
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
