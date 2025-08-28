package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.mod.commands.Command;

public class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("toggle", "Toggle module", "[module]");
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
        module.toggle();
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        if (count == 1) {
            String input = seperated.get(seperated.size() - 1).toLowerCase();
            ModuleManager cm = HexTech.MODULE;
            ArrayList<String> correct = new ArrayList<String>();
            for (Module_eSdgMXWuzcxgQVaJFmKZ x : cm.modules) {
                if (!input.equalsIgnoreCase(HexTech.PREFIX + "toggle") && !x.getName().toLowerCase().startsWith(input)) continue;
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
