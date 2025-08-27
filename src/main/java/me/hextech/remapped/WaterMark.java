package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import me.hextech.HexTech;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.FontAdapter;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.RendererFontAdapter;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class WaterMark
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final BooleanSetting lowercase = this.add(new BooleanSetting("LowerCase", false));
    private final SliderSetting offset = this.add(new SliderSetting("Offset", 2, 0, 20));
    private final StringSetting text = this.add(new StringSetting("Text", "\u029c\u1d07\u04fc\u1d1b\u1d07\u1d04\u029c"));
    private final BooleanSetting version = this.add(new BooleanSetting("Version", true));
    private final BooleanSetting shortVersion = this.add(new BooleanSetting("Short", false));
    private final BooleanSetting fps = this.add(new BooleanSetting("FPS", true));
    private final BooleanSetting time = this.add(new BooleanSetting("Time", true));
    private final BooleanSetting background = this.add(new BooleanSetting("Background", true));
    private final BooleanSetting customFont = this.add(new BooleanSetting("CustomFont", true));
    private final SliderSetting fontSize = this.add(new SliderSetting("FontSize", 18, 10, 30));
    private final SliderSetting round = this.add(new SliderSetting("Round", 5, 0, 10));
    private final ColorSetting headerColor = this.add(new ColorSetting("Header", new Color(135, 135, 135, 255)));
    private final ColorSetting bodyColor = this.add(new ColorSetting("Body", new Color(0, 0, 0, 120)));
    private final ColorSetting backgroundColor = this.add(new ColorSetting("Background", new Color(0, 0, 0, 40)));
    private FontAdapter lexendFont;
    private float currentFontSize = -1.0f;

    public WaterMark() {
        super("WaterMark", Module_JlagirAibYQgkHtbRnhw.Client);
        this.setDescription("Renders a watermark on the screen");
        this.initializeFont();
    }

    private void initializeFont() {
        try {
            this.lexendFont = this.createFontAdapter(this.fontSize.getValueFloat(), "lexenddeca-regular");
            this.currentFontSize = this.fontSize.getValueFloat();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FontAdapter createFontAdapter(float size, String name) throws Exception {
        return new RendererFontAdapter(Font.createFont(0, Objects.requireNonNull(WaterMark.class.getClassLoader().getResourceAsStream("assets/nullpoint/font/" + name + ".ttf"))).deriveFont(0, size), size);
    }

    @Override
    public void onRender2D(DrawContext drawContext, float tickDelta) {
        if (WaterMark.nullCheck()) {
            return;
        }
        if (this.customFont.getValue() && (this.lexendFont == null || this.currentFontSize != this.fontSize.getValueFloat())) {
            this.initializeFont();
        }
        StringBuilder watermarkText = new StringBuilder();
        watermarkText.append(this.text.getValue());
        watermarkText.append(" ");
        if (this.version.getValue()) {
            if (this.shortVersion.getValue()) {
                watermarkText.append("v").append("8");
            } else {
                watermarkText.append("8");
            }
            watermarkText.append(" ");
        }
        if (this.fps.getValue()) {
            String fpsString = String.valueOf(HexTech.FPS.getFps());
            watermarkText.append(fpsString).append(" FPS ");
        }
        if (this.time.getValue()) {
            watermarkText.append(FORMAT.format(new Date()));
        }
        String textToRender = this.lowercase.getValue() ? watermarkText.toString().toLowerCase() : watermarkText.toString();
        MatrixStack matrixStack = drawContext.getMatrices();
        float x = this.offset.getValueInt();
        float y = this.offset.getValueInt();
        if (this.background.getValue()) {
            int n;
            int textWidth;
            int n2 = textWidth = this.customFont.getValue() && this.lexendFont != null ? (int)this.lexendFont.getStringWidth(textToRender) : WaterMark.mc.textRenderer.getWidth(textToRender);
            if (this.customFont.getValue() && this.lexendFont != null) {
                n = (int)this.lexendFont.getFontHeight();
            } else {
                Objects.requireNonNull(WaterMark.mc.textRenderer);
                n = 9;
            }
            int textHeight = n;
            float bgWidth = textWidth + 175;
            float bgHeight = textHeight + 10;
            float headerHeight = 3.0f;
            Render2DUtil.drawRound(matrixStack, x, y, bgWidth, bgHeight, this.round.getValueFloat(), this.backgroundColor.getValue());
            if (this.round.getValueFloat() > 0.0f) {
                this.renderRoundedQuad(matrixStack, this.headerColor.getValue(), x, y, x + bgWidth, y + headerHeight + this.round.getValueFloat(), this.round.getValueFloat(), this.round.getValueFloat(), 0.0, 0.0, 32.0);
            } else {
                Render2DUtil.drawRound(matrixStack, x, y, bgWidth, headerHeight, this.round.getValueFloat(), this.headerColor.getValue());
            }
            Render2DUtil.drawRound(matrixStack, x, y + headerHeight, bgWidth, bgHeight - headerHeight, this.round.getValueFloat(), this.bodyColor.getValue());
            if (this.customFont.getValue() && this.lexendFont != null) {
                this.lexendFont.drawString(drawContext.getMatrices(), textToRender, x + 5.0f, y + (bgHeight - (float)textHeight) / 2.0f, -1);
            } else {
                drawContext.drawTextWithShadow(WaterMark.mc.textRenderer, textToRender, (int)(x + 5.0f), (int)(y + (bgHeight - (float)textHeight) / 2.0f), -1);
            }
        } else if (this.customFont.getValue() && this.lexendFont != null) {
            this.lexendFont.drawString(drawContext.getMatrices(), textToRender, x, y, -1);
        } else {
            drawContext.drawTextWithShadow(WaterMark.mc.textRenderer, textToRender, this.offset.getValueInt(), this.offset.getValueInt(), -1);
        }
    }

    private void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double radC1, double radC2, double radC3, double radC4, double samples) {
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        Render2DUtil.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        this.renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, radC1, radC2, radC3, radC4, samples);
        Render2DUtil.endRender();
    }

    private void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radC1, double radC2, double radC3, double radC4, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        double[][] map = new double[][]{{toX - radC4, toY - radC4, radC4}, {toX - radC2, fromY + radC2, radC2}, {fromX + radC1, fromY + radC1, radC1}, {fromX + radC3, toY - radC3, radC3}};
        for (int i = 0; i < 4; ++i) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = (double)i * 90.0; r < 90.0 + (double)i * 90.0; r += 90.0 / samples) {
                float rad1 = (float)Math.toRadians(r);
                float sin = (float)(Math.sin(rad1) * rad);
                float cos = (float)(Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca).next();
            }
            float rad1 = (float)Math.toRadians(90.0 + (double)i * 90.0);
            float sin = (float)(Math.sin(rad1) * rad);
            float cos = (float)(Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).color(cr, cg, cb, ca).next();
        }
        BufferRenderer.drawWithGlobalProgram((BufferBuilder.BuiltBuffer)bufferBuilder.end());
    }
}
