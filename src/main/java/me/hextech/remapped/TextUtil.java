package me.hextech.remapped;

import java.awt.Color;
import java.util.Objects;

import me.hextech.remapped.api.utils.Wrapper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class TextUtil
implements Wrapper {
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

    public static int drawStringPulse(DrawContext drawContext, OrderedText text, double x, double y, Color startColor, Color endColor, double speed, int counter) {
        char[] stringToCharArray = ChatSetting_qVnAbgCzNciNTevKRovy.chatMessage.get(text).getString().toCharArray();
        int index = 0;
        boolean color = false;
        String s = null;
        for (char c : stringToCharArray) {
            if (c == '\u00a7') {
                color = true;
                continue;
            }
            if (color) {
                s = c == 'r' ? null : "\u00a7" + c;
                color = false;
                continue;
            }
            ++index;
            if (s != null) {
                drawContext.drawTextWithShadow(TextUtil.mc.textRenderer, s + c, (int)x, (int)y, startColor.getRGB());
            } else {
                drawContext.drawTextWithShadow(TextUtil.mc.textRenderer, String.valueOf(c), (int)x, (int)y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB());
            }
            x += TextUtil.mc.textRenderer.getWidth(String.valueOf(c));
        }
        return (int)x;
    }

    public static void drawStringPulse(DrawContext drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter, boolean customFont) {
        char[] stringToCharArray = text.toCharArray();
        int index = 0;
        boolean color = false;
        String s = null;
        for (char c : stringToCharArray) {
            if (c == '\u00a7') {
                color = true;
                continue;
            }
            if (color) {
                s = c == 'r' ? null : "\u00a7" + c;
                color = false;
                continue;
            }
            ++index;
            if (s != null) {
                TextUtil.drawString(drawContext, s + c, x, y, startColor.getRGB(), customFont);
            } else {
                TextUtil.drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB(), customFont);
            }
            x += customFont ? (double)FontRenderers.Arial.getWidth(String.valueOf(c)) : (double)TextUtil.mc.textRenderer.getWidth(String.valueOf(c));
        }
    }

    public static boolean isCustomFont() {
        return ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.customFont.getValue() && FontRenderers.Arial != null;
    }

    public static float getWidth(String s) {
        return TextUtil.isCustomFont() ? FontRenderers.Arial.getWidth(s) : (float)TextUtil.mc.textRenderer.getWidth(s);
    }

    public static float getHeight() {
        float f;
        if (TextUtil.isCustomFont()) {
            f = FontRenderers.Arial.getFontHeight();
        } else {
            Objects.requireNonNull(TextUtil.mc.textRenderer);
            f = 9.0f;
        }
        return f;
    }

    public static void drawStringWithScale(DrawContext drawContext, String text, float x, float y, Color color, float scale) {
        TextUtil.drawStringWithScale(drawContext, text, x, y, color.getRGB(), scale);
    }

    public static void drawStringWithScale(DrawContext drawContext, String text, float x, float y, int color, float scale) {
        MatrixStack matrixStack = drawContext.getMatrices();
        if (scale != 1.0f) {
            matrixStack.push();
            matrixStack.scale(scale, scale, 1.0f);
            if (scale > 1.0f) {
                matrixStack.translate(-x / scale, -y / scale, 0.0f);
            } else {
                matrixStack.translate(x / scale / 2.0f, y / scale / 2.0f, 0.0f);
            }
        }
        TextUtil.drawString(drawContext, text, x, y, color);
        matrixStack.pop();
    }

    public static void drawString(DrawContext drawContext, String text, double x, double y, int color, boolean customFont) {
        if (customFont) {
            FontRenderers.Arial.drawString(drawContext.getMatrices(), text, (float)x, (float)y + 2.0f, color);
        } else {
            drawContext.drawText(TextUtil.mc.textRenderer, text, (int)x, (int)y, color, true);
        }
    }

    public static void drawString(DrawContext drawContext, String text, double x, double y, Color color) {
        TextUtil.drawString(drawContext, text, x, y, color.getRGB());
    }

    public static void drawString(DrawContext drawContext, String text, double x, double y, int color) {
        if (TextUtil.isCustomFont()) {
            FontRenderers.Arial.drawString(drawContext.getMatrices(), text, (float)x, (float)y + 2.0f, color);
        } else {
            drawContext.drawText(TextUtil.mc.textRenderer, text, (int)x, (int)y, color, true);
        }
    }

    public static Vec3d worldSpaceToScreenSpace(Vec3d pos) {
        Camera camera = TextUtil.mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(2978, viewport);
        Vector3f target = new Vector3f();
        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;
        Vector4f transformedCoordinates = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0f).mul(lastWorldSpaceMatrix);
        Matrix4f matrixProj = new Matrix4f(lastProjMat);
        Matrix4f matrixModel = new Matrix4f(lastModMat);
        matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
        return new Vec3d((double)target.x / mc.getWindow().getScaleFactor(), (double)((float)displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }

    public static void drawText(DrawContext context, String text, Vec3d vector) {
        TextUtil.drawText(context, text, vector, -1);
    }

    public static void drawText(DrawContext context, String text, Vec3d vector, int color) {
        Vec3d preVec = vector;
        vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.x, vector.y, vector.z));
        if (vector.z > 0.0 && vector.z < 1.0) {
            double posX = vector.x;
            double posY = vector.y;
            double endPosX = Math.max(vector.x, vector.z);
            float scale = (float)Math.max(1.0 - EntityUtil.getEyesPos().distanceTo(preVec) * 0.025, 0.0);
            float diff = (float)(endPosX - posX) / 2.0f;
            float textWidth = (float)TextUtil.mc.textRenderer.getWidth(text) * scale;
            float tagX = (float)((posX + (double) diff - (double) (textWidth / 2.0f)));
            context.getMatrices().push();
            context.getMatrices().scale(scale, scale, scale);
            TextRenderer textRenderer = TextUtil.mc.textRenderer;
            int n = (int)(tagX / scale);
            Objects.requireNonNull(TextUtil.mc.textRenderer);
            context.drawText(textRenderer, text, n, (int)((posY - 11.0 + 9.0 * 1.2) / (double)scale), color, true);
            context.getMatrices().pop();
        }
    }
}
