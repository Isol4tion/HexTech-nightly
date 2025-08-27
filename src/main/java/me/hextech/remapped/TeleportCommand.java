package me.hextech.remapped;

import java.text.DecimalFormat;
import java.util.List;

public class TeleportCommand extends Command {
   public TeleportCommand() {
      super("tp", "Teleports the player certain blocks away (Vanilla only)", "[x] [y] [z]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length != 3) {
         this.sendUsage();
      } else {
         double x;
         if (this.isNumeric(parameters[0])) {
            x = Double.parseDouble(parameters[0]);
         } else {
            if (!parameters[0].startsWith("~")) {
               this.sendUsage();
               return;
            }

            if (this.isNumeric(parameters[0].replace("~", ""))) {
               x = mc.field_1724.method_23317() + Double.parseDouble(parameters[0].replace("~", ""));
            } else {
               if (!parameters[0].replace("~", "").equals("")) {
                  this.sendUsage();
                  return;
               }

               x = mc.field_1724.method_23317();
            }
         }

         double y;
         if (this.isNumeric(parameters[1])) {
            y = Double.parseDouble(parameters[1]);
         } else {
            if (!parameters[1].startsWith("~")) {
               this.sendUsage();
               return;
            }

            if (this.isNumeric(parameters[1].replace("~", ""))) {
               y = mc.field_1724.method_23318() + Double.parseDouble(parameters[1].replace("~", ""));
            } else {
               if (!parameters[1].replace("~", "").equals("")) {
                  this.sendUsage();
                  return;
               }

               y = mc.field_1724.method_23318();
            }
         }

         double z;
         if (this.isNumeric(parameters[2])) {
            z = Double.parseDouble(parameters[2]);
         } else {
            if (!parameters[2].startsWith("~")) {
               this.sendUsage();
               return;
            }

            if (this.isNumeric(parameters[2].replace("~", ""))) {
               z = mc.field_1724.method_23321() + Double.parseDouble(parameters[2].replace("~", ""));
            } else {
               if (!parameters[2].replace("~", "").equals("")) {
                  this.sendUsage();
                  return;
               }

               z = mc.field_1724.method_23321();
            }
         }

         mc.field_1724.method_5814(x, y, z);
         DecimalFormat df = new DecimalFormat("0.0");
         CommandManager.sendChatMessage("§a[√] §fTeleported to §eX:" + df.format(x) + " Y:" + df.format(y) + " Z:" + df.format(z));
      }
   }

   private boolean isNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d+)?");
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      return new String[]{"~ "};
   }
}
