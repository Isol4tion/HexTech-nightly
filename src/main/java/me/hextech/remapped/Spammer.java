package me.hextech.remapped;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Manager;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Timer;
import net.minecraft.client.network.PlayerListEntry;
import org.apache.commons.io.IOUtils;

public class Spammer
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final String CHARACTERS;
    public final BooleanSetting tellMode = this.add(new BooleanSetting("RandomMsg", false));
    public final BooleanSetting checkSelf = this.add(new BooleanSetting("CheckSelf", false));
    private final SliderSetting randoms = this.add(new SliderSetting("Random", 3.0, 0.0, 20.0, 1.0));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 60.0, 0.1).setSuffix("s"));
    private final List<String> messages = new ArrayList<String>();
    Random random = new Random();
    Timer timer = new Timer();

    public Spammer() {
        super("Spammer", Module_JlagirAibYQgkHtbRnhw.Misc);
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
            randomString = " " + (String)randomString;
        }
        if (!this.messages.isEmpty()) {
            String selectedMessage = this.messages.get(this.random.nextInt(this.messages.size()));
            if (this.tellMode.getValue()) {
                Collection players = mc.method_1562().method_2880();
                ArrayList list = new ArrayList(players);
                int size = list.size();
                if (size == 0) {
                    return;
                }
                PlayerListEntry playerListEntry = (PlayerListEntry)list.get(this.random.nextInt(size));
                if (Spammer.mc.player != null) {
                    while (this.checkSelf.getValue() && Objects.equals(playerListEntry.method_2966().getName(), Spammer.mc.player.method_7334().getName())) {
                        playerListEntry = (PlayerListEntry)list.get(this.random.nextInt(size));
                    }
                }
                mc.method_1562().method_45730("tell " + playerListEntry.method_2966().getName() + " " + selectedMessage + (String)randomString);
            } else {
                mc.method_1562().method_45729(selectedMessage + (String)randomString);
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
            List<String> lines = IOUtils.readLines((InputStream)new FileInputStream(SpammerFile), StandardCharsets.UTF_8);
            this.messages.addAll(lines);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
