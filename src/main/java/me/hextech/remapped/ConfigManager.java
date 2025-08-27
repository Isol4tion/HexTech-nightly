package me.hextech.remapped;

import com.google.common.base.Splitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class ConfigManager implements Wrapper {
   public static boolean canSave;
   public static File options = new File(mc.field_1697, "options.txt");
   public static File bindFile = new File(mc.field_1697, "binds.txt");
   private final Hashtable<String, String> settings = new Hashtable();
   private final Hashtable<String, String> bindSettings = new Hashtable();

   public ConfigManager() {
      this.readSettings();
      this.readBindSettings();
   }

   public static void resetModule() {
      for (Module_eSdgMXWuzcxgQVaJFmKZ module : me.hextech.HexTech.MODULE.modules) {
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
      for (Module_eSdgMXWuzcxgQVaJFmKZ m : me.hextech.HexTech.MODULE.modules) {
         for (Setting s : m.getSettings()) {
            if (!(s instanceof BindSetting)) {
               s.loadSetting();
            }
         }

         m.setState(this.getBoolean(m.getName() + "_state", false));
      }

      for (Module_eSdgMXWuzcxgQVaJFmKZ m : me.hextech.HexTech.MODULE.modules) {
         for (Setting sx : m.getSettings()) {
            if (sx instanceof BindSetting bs) {
               bs.setKey(this.getInt(bs.getLine(), bs.getKey()));
               bs.setHoldEnable(this.getBoolean(bs.getLine() + "_hold", bs.isHoldEnable()));
            }
         }
      }
   }

   public void saveSettings() {
      if (!canSave) {
         System.out.println("Illegal session status, ignore saving.");
      } else {
         try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(options), StandardCharsets.UTF_8));

            try {
               pw.println("prefix:" + me.hextech.HexTech.PREFIX);

               for (ClickGuiTab tab : me.hextech.HexTech.GUI.tabs) {
                  pw.println(tab.getTitle() + "_x:" + tab.getX());
                  pw.println(tab.getTitle() + "_y:" + tab.getY());
               }

               pw.println("armor_x:" + me.hextech.HexTech.GUI.armorHud.getX());
               pw.println("armor_y:" + me.hextech.HexTech.GUI.armorHud.getY());

               for (Module_eSdgMXWuzcxgQVaJFmKZ m : me.hextech.HexTech.MODULE.modules) {
                  for (Setting s : m.getSettings()) {
                     if (!(s instanceof BindSetting)) {
                        if (s instanceof BooleanSetting bs) {
                           pw.println(bs.getLine() + ":" + bs.getValue());
                        } else if (s instanceof SliderSetting ss) {
                           pw.println(ss.getLine() + ":" + ss.getValue());
                        } else if (s instanceof EnumSetting es) {
                           pw.println(es.getLine() + ":" + es.getValue().name());
                        } else if (s instanceof ColorSetting) {
                           ColorSetting cs = (ColorSetting)s;
                           pw.println(cs.getLine() + ":" + cs.getValue().getRGB());
                           pw.println(cs.getLine() + "Rainbow:" + cs.isRainbow);
                           if (cs.injectBoolean) {
                              pw.println(cs.getLine() + "Boolean:" + cs.booleanValue);
                           }
                        } else if (s instanceof StringSetting ss) {
                           pw.println(ss.getLine() + ":" + ss.getValue());
                        }
                     }
                  }

                  pw.println(m.getName() + "_state:" + m.isOn());
               }
            } catch (Throwable var15) {
               try {
                  pw.close();
               } catch (Throwable var12) {
                  var15.addSuppressed(var12);
               }

               throw var15;
            }

            pw.close();
         } catch (Exception var16) {
            System.out.println("[ʜᴇӼᴛᴇᴄʜ] Failed to save settings");
         }

         try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(bindFile), StandardCharsets.UTF_8));

            try {
               for (Module_eSdgMXWuzcxgQVaJFmKZ m : me.hextech.HexTech.MODULE.modules) {
                  for (Setting sx : m.getSettings()) {
                     if (sx instanceof BindSetting bs) {
                        pw.println(bs.getLine() + ":" + bs.getKey());
                        pw.println(bs.getLine() + "_hold:" + bs.isHoldEnable());
                     }
                  }
               }
            } catch (Throwable var13) {
               try {
                  pw.close();
               } catch (Throwable var11) {
                  var13.addSuppressed(var11);
               }

               throw var13;
            }

            pw.close();
         } catch (Exception var14) {
            System.out.println("[ʜᴇӼᴛᴇᴄʜ] Failed to save bind settings");
         }
      }
   }

   public void readSettings() {
      this.readFile(options, this.settings);
   }

   private void readBindSettings() {
      this.readFile(bindFile, this.bindSettings);
   }

   private void readFile(File file, Hashtable<String, String> table) {
      Splitter COLON = Splitter.on(':').limit(2);
      if (file.exists()) {
         try {
            for (String line : IOUtils.readLines(new FileInputStream(file), StandardCharsets.UTF_8)) {
               try {
                  Iterator<String> it = COLON.split(line).iterator();
                  table.put((String)it.next(), (String)it.next());
               } catch (Exception var8) {
                  System.out.println("Skipping bad line: " + line);
               }
            }
         } catch (IOException var9) {
            System.out.println("[ʜᴇӼᴛᴇᴄʜ] Failed to load " + file.getName());
         }
      }
   }

   public int getInt(String key, int def) {
      String v = (String)this.settings.get(key);
      if (v == null) {
         v = (String)this.bindSettings.get(key);
      }

      return v != null && isInteger(v) ? Integer.parseInt(v) : def;
   }

   public boolean getBoolean(String key) {
      return Boolean.parseBoolean((String)this.settings.get(key));
   }

   public boolean getBoolean(String key, boolean def) {
      String v = (String)this.settings.get(key);
      if (v == null) {
         v = (String)this.bindSettings.get(key);
      }

      return v == null ? def : Boolean.parseBoolean(v);
   }

   public String getString(String key) {
      String v = (String)this.settings.get(key);
      if (v == null) {
         v = (String)this.bindSettings.get(key);
      }

      return v;
   }

   public String getString(String key, String def) {
      String v = (String)this.settings.get(key);
      if (v == null) {
         v = (String)this.bindSettings.get(key);
      }

      return v == null ? def : v;
   }

   public float getFloat(String setting, float defaultValue) {
      String s = (String)this.settings.get(setting);
      return s != null && isFloat(s) ? Float.parseFloat(s) : defaultValue;
   }
}
