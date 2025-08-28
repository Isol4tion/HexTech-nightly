package me.hextech.mod.gui.clickgui.tabs;

import me.hextech.HexTech;
import me.hextech.api.managers.GuiManager;
import me.hextech.api.utils.render.Render2DUtil;
import me.hextech.api.utils.render.TextUtil;
import me.hextech.mod.gui.clickgui.ClickGuiScreen;
import me.hextech.mod.gui.clickgui.components.Component;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.impl.client.ClickGui_ABoiivByuLsVqarYqfYv;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;

public class ClickGuiTab
extends Tab {
    protected final boolean drawBorder = true;
    protected final ArrayList<Component> children = new ArrayList();
    public double currentHeight = 0.0;
    protected String title;
    boolean popped = true;
    private Module_eSdgMXWuzcxgQVaJFmKZ.Category category = null;

    public ClickGuiTab(String title, int x, int y) {
        this.title = title;
        this.x = HexTech.CONFIG.getInt(title + "_x", x);
        this.y = HexTech.CONFIG.getInt(title + "_y", y);
        this.width = 100;
        this.mc = MinecraftClient.getInstance();
    }

    public ClickGuiTab(Module_eSdgMXWuzcxgQVaJFmKZ.Category category, int x, int y) {
        this(category.name(), x, y);
        this.category = category;
    }

    public ArrayList<Component> getChildren() {
        return this.children;
    }

    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    @Override
    public final int getX() {
        return this.x;
    }

    @Override
    public final void setX(int x) {
        this.x = x;
    }

    @Override
    public final int getY() {
        return this.y;
    }

    @Override
    public final void setY(int y) {
        this.y = y;
    }

    @Override
    public final int getWidth() {
        return this.width;
    }

    @Override
    public final void setWidth(int width) {
        this.width = width;
    }

    @Override
    public final int getHeight() {
        return this.height;
    }

    @Override
    public final void setHeight(int height) {
        this.height = height;
    }

    public final boolean isGrabbed() {
        return GuiManager.currentGrabbed == this;
    }

    public final void addChild(Component component) {
        this.children.add(component);
    }

    @Override
    public void update(double mouseX, double mouseY, boolean mouseClicked) {
        this.onMouseClick(mouseX, mouseY, mouseClicked);
        if (this.popped) {
            int tempHeight = 1;
            for (Component child : this.children) {
                tempHeight += child.getHeight();
            }
            this.height = tempHeight;
            int i = 15;
            for (Component child : this.children) {
                child.update(i, mouseX, mouseY, mouseClicked);
                i += child.getHeight();
            }
        }
    }

    public void onMouseClick(double mouseX, double mouseY, boolean mouseClicked) {
        if (GuiManager.currentGrabbed == null && mouseX >= (double)this.x && mouseX <= (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY <= (double)(this.y + 13)) {
            if (mouseClicked) {
                GuiManager.currentGrabbed = this;
            } else if (ClickGuiScreen.rightClicked) {
                this.popped = !this.popped;
                ClickGuiScreen.rightClicked = false;
            }
        }
    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks, Color color) {
        int tempHeight = 1;
        for (Component child : this.children) {
            tempHeight += child.getHeight();
        }
        this.height = tempHeight;
        MatrixStack matrixStack = drawContext.getMatrices();
        this.currentHeight = Component.animate(this.currentHeight, this.height);
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.categoryEnd.booleanValue) {
            Render2DUtil.drawRectVertical(matrixStack, this.x, this.y - 2, this.width, 15.0f, color, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.categoryEnd.getValue());
        } else {
            Render2DUtil.drawRect(matrixStack, (float)this.x, (float)(this.y - 2), (float)this.width, 15.0f, color.getRGB());
        }
        Render2DUtil.drawRect(matrixStack, (float)this.x, (float)(this.y - 2 + 15), (float)this.width, 1.0f, new Color(38, 38, 38));
        if (this.popped) {
            Render2DUtil.drawRect(matrixStack, (float)this.x, (float)(this.y + 14), (float)this.width, (float)((int)this.currentHeight), ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.bgColor.getValue());
        }
        if (this.popped) {
            int i = 15;
            for (Component child : this.children) {
                if (child.isVisible()) {
                    child.draw(i, drawContext, partialTicks, color, false);
                    i += child.getHeight();
                    continue;
                }
                child.currentOffset = i - 15;
            }
        }
        TextUtil.drawString(drawContext, this.title, (double)this.x + (double)this.width / 2.0 - (double)(TextUtil.getWidth(this.title) / 2.0f), this.y + 2, new Color(255, 255, 255));
        if (this.category != null) {
            String text = "[" + HexTech.MODULE.categoryModules.get(this.category) + "]";
            TextUtil.drawStringWithScale(drawContext, text, (float)(this.x + this.width - 4) - TextUtil.getWidth(text) * 0.5f, (float)(this.y + 2), new Color(255, 255, 255), 0.5f);
        }
    }
}
