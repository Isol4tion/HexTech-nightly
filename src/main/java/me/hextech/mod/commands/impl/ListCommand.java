package me.hextech.mod.commands.impl;

import me.hextech.HexTech;
import me.hextech.mod.commands.Command;

import java.util.List;

public class ListCommand
extends Command {
    public ListCommand() {
        super("list", "list origin-network user(s)", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        HexTech.COMMAND_QUEUE.add("LIST");
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
