package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.CommandManager;
import me.hextech.mod.commands.Command;

import java.util.List;

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
