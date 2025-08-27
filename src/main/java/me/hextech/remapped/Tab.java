package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Tab {
    public static final int defaultHeight = 15;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected MinecraftClient mc = MinecraftClient.getInstance();

    public abstract void update(double var1, double var3, boolean var5);

    public abstract void draw(DrawContext var1, float var2, Color var3);

    public void moveWindow(int x, int y) {
        this.x -= x;
        this.y -= y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
