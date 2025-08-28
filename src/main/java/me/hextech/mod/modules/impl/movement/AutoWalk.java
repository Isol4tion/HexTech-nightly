package me.hextech.mod.modules.impl.movement;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;

public class AutoWalk
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AutoWalk INSTANCE;

    public AutoWalk() {
        super("AutoWalk", Category.Movement);
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
