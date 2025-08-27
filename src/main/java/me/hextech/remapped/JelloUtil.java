package me.hextech.remapped;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class JelloUtil
implements Wrapper {
    private static float prevCircleStep;
    private static float circleStep;

    public static void drawJello(MatrixStack matrix, Entity target, Color color) {
        double cs = prevCircleStep + (circleStep - prevCircleStep) * mc.method_1488();
        double prevSinAnim = JelloUtil.absSinAnimation(cs - (double)0.45f);
        double sinAnim = JelloUtil.absSinAnimation(cs);
        double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)mc.method_1488() - JelloUtil.mc.method_1561().field_4686.method_19326().method_10216();
        double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)mc.method_1488() - JelloUtil.mc.method_1561().field_4686.method_19326().method_10214() + prevSinAnim * (double)target.method_17682();
        double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)mc.method_1488() - JelloUtil.mc.method_1561().field_4686.method_19326().method_10215();
        double nextY = target.field_6036 + (target.method_23318() - target.field_6036) * (double)mc.method_1488() - JelloUtil.mc.method_1561().field_4686.method_19326().method_10214() + sinAnim * (double)target.method_17682();
        matrix.method_22903();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        Tessellator tessellator = Tessellator.method_1348();
        BufferBuilder bufferBuilder = tessellator.method_1349();
        RenderSystem.setShader(GameRenderer::method_34540);
        bufferBuilder.method_1328(VertexFormat.DrawMode.field_27380, VertexFormats.field_1576);
        for (int i = 0; i <= 30; ++i) {
            float cos = (float)(x + Math.cos((double)i * 6.28 / 30.0) * (target.method_5829().field_1320 - target.method_5829().field_1323 + (target.method_5829().field_1324 - target.method_5829().field_1321)) * 0.5);
            float sin = (float)(z + Math.sin((double)i * 6.28 / 30.0) * (target.method_5829().field_1320 - target.method_5829().field_1323 + (target.method_5829().field_1324 - target.method_5829().field_1321)) * 0.5);
            bufferBuilder.method_22918(matrix.method_23760().method_23761(), cos, (float)nextY, sin).method_39415(color.getRGB()).method_1344();
            bufferBuilder.method_22918(matrix.method_23760().method_23761(), cos, (float)y, sin).method_39415(ColorUtil.injectAlpha(color, 0).getRGB()).method_1344();
        }
        tessellator.method_1350();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrix.method_22909();
    }

    public static void updateJello() {
        prevCircleStep = circleStep;
        circleStep += 0.15f;
    }

    private static double absSinAnimation(double input) {
        return Math.abs(1.0 + Math.sin(input)) / 2.0;
    }
}
