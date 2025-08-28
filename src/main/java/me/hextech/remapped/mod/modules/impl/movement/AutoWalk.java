package me.hextech.remapped.mod.modules.impl.movement;

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
        AutoWalk.mc.options.forwardKey.setPressed(false);
    }

    @Override
    public void onUpdate() {
        AutoWalk.mc.options.forwardKey.setPressed(true);
    }
}
