package me.hextech.mod.gui.clickgui;

import me.hextech.HexTech;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.gui.clickgui.particle.Snow;
import me.hextech.mod.gui.clickgui.tabs.Tab;
import me.hextech.mod.modules.impl.client.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.mod.modules.settings.impl.SliderSetting;
import me.hextech.mod.modules.settings.impl.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class ClickGuiScreen
        extends Screen
        implements Wrapper {
    public static boolean clicked;
    public static boolean rightClicked;
    public static boolean hoverClicked;
    public static boolean MOUSE_BUTTON_4;
    public static boolean MOUSE_BUTTON_5;
    private final ArrayList<Snow> snow = new ArrayList();

    public ClickGuiScreen() {
        super(Text.of("ClickGui"));
    }

    public boolean shouldPause() {
        return false;
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        super.render(drawContext, mouseX, mouseY, partialTicks);
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.snow.getValue()) {
            this.snow.forEach(snow -> snow.drawSnow(drawContext));
        }
        HexTech.GUI.draw(mouseX, mouseY, drawContext, partialTicks);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof StringSetting).map(setting -> (StringSetting) setting).filter(StringSetting::isListening).forEach(setting -> setting.keyType(keyCode)));
        HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof SliderSetting).map(setting -> (SliderSetting) setting).filter(SliderSetting::isListening).forEach(setting -> setting.keyType(keyCode)));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            hoverClicked = false;
            clicked = true;
        } else if (button == 1) {
            rightClicked = true;
        } else if (button == 3) {
            MOUSE_BUTTON_4 = true;
        } else if (button == 4) {
            MOUSE_BUTTON_5 = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            clicked = false;
            hoverClicked = false;
        } else if (button == 1) {
            rightClicked = false;
        } else if (button == 3) {
            MOUSE_BUTTON_4 = false;
        } else if (button == 4) {
            MOUSE_BUTTON_5 = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void close() {
        super.close();
        rightClicked = false;
        hoverClicked = false;
        MOUSE_BUTTON_4 = false;
        MOUSE_BUTTON_5 = false;
        clicked = false;
    }

    public void onDisplayed() {
        super.onDisplayed();
        this.snow.clear();
        Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            for (int y = 0; y < 3; ++y) {
                Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
                this.snow.add(snow);
            }
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (Tab tab : HexTech.GUI.tabs) {
            tab.setY((int) ((double) tab.getY() + verticalAmount * 30.0));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
