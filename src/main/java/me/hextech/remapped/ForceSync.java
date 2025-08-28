package me.hextech.remapped;

import me.hextech.remapped.mod.modules.settings.impl.BooleanSetting;

public class ForceSync
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static ForceSync INSTANCE;
    public final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
    public final BooleanSetting position = this.add(new BooleanSetting("Position", true));

    public ForceSync() {
        super("ForceSync", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }
}
