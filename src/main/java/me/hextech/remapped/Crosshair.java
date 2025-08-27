package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class Crosshair
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Crosshair INSTANCE;
    public final SliderSetting length = this.add(new SliderSetting("Length", 5.0, 0.0, 20.0, 0.1));
    public final SliderSetting thickness = this.add(new SliderSetting("Thickness", 2.0, 0.0, 20.0, 0.1));
    public final SliderSetting interval = this.add(new SliderSetting("Interval", 2.0, 0.0, 20.0, 0.1));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));

    public Crosshair() {
        super("Crosshair", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }

    public void draw(DrawContext context) {
        MatrixStack matrixStack = context.getMatrices();
        float centerX = (float)mc.getWindow().getScaledWidth() / 2.0f;
        float centerY = (float)mc.getWindow().getScaledHeight() / 2.0f;
        Render2DUtil.drawRect(matrixStack, centerX - this.thickness.getValueFloat() / 2.0f, centerY - this.length.getValueFloat() - this.interval.getValueFloat(), this.thickness.getValueFloat(), this.length.getValueFloat(), this.color.getValue());
        Render2DUtil.drawRect(matrixStack, centerX - this.thickness.getValueFloat() / 2.0f, centerY + this.interval.getValueFloat(), this.thickness.getValueFloat(), this.length.getValueFloat(), this.color.getValue());
        Render2DUtil.drawRect(matrixStack, centerX + this.interval.getValueFloat(), centerY - this.thickness.getValueFloat() / 2.0f, this.length.getValueFloat(), this.thickness.getValueFloat(), this.color.getValue());
        Render2DUtil.drawRect(matrixStack, centerX - this.interval.getValueFloat() - this.length.getValueFloat(), centerY - this.thickness.getValueFloat() / 2.0f, this.length.getValueFloat(), this.thickness.getValueFloat(), this.color.getValue());
    }
}
