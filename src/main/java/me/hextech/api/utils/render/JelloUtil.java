package me.hextech.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hextech.api.utils.Wrapper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import java.awt.*;

public class JelloUtil
        implements Wrapper {
    private static float prevCircleStep;
    private static float circleStep;

    public static void drawJello(MatrixStack matrix, Entity target, Color color) {
        double cs = prevCircleStep + (circleStep - prevCircleStep) * mc.getTickDelta();
        double prevSinAnim = JelloUtil.absSinAnimation(cs - (double) 0.45f);
        double sinAnim = JelloUtil.absSinAnimation(cs);
        double x = target.prevX + (target.getX() - target.prevX) * (double) mc.getTickDelta() - JelloUtil.mc.getEntityRenderDispatcher().camera.getPos().getX();
        double y = target.prevY + (target.getY() - target.prevY) * (double) mc.getTickDelta() - JelloUtil.mc.getEntityRenderDispatcher().camera.getPos().getY() + prevSinAnim * (double) target.getHeight();
        double z = target.prevZ + (target.getZ() - target.prevZ) * (double) mc.getTickDelta() - JelloUtil.mc.getEntityRenderDispatcher().camera.getPos().getZ();
        double nextY = target.prevY + (target.getY() - target.prevY) * (double) mc.getTickDelta() - JelloUtil.mc.getEntityRenderDispatcher().camera.getPos().getY() + sinAnim * (double) target.getHeight();
        matrix.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        for (int i = 0; i <= 30; ++i) {
            float cos = (float) (x + Math.cos((double) i * 6.28 / 30.0) * (target.getBoundingBox().maxX - target.getBoundingBox().minX + (target.getBoundingBox().maxZ - target.getBoundingBox().minZ)) * 0.5);
            float sin = (float) (z + Math.sin((double) i * 6.28 / 30.0) * (target.getBoundingBox().maxX - target.getBoundingBox().minX + (target.getBoundingBox().maxZ - target.getBoundingBox().minZ)) * 0.5);
            bufferBuilder.vertex(matrix.peek().getPositionMatrix(), cos, (float) nextY, sin).color(color.getRGB()).next();
            bufferBuilder.vertex(matrix.peek().getPositionMatrix(), cos, (float) y, sin).color(ColorUtil.injectAlpha(color, 0).getRGB()).next();
        }
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrix.pop();
    }

    public static void updateJello() {
        prevCircleStep = circleStep;
        circleStep += 0.15f;
    }

    private static double absSinAnimation(double input) {
        return Math.abs(1.0 + Math.sin(input)) / 2.0;
    }
}
