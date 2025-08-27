package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.BooleanComponent_iwxfewPNzPrRDgHmnnjX;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.ClickGuiTab;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.Component;
import me.hextech.remapped.GuiManager;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.TextUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class BooleanComponent
extends Component {
    final BooleanSetting setting;
    public double currentWidth = 0.0;
    boolean hover = false;

    public BooleanComponent(ClickGuiTab parent, BooleanSetting setting) {
        this.parent = parent;
        this.setting = setting;
    }

    @Override
    public boolean isVisible() {
        if (this.setting.visibility != null) {
            return this.setting.visibility.test(null);
        }
        return true;
    }

    @Override
    public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
        int parentX = this.parent.getX();
        int parentY = this.parent.getY();
        int parentWidth = this.parent.getWidth();
        if (GuiManager.currentGrabbed == null && this.isVisible() && mouseX >= (double)(parentX + 1) && mouseX <= (double)(parentX + parentWidth - 1) && mouseY >= (double)(parentY + offset) && mouseY <= (double)(parentY + offset + this.defaultHeight - 2)) {
            this.hover = true;
            if (mouseClicked) {
                ClickGuiScreen.clicked = false;
                this.setting.toggleValue();
            }
            if (ClickGuiScreen.rightClicked) {
                ClickGuiScreen.rightClicked = false;
                this.setting.popped = !this.setting.popped;
            }
        } else {
            this.hover = false;
        }
    }

    @Override
    public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
        this.currentOffset = BooleanComponent.animate(this.currentOffset, offset);
        if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
            this.currentWidth = 0.0;
            return false;
        }
        int x = this.parent.getX();
        int y = (int)((double)this.parent.getY() + this.currentOffset - 2.0);
        int width = this.parent.getWidth();
        MatrixStack matrixStack = drawContext.method_51448();
        Render2DUtil.drawRect(matrixStack, (float)x + 1.0f, (float)y + 1.0f, (float)width - 2.0f, (float)this.defaultHeight - 1.0f, this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.sbgColor.getValue());
        this.currentWidth = BooleanComponent.animate(this.currentWidth, this.setting.getValue() ? (double)width - 2.0 : 0.0, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.booleanSpeed.getValue());
        switch (BooleanComponent_iwxfewPNzPrRDgHmnnjX.$SwitchMap$me$hextech$mod$modules$impl$client$ClickGui$Type[ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.uiType.getValue().ordinal()]) {
            case 1: {
                TextUtil.drawString(drawContext, this.setting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), this.setting.getValue() ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.enableTextS.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.disableText.getValue());
                break;
            }
            case 2: {
                if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.booleanValue) {
                    Render2DUtil.drawRectHorizontal(matrixStack, (float)x + 1.0f, (float)y + 1.0f, (float)this.currentWidth, (float)this.defaultHeight - (float)(!ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 1 : 0), this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainEnd.getValue());
                } else {
                    Render2DUtil.drawRect(matrixStack, (float)x + 1.0f, (float)y + 1.0f, (float)this.currentWidth, (float)this.defaultHeight - (float)(!ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.maxFill.getValue() ? 1 : 0), this.hover ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mainHover.getValue() : color);
                }
                TextUtil.drawString(drawContext, this.setting.getName(), (double)(x + 4), (double)y + this.getTextOffsetY(), new Color(-1).getRGB());
            }
        }
        if (this.setting.parent) {
            TextUtil.drawString(drawContext, this.setting.popped ? "-" : "+", (double)(x + width - 11), (double)y + this.getTextOffsetY(), new Color(255, 255, 255).getRGB());
        }
        return true;
    }
}
