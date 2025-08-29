package me.hextech.mod.modules.impl.render;

import me.hextech.api.utils.render.AnimateUtil;
import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.EnumSetting;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class HotbarAnimation
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HotbarAnimation INSTANCE;
    public final EnumSetting<AnimateUtil._AcLZzRdHWZkNeKEYTOwI> animMode = this.add(new EnumSetting<AnimateUtil._AcLZzRdHWZkNeKEYTOwI>("AnimMode", AnimateUtil._AcLZzRdHWZkNeKEYTOwI.Mio));
    public final SliderSetting hotbarSpeed = this.add(new SliderSetting("HotbarSpeed", 0.2, 0.01, 1.0, 0.01));

    public HotbarAnimation() {
        super("HotbarAnimation", Category.Render);
        INSTANCE = this;
    }
}
