package me.hextech.mod.modules.settings.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.ModuleManager;
import me.hextech.mod.modules.settings.Setting;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class BindSetting
        extends Setting {
    public boolean hold = false;
    private boolean isListening = false;
    private int key;
    private boolean pressed = false;
    private boolean holdEnable = false;

    public BindSetting(String name, int key) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
        this.key = key;
    }

    public BindSetting(String name, int key, Predicate visibilityIn) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
        this.key = key;
    }

    @Override
    public void loadSetting() {
        this.setKey(HexTech.CONFIG.getInt(this.getLine(), this.key));
        this.setHoldEnable(HexTech.CONFIG.getBoolean(this.getLine() + "_hold"));
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getBind() {
        Object kn;
        if (this.key == -1) {
            return "None";
        }
        if (this.key >= 3 && this.key <= 4) {
            switch (this.key) {
                case 3: {
                    kn = "Mouse_4";
                    break;
                }
                case 4: {
                    kn = "Mouse_5";
                    break;
                }
                default: {
                    kn = "None";
                    break;
                }
            }
        } else {
            Object object = kn = this.key > 0 ? GLFW.glfwGetKeyName(this.key, GLFW.glfwGetKeyScancode(this.key)) : "None";
        }
        if (kn == null) {
            try {
                for (Field declaredField : GLFW.class.getDeclaredFields()) {
                    int a;
                    if (!declaredField.getName().startsWith("GLFW_KEY_") || (a = ((Integer) declaredField.get(null)).intValue()) != this.key)
                        continue;
                    String nb = declaredField.getName().substring("GLFW_KEY_".length());
                    kn = nb.substring(0, 1).toUpperCase() + nb.substring(1).toLowerCase();
                }
            } catch (Exception ignored) {
                kn = "None";
            }
        }
        return ((String) kn).toUpperCase();
    }

    public boolean isListening() {
        return this.isListening;
    }

    public void setListening(boolean set) {
        this.isListening = set;
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isHoldEnable() {
        return this.holdEnable;
    }

    public void setHoldEnable(boolean holdEnable) {
        this.holdEnable = holdEnable;
    }
}
