package me.hextech.remapped;

import java.awt.Font;
import me.hextech.remapped.FontAdapter;
import me.hextech.remapped.FontRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class RendererFontAdapter
implements FontAdapter {
    final FontRenderer fontRenderer;
    final float si;

    public RendererFontAdapter(Font fnt, float si) {
        this.fontRenderer = new FontRenderer(new Font[]{fnt}, si);
        this.si = si;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public float getSize() {
        return this.si;
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, int color) {
        int color1 = color;
        if ((color1 & 0xFC000000) == 0) {
            color1 |= 0xFF000000;
        }
        float a = (float)(color1 >> 24 & 0xFF) / 255.0f;
        float r = (float)(color1 >> 16 & 0xFF) / 255.0f;
        float g = (float)(color1 >> 8 & 0xFF) / 255.0f;
        float b = (float)(color1 & 0xFF) / 255.0f;
        this.drawString(matrices, text, x, y, r, g, b, a);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, double x, double y, int color) {
        this.drawString(matrices, text, (float)x, (float)y, color);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, float r, float g, float b, float a) {
        float v = (float)((int)(a * 255.0f)) / 255.0f;
        this.fontRenderer.drawString(matrices, text, x, y - 3.0f, r, g, b, v);
    }

    @Override
    public void drawGradientString(MatrixStack matrices, String s, float x, float y, int offset, boolean hud) {
        this.fontRenderer.drawGradientString(matrices, s, x, y - 3.0f);
    }

    @Override
    public void drawCenteredString(MatrixStack matrices, String text, double x, double y, int color) {
        int color1 = color;
        if ((color1 & 0xFC000000) == 0) {
            color1 |= 0xFF000000;
        }
        float a = (float)(color1 >> 24 & 0xFF) / 255.0f;
        float r = (float)(color1 >> 16 & 0xFF) / 255.0f;
        float g = (float)(color1 >> 8 & 0xFF) / 255.0f;
        float b = (float)(color1 & 0xFF) / 255.0f;
        this.drawCenteredString(matrices, text, x, y, r, g, b, a);
    }

    @Override
    public void drawCenteredString(MatrixStack matrices, String text, double x, double y, float r, float g, float b, float a) {
        this.fontRenderer.drawCenteredString(matrices, text, (float)x, (float)y, r, g, b, a);
    }

    @Override
    public float getWidth(String text) {
        return this.fontRenderer.getStringWidth(text);
    }

    @Override
    public float getFontHeight() {
        return this.fontRenderer.getStringHeight("abcdefg123");
    }

    @Override
    public float getFontHeight(String text) {
        return this.getFontHeight();
    }

    @Override
    public float getMarginHeight() {
        return this.getFontHeight();
    }

    @Override
    public void drawString(MatrixStack matrices, String s, float x, float y, int color, boolean dropShadow) {
        this.drawString(matrices, s, x, y, color);
    }

    @Override
    public void drawString(MatrixStack matrices, String s, float x, float y, float r, float g, float b, float a, boolean dropShadow) {
        this.drawString(matrices, s, x, y, r, g, b, a);
    }

    @Override
    public String trimStringToWidth(String in, double width) {
        StringBuilder sb = new StringBuilder();
        for (char c : in.toCharArray()) {
            if ((double)this.getWidth(sb.toString() + c) >= width) {
                return sb.toString();
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public String trimStringToWidth(String in, double width, boolean reverse) {
        return this.trimStringToWidth(in, width);
    }

    @Override
    public float getStringWidth(String text) {
        return 0.0f;
    }
}
