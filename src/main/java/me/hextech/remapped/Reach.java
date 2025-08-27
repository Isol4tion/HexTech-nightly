package me.hextech.remapped;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;

public class Reach
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Reach INSTANCE;
    public final SliderSetting distance = this.add(new SliderSetting("Distance", 5.0, 1.0, 15.0, 0.1));

    public Reach() {
        super("Reach", Module_JlagirAibYQgkHtbRnhw.Player);
        INSTANCE = this;
    }
}
