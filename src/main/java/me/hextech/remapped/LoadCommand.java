package me.hextech.remapped;

import java.io.File;
import java.util.List;

public class LoadCommand extends Command {
   public LoadCommand() {
      super("load", "debug", "[config]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else {
         CommandManager.sendChatMessage("§e[!] §fLoading..");
         ConfigManager.options = new File(mc.field_1697, parameters[0] + ".cfg");
         me.hextech.HexTech.CONFIG = new ConfigManager();
         me.hextech.HexTech.PREFIX = me.hextech.HexTech.CONFIG.getString("prefix", me.hextech.HexTech.PREFIX);
         me.hextech.HexTech.CONFIG.loadSettings();
         ConfigManager.options = new File(mc.field_1697, "hextech-OS.txt");
         me.hextech.HexTech.save();
      }
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
