package me.hextech.mod.modules.impl.misc;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class NoInterp
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static NoInterp INSTANCE;
    public final SliderSetting tickDelta = this.add(new SliderSetting("TickDelta", 0.0, 0.0, 10.0, 0.1));

    public NoInterp() {
        super("NoInterp", Category.Misc);
        INSTANCE = this;
    }
}
