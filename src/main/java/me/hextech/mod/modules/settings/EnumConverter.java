package me.hextech.mod.modules.settings;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public class EnumConverter
extends Converter<Enum, JsonElement> {
    private final Class<? extends Enum> clazz;

    public EnumConverter(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    public static int currentEnum(Enum<?> clazz) {
        for (int i = 0; i < clazz.getDeclaringClass().getEnumConstants().length; ++i) {
            Enum<?> e = clazz.getDeclaringClass().getEnumConstants()[i];
            if (!e.name().equalsIgnoreCase(clazz.name())) continue;
            return i;
        }
        return -1;
    }

    public static Enum<?> increaseEnum(Enum<?> clazz) {
        int index = currentEnum(clazz);
        for (int i = 0; i < clazz.getDeclaringClass().getEnumConstants().length; ++i) {
            if (i != index + 1) continue;
            return clazz.getDeclaringClass().getEnumConstants()[i];
        }
        return clazz.getDeclaringClass().getEnumConstants()[0];
    }

    public Enum<?> get(Enum<?> clazz, String string) {
        try {
            for (int i = 0; i < clazz.getDeclaringClass().getEnumConstants().length; ++i) {
                Enum<?> e = clazz.getDeclaringClass().getEnumConstants()[i];
                if (!e.name().equalsIgnoreCase(string)) continue;
                return e;
            }
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public JsonElement doForward(Enum anEnum) {
        return new JsonPrimitive(anEnum.toString());
    }

    public Enum doBackward(String string) {
        try {
            return Enum.valueOf(this.clazz, string);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    @NotNull
    public Enum doBackward(JsonElement jsonElement) {
        try {
            return Enum.valueOf(this.clazz, jsonElement.getAsString());
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
