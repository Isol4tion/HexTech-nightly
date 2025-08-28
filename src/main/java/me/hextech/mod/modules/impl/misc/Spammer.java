package me.hextech.mod.modules.impl.misc;

import me.hextech.api.managers.Manager;
import me.hextech.api.utils.math.Timer;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.BooleanSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.network.PlayerListEntry;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Spammer
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final BooleanSetting tellMode = this.add(new BooleanSetting("RandomMsg", false));
    public final BooleanSetting checkSelf = this.add(new BooleanSetting("CheckSelf", false));
    private final SliderSetting randoms = this.add(new SliderSetting("Random", 3.0, 0.0, 20.0, 1.0));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 60.0, 0.1).setSuffix("s"));
    private final List<String> messages = new ArrayList<String>();
    Random random = new Random();
    Timer timer = new Timer();

    public Spammer() {
        super("Spammer", Category.Misc);
        this.readMessages();
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passed(this.delay.getValue())) {
            return;
        }
        this.timer.reset();
        Object randomString = this.generateRandomString(this.randoms.getValueInt());
        if (!((String)randomString).isEmpty()) {
            randomString = " " + randomString;
        }
        if (!this.messages.isEmpty()) {
            String selectedMessage = this.messages.get(this.random.nextInt(this.messages.size()));
            if (this.tellMode.getValue()) {
                Collection players = mc.getNetworkHandler().getPlayerList();
                ArrayList list = new ArrayList(players);
                int size = list.size();
                if (size == 0) {
                    return;
                }
                PlayerListEntry playerListEntry = (PlayerListEntry)list.get(this.random.nextInt(size));
                if (Spammer.mc.player != null) {
                    while (this.checkSelf.getValue() && Objects.equals(playerListEntry.getProfile().getName(), Spammer.mc.player.getGameProfile().getName())) {
                        playerListEntry = (PlayerListEntry)list.get(this.random.nextInt(size));
                    }
                }
                mc.getNetworkHandler().sendChatCommand("tell " + playerListEntry.getProfile().getName() + " " + selectedMessage + randomString);
            } else {
                mc.getNetworkHandler().sendChatMessage(selectedMessage + randomString);
            }
        }
    }

    private String generateRandomString(int LENGTH) {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; ++i) {
            int index = this.random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length());
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(index));
        }
        return sb.toString();
    }

    private void readMessages() {
        try {
            File SpammerFile = Manager.getFile("spammer.txt");
            if (!SpammerFile.exists()) {
                SpammerFile.createNewFile();
                PrintWriter writer = new PrintWriter(SpammerFile);
                writer.write("\u56fd\u670d2b2t\u6700\u5f3ahack HexTech-Nightly \u6bcb\u5eb8\u7f6e\u7591\uff0c\u83b7\u53d6\u52a0QQ2353761389\n");
                writer.close();
            }
            List lines = IOUtils.readLines(new FileInputStream(SpammerFile), StandardCharsets.UTF_8);
            this.messages.addAll(lines);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
