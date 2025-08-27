package me.hextech.remapped;

import java.util.function.Predicate;
import me.hextech.HexTech;
import me.hextech.remapped.EnumConverter;
import me.hextech.remapped.ModuleManager;
import me.hextech.remapped.Setting;

public class EnumSetting<T extends Enum<T>>
extends Setting {
    public boolean popped = false;
    private T value;

    public EnumSetting(String name, T defaultValue) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
        this.value = defaultValue;
    }

    public EnumSetting(String name, T defaultValue, Predicate visibilityIn) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
        this.value = defaultValue;
    }

    public void increaseEnum() {
        this.value = EnumConverter.increaseEnum(this.value);
    }

    public final T getValue() {
        return this.value;
    }

    public void setEnumValue(String value) {
        for (Enum e : (Enum[])this.value.getClass().getEnumConstants()) {
            if (!e.name().equalsIgnoreCase(value)) continue;
            this.value = e;
        }
    }

    @Override
    public void loadSetting() {
        EnumConverter converter = new EnumConverter(this.value.getClass());
        String enumString = HexTech.CONFIG.getString(this.getLine());
        if (enumString == null) {
            return;
        }
        Enum value = converter.doBackward(enumString);
        if (value != null) {
            this.value = value;
        }
    }

    public boolean is(T mode) {
        return this.getValue() == mode;
    }
}
