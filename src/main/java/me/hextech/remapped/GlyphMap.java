package me.hextech.remapped;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import me.hextech.remapped.Glyph;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;

class GlyphMap {
    private static final int PADDING;
    final char fromIncl;
    final char toExcl;
    final Font[] font;
    final Identifier bindToTexture;
    private final Char2ObjectArrayMap<Glyph> glyphs = new Char2ObjectArrayMap();
    int width;
    int height;
    boolean generated = false;

    public GlyphMap(char from, char to, Font[] fonts, Identifier identifier) {
        this.fromIncl = from;
        this.toExcl = to;
        this.font = fonts;
        this.bindToTexture = identifier;
    }

    public static void registerBufferedImageTexture(Identifier i, BufferedImage bi) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)bi, "png", out);
            byte[] bytes = out.toByteArray();
            ByteBuffer data = BufferUtils.createByteBuffer((int)bytes.length).put(bytes);
            data.flip();
            NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.method_4324((ByteBuffer)data));
            Wrapper.mc.execute(() -> Wrapper.mc.method_1531().method_4616(i, (AbstractTexture)tex));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Glyph getGlyph(char c) {
        if (!this.generated) {
            this.generate();
        }
        return this.glyphs.get(c);
    }

    public void destroy() {
        MinecraftClient.method_1551().method_1531().method_4615(this.bindToTexture);
        this.glyphs.clear();
        this.width = -1;
        this.height = -1;
        this.generated = false;
    }

    public boolean contains(char c) {
        return c >= this.fromIncl && c < this.toExcl;
    }

    private Font getFontForGlyph(char c) {
        for (Font font1 : this.font) {
            if (!font1.canDisplay(c)) continue;
            return font1;
        }
        return this.font[0];
    }

    public void generate() {
        if (this.generated) {
            return;
        }
        int range = this.toExcl - this.fromIncl - 1;
        int charsVert = (int)(Math.ceil(Math.sqrt(range)) * 1.5);
        this.glyphs.clear();
        int generatedChars = 0;
        int charNX = 0;
        int maxX = 0;
        int maxY = 0;
        int currentX = 0;
        int currentY = 0;
        int currentRowMaxY = 0;
        ArrayList<Glyph> glyphs1 = new ArrayList<Glyph>();
        AffineTransform af = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(af, true, true);
        while (generatedChars <= range) {
            char currentChar = (char)(this.fromIncl + generatedChars);
            Font font = this.getFontForGlyph(currentChar);
            Rectangle2D stringBounds = font.getStringBounds(String.valueOf(currentChar), frc);
            int width = (int)Math.ceil(stringBounds.getWidth());
            int height = (int)Math.ceil(stringBounds.getHeight());
            ++generatedChars;
            maxX = Math.max(maxX, currentX + width);
            maxY = Math.max(maxY, currentY + height);
            if (charNX >= charsVert) {
                currentX = 0;
                currentY += currentRowMaxY + 5;
                charNX = 0;
                currentRowMaxY = 0;
            }
            currentRowMaxY = Math.max(currentRowMaxY, height);
            glyphs1.add(new Glyph(currentX, currentY, width, height, currentChar, this));
            currentX += width + 5;
            ++charNX;
        }
        BufferedImage bi = new BufferedImage(Math.max(maxX + 5, 1), Math.max(maxY + 5, 1), 2);
        this.width = bi.getWidth();
        this.height = bi.getHeight();
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, this.width, this.height);
        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for (Glyph glyph : glyphs1) {
            g2d.setFont(this.getFontForGlyph(glyph.value()));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            g2d.drawString(String.valueOf(glyph.value()), glyph.u(), glyph.v() + fontMetrics.getAscent());
            this.glyphs.put(glyph.value(), glyph);
        }
        GlyphMap.registerBufferedImageTexture(this.bindToTexture, bi);
        this.generated = true;
    }
}
