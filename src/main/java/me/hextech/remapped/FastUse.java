package me.hextech.remapped;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;

public class FastUse
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.0, 0.0, 4.0, 1.0));

    public FastUse() {
        super("FastUse", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    @Override
    public void onUpdate() {
        if (FastUse.mc.field_1752 <= 4 - this.delay.getValueInt()) {
            FastUse.mc.field_1752 = 0;
        }
    }
}
