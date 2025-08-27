package me.hextech.remapped;

import java.util.List;

public class ReloadAllCommand extends Command {
   public ReloadAllCommand() {
      super("reloadall", "debug", "");
   }

   @Override
   public void runCommand(String[] parameters) throws Throwable {
      CommandManager.sendChatMessage("§e[!] §fReloading..");
      me.hextech.HexTech.unload();
      me.hextech.HexTech.load();
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
