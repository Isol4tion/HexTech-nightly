package me.hextech.remapped.mod.modules.impl.render;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;

public class AspectRatio
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AspectRatio INSTANCE;
    public final SliderSetting ratio = this.add(new SliderSetting("Ratio", 1.78, 0.0, 5.0, 0.01));

    public AspectRatio() {
        super("AspectRatio", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }
}
