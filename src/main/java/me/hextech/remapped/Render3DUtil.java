package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.BreakESP;
import me.hextech.remapped.FontRenderers;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class Render3DUtil
implements Wrapper {
    public static MatrixStack matrixFrom(double x, double y, double z) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = Render3DUtil.mc.field_1773.method_19418();
        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(x - camera.method_19326().field_1352, y - camera.method_19326().field_1351, z - camera.method_19326().field_1350);
        return matrices;
    }

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void endRender() {
        RenderSystem.disableBlend();
    }

    public static void drawText3D(String text, Vec3d vec3d, Color color) {
        Render3DUtil.drawText3D(Text.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, 0.0, 0.9, color.getRGB());
    }

    public static void drawText3D(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, 0.0, 1.0, color);
    }

    public static void drawText3DMine(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, 0.0, SpeedMine.INSTANCE.textscale.getValueFloat(), color);
    }

    public static void drawText3DSlient(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, SpeedMine.INSTANCE.doubletext.getValueFloat(), SpeedMine.INSTANCE.textscale.getValueFloat(), color);
    }

    public static void drawText3DBreak(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, 0.0, BreakESP.INSTANCE.namescale.getValueFloat(), color);
    }

    public static void drawText3DBreakMine(Text text, Vec3d vec3d, double offX, double offY, double scale, Color color) {
        Render3DUtil.drawText3D(text, vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, offX, offY, scale, color.getRGB());
    }

    public static void drawText3D(Text text, Vec3d vec3d, double offX, double offY, double scale, Color color) {
        Render3DUtil.drawText3D(text, vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, offX, offY, scale, color.getRGB());
    }

    public static void drawText3D(Text text, double x, double y, double z, double offX, double offY, double scale, int color) {
        GL11.glDisable((int)2929);
        MatrixStack matrices = Render3DUtil.matrixFrom(x, y, z);
        Camera camera = Render3DUtil.mc.field_1773.method_19418();
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees(-camera.method_19330()));
        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(camera.method_19329()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.method_22904(offX, offY, 0.0);
        matrices.method_22905(-0.025f * (float)scale, -0.025f * (float)scale, 1.0f);
        int halfWidth = Render3DUtil.mc.field_1772.method_27525((StringVisitable)text) / 2;
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.method_22991((BufferBuilder)Tessellator.method_1348().method_1349());
        matrices.method_22903();
        matrices.method_46416(1.0f, 1.0f, 0.0f);
        Render3DUtil.mc.field_1772.method_30882(Text.method_30163((String)text.getString().replaceAll("\u00a7[a-zA-Z0-9]", "")), (float)(-halfWidth), 0.0f, 0x202020, false, matrices.method_23760().method_23761(), (VertexConsumerProvider)immediate, TextRenderer.TextLayerType.field_33994, 0, 0xF000F0);
        immediate.method_22993();
        matrices.method_22909();
        Render3DUtil.mc.field_1772.method_30882((Text)text.method_27661(), (float)(-halfWidth), 0.0f, color, false, matrices.method_23760().method_23761(), (VertexConsumerProvider)immediate, TextRenderer.TextLayerType.field_33994, 0, 0xF000F0);
        immediate.method_22993();
        RenderSystem.disableBlend();
        GL11.glEnable((int)2929);
    }

    public static void drawTextIn3D(String text, Vec3d pos, double offX, double offY, double textOffset, Color color) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = Render3DUtil.mc.field_1773.method_19418();
        RenderSystem.disableCull();
        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(pos.method_10216() - camera.method_19326().field_1352, pos.method_10214() - camera.method_19326().field_1351, pos.method_10215() - camera.method_19326().field_1350);
        matrices.method_22907(RotationAxis.field_40716.rotationDegrees(-camera.method_19330()));
        matrices.method_22907(RotationAxis.field_40714.rotationDegrees(camera.method_19329()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.method_22904(offX, offY - 0.1, -0.01);
        matrices.method_22905(-0.025f, -0.025f, 0.0f);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.method_22991((BufferBuilder)Tessellator.method_1348().method_1349());
        FontRenderers.Arial.drawCenteredString(matrices, text, textOffset, 0.0, color.getRGB());
        immediate.method_22993();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void drawFadeFill(MatrixStack stack, Box box, Color c, Color c1) {
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::method_34540);
        buffer.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1576);
        Matrix4f posMatrix = stack.method_23760().method_23761();
        float minX = (float)(box.field_1323 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10216());
        float minY = (float)(box.field_1322 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10214());
        float minZ = (float)(box.field_1321 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10215());
        float maxX = (float)(box.field_1320 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10216());
        float maxY = (float)(box.field_1325 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10214());
        float maxZ = (float)(box.field_1324 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10215());
        buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB()).method_1344();
        buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB()).method_1344();
        RenderSystem.disableCull();
        tessellator.method_1350();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void drawFill(MatrixStack matrixStack, Box bb, Color color) {
        Render3DUtil.draw3DBox(matrixStack, bb, color, false, true);
    }

    public static void drawBox(MatrixStack matrixStack, Box bb, Color color) {
        Render3DUtil.draw3DBox(matrixStack, bb, color, true, false);
    }

    public static void drawFadingBox(MatrixStack matrixStack, Box pos, Color startColor, Color endColor, double height) {
        for (Direction face : Direction.values()) {
            if (face == Direction.UP) continue;
            Render3DUtil.drawFadingSide(matrixStack, pos, face, startColor, endColor, height);
        }
    }

    public static void drawFadingSide(MatrixStack matrixStack, Box bb, Direction face, Color startColor, Color endColor, double height) {
        matrixStack.method_22903();
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.setShader(GameRenderer::method_34540);
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1576);
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        if (face == Direction.DOWN) {
            x1 = bb.field_1323;
            x2 = bb.field_1320;
            y1 = bb.field_1322;
            y2 = bb.field_1322;
            z1 = bb.field_1321;
            z2 = bb.field_1324;
        } else if (face == Direction.UP) {
            x1 = bb.field_1323;
            x2 = bb.field_1320;
            y1 = bb.field_1325 + height;
            y2 = bb.field_1325 + height;
            z1 = bb.field_1321;
            z2 = bb.field_1324;
        } else if (face == Direction.field_11034) {
            x1 = bb.field_1320;
            x2 = bb.field_1320;
            y1 = bb.field_1322;
            y2 = bb.field_1325 + height;
            z1 = bb.field_1321;
            z2 = bb.field_1324;
        } else if (face == Direction.field_11039) {
            x1 = bb.field_1323;
            x2 = bb.field_1323;
            y1 = bb.field_1322;
            y2 = bb.field_1325 + height;
            z1 = bb.field_1321;
            z2 = bb.field_1324;
        } else if (face == Direction.field_11035) {
            x1 = bb.field_1323;
            x2 = bb.field_1320;
            y1 = bb.field_1322;
            y2 = bb.field_1325 + height;
            z1 = bb.field_1324;
            z2 = bb.field_1324;
        } else if (face == Direction.field_11043) {
            x1 = bb.field_1323;
            x2 = bb.field_1320;
            y1 = bb.field_1322;
            y2 = bb.field_1325 + height;
            z1 = bb.field_1321;
            z2 = bb.field_1321;
        }
        if (face == Direction.field_11034 || face == Direction.field_11039 || face == Direction.field_11043 || face == Direction.field_11035) {
            Render3DUtil.buildPosColor(matrix, bufferBuilder, red, green, blue, alpha, red2, green2, blue2, alpha2, x1, y1, z1, x2, y2, z2);
        } else if (face == Direction.UP) {
            Render3DUtil.buildPosColor(matrix, bufferBuilder, red2, green2, blue2, alpha2, x1, y1, z1, x2, y2, z2);
        } else if (face == Direction.DOWN) {
            Render3DUtil.buildPosColor(matrix, bufferBuilder, red, green, blue, alpha, x1, y1, z1, x2, y2, z2);
        }
        tessellator.method_1350();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrixStack.method_22909();
    }

    private static void buildPosColor(Matrix4f matrix, BufferBuilder builder, float red2, float green2, float blue2, float alpha2, double x1, double y1, double z1, double x2, double y2, double z2) {
        Render3DUtil.buildPosColor(matrix, builder, red2, green2, blue2, alpha2, red2, green2, blue2, alpha2, x1, y1, z1, x2, y2, z2);
    }

    private static void buildPosColor(Matrix4f matrix, BufferBuilder builder, float red, float green, float blue, float alpha, float red2, float green2, float blue2, float alpha2, double x1, double y1, double z1, double x2, double y2, double z2) {
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z1).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y1, (float)z2).method_22915(red, green, blue, alpha).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x1, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z1).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
        builder.method_22918(matrix, (float)x2, (float)y2, (float)z2).method_22915(red2, green2, blue2, alpha2).method_1344();
    }

    public static void drawLine(Box b, Color color, float lineWidth) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        MatrixStack matrices = Render3DUtil.matrixFrom(b.field_1323, b.field_1322, b.field_1321);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)lineWidth);
        buffer.method_1328(VertexFormat.DrawMode.field_27377, VertexFormats.field_29337);
        Box box = b.method_997(new Vec3d(b.field_1323, b.field_1322, b.field_1321).method_22882());
        float x1 = (float)box.field_1323;
        float y1 = (float)box.field_1322;
        float z1 = (float)box.field_1321;
        float x2 = (float)box.field_1320;
        float y2 = (float)box.field_1325;
        float z2 = (float)box.field_1324;
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z1, x2, y1, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z1, x2, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z2, x1, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z2, x1, y1, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z2, x1, y2, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z1, x1, y2, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z2, x2, y2, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z1, x2, y2, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y2, z1, x2, y2, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y2, z1, x2, y2, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y2, z2, x1, y2, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y2, z2, x1, y2, z1, color);
        tessellator.method_1350();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawSphere(MatrixStack matrix, EndCrystalEntity entity, Float radius, Float height, Float lineWidth, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        double x = entity.field_6038 + (entity.getX() - entity.field_6038) * (double)mc.getTickDelta();
        double y = entity.field_5971 + (entity.getY() - entity.field_5971) * (double)mc.getTickDelta();
        double z = entity.field_5989 + (entity.getZ() - entity.field_5989) * (double)mc.getTickDelta();
        double pix2 = Math.PI * 2;
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.setShader(GameRenderer::method_34540);
        RenderSystem.lineWidth((float)lineWidth.floatValue());
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_29345, VertexFormats.field_1576);
        for (int i = 0; i <= 180; ++i) {
            bufferBuilder.method_22918(matrix.method_23760().method_23761(), (float)(x + (double)radius.floatValue() * Math.cos((double)i * pix2 / 45.0)), (float)(y + (double)height.floatValue()), (float)(z + (double)radius.floatValue() * Math.sin((double)i * pix2 / 45.0))).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
        }
        tessellator.method_1350();
        RenderSystem.disableBlend();
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color) {
        Render3DUtil.draw3DBox(matrixStack, box, color, true, true);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color, boolean outline, boolean fill) {
        box = box.method_997(Render3DUtil.mc.field_1773.method_19418().method_19326().method_22882());
        RenderSystem.enableBlend();
        GL11.glDisable((int)2929);
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        if (outline) {
            RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            RenderSystem.setShader(GameRenderer::method_34539);
            bufferBuilder.method_1328(VertexFormat.DrawMode.field_29344, VertexFormats.field_1592);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
            tessellator.method_1350();
        }
        if (fill) {
            RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            RenderSystem.setShader(GameRenderer::method_34539);
            bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1592);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
            tessellator.method_1350();
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)2929);
        RenderSystem.disableBlend();
    }

    public static void drawHole(MatrixStack matrixStack, BlockPos pos, Color color, boolean outline, boolean fill, float height, float lineWidth) {
        if (outline) {
            Render3DUtil.drawHoleLine(new Box(pos), color, lineWidth);
        }
        Box box = new Box(pos);
        box = box.method_997(Render3DUtil.mc.field_1773.method_19418().method_19326().method_22882());
        RenderSystem.enableBlend();
        GL11.glDisable((int)2929);
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        if (fill) {
            RenderSystem.setShader(GameRenderer::method_34540);
            RenderSystem.disableCull();
            bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1576);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322 + height, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322 + height, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322 + height, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322 + height, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322 + height, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322 + height, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322 + height, (float)box.field_1324).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322 + height, (float)box.field_1321).method_1336(color.getRed(), color.getGreen(), color.getBlue(), 0).method_1344();
            tessellator.method_1350();
            RenderSystem.enableCull();
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)2929);
        RenderSystem.disableBlend();
    }

    public static void drawHoleLine(Box b, Color color, float lineWidth) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        MatrixStack matrices = Render3DUtil.matrixFrom(b.field_1323, b.field_1322, b.field_1321);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)lineWidth);
        buffer.method_1328(VertexFormat.DrawMode.field_27377, VertexFormats.field_29337);
        Box box = b.method_997(new Vec3d(b.field_1323, b.field_1322, b.field_1321).method_22882());
        float x1 = (float)box.field_1323;
        float y1 = (float)box.field_1322;
        float z1 = (float)box.field_1321;
        float x2 = (float)box.field_1320;
        float z2 = (float)box.field_1324;
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z1, x2, y1, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z1, x2, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z2, x1, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z2, x1, y1, z1, color);
        tessellator.method_1350();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color, float width) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        MatrixStack matrices = Render3DUtil.matrixFrom(x1, y1, z1);
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)width);
        buffer.method_1328(VertexFormat.DrawMode.field_27377, VertexFormats.field_29337);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, 0.0, 0.0, 0.0, (float)(x2 - x1), (float)(y2 - y1), (float)(z2 - z1), color);
        tessellator.method_1350();
        RenderSystem.enableCull();
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
    }

    public static void vertexLine(MatrixStack matrices, VertexConsumer buffer, double x1, double y1, double z1, double x2, double y2, double z2, Color lineColor) {
        Matrix4f model = matrices.method_23760().method_23761();
        Matrix3f normal = matrices.method_23760().method_23762();
        Vector3f normalVec = Render3DUtil.getNormal((float)x1, (float)y1, (float)z1, (float)x2, (float)y2, (float)z2);
        buffer.method_22918(model, (float)x1, (float)y1, (float)z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_23763(normal, normalVec.x(), normalVec.y(), normalVec.z()).method_1344();
        buffer.method_22918(model, (float)x2, (float)y2, (float)z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_23763(normal, normalVec.x(), normalVec.y(), normalVec.z()).method_1344();
    }

    public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = MathHelper.method_15355((float)(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal));
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    public static void drawholeLine(Box b, Color color, float lineWidth) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        MatrixStack matrices = Render3DUtil.matrixFrom(b.field_1323, b.field_1322, b.field_1321);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.method_1349();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)lineWidth);
        buffer.method_1328(VertexFormat.DrawMode.field_27377, VertexFormats.field_29337);
        Box box = b.method_997(new Vec3d(b.field_1323, b.field_1322, b.field_1321).method_22882());
        float x1 = (float)box.field_1323;
        float y1 = (float)box.field_1322;
        float z1 = (float)box.field_1321;
        float x2 = (float)box.field_1320;
        float z2 = (float)box.field_1324;
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z1, x2, y1, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z1, x2, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z2, x1, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z2, x1, y1, z1, color);
        tessellator.method_1350();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawHoleBox(MatrixStack matrixStack, Box box, Color color, double height, boolean fade, boolean invertFade, int alpha) {
        if (fade) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            Render3DUtil.drawFadingBox(matrixStack, box, invertFade ? endColor : color, invertFade ? color : endColor, height);
            return;
        }
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        RenderSystem.setShader(GameRenderer::method_34539);
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_27382, VertexFormats.field_1592);
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_1344();
        bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_1344();
        tessellator.method_1350();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
    }
}
