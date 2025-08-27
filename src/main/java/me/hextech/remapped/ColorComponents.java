package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.ClickGuiTab;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.ColorComponents_iaNdYpCDFPTJpxKuaChQ;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.ColorUtil;
import me.hextech.remapped.Component;
import me.hextech.remapped.GuiManager;
import me.hextech.remapped.MathUtil;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.TextUtil;
import me.hextech.remapped.Timer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

public class ColorComponents
extends Component {
    private final ColorSetting colorSetting;
    private final Timer clickTimer = new Timer();
    public double currentWidth = 0.0;
    boolean clicked = false;
    boolean popped = false;
    boolean hover = false;
    private float hue;
    private float saturation;
    private float brightness;
    private int alpha;
    private boolean afocused;
    private boolean hfocused;
    private boolean sbfocused;
    private float spos;
    private float bpos;
    private float hpos;
    private float apos;
    private Color prevColor;
    private boolean firstInit;
    private double lastMouseX;
    private double lastMouseY;

    public ColorComponents(ClickGuiTab parent, ColorSetting setting) {
        this.parent = parent;
        this.colorSetting = setting;
        this.prevColor = this.getColorSetting().getValue();
        this.updatePos();
        this.firstInit = true;
    }

    public ColorSetting getColorSetting() {
        return this.colorSetting;
    }

    @Override
    public boolean isVisible() {
        if (this.colorSetting.visibility != null) {
            return this.colorSetting.visibility.test(null);
        }
        return true;
    }

    private void updatePos() {
        float[] hsb = Color.RGBtoHSB(this.getColorSetting().getValue().getRed(), this.getColorSetting().getValue().getGreen(), this.getColorSetting().getValue().getBlue(), null);
        this.hue = -1.0f + hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = this.getColorSetting().getValue().getAlpha();
    }

    private void setColor(Color color) {
        this.getColorSetting().setValue(color.getRGB());
        this.prevColor = color;
    }

    @Override
    public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
        int x = this.parent.getX();
        int y = (int)((double)this.parent.getY() + this.currentOffset) - 2;
        int width = this.parent.getWidth();
        double cx = x + 3;
        double cy = y + this.defaultHeight;
        double cw = width - 19;
        double ch = this.getHeight() - 17;
        this.hover = Render2DUtil.isHovered(mouseX, mouseY, (float)x + 1.0f, (float)y + 1.0f, (float)width - 2.0f, this.defaultHeight);
        if (this.hover && GuiManager.currentGrabbed == null && this.isVisible() && ClickGuiScreen.rightClicked) {
            ClickGuiScreen.rightClicked = false;
            boolean bl = this.popped = !this.popped;
        }
        if (this.popped) {
            this.setHeight(45 + this.defaultHeight);
        } else {
            this.setHeight(this.defaultHeight);
        }
        if ((ClickGuiScreen.clicked || ClickGuiScreen.hoverClicked) && this.isVisible()) {
            if (!this.clicked) {
                if (Render2DUtil.isHovered(mouseX, mouseY, cx + cw + 9.0, cy, 4.0, ch)) {
                    this.afocused = true;
                    ClickGuiScreen.hoverClicked = true;
                    ClickGuiScreen.clicked = false;
                }
                if (Render2DUtil.isHovered(mouseX, mouseY, cx + cw + 4.0, cy, 4.0, ch)) {
                    this.hfocused = true;
                    ClickGuiScreen.hoverClicked = true;
                    ClickGuiScreen.clicked = false;
                    if (this.colorSetting.isRainbow) {
                        this.colorSetting.setRainbow(false);
                        this.lastMouseX = 0.0;
                        this.lastMouseY = 0.0;
                    } else {
                        if (!this.clickTimer.passedMs(400L) && mouseX == this.lastMouseX && mouseY == this.lastMouseY) {
                            this.colorSetting.setRainbow(!this.colorSetting.isRainbow);
                        }
                        this.clickTimer.reset();
                        this.lastMouseX = mouseX;
                        this.lastMouseY = mouseY;
                    }
                }
                if (Render2DUtil.isHovered(mouseX, mouseY, cx, cy, cw, ch)) {
                    this.sbfocused = true;
                    ClickGuiScreen.hoverClicked = true;
                    ClickGuiScreen.clicked = false;
                }
                if (GuiManager.currentGrabbed == null && this.isVisible() && this.hover && this.getColorSetting().injectBoolean) {
                    this.getColorSetting().booleanValue = !this.getColorSetting().booleanValue;
                    ClickGuiScreen.clicked = false;
                }
            }
            this.clicked = true;
        } else {
            this.clicked = false;
            this.sbfocused = false;
            this.afocused = false;
            this.hfocused = false;
        }
        if (!this.popped) {
            return;
        }
        if (GuiManager.currentGrabbed == null && this.isVisible()) {
            Color value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
            if (this.sbfocused) {
                this.saturation = (float)((double)MathUtil.clamp((float)(mouseX - cx), 0.0f, (float)cw) / cw);
                this.brightness = (float)((ch - (double)MathUtil.clamp((float)(mouseY - cy), 0.0f, (float)ch)) / ch);
                value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
                this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
            }
            if (this.hfocused) {
                this.hue = (float)(-((ch - (double)MathUtil.clamp((float)(mouseY - cy), 0.0f, (float)ch)) / ch));
                value = Color.getHSBColor(this.hue, this.saturation, this.brightness);
                this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
            }
            if (this.afocused) {
                this.alpha = (int)((ch - (double)MathUtil.clamp((float)(mouseY - cy), 0.0f, (float)ch)) / ch * 255.0);
                this.setColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), this.alpha));
            }
        }
    }

    @Override
    public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
        this.currentOffset = ColorComponents.animate(this.currentOffset, offset);
        if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
            this.currentWidth = 0.0;
            return false;
        }
        int x = this.parent.getX();
        int y = (int)((double)this.parent.getY() + this.currentOffset - 2.0);
        int width = this.parent.getWidth();
        MatrixStack matrixStack = drawContext.getMatrices();
        boolean unShift = !this.hover || !InputUtil.isKeyPressed((long)mc.getWindow().getHandle(), (int)340);
        Render2DUtil.drawRect(matrixStack, (float)x + 1.0f, (float)y + 1.0f, (float)width - 2.0f, (float)this.defaultHeight - 1.0f, this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.sbgColor.getValue());
        if (this.colorSetting.injectBoolean) {
            this.currentWidth = ColorComponents.animate(this.currentWidth, this.colorSetting.booleanValue ? (double)width - 2.0 : 0.0, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.booleanSpeed.getValue());
            switch (ColorComponents_iaNdYpCDFPTJpxKuaChQ.$SwitchMap$me$hextech$mod$modules$impl$client$ClickGui$Type[ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.uiType.getValue().ordinal()]) {
                case 1: {
                    if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.booleanValue) {
                        Render2DUtil.drawRectHorizontal(matrixStack, (float)x + 1.0f, (float)y + 1.0f, (float)this.currentWidth, (float)this.defaultHeight - (float)(!ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 1 : 0), this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.getValue());
                    } else {
                        Render2DUtil.drawRect(matrixStack, (float)x + 1.0f, (float)y + 1.0f, (float)this.currentWidth, (float)this.defaultHeight - (float)(!ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 1 : 0), this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color);
                    }
                    if (!unShift) break;
                    TextUtil.drawString(drawContext, this.colorSetting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), -1);
                    break;
                }
                case 2: {
                    if (!unShift) break;
                    TextUtil.drawString(drawContext, this.colorSetting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), this.colorSetting.booleanValue ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.enableTextS.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.disableText.getValue());
                }
            }
        } else if (unShift) {
            TextUtil.drawString(drawContext, this.colorSetting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), -1);
        }
        if (!unShift) {
            TextUtil.drawString(drawContext, "\u00a7aL-Copy \u00a7cR-Paste", (double)(x + 4), (double)y + this.getTextOffsetY(), -1);
        }
        Render2DUtil.drawRound(matrixStack, x + width - 16, (float)((double)y + this.getTextOffsetY()), 12.0f, 8.0f, 1.0f, ColorUtil.injectAlpha(this.getColorSetting().getValue(), 255));
        if (back) {
            return true;
        }
        if (!this.popped) {
            return true;
        }
        double cx = x;
        double cy = y + this.defaultHeight + 1;
        double cw = width - 15;
        double ch = this.defaultHeight - 32 + 60;
        if (this.prevColor != this.getColorSetting().getValue()) {
            this.updatePos();
            this.prevColor = this.getColorSetting().getValue();
        }
        if (this.firstInit) {
            this.spos = (float)(cx + cw - (cw - cw * (double)this.saturation));
            this.bpos = (float)(cy + (ch - ch * (double)this.brightness));
            this.hpos = (float)(cy + (ch - 3.0 + (ch - 3.0) * (double)this.hue));
            this.apos = (float)(cy + (ch - 3.0 - (ch - 3.0) * (double)((float)this.alpha / 255.0f)));
            this.firstInit = false;
        }
        this.spos = (float)ColorComponents.animate(this.spos, (float)(cx + cw - (cw - cw * (double)this.saturation)), 0.6f);
        this.bpos = (float)ColorComponents.animate(this.bpos, (float)(cy + (ch - ch * (double)this.brightness)), 0.6f);
        this.hpos = (float)ColorComponents.animate(this.hpos, (float)(cy + (ch - 3.0 + (ch - 3.0) * (double)this.hue)), 0.6f);
        this.apos = (float)ColorComponents.animate(this.apos, (float)(cy + (ch - 3.0 - (ch - 3.0) * (double)((float)this.alpha / 255.0f))), 0.6f);
        Color colorA = Color.getHSBColor(this.hue, 0.0f, 1.0f);
        Color colorB = Color.getHSBColor(this.hue, 1.0f, 1.0f);
        Color colorC = new Color(0, 0, 0, 0);
        Color colorD = new Color(0, 0, 0);
        Render2DUtil.horizontalGradient(matrixStack, (float)cx + 2.0f, (float)cy, (float)(cx + cw), (float)(cy + ch), colorA, colorB);
        Render2DUtil.verticalGradient(matrixStack, (float)(cx + 2.0), (float)cy, (float)(cx + cw), (float)(cy + ch), colorC, colorD);
        float i = 1.0f;
        while ((double)i < ch - 2.0) {
            float curHue = (float)(1.0 / (ch / (double)i));
            Render2DUtil.drawRect(matrixStack, (float)(cx + cw + 4.0), (float)(cy + (double)i), 4.0f, 1.0f, Color.getHSBColor(curHue, 1.0f, 1.0f));
            i += 1.0f;
        }
        Render2DUtil.verticalGradient(matrixStack, (float)(cx + cw + 9.0), (float)(cy + (double)0.8f), (float)(cx + cw + 12.5), (float)(cy + ch - 2.0), new Color(this.getColorSetting().getValue().getRed(), this.getColorSetting().getValue().getGreen(), this.getColorSetting().getValue().getBlue(), 255), new Color(0, 0, 0, 0));
        Render2DUtil.drawRect(matrixStack, (float)(cx + cw + 3.0), this.hpos + 0.5f, 5.0f, 1.0f, Color.WHITE);
        Render2DUtil.drawRect(matrixStack, (float)(cx + cw + 8.0), this.apos + 0.5f, 5.0f, 1.0f, Color.WHITE);
        Render2DUtil.drawRound(matrixStack, this.spos - 1.5f, this.bpos - 1.5f, 3.0f, 3.0f, 1.5f, new Color(-1));
        return true;
    }
}
