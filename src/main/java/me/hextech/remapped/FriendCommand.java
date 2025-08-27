package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("friend", "Set friend", "[name/reset/list] | [add/del] [name]");
   }

   @Override
   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else if (parameters[0].equals("reset")) {
         FriendManager.friendList.clear();
         CommandManager.sendChatMessage("§a[√] §bFriends list §egot reset");
      } else if (parameters[0].equals("list")) {
         if (FriendManager.friendList.isEmpty()) {
            CommandManager.sendChatMessage("§e[!] §bFriends list §eempty");
         } else {
            StringBuilder friends = new StringBuilder();
            boolean first = true;

            for (String name : FriendManager.friendList) {
               if (!first) {
                  friends.append(", ");
               }

               friends.append(name);
               first = false;
            }

            CommandManager.sendChatMessage("§e[~] §bFriends§e:§a" + friends);
         }
      } else if (parameters[0].equals("add")) {
         if (parameters.length == 2) {
            me.hextech.HexTech.FRIEND.addFriend(parameters[1]);
            CommandManager.sendChatMessage(
               "§a[√] §b" + parameters[1] + (FriendManager.isFriend(parameters[1]) ? " §ahas been friended" : " §chas been unfriended")
            );
         } else {
            this.sendUsage();
         }
      } else if (parameters[0].equals("del")) {
         if (parameters.length == 2) {
            FriendManager.removeFriend(parameters[1]);
            CommandManager.sendChatMessage(
               "§a[√] §b" + parameters[1] + (FriendManager.isFriend(parameters[1]) ? " §ahas been friended" : " §chas been unfriended")
            );
         } else {
            this.sendUsage();
         }
      } else if (parameters.length == 1) {
         CommandManager.sendChatMessage("§a[√] §b" + parameters[0] + (FriendManager.isFriend(parameters[0]) ? " §ais friended" : " §cisn't friended"));
      } else {
         this.sendUsage();
      }
   }

   @Override
   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.get(seperated.size() - 1)).toLowerCase();
         List<String> correct = new ArrayList();

         for (String x : List.of("add", "del", "list", "reset")) {
            if (input.equalsIgnoreCase(me.hextech.HexTech.PREFIX + "friend") || x.toLowerCase().startsWith(input)) {
               correct.add(x);
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
