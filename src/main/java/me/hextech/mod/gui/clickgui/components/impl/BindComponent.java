package me.hextech.mod.gui.clickgui.components.impl;

import me.hextech.api.managers.GuiManager;
import me.hextech.api.utils.render.Render2DUtil;
import me.hextech.api.utils.render.TextUtil;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.gui.clickgui.components.Component;
import me.hextech.mod.gui.clickgui.tabs.ClickGuiTab;
import me.hextech.mod.modules.impl.client.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.mod.modules.settings.impl.BindSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

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
            if (GuiManager.currentGrabbed == null && this.isVisible() && mouseX >= (double) (parentX + 1) && mouseX <= (double) (parentX + parentWidth - 1) && mouseY >= (double) (parentY + offset) && mouseY <= (double) (parentY + offset + this.defaultHeight - 2)) {
                this.hover = true;
                if (mouseClicked) {
                    ClickGuiScreen.clicked = false;
                    if (this.bind.getName().equals("Key") && InputUtil.isKeyPressed(mc.getWindow().getHandle(), 340)) {
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
        if (back && Math.abs(this.currentOffset - (double) offset) <= 0.5) {
            return false;
        }
        int y = (int) ((double) this.parent.getY() + this.currentOffset - 2.0);
        int width = this.parent.getWidth();
        MatrixStack matrixStack = drawContext.getMatrices();
        String text = this.hover && this.bind.getName().equals("Key") && InputUtil.isKeyPressed(mc.getWindow().getHandle(), 340) ? "Hold " + (this.bind.isHoldEnable() ? "\u00a7aOn" : "\u00a7cOff") : (this.bind.isListening() ? this.bind.getName() + ": Press Key.." : this.bind.getName() + ": " + this.bind.getBind());
        if (this.hover) {
            Render2DUtil.drawRect(matrixStack, (float) parentX + 1.0f, (float) y + 1.0f, (float) width - 3.0f, (float) this.defaultHeight - 1.0f, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.shColor.getValue());
        }
        TextUtil.drawString(drawContext, text, parentX + 4, (float) ((double) parentY + this.getTextOffsetY() + this.currentOffset) - 2.0f, 0xFFFFFF);
        return true;
    }
}
