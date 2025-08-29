package me.hextech.mod.gui.clickgui.components;

import me.hextech.api.utils.Wrapper;
import me.hextech.api.utils.render.AnimateUtil;
import me.hextech.mod.gui.clickgui.tabs.ClickGuiTab;
import me.hextech.mod.modules.impl.client.ClickGui_ABoiivByuLsVqarYqfYv;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.Objects;

public abstract class Component
        implements Wrapper {
    public int defaultHeight = 16;
    public double currentOffset = 0.0;
    protected ClickGuiTab parent;
    private int height = this.defaultHeight;

    public static double animate(double current, double endPoint) {
        return Component.animate(current, endPoint, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.animationSpeed.getValue());
    }

    public static double animate(double current, double endPoint, double speed) {
        if (speed >= 1.0) {
            return endPoint;
        }
        if (speed == 0.0) {
            return current;
        }
        return AnimateUtil.animate(current, endPoint, speed, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.animMode.getValue());
    }

    public boolean isVisible() {
        return true;
    }

    public int getHeight() {
        if (!this.isVisible()) {
            return 0;
        }
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ClickGuiTab getParent() {
        return this.parent;
    }

    public void setParent(ClickGuiTab parent) {
        this.parent = parent;
    }

    public abstract void update(int var1, double var2, double var4, boolean var6);

    public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
        this.currentOffset = offset;
        return false;
    }

    public double getTextOffsetY() {
        Objects.requireNonNull(Wrapper.mc.textRenderer);
        return (double) (this.defaultHeight - 9) / 2.0 + 1.0;
    }
}
