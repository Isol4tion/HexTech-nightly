package me.hextech.api.managers;

import me.hextech.HexTech;
import me.hextech.api.interfaces.IChatHud;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.commands.Command;
import me.hextech.mod.commands.impl.*;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.setting.ChatSetting_qVnAbgCzNciNTevKRovy;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CommandManager
        implements Wrapper {
    public static final String syncCode = "§)";
    public final AimCommand aim = new AimCommand();
    public final BindCommand bind = new BindCommand();
    public final ClipCommand clip = new ClipCommand();
    public final FriendCommand friend = new FriendCommand();
    public final GamemodeCommand gamemode = new GamemodeCommand();
    public final HelpCommand help = new HelpCommand();
    public final PrefixCommand prefix = new PrefixCommand();
    public final LoadCommand load = new LoadCommand();
    public final RejoinCommand rejoin = new RejoinCommand();
    public final ReloadCommand reload = new ReloadCommand();
    public final ReloadAllCommand reloadHack = new ReloadAllCommand();
    public final SaveCommand save = new SaveCommand();
    public final TeleportCommand tp = new TeleportCommand();
    public final Toggle2Command t = new Toggle2Command();
    public final ToggleCommand toggle = new ToggleCommand();
    public final WatermarkCommand watermark = new WatermarkCommand();
    public final ChatCommand chat = new ChatCommand();
    public final ListCommand list = new ListCommand();
    private final HashMap<String, Command> commands = new HashMap();

    public CommandManager() {
        try {
            for (Field field : CommandManager.class.getDeclaredFields()) {
                if (!Command.class.isAssignableFrom(field.getType())) continue;
                Command cmd = (Command) field.get(this);
                this.commands.put(cmd.getName(), cmd);
            }
        } catch (Exception e) {
            System.out.println("Error initializing \u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c commands.");
            System.out.println(e.getStackTrace().toString());
        }
    }

    public static void sendChatMessage(String message) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        String startCode = "";
        String endCode = "";
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_qVnAbgCzNciNTevKRovy.Code.Earth) {
            startCode = "<";
            endCode = ">";
        }
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_qVnAbgCzNciNTevKRovy.Code.Custom) {
            startCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.start.getValue();
            endCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.end.getValue();
        }
        CommandManager.mc.inGameHud.getChatHud().addMessage(Text.of("\u00a7(\u00a7r" + startCode + ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.hackName.getValue() + "\u00a7r" + endCode + "\u00a7f " + message));
    }

    public static void sendChatMessageWidthId(String message, int id) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        String startCode = "";
        String endCode = "";
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_qVnAbgCzNciNTevKRovy.Code.Earth) {
            startCode = "<";
            endCode = ">";
        }
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_qVnAbgCzNciNTevKRovy.Code.Custom) {
            startCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.start.getValue();
            endCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.end.getValue();
        }
        ((IChatHud) CommandManager.mc.inGameHud.getChatHud()).nullpoint_nextgen_master$add(Text.of("\u00a7(\u00a7r" + startCode + ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.hackName.getValue() + "\u00a7r" + endCode + "\u00a7f " + message), id);
    }

    public static void sendChatMessageWidthIdNoSync(String message, int id) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        ((IChatHud) CommandManager.mc.inGameHud.getChatHud()).nullpoint_nextgen_master$add(Text.of("\u00a7f" + message), id);
    }

    public Command getCommandBySyntax(String string) {
        return this.commands.get(string);
    }

    public HashMap<String, Command> getCommands() {
        return this.commands;
    }

    public int getNumOfCommands() {
        return this.commands.size();
    }

    public void command(String[] commandIn) throws Throwable {
        Command command = this.commands.get(commandIn[0].substring(HexTech.PREFIX.length()).toLowerCase());
        if (command == null) {
            CommandManager.sendChatMessage("\u00a7c[!] \u00a7fInvalid Command! Type \u00a7ehelp \u00a7ffor a list of commands.");
        } else {
            String[] parameterList = new String[commandIn.length - 1];
            System.arraycopy(commandIn, 1, parameterList, 0, commandIn.length - 1);
            if (parameterList.length == 1 && parameterList[0].equals("help")) {
                command.sendUsage();
                return;
            }
            command.runCommand(parameterList);
        }
    }
}
