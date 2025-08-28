package me.hextech.mod.modules.settings.impl;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;

import java.awt.*;

public class ColorsSetting
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ColorsSetting INSTANCE;
    public final ColorSetting online = this.add(new ColorSetting("Online", new Color(255, 255, 255, 202)).injectBoolean(true));
    public final ColorSetting box = this.add(new ColorSetting("Box", new Color(220, 220, 220, 45)).injectBoolean(true));

    public ColorsSetting() {
        super("ColorSetting", Category.Setting);
        INSTANCE = this;
    }

    @Override
    public void enable() {
        this.state = true;
    }

    @Override
    public void disable() {
        this.state = true;
    }

    @Override
    public boolean isOn() {
        return true;
    }
}
