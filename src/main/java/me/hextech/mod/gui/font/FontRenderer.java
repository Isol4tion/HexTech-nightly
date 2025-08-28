package me.hextech.mod.gui.font;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import me.hextech.HexTech;
import me.hextech.api.utils.Wrapper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.Closeable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FontRenderer
implements Closeable {
    private static final Char2IntArrayMap colorCodes;
    private static final int BLOCK_SIZE  = 256;
    private static final Object2ObjectArrayMap<Identifier, ObjectList<_ZitfNZXjZiLiXZJDgFqm>> GLYPH_PAGE_CACHE;
    private static final char RND_START = 'a';
    private static final char RND_END = 'z';
    private static final Random RND;
    private final float originalSize;
    private final ObjectList<GlyphMap> maps = new ObjectArrayList();
    private final Char2ObjectArrayMap<Glyph> allGlyphs = new Char2ObjectArrayMap();
    private int scaleMul = 0;
    private Font[] fonts;
    private int previousGameScale = -1;

    public FontRenderer(Font @NotNull [] fonts, float sizePx) {
        Preconditions.checkArgument((fonts.length > 0 ? 1 : 0) != 0, "fonts.length == 0");
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
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == '\u00a7') {
                ++i;
                continue;
            }
            f.append(c);
        }
        return f.toString();
    }

    public static int getGuiScale() {
        return (int)Wrapper.mc.getWindow().getScaleFactor();
    }

    @Contract(value="_ -> new", pure=true)
    public static int @NotNull [] RGBIntToRGB(int in) {
        int red = in >> 16 & 0xFF;
        int green = in >> 8 & 0xFF;
        int blue = in & 0xFF;
        return new int[]{red, green, blue};
    }

    @Contract(value="-> new", pure=true)
    @NotNull
    public static Identifier randomIdentifier() {
        return new Identifier("hextech", "temp/" + FontRenderer.randomString(32));
    }

    private static String randomString(int length) {
        return IntStream.range(0, length).mapToObj(operand -> String.valueOf((char)RND.nextInt(97, 123))).collect(Collectors.joining());
    }

    private void sizeCheck() {
        int gs = FontRenderer.getGuiScale();
        if (gs != this.previousGameScale) {
            this.close();
            this.init(this.fonts, this.originalSize);
        }
    }

    private void init(Font @NotNull [] fonts, float sizePx) {
        this.scaleMul = this.previousGameScale = FontRenderer.getGuiScale();
        this.fonts = new Font[fonts.length];
        for (int i = 0; i < fonts.length; ++i) {
            this.fonts[i] = fonts[i].deriveFont(sizePx * (float)this.scaleMul);
        }
    }

    @NotNull
    private GlyphMap generateMap(char from, char to) {
        GlyphMap gm = new GlyphMap(from, to, this.fonts, FontRenderer.randomIdentifier());
        this.maps.add(gm);
        return gm;
    }

    private Glyph locateGlyph0(char glyph) {
        for (GlyphMap map : this.maps) {
            if (!map.contains(glyph)) continue;
            return map.getGlyph(glyph);
        }
        int base = FontRenderer.floorNearestMulN(glyph);
        GlyphMap glyphMap = this.generateMap((char)base, (char)(base + 256));
        return glyphMap.getGlyph(glyph);
    }

    private Glyph locateGlyph1(char glyph) {
        return this.allGlyphs.computeIfAbsent(glyph, this::locateGlyph0);
    }

    public void drawString(@NotNull MatrixStack stack, @NotNull String s, float x, float y, float r, float g, float b, float a) {
        this.drawString(stack, s, x + 1.0f, y + 1.0f, 0.0f, 0.0f, 0.0f, a, true);
        this.drawString(stack, s, x, y, r, g, b, a, false);
    }

    public void drawString(@NotNull MatrixStack stack, @NotNull String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
        this.sizeCheck();
        float r2 = r;
        float g2 = g;
        float b2 = b;
        stack.push();
        stack.translate(x, y, 0.0f);
        stack.scale(1.0f / (float)this.scaleMul, 1.0f / (float)this.scaleMul, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        Matrix4f mat = stack.peek().getPositionMatrix();
        char[] chars = s.toCharArray();
        float xOffset = 0.0f;
        float yOffset = 0.0f;
        boolean inSel = false;
        int lineStart = 0;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (inSel) {
                inSel = false;
                char c1 = Character.toUpperCase(c);
                if (colorCodes.containsKey(c1) && !shadow) {
                    int ii = colorCodes.get(c1);
                    int[] col = FontRenderer.RGBIntToRGB(ii);
                    r2 = (float)col[0] / 255.0f;
                    g2 = (float)col[1] / 255.0f;
                    b2 = (float)col[2] / 255.0f;
                    continue;
                }
                if (c1 != 'R') continue;
                r2 = r;
                g2 = g;
                b2 = b;
                continue;
            }
            if (c == '\u00a7') {
                inSel = true;
                continue;
            }
            if (c == '\n') {
                yOffset += this.getStringHeight(s.substring(lineStart, i)) * (float)this.scaleMul;
                xOffset = 0.0f;
                lineStart = i + 1;
                continue;
            }
            Glyph glyph = this.locateGlyph1(c);
            if (glyph.value() != ' ') {
                Identifier i1 = glyph.owner().bindToTexture;
                _ZitfNZXjZiLiXZJDgFqm entry = new _ZitfNZXjZiLiXZJDgFqm(xOffset, yOffset, r2, g2, b2, glyph);
                GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList()).add(entry);
            }
            xOffset += (float)glyph.width();
        }
        for (Identifier identifier : GLYPH_PAGE_CACHE.keySet()) {
            RenderSystem.setShaderTexture(0, identifier);
            List<_ZitfNZXjZiLiXZJDgFqm> objects = GLYPH_PAGE_CACHE.get(identifier);
            bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            for (_ZitfNZXjZiLiXZJDgFqm object : objects) {
                float xo = object.atX;
                float yo = object.atY;
                float cr = object.r;
                float cg = object.g;
                float cb = object.b;
                Glyph glyph = object.toDraw;
                GlyphMap owner = glyph.owner();
                float w = glyph.width();
                float h = glyph.height();
                float u1 = (float)glyph.u() / (float)owner.width;
                float v1 = (float)glyph.v() / (float)owner.height;
                float u2 = (float)(glyph.u() + glyph.width()) / (float)owner.width;
                float v2 = (float)(glyph.v() + glyph.height()) / (float)owner.height;
                bb.vertex(mat, xo + 0.0f, yo + h, 0.0f).texture(u1, v2).color(cr, cg, cb, a).next();
                bb.vertex(mat, xo + w, yo + h, 0.0f).texture(u2, v2).color(cr, cg, cb, a).next();
                bb.vertex(mat, xo + w, yo + 0.0f, 0.0f).texture(u2, v1).color(cr, cg, cb, a).next();
                bb.vertex(mat, xo + 0.0f, yo + 0.0f, 0.0f).texture(u1, v1).color(cr, cg, cb, a).next();
            }
            BufferRenderer.drawWithGlobalProgram(bb.end());
        }
        stack.pop();
        GLYPH_PAGE_CACHE.clear();
    }

    public void drawGradientString(@NotNull MatrixStack stack, @NotNull String s, float x, float y) {
        this.sizeCheck();
        stack.push();
        stack.translate(x, y, 0.0f);
        stack.scale(1.0f / (float)this.scaleMul, 1.0f / (float)this.scaleMul, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        Matrix4f mat = stack.peek().getPositionMatrix();
        char[] chars = s.toCharArray();
        float xOffset = 0.0f;
        float yOffset = 0.0f;
        int lineStart = 0;
        float a = 1.0f;
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            Color color = HexTech.GUI.getColor();
            a = (float)color.getAlpha() / 255.0f;
            if (c == '\n') {
                yOffset += this.getStringHeight(s.substring(lineStart, i)) * (float)this.scaleMul;
                xOffset = 0.0f;
                lineStart = i + 1;
                continue;
            }
            Glyph glyph = this.locateGlyph1(c);
            if (glyph.value() != ' ') {
                Identifier i1 = glyph.owner().bindToTexture;
                _ZitfNZXjZiLiXZJDgFqm entry = new _ZitfNZXjZiLiXZJDgFqm(xOffset, yOffset, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, glyph);
                (GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList())).add(entry);
            }
            xOffset += (float)glyph.width();
        }
        for (Identifier identifier : GLYPH_PAGE_CACHE.keySet()) {
            RenderSystem.setShaderTexture(0, identifier);
            List<_ZitfNZXjZiLiXZJDgFqm> objects = GLYPH_PAGE_CACHE.get(identifier);
            bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            for (_ZitfNZXjZiLiXZJDgFqm object : objects) {
                float xo = object.atX;
                float yo = object.atY;
                float cr = object.r;
                float cg = object.g;
                float cb = object.b;
                Glyph glyph = object.toDraw;
                GlyphMap owner = glyph.owner();
                float w = glyph.width();
                float h = glyph.height();
                float u1 = (float)glyph.u() / (float)owner.width;
                float v1 = (float)glyph.v() / (float)owner.height;
                float u2 = (float)(glyph.u() + glyph.width()) / (float)owner.width;
                float v2 = (float)(glyph.v() + glyph.height()) / (float)owner.height;
                bb.vertex(mat, xo + 0.0f, yo + h, 0.0f).texture(u1, v2).color(cr, cg, cb, a).next();
                bb.vertex(mat, xo + w, yo + h, 0.0f).texture(u2, v2).color(cr, cg, cb, a).next();
                bb.vertex(mat, xo + w, yo + 0.0f, 0.0f).texture(u2, v1).color(cr, cg, cb, a).next();
                bb.vertex(mat, xo + 0.0f, yo + 0.0f, 0.0f).texture(u1, v1).color(cr, cg, cb, a).next();
            }
            BufferRenderer.drawWithGlobalProgram(bb.end());
        }
        stack.pop();
        GLYPH_PAGE_CACHE.clear();
    }

    public void drawCenteredString(MatrixStack stack, String s, float x, float y, float r, float g, float b, float a) {
        this.drawString(stack, s, x - this.getStringWidth(s) / 2.0f, y, r, g, b, a);
    }

    public float getStringWidth(String text) {
        char[] c = FontRenderer.stripControlCodes(text).toCharArray();
        float currentLine = 0.0f;
        float maxPreviousLines = 0.0f;
        for (char c1 : c) {
            if (c1 == '\n') {
                maxPreviousLines = Math.max(currentLine, maxPreviousLines);
                currentLine = 0.0f;
                continue;
            }
            Glyph glyph = this.locateGlyph1(c1);
            float gWidth = glyph == null ? 1.0f : (float)glyph.width();
            currentLine += gWidth / (float)this.scaleMul;
        }
        return Math.max(currentLine, maxPreviousLines);
    }

    public float getStringHeight(String text) {
        char[] c = FontRenderer.stripControlCodes(text).toCharArray();
        if (c.length == 0) {
            c = new char[]{' '};
        }
        float currentLine = 0.0f;
        float previous = 0.0f;
        for (char c1 : c) {
            if (c1 == '\n') {
                if (currentLine == 0.0f) {
                    currentLine = (float)this.locateGlyph1(' ').height() / (float)this.scaleMul;
                }
                previous += currentLine;
                currentLine = 0.0f;
                continue;
            }
            Glyph glyph = this.locateGlyph1(c1);
            currentLine = Math.max((float)glyph.height() / (float)this.scaleMul, currentLine);
        }
        return currentLine + previous;
    }

    @Override
    public void close() {
        for (GlyphMap map : this.maps) {
            map.destroy();
        }
        this.maps.clear();
        this.allGlyphs.clear();
    }

    static {
        Char2IntArrayMap char2IntArrayMap = new Char2IntArrayMap();
        char2IntArrayMap.put('0', 0);
        char2IntArrayMap.put('1', 170);
        char2IntArrayMap.put('2', 43520);
        char2IntArrayMap.put('3', 43690);
        char2IntArrayMap.put('4', 0xAA0000);
        char2IntArrayMap.put('5', 0xAA00AA);
        char2IntArrayMap.put('6', 0xFFAA00);
        char2IntArrayMap.put('7', 0xAAAAAA);
        char2IntArrayMap.put('8', 0x555555);
        char2IntArrayMap.put('9', 0x5555FF);
        char2IntArrayMap.put('A', 0x55FF55);
        char2IntArrayMap.put('B', 0x55FFFF);
        char2IntArrayMap.put('C', 0xFF5555);
        char2IntArrayMap.put('D', 0xFF55FF);
        char2IntArrayMap.put('E', 0xFFFF55);
        char2IntArrayMap.put('F', 0xFFFFFF);
        colorCodes = char2IntArrayMap;
        GLYPH_PAGE_CACHE = new Object2ObjectArrayMap();
        RND = new Random();
    }

    record _ZitfNZXjZiLiXZJDgFqm(float atX, float atY, float r, float g, float b, Glyph toDraw) {
    }
}
