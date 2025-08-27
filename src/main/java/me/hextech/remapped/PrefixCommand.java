package me.hextech.remapped;

import java.util.List;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("prefix", "Set prefix", "[prefix]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else if (parameters[0].startsWith("/")) {
         CommandManager.sendChatMessage("§6[!] §fPlease specify a valid §bprefix.");
      } else {
         me.hextech.HexTech.PREFIX = parameters[0];
         CommandManager.sendChatMessage("§a[√] §bPrefix §fset to §e" + parameters[0]);
      }
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
