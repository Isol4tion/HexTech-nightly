package me.hextech.mod.modules.impl.misc;

import me.hextech.mod.modules.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn
        extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public AutoRespawn() {
        super("AutoRespawn", Category.Misc);
        this.setDescription("Automatically respawns when you die.");
    }

    @Override
    public void onUpdate() {
        if (AutoRespawn.mc.currentScreen instanceof DeathScreen) {
            AutoRespawn.mc.player.requestRespawn();
            mc.setScreen(null);
        }
    }
}
