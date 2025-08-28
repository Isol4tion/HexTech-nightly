package me.hextech.remapped.mod.gui.font;

import net.minecraft.client.util.math.MatrixStack;

public interface FontAdapter {
    void drawString(MatrixStack var1, String var2, float var3, float var4, int var5);

    void drawString(MatrixStack var1, String var2, double var3, double var5, int var7);

    void drawString(MatrixStack var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8);

    void drawGradientString(MatrixStack var1, String var2, float var3, float var4, int var5, boolean var6);

    void drawCenteredString(MatrixStack var1, String var2, double var3, double var5, int var7);

    void drawCenteredString(MatrixStack var1, String var2, double var3, double var5, float var7, float var8, float var9, float var10);

    float getWidth(String var1);

    float getFontHeight();

    float getFontHeight(String var1);

    float getMarginHeight();

    void drawString(MatrixStack var1, String var2, float var3, float var4, int var5, boolean var6);

    void drawString(MatrixStack var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8, boolean var9);

    String trimStringToWidth(String var1, double var2);

    String trimStringToWidth(String var1, double var2, boolean var4);

    float getStringWidth(String var1);
}
