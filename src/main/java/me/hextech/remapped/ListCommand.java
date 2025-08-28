package me.hextech.remapped;

import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.mod.commands.Command;

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
