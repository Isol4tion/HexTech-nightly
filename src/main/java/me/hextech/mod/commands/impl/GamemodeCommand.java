package me.hextech.mod.commands.impl;

import me.hextech.mod.commands.Command;
import net.minecraft.world.GameMode;

import java.util.List;

public class GamemodeCommand
        extends Command {
    public GamemodeCommand() {
        super("gamemode", "change gamemode(client side)", "[gamemode]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        String moduleName = parameters[0];
        if (moduleName.equalsIgnoreCase("survival")) {
            GamemodeCommand.mc.interactionManager.setGameMode(GameMode.SURVIVAL);
        } else if (moduleName.equalsIgnoreCase("creative")) {
            GamemodeCommand.mc.interactionManager.setGameMode(GameMode.CREATIVE);
        } else if (moduleName.equalsIgnoreCase("adventure")) {
            GamemodeCommand.mc.interactionManager.setGameMode(GameMode.ADVENTURE);
        } else if (moduleName.equalsIgnoreCase("spectator")) {
            GamemodeCommand.mc.interactionManager.setGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        if (count == 1) {
            return new String[]{"survival", "creative", "adventure", "spectator"};
        }
        return null;
    }
}
