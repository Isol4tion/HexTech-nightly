package me.hextech.remapped;

import me.hextech.remapped.api.utils.render.AnimateUtil;
import me.hextech.remapped.mod.modules.settings.impl.EnumSetting;

public class HotbarAnimation
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static HotbarAnimation INSTANCE;
    public final EnumSetting<AnimateUtil._AcLZzRdHWZkNeKEYTOwI> animMode = this.add(new EnumSetting<AnimateUtil._AcLZzRdHWZkNeKEYTOwI>("AnimMode", AnimateUtil._AcLZzRdHWZkNeKEYTOwI.Mio));
    public final SliderSetting hotbarSpeed = this.add(new SliderSetting("HotbarSpeed", 0.2, 0.01, 1.0, 0.01));

    public HotbarAnimation() {
        super("HotbarAnimation", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
    }
}
