package me.hextech.remapped;

import java.io.File;
import net.minecraft.client.MinecraftClient;

public class Manager {
   public static MinecraftClient mc = MinecraftClient.method_1551();

   public static File getFile(String s) {
      File folder = new File(mc.field_1697.getPath() + File.separator + "HexTech".toLowerCase());
      if (!folder.exists()) {
         folder.mkdirs();
      }

      return new File(folder, s);
   }
}
