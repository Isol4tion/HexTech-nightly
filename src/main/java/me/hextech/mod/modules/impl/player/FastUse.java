package me.hextech.mod.modules.impl.player;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.mod.modules.settings.impl.SliderSetting;

public class FastUse
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.0, 0.0, 4.0, 1.0));

    public FastUse() {
        super("FastUse", Category.Player);
    }

    @Override
    public void onUpdate() {
        if (FastUse.mc.itemUseCooldown <= 4 - this.delay.getValueInt()) {
            FastUse.mc.itemUseCooldown = 0;
        }
    }
}
