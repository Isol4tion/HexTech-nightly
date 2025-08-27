package me.hextech.remapped;

import java.io.File;
import java.util.List;

public class SaveCommand extends Command {
   public SaveCommand() {
      super("save", "save", "");
   }

   @Override
   public void runCommand(String[] parameters) {
      CommandManager.sendChatMessage("§e[!] §fSaving..");
      if (parameters.length == 1) {
         ConfigManager.options = new File(mc.field_1697, parameters[0] + ".cfg");
         me.hextech.HexTech.save();
         ConfigManager.options = new File(mc.field_1697, "nullpoint_options.txt");
      }

      me.hextech.HexTech.save();
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
