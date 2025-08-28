package me.hextech.remapped;

import java.util.function.Predicate;
import me.hextech.HexTech;
import me.hextech.remapped.api.utils.Wrapper;
import me.hextech.remapped.mod.modules.settings.Setting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;

public class StringSetting
extends Setting {
    public static StringSetting current;
    private boolean isListening = false;
    private String text;

    public StringSetting(String name, String text) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name);
        this.text = text;
    }

    public StringSetting(String name, String text, Predicate visibilityIn) {
        super(name, ModuleManager.lastLoadMod.getName() + "_" + name, visibilityIn);
        this.text = text;
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && !str.isEmpty()) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public void loadSetting() {
        this.setValue(HexTech.CONFIG.getString(this.getLine(), this.text));
    }

    public String getValue() {
        return this.text;
    }

    public void setValue(String text) {
        this.text = text;
    }

    public boolean isListening() {
        return this.isListening && current == this;
    }

    public void setListening(boolean set) {
        this.isListening = set;
        if (this.isListening) {
            current = this;
        }
    }

    public void keyType(int keyCode) {
        switch (keyCode) {
            case 86: {
                if (!InputUtil.isKeyPressed(Wrapper.mc.getWindow().getHandle(), 341)) break;
                this.setValue(this.getValue() + SelectionManager.getClipboard(Wrapper.mc));
                break;
            }
            case 256: 
            case 257: 
            case 335: {
                this.setListening(false);
                break;
            }
            case 259: {
                this.setValue(StringSetting.removeLastChar(this.getValue()));
            }
        }
    }

    public void charType(char c) {
        this.setValue(this.getValue() + c);
    }
}
