package me.hextech.remapped;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AntiSpam extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final StringSetting name = this.add(new StringSetting("Name", "zhuan_gan_"));

   public AntiSpam() {
      super("AntiSpam", Module_JlagirAibYQgkHtbRnhw.Misc);
   }

   @EventHandler
   private void PacketReceive(PacketEvent_YXFfxdDjQAfjBumqRbBu receive) {
      if (receive.getPacket() instanceof GameMessageS2CPacket e && e.comp_763().getString().contains(this.name.getValue())) {
         receive.cancel();
      }
   }
}
