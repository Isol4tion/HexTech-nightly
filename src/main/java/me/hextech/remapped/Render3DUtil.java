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
        Camera camera = Render3DUtil.mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);
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
        Render3DUtil.drawText3D(Text.of((String)text), vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.9, color.getRGB());
    }

    public static void drawText3D(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.of((String)text), vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 1.0, color);
    }

    public static void drawText3DMine(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.of((String)text), vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, SpeedMine.INSTANCE.textscale.getValueFloat(), color);
    }

    public static void drawText3DSlient(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.of((String)text), vec3d.x, vec3d.y, vec3d.z, 0.0, SpeedMine.INSTANCE.doubletext.getValueFloat(), SpeedMine.INSTANCE.textscale.getValueFloat(), color);
    }

    public static void drawText3DBreak(String text, Vec3d vec3d, int color) {
        Render3DUtil.drawText3D(Text.of((String)text), vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, BreakESP.INSTANCE.namescale.getValueFloat(), color);
    }

    public static void drawText3DBreakMine(Text text, Vec3d vec3d, double offX, double offY, double scale, Color color) {
        Render3DUtil.drawText3D(text, vec3d.x, vec3d.y, vec3d.z, offX, offY, scale, color.getRGB());
    }

    public static void drawText3D(Text text, Vec3d vec3d, double offX, double offY, double scale, Color color) {
        Render3DUtil.drawText3D(text, vec3d.x, vec3d.y, vec3d.z, offX, offY, scale, color.getRGB());
    }

    public static void drawText3D(Text text, double x, double y, double z, double offX, double offY, double scale, int color) {
        GL11.glDisable((int)2929);
        MatrixStack matrices = Render3DUtil.matrixFrom(x, y, z);
        Camera camera = Render3DUtil.mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.translate(offX, offY, 0.0);
        matrices.scale(-0.025f * (float)scale, -0.025f * (float)scale, 1.0f);
        int halfWidth = Render3DUtil.mc.textRenderer.getWidth((StringVisitable)text) / 2;
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.method_22991((BufferBuilder)Tessellator.getInstance().getBuffer());
        matrices.push();
        matrices.translate(1.0f, 1.0f, 0.0f);
        Render3DUtil.mc.textRenderer.method_30882(Text.of((String)text.getString().replaceAll("\u00a7[a-zA-Z0-9]", "")), (float)(-halfWidth), 0.0f, 0x202020, false, matrices.peek().getPositionMatrix(), (VertexConsumerProvider)immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        immediate.draw();
        matrices.pop();
        Render3DUtil.mc.textRenderer.method_30882((Text)text.copy(), (float)(-halfWidth), 0.0f, color, false, matrices.peek().getPositionMatrix(), (VertexConsumerProvider)immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        immediate.draw();
        RenderSystem.disableBlend();
        GL11.glEnable((int)2929);
    }

    public static void drawTextIn3D(String text, Vec3d pos, double offX, double offY, double textOffset, Color color) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = Render3DUtil.mc.gameRenderer.getCamera();
        RenderSystem.disableCull();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        matrices.translate(pos.getX() - camera.getPos().x, pos.getY() - camera.getPos().y, pos.getZ() - camera.getPos().z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.translate(offX, offY - 0.1, -0.01);
        matrices.scale(-0.025f, -0.025f, 0.0f);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.method_22991((BufferBuilder)Tessellator.getInstance().getBuffer());
        FontRenderers.Arial.drawCenteredString(matrices, text, textOffset, 0.0, color.getRGB());
        immediate.draw();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void drawFadeFill(MatrixStack stack, Box box, Color c, Color c1) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f posMatrix = stack.peek().getPositionMatrix();
        float minX = (float)(box.minX - Render3DUtil.mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float)(box.minY - Render3DUtil.mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float)(box.minZ - Render3DUtil.mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float)(box.maxX - Render3DUtil.mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float)(box.maxY - Render3DUtil.mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float)(box.maxZ - Render3DUtil.mc.getEntityRenderDispatcher().camera.getPos().getZ());
        buffer.vertex(posMatrix, minX, minY, minZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, maxX, minY, minZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, maxX, minY, maxZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, minX, minY, maxZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, minX, minY, minZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, minX, maxY, minZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, maxX, maxY, minZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, maxX, minY, minZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, maxX, minY, minZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, maxX, maxY, minZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, maxX, maxY, maxZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, maxX, minY, maxZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, minX, minY, maxZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, maxX, minY, maxZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, maxX, maxY, maxZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, minX, maxY, maxZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, minX, minY, minZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, minX, minY, maxZ).color(c.getRGB()).next();
        buffer.vertex(posMatrix, minX, maxY, maxZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, minX, maxY, minZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, minX, maxY, minZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, minX, maxY, maxZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, maxX, maxY, maxZ).color(c1.getRGB()).next();
        buffer.vertex(posMatrix, maxX, maxY, minZ).color(c1.getRGB()).next();
        RenderSystem.disableCull();
        tessellator.draw();
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
        matrixStack.push();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
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
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.minY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == Direction.UP) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.maxY + height;
            y2 = bb.maxY + height;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == Direction.EAST) {
            x1 = bb.maxX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY + height;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == Direction.WEST) {
            x1 = bb.minX;
            x2 = bb.minX;
            y1 = bb.minY;
            y2 = bb.maxY + height;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        } else if (face == Direction.SOUTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY + height;
            z1 = bb.maxZ;
            z2 = bb.maxZ;
        } else if (face == Direction.NORTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY + height;
            z1 = bb.minZ;
            z2 = bb.minZ;
        }
        if (face == Direction.EAST || face == Direction.WEST || face == Direction.NORTH || face == Direction.SOUTH) {
            Render3DUtil.buildPosColor(matrix, bufferBuilder, red, green, blue, alpha, red2, green2, blue2, alpha2, x1, y1, z1, x2, y2, z2);
        } else if (face == Direction.UP) {
            Render3DUtil.buildPosColor(matrix, bufferBuilder, red2, green2, blue2, alpha2, x1, y1, z1, x2, y2, z2);
        } else if (face == Direction.DOWN) {
            Render3DUtil.buildPosColor(matrix, bufferBuilder, red, green, blue, alpha, x1, y1, z1, x2, y2, z2);
        }
        tessellator.draw();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private static void buildPosColor(Matrix4f matrix, BufferBuilder builder, float red2, float green2, float blue2, float alpha2, double x1, double y1, double z1, double x2, double y2, double z2) {
        Render3DUtil.buildPosColor(matrix, builder, red2, green2, blue2, alpha2, red2, green2, blue2, alpha2, x1, y1, z1, x2, y2, z2);
    }

    private static void buildPosColor(Matrix4f matrix, BufferBuilder builder, float red, float green, float blue, float alpha, float red2, float green2, float blue2, float alpha2, double x1, double y1, double z1, double x2, double y2, double z2) {
        builder.vertex(matrix, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x1, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z1).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
        builder.vertex(matrix, (float)x2, (float)y2, (float)z2).color(red2, green2, blue2, alpha2).next();
    }

    public static void drawLine(Box b, Color color, float lineWidth) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        MatrixStack matrices = Render3DUtil.matrixFrom(b.minX, b.minY, b.minZ);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)lineWidth);
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Box box = b.offset(new Vec3d(b.minX, b.minY, b.minZ).negate());
        float x1 = (float)box.minX;
        float y1 = (float)box.minY;
        float z1 = (float)box.minZ;
        float x2 = (float)box.maxX;
        float y2 = (float)box.maxY;
        float z2 = (float)box.maxZ;
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
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawSphere(MatrixStack matrix, EndCrystalEntity entity, Float radius, Float height, Float lineWidth, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        double x = entity.lastRenderX + (entity.getX() - entity.lastRenderX) * (double)mc.getTickDelta();
        double y = entity.lastRenderY + (entity.getY() - entity.lastRenderY) * (double)mc.getTickDelta();
        double z = entity.lastRenderZ + (entity.getZ() - entity.lastRenderZ) * (double)mc.getTickDelta();
        double pix2 = Math.PI * 2;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth((float)lineWidth.floatValue());
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 180; ++i) {
            bufferBuilder.vertex(matrix.peek().getPositionMatrix(), (float)(x + (double)radius.floatValue() * Math.cos((double)i * pix2 / 45.0)), (float)(y + (double)height.floatValue()), (float)(z + (double)radius.floatValue() * Math.sin((double)i * pix2 / 45.0))).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        }
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color) {
        Render3DUtil.draw3DBox(matrixStack, box, color, true, true);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color, boolean outline, boolean fill) {
        box = box.offset(Render3DUtil.mc.gameRenderer.getCamera().getPos().negate());
        RenderSystem.enableBlend();
        GL11.glDisable((int)2929);
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (outline) {
            RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            RenderSystem.setShader(GameRenderer::getPositionProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
            tessellator.draw();
        }
        if (fill) {
            RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            RenderSystem.setShader(GameRenderer::getPositionProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
            tessellator.draw();
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
        box = box.offset(Render3DUtil.mc.gameRenderer.getCamera().getPos().negate());
        RenderSystem.enableBlend();
        GL11.glDisable((int)2929);
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (fill) {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.disableCull();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY + height, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY + height, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY + height, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY + height, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY + height, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY + height, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY + height, (float)box.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY + height, (float)box.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
            tessellator.draw();
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
        MatrixStack matrices = Render3DUtil.matrixFrom(b.minX, b.minY, b.minZ);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)lineWidth);
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Box box = b.offset(new Vec3d(b.minX, b.minY, b.minZ).negate());
        float x1 = (float)box.minX;
        float y1 = (float)box.minY;
        float z1 = (float)box.minZ;
        float x2 = (float)box.maxX;
        float z2 = (float)box.maxZ;
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z1, x2, y1, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z1, x2, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z2, x1, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z2, x1, y1, z1, color);
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color, float width) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        MatrixStack matrices = Render3DUtil.matrixFrom(x1, y1, z1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)width);
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, 0.0, 0.0, 0.0, (float)(x2 - x1), (float)(y2 - y1), (float)(z2 - z1), color);
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
    }

    public static void vertexLine(MatrixStack matrices, VertexConsumer buffer, double x1, double y1, double z1, double x2, double y2, double z2, Color lineColor) {
        Matrix4f model = matrices.peek().getPositionMatrix();
        Matrix3f normal = matrices.peek().getNormalMatrix();
        Vector3f normalVec = Render3DUtil.getNormal((float)x1, (float)y1, (float)z1, (float)x2, (float)y2, (float)z2);
        buffer.vertex(model, (float)x1, (float)y1, (float)z1).color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_23763(normal, normalVec.x(), normalVec.y(), normalVec.z()).next();
        buffer.vertex(model, (float)x2, (float)y2, (float)z2).color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_23763(normal, normalVec.x(), normalVec.y(), normalVec.z()).next();
    }

    public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = MathHelper.sqrt((float)(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal));
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    public static void drawholeLine(Box b, Color color, float lineWidth) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        MatrixStack matrices = Render3DUtil.matrixFrom(b.minX, b.minY, b.minZ);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::method_34535);
        RenderSystem.lineWidth((float)lineWidth);
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Box box = b.offset(new Vec3d(b.minX, b.minY, b.minZ).negate());
        float x1 = (float)box.minX;
        float y1 = (float)box.minY;
        float z1 = (float)box.minZ;
        float x2 = (float)box.maxX;
        float z2 = (float)box.maxZ;
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z1, x2, y1, z1, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z1, x2, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x2, y1, z2, x1, y1, z2, color);
        Render3DUtil.vertexLine(matrices, (VertexConsumer)buffer, x1, y1, z2, x1, y1, z1, color);
        tessellator.draw();
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
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ).next();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ).next();
        tessellator.draw();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
    }
}
