package me.hextech.remapped;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.client.network.PlayerListEntry;
import org.apache.commons.io.IOUtils;

public class Spammer extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final String CHARACTERS;
   public final BooleanSetting tellMode = this.add(new BooleanSetting("RandomMsg", false));
   public final BooleanSetting checkSelf = this.add(new BooleanSetting("CheckSelf", false));
   private final SliderSetting randoms = this.add(new SliderSetting("Random", 3.0, 0.0, 20.0, 1.0));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 60.0, 0.1).setSuffix("s"));
   private final List<String> messages = new ArrayList();
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
      if (this.timer.passed(this.delay.getValue())) {
         this.timer.reset();
         String randomString = this.generateRandomString(this.randoms.getValueInt());
         if (!randomString.isEmpty()) {
            randomString = " " + randomString;
         }

         if (!this.messages.isEmpty()) {
            String selectedMessage = (String)this.messages.get(this.random.nextInt(this.messages.size()));
            if (this.tellMode.getValue()) {
               Collection<PlayerListEntry> players = mc.method_1562().method_2880();
               List<PlayerListEntry> list = new ArrayList(players);
               int size = list.size();
               if (size == 0) {
                  return;
               }

               PlayerListEntry playerListEntry = (PlayerListEntry)list.get(this.random.nextInt(size));
               if (mc.field_1724 != null) {
                  while (this.checkSelf.getValue() && Objects.equals(playerListEntry.method_2966().getName(), mc.field_1724.method_7334().getName())) {
                     playerListEntry = (PlayerListEntry)list.get(this.random.nextInt(size));
                  }
               }

               mc.method_1562().method_45730("tell " + playerListEntry.method_2966().getName() + " " + selectedMessage + randomString);
            } else {
               mc.method_1562().method_45729(selectedMessage + randomString);
            }
         }
      }
   }

   private String generateRandomString(int LENGTH) {
      StringBuilder sb = new StringBuilder(LENGTH);

      for (int i = 0; i < LENGTH; i++) {
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
            writer.write("国服2b2t最强hack HexTech-Nightly 毋庸置疑，获取加QQ2353761389\n");
            writer.close();
         }

         List<String> lines = IOUtils.readLines(new FileInputStream(SpammerFile), StandardCharsets.UTF_8);
         this.messages.addAll(lines);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }
}
