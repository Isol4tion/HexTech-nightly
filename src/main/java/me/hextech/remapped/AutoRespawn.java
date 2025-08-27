package me.hextech.remapped;

import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public AutoRespawn() {
        super("AutoRespawn", Module_JlagirAibYQgkHtbRnhw.Misc);
        this.setDescription("Automatically respawns when you die.");
    }

    @Override
    public void onUpdate() {
        if (AutoRespawn.mc.currentScreen instanceof DeathScreen) {
            AutoRespawn.mc.player.method_7331();
            mc.setScreen(null);
        }
    }
}
