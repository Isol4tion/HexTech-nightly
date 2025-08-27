package me.hextech.remapped;

import java.util.List;

public class ListCommand extends Command {
   public ListCommand() {
      super("list", "list origin-network user(s)", "");
   }

   @Override
   public void runCommand(String[] parameters) {
      me.hextech.HexTech.COMMAND_QUEUE.add("LIST");
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
