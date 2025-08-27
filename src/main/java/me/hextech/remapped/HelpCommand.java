package me.hextech.remapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class HelpCommand extends Command {
   final int indexesPerPage = 5;

   public HelpCommand() {
      super("help", "Shows the avaiable commands.", "[page, module]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.ShowCommands(1);
      } else if (StringUtils.isNumeric(parameters[0])) {
         int page = Integer.parseInt(parameters[0]);
         this.ShowCommands(page);
      } else {
         Module_eSdgMXWuzcxgQVaJFmKZ module = me.hextech.HexTech.MODULE.getModuleByName(parameters[0]);
         if (module == null) {
            CommandManager.sendChatMessage("Could not find Module '" + parameters[0] + "'.");
         } else {
            CommandManager.sendChatMessage("------------ " + module.getName() + "Help ------------");
            CommandManager.sendChatMessage("Name: " + module.getName());
            CommandManager.sendChatMessage("Description: " + module.getDescription());
            CommandManager.sendChatMessage("Keybind: " + module.getBind().getBind() + " " + module.getBind().getKey());
         }
      }
   }

   private void ShowCommands(int page) {
      CommandManager.sendChatMessage("------------ Help [Page " + page + " of 5] ------------");
      CommandManager.sendChatMessage("Use " + me.hextech.HexTech.PREFIX + "help [n] to get page n of help.");
      HashMap<String, Command> commands = me.hextech.HexTech.COMMAND.getCommands();
      Set<String> keySet = commands.keySet();
      ArrayList<String> listOfCommands = new ArrayList(keySet);

      for (int i = (page - 1) * 5; i < page * 5; i++) {
         if (i >= 0 && i < me.hextech.HexTech.COMMAND.getNumOfCommands()) {
            CommandManager.sendChatMessage(" " + me.hextech.HexTech.PREFIX + (String)listOfCommands.get(i));
         }
      }
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
