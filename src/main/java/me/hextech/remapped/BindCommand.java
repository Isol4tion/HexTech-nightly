package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;

public class BindCommand extends Command {
   public BindCommand() {
      super("bind", "Bind key", "[module] [key]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else {
         String moduleName = parameters[0];
         Module_eSdgMXWuzcxgQVaJFmKZ module = me.hextech.HexTech.MODULE.getModuleByName(moduleName);
         if (module == null) {
            CommandManager.sendChatMessage("§4[!] §fUnknown §bmodule!");
         } else if (parameters.length == 1) {
            CommandManager.sendChatMessage("§6[!] §fPlease specify a §bkey.");
         } else {
            String rkey = parameters[1];
            if (rkey == null) {
               CommandManager.sendChatMessage("§4Unknown Error");
            } else {
               if (module.setBind(rkey.toUpperCase())) {
                  CommandManager.sendChatMessage("§a[√] §fBind for §a" + module.getName() + "§f set to §7" + rkey.toUpperCase());
               }
            }
         }
      }
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.get(seperated.size() - 1)).toLowerCase();
         ModuleManager cm = me.hextech.HexTech.MODULE;
         List<String> correct = new ArrayList();

         for (Module_eSdgMXWuzcxgQVaJFmKZ x : cm.modules) {
            if (input.equalsIgnoreCase(me.hextech.HexTech.PREFIX + "bind") || x.getName().toLowerCase().startsWith(input)) {
               correct.add(x.getName());
            }
         }

         int numCmds = correct.size();
         String[] commands = new String[numCmds];
         int i = 0;

         for (String xx : correct) {
            commands[i++] = xx;
         }

         return commands;
      }
   }
}
