package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;
import me.hextech.HexTech;
import me.hextech.remapped.api.managers.CommandManager;
import me.hextech.remapped.mod.commands.Command;

public class FriendCommand
extends Command {
    public FriendCommand() {
        super("friend", "Set friend", "[name/reset/list] | [add/del] [name]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        if (parameters[0].equals("reset")) {
            FriendManager.friendList.clear();
            CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7bFriends list \u00a7egot reset");
            return;
        }
        if (parameters[0].equals("list")) {
            if (FriendManager.friendList.isEmpty()) {
                CommandManager.sendChatMessage("\u00a7e[!] \u00a7bFriends list \u00a7eempty");
                return;
            }
            StringBuilder friends = new StringBuilder();
            boolean first = true;
            for (String name : FriendManager.friendList) {
                if (!first) {
                    friends.append(", ");
                }
                friends.append(name);
                first = false;
            }
            CommandManager.sendChatMessage("\u00a7e[~] \u00a7bFriends\u00a7e:\u00a7a" + friends);
            return;
        }
        if (parameters[0].equals("add")) {
            if (parameters.length == 2) {
                HexTech.FRIEND.addFriend(parameters[1]);
                CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7b" + parameters[1] + (FriendManager.isFriend(parameters[1]) ? " \u00a7ahas been friended" : " \u00a7chas been unfriended"));
                return;
            }
            this.sendUsage();
            return;
        }
        if (parameters[0].equals("del")) {
            if (parameters.length == 2) {
                FriendManager.removeFriend(parameters[1]);
                CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7b" + parameters[1] + (FriendManager.isFriend(parameters[1]) ? " \u00a7ahas been friended" : " \u00a7chas been unfriended"));
                return;
            }
            this.sendUsage();
            return;
        }
        if (parameters.length == 1) {
            CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7b" + parameters[0] + (FriendManager.isFriend(parameters[0]) ? " \u00a7ais friended" : " \u00a7cisn't friended"));
            return;
        }
        this.sendUsage();
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        if (count == 1) {
            String input = seperated.get(seperated.size() - 1).toLowerCase();
            ArrayList<String> correct = new ArrayList<String>();
            List<String> list = List.of("add", "del", "list", "reset");
            for (String x : list) {
                if (!input.equalsIgnoreCase(HexTech.PREFIX + "friend") && !x.toLowerCase().startsWith(input)) continue;
                correct.add(x);
            }
            int numCmds = correct.size();
            String[] commands = new String[numCmds];
            int i = 0;
            for (String x : correct) {
                commands[i++] = x;
            }
            return commands;
        }
        return null;
    }
}
