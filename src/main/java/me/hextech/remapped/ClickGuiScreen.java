package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Random;
import me.hextech.HexTech;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Snow;
import me.hextech.remapped.StringSetting;
import me.hextech.remapped.Tab;
import me.hextech.remapped.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

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
        super(Text.of((String)"ClickGui"));
    }

    public boolean method_25421() {
        return false;
    }

    public void method_25394(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        super.method_25394(drawContext, mouseX, mouseY, partialTicks);
        if (ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.snow.getValue()) {
            this.snow.forEach(snow -> snow.drawSnow(drawContext));
        }
        HexTech.GUI.draw(mouseX, mouseY, drawContext, partialTicks);
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof StringSetting).map(setting -> (StringSetting)setting).filter(StringSetting::isListening).forEach(setting -> setting.keyType(keyCode)));
        HexTech.MODULE.modules.forEach(module -> module.getSettings().stream().filter(setting -> setting instanceof SliderSetting).map(setting -> (SliderSetting)setting).filter(SliderSetting::isListening).forEach(setting -> setting.keyType(keyCode)));
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
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
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
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
        return super.method_25406(mouseX, mouseY, button);
    }

    public void method_25419() {
        super.close();
        rightClicked = false;
        hoverClicked = false;
        MOUSE_BUTTON_4 = false;
        MOUSE_BUTTON_5 = false;
        clicked = false;
    }

    public void method_49589() {
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

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (Tab tab : HexTech.GUI.tabs) {
            tab.setY((int)((double)tab.getY() + verticalAmount * 30.0));
        }
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
