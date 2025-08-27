package me.hextech.remapped;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;

public class AutoWalk
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoWalk INSTANCE;

    public AutoWalk() {
        super("AutoWalk", Module_JlagirAibYQgkHtbRnhw.Movement);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        AutoWalk.mc.field_1690.field_1894.method_23481(false);
    }

    @Override
    public void onUpdate() {
        AutoWalk.mc.field_1690.field_1894.method_23481(true);
    }
}
