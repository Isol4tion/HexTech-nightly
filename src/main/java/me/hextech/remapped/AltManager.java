package me.hextech.remapped;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import me.hextech.asm.accessors.IMinecraftClient;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.Session.AccountType;
import org.apache.commons.io.IOUtils;

public class AltManager implements Wrapper {
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

         for (String s : IOUtils.readLines(new FileInputStream(altFile), StandardCharsets.UTF_8)) {
            this.alts.add(new Alt(s));
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }

   public void saveAlts() {
      PrintWriter printwriter = null;

      try {
         File altFile = Manager.getFile("alt.txt");
         System.out.println("[ʜᴇӼᴛᴇᴄʜ] Saving Alts");
         printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(altFile), StandardCharsets.UTF_8));

         for (Alt alt : this.alts) {
            printwriter.println(alt.getEmail());
         }
      } catch (Exception var5) {
         System.out.println("[ʜᴇӼᴛᴇᴄʜ] Failed to save alts");
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
         ((IMinecraftClient)Wrapper.mc)
            .setSession(
               new Session(alt, UUID.fromString("66123666-1234-5432-6666-667563866600"), "", Optional.empty(), Optional.empty(), AccountType.field_1988)
            );
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public void loginToken(String name, String token, String uuid) {
      try {
         ((IMinecraftClient)Wrapper.mc).setSession(new Session(name, UUID.fromString(uuid), token, Optional.empty(), Optional.empty(), AccountType.field_1988));
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }
}
