package me.hextech.remapped;

import java.util.List;
import net.minecraft.world.GameMode;

public class GamemodeCommand extends Command {
   public GamemodeCommand() {
      super("gamemode", "change gamemode(client side)", "[gamemode]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else {
         String moduleName = parameters[0];
         if (moduleName.equalsIgnoreCase("survival")) {
            mc.field_1761.method_2907(GameMode.field_9215);
         } else if (moduleName.equalsIgnoreCase("creative")) {
            mc.field_1761.method_2907(GameMode.field_9220);
         } else if (moduleName.equalsIgnoreCase("adventure")) {
            mc.field_1761.method_2907(GameMode.field_9216);
         } else if (moduleName.equalsIgnoreCase("spectator")) {
            mc.field_1761.method_2907(GameMode.field_9219);
         }
      }
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return count == 1 ? new String[]{"survival", "creative", "adventure", "spectator"} : null;
   }
}
