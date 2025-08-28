package me.hextech.mod.modules.settings.impl;

import me.hextech.HexTech;
import me.hextech.api.managers.ModuleManager;
import me.hextech.mod.modules.settings.Setting;

import java.util.function.Predicate;

public class BooleanSetting
extends Setting {
    public boolean parent = false;
    public boolean popped = false;
    public Runnable task = null;
    public boolean injectTask = false;
    private boolean value;

    public BooleanSetting(String name, boolean defaultValue) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
        this.value = defaultValue;
    }

    public BooleanSetting(String name, boolean defaultValue, Predicate visibilityIn) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
        this.value = defaultValue;
    }

    public final boolean getValue() {
        return this.value;
    }

    public final void setValue(boolean value) {
        if (this.injectTask && value != this.value) {
            this.task.run();
        }
        this.value = value;
    }

    public final void toggleValue() {
        this.value = !this.value;
    }

    public final boolean isOpen() {
        if (this.parent) {
            return this.popped;
        }
        return true;
    }

    @Override
    public void loadSetting() {
        this.value = HexTech.CONFIG.getBoolean(this.getLine(), this.value);
    }

    public BooleanSetting injectTask(Runnable task) {
        this.task = task;
        this.injectTask = true;
        return this;
    }

    public BooleanSetting setParent() {
        this.parent = true;
        return this;
    }
}
