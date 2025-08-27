package me.hextech.remapped;

import com.google.common.base.Splitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import me.hextech.HexTech;
import me.hextech.remapped.BindSetting;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiTab;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Setting;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.StringSetting;
import me.hextech.remapped.Wrapper;
import org.apache.commons.io.IOUtils;

public class ConfigManager
implements Wrapper {
    public static boolean canSave = true;
    public static File options = new File(ConfigManager.mc.runDirectory, "options.txt");
    public static File bindFile = new File(ConfigManager.mc.runDirectory, "binds.txt");
    private final Hashtable<String, String> settings = new Hashtable();
    private final Hashtable<String, String> bindSettings = new Hashtable();

    public ConfigManager() {
        this.readSettings();
        this.readBindSettings();
    }

    public static void resetModule() {
        for (Module_eSdgMXWuzcxgQVaJFmKZ module : HexTech.MODULE.modules) {
            module.setState(false);
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isFloat(String str) {
        String pattern = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
        return str.matches(pattern);
    }

    public void loadSettings() {
        for (Module_eSdgMXWuzcxgQVaJFmKZ m : HexTech.MODULE.modules) {
            for (Setting s : m.getSettings()) {
                if (s instanceof BindSetting) continue;
                s.loadSetting();
            }
            m.setState(this.getBoolean(m.getName() + "_state", false));
        }
        for (Module_eSdgMXWuzcxgQVaJFmKZ m : HexTech.MODULE.modules) {
            for (Setting s : m.getSettings()) {
                if (!(s instanceof BindSetting)) continue;
                BindSetting bs = (BindSetting)s;
                bs.setKey(this.getInt(bs.getLine(), bs.getKey()));
                bs.setHoldEnable(this.getBoolean(bs.getLine() + "_hold", bs.isHoldEnable()));
            }
        }
    }

    public void saveSettings() {
        Setting bs;
        PrintWriter pw;
        if (!canSave) {
            System.out.println("Illegal session status, ignore saving.");
            return;
        }
        try {
            pw = new PrintWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(options), StandardCharsets.UTF_8));
            try {
                pw.println("prefix:" + HexTech.PREFIX);
                for (ClickGuiTab tab : HexTech.GUI.tabs) {
                    pw.println(tab.getTitle() + "_x:" + tab.getX());
                    pw.println(tab.getTitle() + "_y:" + tab.getY());
                }
                pw.println("armor_x:" + HexTech.GUI.armorHud.getX());
                pw.println("armor_y:" + HexTech.GUI.armorHud.getY());
                for (Module_eSdgMXWuzcxgQVaJFmKZ m : HexTech.MODULE.modules) {
                    for (Setting s : m.getSettings()) {
                        if (s instanceof BindSetting) continue;
                        if (s instanceof BooleanSetting) {
                            bs = (BooleanSetting)s;
                            pw.println(bs.getLine() + ":" + ((BooleanSetting)bs).getValue());
                            continue;
                        }
                        if (s instanceof SliderSetting) {
                            SliderSetting ss = (SliderSetting)s;
                            pw.println(ss.getLine() + ":" + ss.getValue());
                            continue;
                        }
                        if (s instanceof EnumSetting) {
                            EnumSetting es = (EnumSetting)s;
                            pw.println(es.getLine() + ":" + ((Enum)es.getValue()).name());
                            continue;
                        }
                        if (s instanceof ColorSetting) {
                            ColorSetting cs = (ColorSetting)s;
                            pw.println(cs.getLine() + ":" + cs.getValue().getRGB());
                            pw.println(cs.getLine() + "Rainbow:" + cs.isRainbow);
                            if (!cs.injectBoolean) continue;
                            pw.println(cs.getLine() + "Boolean:" + cs.booleanValue);
                            continue;
                        }
                        if (!(s instanceof StringSetting)) continue;
                        StringSetting ss = (StringSetting)s;
                        pw.println(ss.getLine() + ":" + ss.getValue());
                    }
                    pw.println(m.getName() + "_state:" + m.isOn());
                }
            }
            finally {
                pw.close();
            }
        }
        catch (Exception ex) {
            System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Failed to save settings");
        }
        try {
            pw = new PrintWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(bindFile), StandardCharsets.UTF_8));
            try {
                for (Module_eSdgMXWuzcxgQVaJFmKZ m : HexTech.MODULE.modules) {
                    for (Setting s : m.getSettings()) {
                        if (!(s instanceof BindSetting)) continue;
                        bs = (BindSetting)s;
                        pw.println(bs.getLine() + ":" + ((BindSetting)bs).getKey());
                        pw.println(bs.getLine() + "_hold:" + ((BindSetting)bs).isHoldEnable());
                    }
                }
            }
            finally {
                pw.close();
            }
        }
        catch (Exception ex) {
            System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Failed to save bind settings");
        }
    }

    public void readSettings() {
        this.readFile(options, this.settings);
    }

    private void readBindSettings() {
        this.readFile(bindFile, this.bindSettings);
    }

    private void readFile(File file, Hashtable<String, String> table) {
        Splitter COLON = Splitter.on((char)':').limit(2);
        if (!file.exists()) {
            return;
        }
        try {
            List lines = IOUtils.readLines((InputStream)new FileInputStream(file), (Charset)StandardCharsets.UTF_8);
            for (String line : lines) {
                try {
                    Iterator it = COLON.split((CharSequence)line).iterator();
                    table.put((String)it.next(), (String)it.next());
                }
                catch (Exception ignored) {
                    System.out.println("Skipping bad line: " + line);
                }
            }
        }
        catch (IOException ex) {
            System.out.println("[\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c] Failed to load " + file.getName());
        }
    }

    public int getInt(String key, int def) {
        String v = this.settings.get(key);
        if (v == null) {
            v = this.bindSettings.get(key);
        }
        return v == null || !ConfigManager.isInteger(v) ? def : Integer.parseInt(v);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.settings.get(key));
    }

    public boolean getBoolean(String key, boolean def) {
        String v = this.settings.get(key);
        if (v == null) {
            v = this.bindSettings.get(key);
        }
        return v == null ? def : Boolean.parseBoolean(v);
    }

    public String getString(String key) {
        String v = this.settings.get(key);
        if (v == null) {
            v = this.bindSettings.get(key);
        }
        return v;
    }

    public String getString(String key, String def) {
        String v = this.settings.get(key);
        if (v == null) {
            v = this.bindSettings.get(key);
        }
        return v == null ? def : v;
    }

    public float getFloat(String setting, float defaultValue) {
        String s = this.settings.get(setting);
        if (s == null || !ConfigManager.isFloat(s)) {
            return defaultValue;
        }
        return Float.parseFloat(s);
    }
}
