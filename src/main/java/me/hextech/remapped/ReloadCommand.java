package me.hextech.remapped;

import java.util.List;

public class ReloadCommand extends Command {
   public ReloadCommand() {
      super("reload", "debug", "");
   }

   @Override
   public void runCommand(String[] parameters) {
      CommandManager.sendChatMessage("§e[!] §fReloading..");
      me.hextech.HexTech.CONFIG = new ConfigManager();
      me.hextech.HexTech.PREFIX = me.hextech.HexTech.CONFIG.getString("prefix", me.hextech.HexTech.PREFIX);
      me.hextech.HexTech.CONFIG.loadSettings();
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
