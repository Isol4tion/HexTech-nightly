package me.hextech.remapped;

import net.minecraft.client.util.math.MatrixStack;

public interface FontAdapter {
    public void drawString(MatrixStack var1, String var2, float var3, float var4, int var5);

    public void drawString(MatrixStack var1, String var2, double var3, double var5, int var7);

    public void drawString(MatrixStack var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8);

    public void drawGradientString(MatrixStack var1, String var2, float var3, float var4, int var5, boolean var6);

    public void drawCenteredString(MatrixStack var1, String var2, double var3, double var5, int var7);

    public void drawCenteredString(MatrixStack var1, String var2, double var3, double var5, float var7, float var8, float var9, float var10);

    public float getWidth(String var1);

    public float getFontHeight();

    public float getFontHeight(String var1);

    public float getMarginHeight();

    public void drawString(MatrixStack var1, String var2, float var3, float var4, int var5, boolean var6);

    public void drawString(MatrixStack var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8, boolean var9);

    public String trimStringToWidth(String var1, double var2);

    public String trimStringToWidth(String var1, double var2, boolean var4);

    public float getStringWidth(String var1);
}
