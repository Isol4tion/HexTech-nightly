package me.hextech.remapped.mod.commands.impl;

import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.ModuleManager;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.mod.commands.Command;

public class BindCommand
extends Command {
    public BindCommand() {
        super("bind", "Bind key", "[module] [key]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        String moduleName = parameters[0];
        Module_eSdgMXWuzcxgQVaJFmKZ module = HexTech.MODULE.getModuleByName(moduleName);
        if (module == null) {
            CommandManager.sendChatMessage("\u00a74[!] \u00a7fUnknown \u00a7bmodule!");
            return;
        }
        if (parameters.length == 1) {
            CommandManager.sendChatMessage("\u00a76[!] \u00a7fPlease specify a \u00a7bkey.");
            return;
        }
        String rkey = parameters[1];
        if (rkey == null) {
            CommandManager.sendChatMessage("\u00a74Unknown Error");
            return;
        }
        if (module.setBind(rkey.toUpperCase())) {
            CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7fBind for \u00a7a" + module.getName() + "\u00a7f set to \u00a77" + rkey.toUpperCase());
        }
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        if (count == 1) {
            String input = seperated.get(seperated.size() - 1).toLowerCase();
            ModuleManager cm = HexTech.MODULE;
            ArrayList<String> correct = new ArrayList<String>();
            for (Module_eSdgMXWuzcxgQVaJFmKZ x : cm.modules) {
                if (!input.equalsIgnoreCase(HexTech.PREFIX + "bind") && !x.getName().toLowerCase().startsWith(input)) continue;
                correct.add(x.getName());
            }
            int numCmds = correct.size();
            String[] commands = new String[numCmds];
            int i = 0;
            for (String x : correct) {
                commands[i++] = x;
            }
            return commands;
        }
        return null;
    }
}
