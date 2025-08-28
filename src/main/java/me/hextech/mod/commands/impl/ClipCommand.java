package me.hextech.mod.commands.impl;

import me.hextech.api.managers.CommandManager;
import me.hextech.mod.commands.Command;

import java.text.DecimalFormat;
import java.util.List;

public class ClipCommand
extends Command {
    public ClipCommand() {
        super("clip", "Teleports the player certain blocks away (Vanilla only)", "[x] [y] [z]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length != 3) {
            this.sendUsage();
            return;
        }
        if (!this.isNumeric(parameters[0])) {
            this.sendUsage();
            return;
        }
        double x = ClipCommand.mc.player.getX() + Double.parseDouble(parameters[0]);
        if (!this.isNumeric(parameters[1])) {
            this.sendUsage();
            return;
        }
        double y = ClipCommand.mc.player.getY() + Double.parseDouble(parameters[1]);
        if (!this.isNumeric(parameters[2])) {
            this.sendUsage();
            return;
        }
        double z = ClipCommand.mc.player.getZ() + Double.parseDouble(parameters[2]);
        ClipCommand.mc.player.setPosition(x, y, z);
        DecimalFormat df = new DecimalFormat("0.0");
        CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7fTeleported to \u00a7eX:" + df.format(x) + " Y:" + df.format(y) + " Z:" + df.format(z));
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return new String[]{"0 "};
    }
}
