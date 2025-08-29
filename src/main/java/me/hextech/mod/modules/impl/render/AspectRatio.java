package me.hextech.mod.modules.impl.render;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class AspectRatio
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static AspectRatio INSTANCE;
    public final SliderSetting ratio = this.add(new SliderSetting("Ratio", 1.78, 0.0, 5.0, 0.01));

    public AspectRatio() {
        super("AspectRatio", Category.Render);
        INSTANCE = this;
    }
}
