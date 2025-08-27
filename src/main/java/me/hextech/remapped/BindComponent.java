package me.hextech.remapped;

import java.awt.Color;
import me.hextech.remapped.BindSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.ClickGuiTab;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.Component;
import me.hextech.remapped.GuiManager;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.TextUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

public class BindComponent
extends Component {
    private final BindSetting bind;
    boolean hover = false;

    public BindComponent(ClickGuiTab parent, BindSetting bind) {
        this.bind = bind;
        this.parent = parent;
    }

    @Override
    public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
        if (GuiManager.currentGrabbed == null && this.isVisible()) {
            int parentX = this.parent.getX();
            int parentY = this.parent.getY();
            int parentWidth = this.parent.getWidth();
            if (GuiManager.currentGrabbed == null && this.isVisible() && mouseX >= (double)(parentX + 1) && mouseX <= (double)(parentX + parentWidth - 1) && mouseY >= (double)(parentY + offset) && mouseY <= (double)(parentY + offset + this.defaultHeight - 2)) {
                this.hover = true;
                if (mouseClicked) {
                    ClickGuiScreen.clicked = false;
                    if (this.bind.getName().equals("Key") && InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)340)) {
                        this.bind.setHoldEnable(!this.bind.isHoldEnable());
                    } else {
                        this.bind.setListening(!this.bind.isListening());
                    }
                }
            } else {
                this.hover = false;
            }
        } else {
            this.hover = false;
        }
    }

    @Override
    public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
        if (back) {
            this.bind.setListening(false);
        }
        int parentX = this.parent.getX();
        int parentY = this.parent.getY();
        this.currentOffset = BindComponent.animate(this.currentOffset, offset);
        if (back && Math.abs(this.currentOffset - (double)offset) <= 0.5) {
            return false;
        }
        int y = (int)((double)this.parent.getY() + this.currentOffset - 2.0);
        int width = this.parent.getWidth();
        MatrixStack matrixStack = drawContext.method_51448();
        String text = this.hover && this.bind.getName().equals("Key") && InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)340) ? "Hold " + (this.bind.isHoldEnable() ? "\u00a7aOn" : "\u00a7cOff") : (this.bind.isListening() ? this.bind.getName() + ": Press Key.." : this.bind.getName() + ": " + this.bind.getBind());
        if (this.hover) {
            Render2DUtil.drawRect(matrixStack, (float)parentX + 1.0f, (float)y + 1.0f, (float)width - 3.0f, (float)this.defaultHeight - 1.0f, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue());
        }
        TextUtil.drawString(drawContext, text, (double)(parentX + 4), (double)((float)((double)parentY + this.getTextOffsetY() + this.currentOffset) - 2.0f), 0xFFFFFF);
        return true;
    }
}
