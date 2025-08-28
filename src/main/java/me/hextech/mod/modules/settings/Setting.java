package me.hextech.mod.modules.settings;

import java.util.function.Predicate;

public abstract class Setting {
    public static Setting current;
    public final Predicate visibility;
    private final String name;
    private final String line;
    public boolean hide = false;

    public Setting(String name, String line) {
        this.name = name;
        this.line = line;
        this.visibility = null;
    }

    public Setting(String name, String line, Predicate visibilityIn) {
        this.name = name;
        this.line = line;
        this.visibility = visibilityIn;
    }

    public final String getName() {
        return this.name;
    }

    public final String getLine() {
        return this.line;
    }

    public abstract void loadSetting();

    public void hide() {
        this.hide = true;
    }
}
