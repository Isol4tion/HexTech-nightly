package me.hextech.remapped;

import java.lang.reflect.Field;
import java.util.HashMap;
import me.hextech.HexTech;
import me.hextech.remapped.AimCommand;
import me.hextech.remapped.BindCommand;
import me.hextech.remapped.ChatCommand;
import me.hextech.remapped.ChatSetting_FjbVfiTpsZbQdeUlhhhi;
import me.hextech.remapped.ChatSetting_qVnAbgCzNciNTevKRovy;
import me.hextech.remapped.ClipCommand;
import me.hextech.remapped.Command;
import me.hextech.remapped.FriendCommand;
import me.hextech.remapped.GamemodeCommand;
import me.hextech.remapped.HelpCommand;
import me.hextech.remapped.IChatHud;
import me.hextech.remapped.ListCommand;
import me.hextech.remapped.LoadCommand;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.PrefixCommand;
import me.hextech.remapped.RejoinCommand;
import me.hextech.remapped.ReloadAllCommand;
import me.hextech.remapped.ReloadCommand;
import me.hextech.remapped.SaveCommand;
import me.hextech.remapped.TeleportCommand;
import me.hextech.remapped.Toggle2Command;
import me.hextech.remapped.ToggleCommand;
import me.hextech.remapped.WatermarkCommand;
import me.hextech.remapped.Wrapper;
import net.minecraft.text.Text;

public class CommandManager
implements Wrapper {
    public static final String syncCode;
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
                Command cmd = (Command)field.get(this);
                this.commands.put(cmd.getName(), cmd);
            }
        }
        catch (Exception e) {
            System.out.println("Error initializing HexTech-nightly Cracked By NoWhisper commands.");
            System.out.println(e.getStackTrace().toString());
        }
    }

    public static void sendChatMessage(String message) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        String startCode = "";
        String endCode = "";
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_FjbVfiTpsZbQdeUlhhhi.Earth) {
            startCode = "<";
            endCode = ">";
        }
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_FjbVfiTpsZbQdeUlhhhi.Custom) {
            startCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.start.getValue();
            endCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.end.getValue();
        }
        CommandManager.mc.field_1705.method_1743().method_1812(Text.method_30163((String)("\u00a7(\u00a7r" + startCode + ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.hackName.getValue() + "\u00a7r" + endCode + "\u00a7f " + message)));
    }

    public static void sendChatMessageWidthId(String message, int id) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        String startCode = "";
        String endCode = "";
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_FjbVfiTpsZbQdeUlhhhi.Earth) {
            startCode = "<";
            endCode = ">";
        }
        if (ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.messageCode.getValue() == ChatSetting_FjbVfiTpsZbQdeUlhhhi.Custom) {
            startCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.start.getValue();
            endCode = ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.end.getValue();
        }
        ((IChatHud)CommandManager.mc.field_1705.method_1743()).nullpoint_nextgen_master$add(Text.method_30163((String)("\u00a7(\u00a7r" + startCode + ChatSetting_qVnAbgCzNciNTevKRovy.INSTANCE.hackName.getValue() + "\u00a7r" + endCode + "\u00a7f " + message)), id);
    }

    public static void sendChatMessageWidthIdNoSync(String message, int id) {
        if (Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
            return;
        }
        ((IChatHud)CommandManager.mc.field_1705.method_1743()).nullpoint_nextgen_master$add(Text.method_30163((String)("\u00a7f" + message)), id);
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
