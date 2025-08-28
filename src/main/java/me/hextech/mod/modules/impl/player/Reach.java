package me.hextech.mod.modules.impl.player;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class Reach
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Reach INSTANCE;
    public final SliderSetting distance = this.add(new SliderSetting("Distance", 5.0, 1.0, 15.0, 0.1));

    public Reach() {
        super("Reach", Category.Player);
        INSTANCE = this;
    }
}
