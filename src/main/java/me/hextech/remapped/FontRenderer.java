package me.hextech.remapped;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.awt.Color;
import java.awt.Font;
import java.io.Closeable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class FontRenderer implements Closeable {
   private static final Char2IntArrayMap colorCodes;
   private static final int BLOCK_SIZE;
   private static final Object2ObjectArrayMap<Identifier, ObjectList<FontRenderer_ZitfNZXjZiLiXZJDgFqm>> GLYPH_PAGE_CACHE = new Object2ObjectArrayMap();
   private static final char RND_START;
   private static final char RND_END;
   private static final Random RND = new Random();
   private final float originalSize;
   private final ObjectList<GlyphMap> maps = new ObjectArrayList();
   private final Char2ObjectArrayMap<Glyph> allGlyphs = new Char2ObjectArrayMap();
   private int scaleMul = 0;
   private Font[] fonts;
   private int previousGameScale = -1;

   public FontRenderer(@NotNull Font[] fonts, float sizePx) {
      Preconditions.checkArgument(fonts.length > 0, "fonts.length == 0");
      this.originalSize = sizePx;
      this.init(fonts, sizePx);
   }

   private static int floorNearestMulN(int x) {
      return 256 * (int)Math.floor((double)x / 256.0);
   }

   @NotNull
   public static String stripControlCodes(@NotNull String text) {
      char[] chars = text.toCharArray();
      StringBuilder f = new StringBuilder();

      for (int i = 0; i < chars.length; i++) {
         char c = chars[i];
         if (c == 167) {
            i++;
         } else {
            f.append(c);
         }
      }

      return f.toString();
   }

   public static int getGuiScale() {
      return (int)Wrapper.mc.method_22683().method_4495();
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static int[] RGBIntToRGB(int in) {
      int red = in >> 16 & 0xFF;
      int green = in >> 8 & 0xFF;
      int blue = in & 0xFF;
      return new int[]{red, green, blue};
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   public static Identifier randomIdentifier() {
      return new Identifier("hextech", "temp/" + randomString(32));
   }

   private static String randomString(int length) {
      return (String)IntStream.range(0, length).mapToObj(operand -> String.valueOf((char)RND.nextInt(97, 123))).collect(Collectors.joining());
   }

   private void sizeCheck() {
      int gs = getGuiScale();
      if (gs != this.previousGameScale) {
         this.close();
         this.init(this.fonts, this.originalSize);
      }
   }

   private void init(@NotNull Font[] fonts, float sizePx) {
      this.previousGameScale = getGuiScale();
      this.scaleMul = this.previousGameScale;
      this.fonts = new Font[fonts.length];

      for (int i = 0; i < fonts.length; i++) {
         this.fonts[i] = fonts[i].deriveFont(sizePx * (float)this.scaleMul);
      }
   }

   @NotNull
   private GlyphMap generateMap(char from, char to) {
      GlyphMap gm = new GlyphMap(from, to, this.fonts, randomIdentifier());
      this.maps.add(gm);
      return gm;
   }

   private Glyph locateGlyph0(char glyph) {
      ObjectListIterator base = this.maps.iterator();

      while (base.hasNext()) {
         GlyphMap map = (GlyphMap)base.next();
         if (map.contains(glyph)) {
            return map.getGlyph(glyph);
         }
      }

      int basex = floorNearestMulN(glyph);
      GlyphMap glyphMap = this.generateMap((char)basex, (char)(basex + 256));
      return glyphMap.getGlyph(glyph);
   }

   private Glyph locateGlyph1(char glyph) {
      return (Glyph)this.allGlyphs.computeIfAbsent(glyph, this::locateGlyph0);
   }

   public void drawString(@NotNull MatrixStack stack, @NotNull String s, float x, float y, float r, float g, float b, float a) {
      this.drawString(stack, s, x + 1.0F, y + 1.0F, 0.0F, 0.0F, 0.0F, a, true);
      this.drawString(stack, s, x, y, r, g, b, a, false);
   }

   public void drawString(@NotNull MatrixStack stack, @NotNull String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
      this.sizeCheck();
      float r2 = r;
      float g2 = g;
      float b2 = b;
      stack.method_22903();
      stack.method_46416(x, y, 0.0F);
      stack.method_22905(1.0F / (float)this.scaleMul, 1.0F / (float)this.scaleMul, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      RenderSystem.setShader(GameRenderer::method_34543);
      BufferBuilder bb = Tessellator.method_1348().method_1349();
      Matrix4f mat = stack.method_23760().method_23761();
      char[] chars = s.toCharArray();
      float xOffset = 0.0F;
      float yOffset = 0.0F;
      boolean inSel = false;
      int lineStart = 0;

      for (int i = 0; i < chars.length; i++) {
         char c = chars[i];
         if (inSel) {
            inSel = false;
            char c1 = Character.toUpperCase(c);
            if (colorCodes.containsKey(c1) && !shadow) {
               int ii = colorCodes.get(c1);
               int[] col = RGBIntToRGB(ii);
               r2 = (float)col[0] / 255.0F;
               g2 = (float)col[1] / 255.0F;
               b2 = (float)col[2] / 255.0F;
            } else if (c1 == 'R') {
               r2 = r;
               g2 = g;
               b2 = b;
            }
         } else if (c == 167) {
            inSel = true;
         } else if (c == '\n') {
            yOffset += this.getStringHeight(s.substring(lineStart, i)) * (float)this.scaleMul;
            xOffset = 0.0F;
            lineStart = i + 1;
         } else {
            Glyph glyph = this.locateGlyph1(c);
            if (glyph.value() != ' ') {
               Identifier i1 = glyph.owner().bindToTexture;
               FontRenderer_ZitfNZXjZiLiXZJDgFqm entry = new FontRenderer_ZitfNZXjZiLiXZJDgFqm(xOffset, yOffset, r2, g2, b2, glyph);
               ((ObjectList)GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList())).add(entry);
            }

            xOffset += (float)glyph.width();
         }
      }

      ObjectIterator var38 = GLYPH_PAGE_CACHE.keySet().iterator();

      while (var38.hasNext()) {
         Identifier identifier = (Identifier)var38.next();
         RenderSystem.setShaderTexture(0, identifier);
         List<FontRenderer_ZitfNZXjZiLiXZJDgFqm> objects = (List<FontRenderer_ZitfNZXjZiLiXZJDgFqm>)GLYPH_PAGE_CACHE.get(identifier);
         bb.method_1328(DrawMode.field_27382, VertexFormats.field_1575);

         for (FontRenderer_ZitfNZXjZiLiXZJDgFqm object : objects) {
            float xo = object.atX;
            float yo = object.atY;
            float cr = object.r;
            float cg = object.g;
            float cb = object.b;
            Glyph glyph = object.toDraw;
            GlyphMap owner = glyph.owner();
            float w = (float)glyph.width();
            float h = (float)glyph.height();
            float u1 = (float)glyph.u() / (float)owner.width;
            float v1 = (float)glyph.v() / (float)owner.height;
            float u2 = (float)(glyph.u() + glyph.width()) / (float)owner.width;
            float v2 = (float)(glyph.v() + glyph.height()) / (float)owner.height;
            bb.method_22918(mat, xo + 0.0F, yo + h, 0.0F).method_22913(u1, v2).method_22915(cr, cg, cb, a).method_1344();
            bb.method_22918(mat, xo + w, yo + h, 0.0F).method_22913(u2, v2).method_22915(cr, cg, cb, a).method_1344();
            bb.method_22918(mat, xo + w, yo + 0.0F, 0.0F).method_22913(u2, v1).method_22915(cr, cg, cb, a).method_1344();
            bb.method_22918(mat, xo + 0.0F, yo + 0.0F, 0.0F).method_22913(u1, v1).method_22915(cr, cg, cb, a).method_1344();
         }

         BufferRenderer.method_43433(bb.method_1326());
      }

      stack.method_22909();
      GLYPH_PAGE_CACHE.clear();
   }

   public void drawGradientString(@NotNull MatrixStack stack, @NotNull String s, float x, float y) {
      this.sizeCheck();
      stack.method_22903();
      stack.method_46416(x, y, 0.0F);
      stack.method_22905(1.0F / (float)this.scaleMul, 1.0F / (float)this.scaleMul, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      RenderSystem.setShader(GameRenderer::method_34543);
      BufferBuilder bb = Tessellator.method_1348().method_1349();
      Matrix4f mat = stack.method_23760().method_23761();
      char[] chars = s.toCharArray();
      float xOffset = 0.0F;
      float yOffset = 0.0F;
      int lineStart = 0;
      float a = 1.0F;

      for (int i = 0; i < chars.length; i++) {
         char c = chars[i];
         Color color = me.hextech.HexTech.GUI.getColor();
         a = (float)color.getAlpha() / 255.0F;
         if (c == '\n') {
            yOffset += this.getStringHeight(s.substring(lineStart, i)) * (float)this.scaleMul;
            xOffset = 0.0F;
            lineStart = i + 1;
         } else {
            Glyph glyph = this.locateGlyph1(c);
            if (glyph.value() != ' ') {
               Identifier i1 = glyph.owner().bindToTexture;
               FontRenderer_ZitfNZXjZiLiXZJDgFqm entry = new FontRenderer_ZitfNZXjZiLiXZJDgFqm(
                  xOffset, yOffset, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, glyph
               );
               ((ObjectList)GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList())).add(entry);
            }

            xOffset += (float)glyph.width();
         }
      }

      ObjectIterator var30 = GLYPH_PAGE_CACHE.keySet().iterator();

      while (var30.hasNext()) {
         Identifier identifier = (Identifier)var30.next();
         RenderSystem.setShaderTexture(0, identifier);
         List<FontRenderer_ZitfNZXjZiLiXZJDgFqm> objects = (List<FontRenderer_ZitfNZXjZiLiXZJDgFqm>)GLYPH_PAGE_CACHE.get(identifier);
         bb.method_1328(DrawMode.field_27382, VertexFormats.field_1575);

         for (FontRenderer_ZitfNZXjZiLiXZJDgFqm object : objects) {
            float xo = object.atX;
            float yo = object.atY;
            float cr = object.r;
            float cg = object.g;
            float cb = object.b;
            Glyph glyph = object.toDraw;
            GlyphMap owner = glyph.owner();
            float w = (float)glyph.width();
            float h = (float)glyph.height();
            float u1 = (float)glyph.u() / (float)owner.width;
            float v1 = (float)glyph.v() / (float)owner.height;
            float u2 = (float)(glyph.u() + glyph.width()) / (float)owner.width;
            float v2 = (float)(glyph.v() + glyph.height()) / (float)owner.height;
            bb.method_22918(mat, xo + 0.0F, yo + h, 0.0F).method_22913(u1, v2).method_22915(cr, cg, cb, a).method_1344();
            bb.method_22918(mat, xo + w, yo + h, 0.0F).method_22913(u2, v2).method_22915(cr, cg, cb, a).method_1344();
            bb.method_22918(mat, xo + w, yo + 0.0F, 0.0F).method_22913(u2, v1).method_22915(cr, cg, cb, a).method_1344();
            bb.method_22918(mat, xo + 0.0F, yo + 0.0F, 0.0F).method_22913(u1, v1).method_22915(cr, cg, cb, a).method_1344();
         }

         BufferRenderer.method_43433(bb.method_1326());
      }

      stack.method_22909();
      GLYPH_PAGE_CACHE.clear();
   }

   public void drawCenteredString(MatrixStack stack, String s, float x, float y, float r, float g, float b, float a) {
      this.drawString(stack, s, x - this.getStringWidth(s) / 2.0F, y, r, g, b, a);
   }

   public float getStringWidth(String text) {
      char[] c = stripControlCodes(text).toCharArray();
      float currentLine = 0.0F;
      float maxPreviousLines = 0.0F;

      for (char c1 : c) {
         if (c1 == '\n') {
            maxPreviousLines = Math.max(currentLine, maxPreviousLines);
            currentLine = 0.0F;
         } else {
            Glyph glyph = this.locateGlyph1(c1);
            float gWidth = glyph == null ? 1.0F : (float)glyph.width();
            currentLine += gWidth / (float)this.scaleMul;
         }
      }

      return Math.max(currentLine, maxPreviousLines);
   }

   public float getStringHeight(String text) {
      char[] c = stripControlCodes(text).toCharArray();
      if (c.length == 0) {
         c = new char[]{' '};
      }

      float currentLine = 0.0F;
      float previous = 0.0F;

      for (char c1 : c) {
         if (c1 == '\n') {
            if (currentLine == 0.0F) {
               currentLine = (float)this.locateGlyph1(' ').height() / (float)this.scaleMul;
            }

            previous += currentLine;
            currentLine = 0.0F;
         } else {
            Glyph glyph = this.locateGlyph1(c1);
            currentLine = Math.max((float)glyph.height() / (float)this.scaleMul, currentLine);
         }
      }

      return currentLine + previous;
   }

   public void close() {
      ObjectListIterator var1 = this.maps.iterator();

      while (var1.hasNext()) {
         GlyphMap map = (GlyphMap)var1.next();
         map.destroy();
      }

      this.maps.clear();
      this.allGlyphs.clear();
   }

   static {
      Char2IntArrayMap var10000 = new Char2IntArrayMap();
      var10000.put('0', 0);
      var10000.put('1', 170);
      var10000.put('2', 43520);
      var10000.put('3', 43690);
      var10000.put('4', 11141120);
      var10000.put('5', 11141290);
      var10000.put('6', 16755200);
      var10000.put('7', 11184810);
      var10000.put('8', 5592405);
      var10000.put('9', 5592575);
      var10000.put('A', 5635925);
      var10000.put('B', 5636095);
      var10000.put('C', 16733525);
      var10000.put('D', 16733695);
      var10000.put('E', 16777045);
      var10000.put('F', 16777215);
      colorCodes = var10000;
   }
}
