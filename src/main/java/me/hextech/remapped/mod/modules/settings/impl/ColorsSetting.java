package me.hextech.remapped.mod.modules.settings.impl;

import java.awt.Color;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;

public class ColorsSetting
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ColorsSetting INSTANCE;
    public final ColorSetting online = this.add(new ColorSetting("Online", new Color(255, 255, 255, 202)).injectBoolean(true));
    public final ColorSetting box = this.add(new ColorSetting("Box", new Color(220, 220, 220, 45)).injectBoolean(true));

    public ColorsSetting() {
        super("ColorSetting", Module_JlagirAibYQgkHtbRnhw.Setting);
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
