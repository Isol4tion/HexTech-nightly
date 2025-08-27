package me.hextech.remapped;

import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public AutoRespawn() {
      super("AutoRespawn", Module_JlagirAibYQgkHtbRnhw.Misc);
      this.setDescription("Automatically respawns when you die.");
   }

   @Override
   public void onUpdate() {
      if (mc.field_1755 instanceof DeathScreen) {
         mc.field_1724.method_7331();
         mc.method_1507(null);
      }
   }
}
