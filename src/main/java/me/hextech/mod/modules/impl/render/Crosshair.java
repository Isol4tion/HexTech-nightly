package me.hextech.mod.modules.impl.render;

import me.hextech.api.utils.render.Render2DUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.ColorSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Crosshair
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Crosshair INSTANCE;
    public final SliderSetting length = this.add(new SliderSetting("Length", 5.0, 0.0, 20.0, 0.1));
    public final SliderSetting thickness = this.add(new SliderSetting("Thickness", 2.0, 0.0, 20.0, 0.1));
    public final SliderSetting interval = this.add(new SliderSetting("Interval", 2.0, 0.0, 20.0, 0.1));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));

    public Crosshair() {
        super("Crosshair", Category.Render);
        INSTANCE = this;
    }

    public void draw(DrawContext context) {
        MatrixStack matrixStack = context.getMatrices();
        float centerX = (float) mc.getWindow().getScaledWidth() / 2.0f;
        float centerY = (float) mc.getWindow().getScaledHeight() / 2.0f;
        Render2DUtil.drawRect(matrixStack, centerX - this.thickness.getValueFloat() / 2.0f, centerY - this.length.getValueFloat() - this.interval.getValueFloat(), this.thickness.getValueFloat(), this.length.getValueFloat(), this.color.getValue());
        Render2DUtil.drawRect(matrixStack, centerX - this.thickness.getValueFloat() / 2.0f, centerY + this.interval.getValueFloat(), this.thickness.getValueFloat(), this.length.getValueFloat(), this.color.getValue());
        Render2DUtil.drawRect(matrixStack, centerX + this.interval.getValueFloat(), centerY - this.thickness.getValueFloat() / 2.0f, this.length.getValueFloat(), this.thickness.getValueFloat(), this.color.getValue());
        Render2DUtil.drawRect(matrixStack, centerX - this.interval.getValueFloat() - this.length.getValueFloat(), centerY - this.thickness.getValueFloat() / 2.0f, this.length.getValueFloat(), this.thickness.getValueFloat(), this.color.getValue());
    }
}
