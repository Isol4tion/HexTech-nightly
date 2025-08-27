package me.hextech.remapped;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.hextech.remapped.BindComponent;
import me.hextech.remapped.BindSetting;
import me.hextech.remapped.BooleanComponent;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.ClickGuiScreen;
import me.hextech.remapped.ClickGuiTab;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.ColorComponents;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.Component;
import me.hextech.remapped.EnumComponent;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.FadeUtils;
import me.hextech.remapped.GuiManager;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Render2DUtil;
import me.hextech.remapped.Setting;
import me.hextech.remapped.SliderComponent;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.StringComponent;
import me.hextech.remapped.StringSetting;
import me.hextech.remapped.TextUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class ModuleComponent
extends Component {
    private final String text;
    private final Module_eSdgMXWuzcxgQVaJFmKZ module;
    private final ClickGuiTab parent;
    private final List<Component> settingsList = new ArrayList<Component>();
    public boolean isPopped = false;
    public double currentWidth = 0.0;
    boolean hovered = false;
    private boolean popped = false;
    private int expandedHeight = this.defaultHeight;

    public ModuleComponent(String text, ClickGuiTab parent, Module_eSdgMXWuzcxgQVaJFmKZ module) {
        this.text = text;
        this.parent = parent;
        this.module = module;
        for (Setting setting : this.module.getSettings()) {
            Component c = setting.hide ? null : (setting instanceof SliderSetting ? new SliderComponent(this.parent, (SliderSetting)setting) : (setting instanceof BooleanSetting ? new BooleanComponent(this.parent, (BooleanSetting)setting) : (setting instanceof BindSetting ? new BindComponent(this.parent, (BindSetting)setting) : (setting instanceof EnumSetting ? new EnumComponent(this.parent, (EnumSetting)setting) : (setting instanceof ColorSetting ? new ColorComponents(this.parent, (ColorSetting)setting) : (setting instanceof StringSetting ? new StringComponent(this.parent, (StringSetting)setting) : null))))));
            if (c == null) continue;
            this.settingsList.add(c);
        }
        this.RecalculateExpandedHeight();
    }

    public List<Component> getSettingsList() {
        return this.settingsList;
    }

    @Override
    public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
        int parentX = this.parent.getX();
        int parentY = this.parent.getY();
        int parentWidth = this.parent.getWidth();
        if (this.popped) {
            int i = offset + this.defaultHeight + 1;
            for (Component children : this.settingsList) {
                children.update(i, mouseX, mouseY, mouseClicked);
                i += children.getHeight();
            }
        }
        boolean bl = this.hovered = mouseX >= (double)parentX && mouseX <= (double)(parentX + parentWidth) && mouseY >= (double)(parentY + offset) && mouseY <= (double)(parentY + offset + this.defaultHeight - 1);
        if (this.hovered && GuiManager.currentGrabbed == null) {
            if (mouseClicked) {
                ClickGuiScreen.clicked = false;
                if (InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)340)) {
                    this.module.drawnSetting.setValue(!this.module.drawnSetting.getValue());
                } else {
                    this.module.toggle();
                }
            }
            if (ClickGuiScreen.rightClicked) {
                ClickGuiScreen.rightClicked = false;
                this.popped = !this.popped;
            }
        }
        this.RecalculateExpandedHeight();
        if (this.popped) {
            this.setHeight(this.expandedHeight);
        } else {
            this.setHeight(this.defaultHeight);
        }
    }

    @Override
    public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
        int parentX = this.parent.getX();
        int parentY = this.parent.getY();
        int parentWidth = this.parent.getWidth();
        MatrixStack matrixStack = drawContext.method_51448();
        this.currentOffset = ModuleComponent.animate(this.currentOffset, offset);
        if (ClickGui_ABoiivByuLsVqarYqfYv.fade.getQuad(FadeUtils.Out) >= 1.0 && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.scissor.getValue()) {
            this.setScissorRegion(parentX * 2, (int)(((double)parentY + this.currentOffset + (double)this.defaultHeight) * 2.0), parentWidth * 2, mc.method_22683().method_4507() - (int)((double)parentY + this.currentOffset + (double)this.defaultHeight));
        }
        if (this.popped) {
            this.isPopped = true;
            int i = offset + this.defaultHeight + 1;
            for (Component children : this.settingsList) {
                if (children.isVisible()) {
                    children.draw(i, drawContext, partialTicks, color, false);
                    i += children.getHeight();
                    continue;
                }
                if (children instanceof SliderComponent) {
                    SliderComponent sliderComponent = (SliderComponent)children;
                    sliderComponent.renderSliderPosition = 0.0;
                } else if (children instanceof BooleanComponent) {
                    BooleanComponent booleanComponent = (BooleanComponent)children;
                    booleanComponent.currentWidth = 0.0;
                } else if (children instanceof ColorComponents) {
                    ColorComponents colorComponents = (ColorComponents)children;
                    colorComponents.currentWidth = 0.0;
                }
                children.currentOffset = i - this.defaultHeight;
            }
        } else if (this.isPopped) {
            boolean finish2 = true;
            boolean finish = false;
            for (Component children : this.settingsList) {
                if (!children.isVisible()) continue;
                if (!children.draw((int)this.currentOffset, drawContext, partialTicks, color, true)) {
                    finish = true;
                    continue;
                }
                finish2 = false;
            }
            if (finish && finish2) {
                this.isPopped = false;
            }
        } else {
            for (Component children : this.settingsList) {
                children.currentOffset = this.currentOffset;
            }
        }
        if (ClickGui_ABoiivByuLsVqarYqfYv.fade.getQuad(FadeUtils.Out) >= 1.0 && ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.scissor.getValue()) {
            GL11.glDisable((int)3089);
        }
        this.currentWidth = ModuleComponent.animate(this.currentWidth, this.module.isOn() ? (double)parentWidth - 2.0 : 0.0, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.booleanSpeed.getValue());
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.moduleEnd.booleanValue) {
            Render2DUtil.drawRectHorizontal(matrixStack, parentX + 1, (int)((double)parentY + this.currentOffset), (float)this.currentWidth, this.defaultHeight - 1, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.moduleEnable.getValue(), ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.moduleEnd.getValue());
        } else {
            Render2DUtil.drawRect(matrixStack, (float)(parentX + 1), (float)((int)((double)parentY + this.currentOffset)), (float)this.currentWidth, (float)(this.defaultHeight - 1), ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.moduleEnable.getValue());
        }
        Render2DUtil.drawRect(matrixStack, (float)(parentX + 1), (float)((int)((double)parentY + this.currentOffset)), (float)(parentWidth - 2), (float)(this.defaultHeight - 1), this.hovered ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mhColor.getValue() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.mbgColor.getValue());
        if (this.hovered && InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)340)) {
            TextUtil.drawString(drawContext, "Drawn " + (this.module.drawnSetting.getValue() ? "\u00a7aOn" : "\u00a7cOff"), (double)(parentX + 4), (double)((float)((double)parentY + this.getTextOffsetY() + this.currentOffset) - 1.0f), -1);
        } else {
            TextUtil.drawString(drawContext, this.text, (double)(parentX + 4), (double)((float)((double)parentY + this.getTextOffsetY() + this.currentOffset) - 1.0f), this.module.isOn() ? ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.enableText.getValue().getRGB() : ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.disableText.getValue().getRGB());
        }
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.bindC.booleanValue && this.module.getBind().getKey() != -1) {
            String bindText = "[" + this.module.getBind().getBind() + "]";
            TextUtil.drawStringWithScale(drawContext, bindText, (float)(parentX + 5) + TextUtil.getWidth(this.text), (float)((double)parentY + this.getTextOffsetY() + this.currentOffset - (double)(TextUtil.getHeight() / 4.0f)), ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.bindC.getValue(), 0.5f);
        }
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.gearColor.booleanValue) {
            if (this.isPopped) {
                TextUtil.drawString(drawContext, "\u2026", (double)(parentX + parentWidth - 12), (double)parentY + this.getTextOffsetY() + this.currentOffset - 3.0, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.gearColor.getValue().getRGB());
            } else {
                TextUtil.drawString(drawContext, "+", (double)(parentX + parentWidth - 11), (double)parentY + this.getTextOffsetY() + this.currentOffset, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.gearColor.getValue().getRGB());
            }
        }
        return true;
    }

    public void setScissorRegion(int x, int y, int width, int height) {
        if (y > mc.method_22683().method_4507()) {
            return;
        }
        double scaledY = mc.method_22683().method_4507() - (y + height);
        GL11.glEnable((int)3089);
        GL11.glScissor((int)x, (int)((int)scaledY), (int)width, (int)height);
    }

    public void RecalculateExpandedHeight() {
        int height = this.defaultHeight;
        for (Component children : this.settingsList) {
            if (children == null || !children.isVisible()) continue;
            height += children.getHeight();
        }
        this.expandedHeight = height;
    }
}
