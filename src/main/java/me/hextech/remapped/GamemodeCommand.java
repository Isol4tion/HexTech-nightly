package me.hextech.remapped;

import java.util.List;
import me.hextech.remapped.Command;
import net.minecraft.world.GameMode;

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
            GamemodeCommand.mc.field_1761.method_2907(GameMode.field_9215);
        } else if (moduleName.equalsIgnoreCase("creative")) {
            GamemodeCommand.mc.field_1761.method_2907(GameMode.field_9220);
        } else if (moduleName.equalsIgnoreCase("adventure")) {
            GamemodeCommand.mc.field_1761.method_2907(GameMode.field_9216);
        } else if (moduleName.equalsIgnoreCase("spectator")) {
            GamemodeCommand.mc.field_1761.method_2907(GameMode.field_9219);
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
