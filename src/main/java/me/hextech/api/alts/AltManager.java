package me.hextech.api.alts;

import me.hextech.api.managers.Manager;
import me.hextech.api.utils.Wrapper;
import me.hextech.asm.accessors.IMinecraftClient;
import net.minecraft.client.session.Session;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AltManager
        implements Wrapper {
    private final ArrayList<Alt> alts = new ArrayList();

    public AltManager() {
        this.readAlts();
    }

    public void readAlts() {
        try {
            File altFile = Manager.getFile("alt.txt");
            if (!altFile.exists()) {
                throw new IOException("File not found! Could not load alts...");
            }
            List<String> list = IOUtils.readLines(new FileInputStream(altFile), StandardCharsets.UTF_8);
            for (String s : list) {
                this.alts.add(new Alt(s));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void saveAlts() {
        PrintWriter printwriter = null;
        try {
            File altFile = Manager.getFile("alt.txt");
            System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Saving Alts");
            printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(altFile), StandardCharsets.UTF_8));
            for (Alt alt : this.alts) {
                printwriter.println(alt.getEmail());
            }
        } catch (Exception exception) {
            System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Failed to save alts");
        }
        printwriter.close();
    }

    public void addAlt(Alt alt) {
        this.alts.add(alt);
    }

    public void removeAlt(Alt alt) {
        this.alts.remove(alt);
    }

    public ArrayList<Alt> getAlts() {
        return this.alts;
    }

    public void loginCracked(String alt) {
        try {
            ((IMinecraftClient) Wrapper.mc).setSession(new Session(alt, UUID.fromString("66123666-1234-5432-6666-667563866600"), "", Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginToken(String name, String token, String uuid) {
        try {
            ((IMinecraftClient) Wrapper.mc).setSession(new Session(name, UUID.fromString(uuid), token, Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
