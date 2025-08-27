package me.hextech.remapped;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class FontRenderers {
   public static FontAdapter Arial;
   public static FontAdapter Calibri;
   public static FontAdapter defaul;
   public static FontAdapter ui;

   @NotNull
   public static RendererFontAdapter createDefault(float size, String name) throws IOException, FontFormatException {
      return new RendererFontAdapter(
         Font.createFont(
               0, (InputStream)Objects.requireNonNull(FontRenderers.class.getClassLoader().getResourceAsStream("assets/minecraft/font/" + name + ".ttf"))
            )
            .deriveFont(0, size),
         size
      );
   }

   public static void createDefault(float size) {
      try {
         ui = createDefault(size, "MiSans");
      } catch (FontFormatException | IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static RendererFontAdapter createArial(float size) {
      Font font = new Font("tahoma", 0, (int)size);
      return new RendererFontAdapter(font, size);
   }

   public static RendererFontAdapter create(String name, int style, float size) {
      return new RendererFontAdapter(new Font(name, style, (int)size), size);
   }

   public final FontAdapter getDefault() {
      return defaul;
   }

   public final void setDefault(@NotNull FontAdapter fontAdapter) {
      defaul = fontAdapter;
   }
}
