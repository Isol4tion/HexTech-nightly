package me.hextech.mod.modules.settings.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.ConfigManager;
import me.hextech.api.managers.ModuleManager;
import me.hextech.api.utils.Wrapper;
import me.hextech.mod.modules.settings.Setting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;

import java.util.function.Predicate;

public class SliderSetting
extends Setting {
    private final double defaultValue;
    private final double minValue;
    private final double maxValue;
    private final double increment;
    public boolean isListening = false;
    public boolean update = false;
    public Runnable task = null;
    public boolean injectTask = false;
    public String temp;
    private double value;
    private String suffix = "";

    public SliderSetting(String name, double value, double min, double max, double increment) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
        this.value = value;
        this.defaultValue = value;
        this.minValue = min;
        this.maxValue = max;
        this.increment = increment;
    }

    public SliderSetting(String name, double value, double min, double max) {
        this(name, value, min, max, 0.1);
    }

    public SliderSetting(String name, int value, int min, int max) {
        this(name, value, min, max, 1.0);
    }

    public SliderSetting(String name, double value, double min, double max, double increment, Predicate visibilityIn) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
        this.value = value;
        this.defaultValue = value;
        this.minValue = min;
        this.maxValue = max;
        this.increment = increment;
    }

    public SliderSetting(String name, double value, double min, double max, Predicate visibilityIn) {
        this(name, value, min, max, 0.1, visibilityIn);
    }

    public SliderSetting(String name, int value, int min, int max, Predicate visibilityIn) {
        this(name, value, min, max, 1.0, visibilityIn);
    }

    public static String removeLastChar(String str) {
        if (!str.isEmpty()) {
            return str.substring(0, str.length() - 1);
        }
        return "";
    }

    public final double getValue() {
        return this.value;
    }

    public final void setValue(double value) {
        if (this.injectTask) {
            this.task.run();
        }
        this.value = (double)Math.round(value / this.increment) * this.increment;
    }

    public final float getValueFloat() {
        return (float)this.value;
    }

    public final int getValueInt() {
        return (int)this.value;
    }

    public final double getMinimum() {
        return this.minValue;
    }

    public final double getMaximum() {
        return this.maxValue;
    }

    public final double getIncrement() {
        return this.increment;
    }

    public final double getRange() {
        return this.maxValue - this.minValue;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public SliderSetting setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public void loadSetting() {
        this.setValue(HexTech.CONFIG.getFloat(this.getLine(), (float)this.defaultValue));
    }

    public boolean isListening() {
        return this.isListening && current == this;
    }

    public void setListening(boolean set) {
        this.isListening = set;
        if (this.isListening) {
            this.temp = String.valueOf(this.getValueFloat());
            current = this;
        }
    }

    public void keyType(int keyCode) {
        switch (keyCode) {
            case 86: {
                if (!InputUtil.isKeyPressed(Wrapper.mc.getWindow().getHandle(), 341)) break;
                this.temp = this.temp + SelectionManager.getClipboard(Wrapper.mc);
                break;
            }
            case 256: 
            case 257: 
            case 335: {
                if (this.temp.isEmpty()) {
                    this.setValue(this.defaultValue);
                } else if (ConfigManager.isFloat(this.temp)) {
                    this.setValue(Float.parseFloat(this.temp));
                    this.update = true;
                }
                this.setListening(false);
                break;
            }
            case 259: {
                this.temp = SliderSetting.removeLastChar(this.temp);
            }
        }
    }

    public SliderSetting injectTask(Runnable task) {
        this.task = task;
        this.injectTask = true;
        return this;
    }

    public void charType(char c) {
        this.temp = this.temp + c;
    }
}
